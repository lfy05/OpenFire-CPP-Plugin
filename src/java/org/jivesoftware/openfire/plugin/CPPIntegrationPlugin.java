package org.jivesoftware.openfire.plugin;

import com.OF.protocol.Protocol;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.util.Log;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.util.JiveGlobals;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import java.io.File;

public class CPPIntegrationPlugin implements Plugin, PacketInterceptor {
    private static CPPIntegrationPlugin singletonIntegPlugin;
    private SDKConnection connection;
    private boolean status = false;

    public void initializePlugin(PluginManager pluginManager, File file) {
        // prepare connection
        singletonIntegPlugin = new CPPIntegrationPlugin();
        Log.info("C++IntegrationPlugin: Autostart connection to C++ SDK");
        this.start();

        // register interceptor
        InterceptorManager interceptorManager = InterceptorManager.getInstance();
        interceptorManager.addInterceptor(this);
    }

    public void destroyPlugin() {
        // close connection
        this.close();
        Log.info("C++IntegrationPlugin: Plugin destroyed. Connection Closed");
    }

    @Override
    public void interceptPacket(Packet packet, Session session, boolean b, boolean b1) throws PacketRejectedException {
        if(b){
            try{
                Message msgPacket = (Message) packet;
                send(Protocol.getToCPPSDKBytes(msgPacket));
            } catch (Exception e){
                // do nothing since this is not a message packet
            }
        }
    }

    /**
     * Start a connection
     * @return  true if connection is established
     */
    public boolean start(){
        // validate and update parameters
        String addr = JiveGlobals.getProperty("CPPSDKIntegration.SDKAddr");
        String port = JiveGlobals.getProperty("CPPSDKIntegration.SDKPort");
        if(addr == null || port == null
                || addr.length() == 0 || port.length() == 0){
            // invalid parameters
            // don't do anything.
            Log.warn("C++SDKIntegrationPlugin: Autostart FAILED -- Invalid C++ SDK Connection Parameters " +
                    "Please specify valid connection parameters under Server Settings -> C++ SDK Integration Settings." +
                    " and restart the connection manually");
            return false;
        } else {
            // validate port
            try{
                Integer.parseInt(port);
            }catch (NumberFormatException e){
                Log.warn("C++SDKIntegrationPlugin: Autostart FAILED -- Invalid C++ SDK Connection Parameters " +
                        "Please specify valid port under Server Settings -> C++ SDK Integration Settings." +
                        " and restart the connection manually");
                return false;
            }
        }

        // parameter valid establish connection
        Log.info("C++SDKIntegrationPlugin: Attempt establishing Connection with C++ SDK at " +
                addr + ":" + port);
        singletonIntegPlugin.connection = new SDKConnection(addr, Integer.parseInt(port));

        singletonIntegPlugin.connection.setInPacketListener(new InPacketListener() {
            @Override
            public void onPacketReceived(byte[] packet) {
                Log.info("C++SDKIntegrationPlugin: Packet received: " + new String(packet));
                if(Protocol.getType(packet) == Protocol.OF_PACKET_TYPES.MESSAGE){
                    Message recvPacket = Protocol.getXMPPPacket(packet);
                    Log.info("C++sdkIntegrationPlugin: Received message from " + recvPacket.getFrom() + " to " + recvPacket.getTo() + " -- " + recvPacket.getBody());
                    Log.info("C++SDKIntegrationPlugin: Routing now...");
                    XMPPServer.getInstance().getMessageRouter().route(recvPacket);
                }
            }
        });

        Log.info("C++SDKIntegrationPlugin: Connection about to start");

        // start connection and return the result
        return singletonIntegPlugin.connection.start();
    }

    /**
     * Send data through established connection
     * @param data
     */
    public void send(byte[] data){
        singletonIntegPlugin.connection.send(data);
    }

    /**
     * Restart the connection
     * @return  true if a new connection is established
     */
    public boolean restart(){
        // restart process
        Log.info("C++IntegrationPlugin: Restarting connection...");

        // close previous connection if there is one
        if(singletonIntegPlugin.connection != null && singletonIntegPlugin.connection.getStatus()) {
            Log.info("C++IntegrationPlugin: connection status " + singletonIntegPlugin.connection.getStatus());
            close();

            // wait for previous connection to close
            try{
                Thread.sleep(2000);
                Log.info("c++IntegrationPlugin: Waiting for connection to close...");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        // start a new connection
        return this.start();
    }

    /**
     * Close current connection.
     * @return true if connection closure requested, false if connection is unavailable
     */
    public boolean close(){
        if (singletonIntegPlugin.connection != null){
            singletonIntegPlugin.connection.close();
            return true;
        }

        return false;
    }

    // setters and getters
    public static CPPIntegrationPlugin getPluginInstance() {
        return singletonIntegPlugin;
    }

    public boolean getStatus() {
        return status;
    }
}
