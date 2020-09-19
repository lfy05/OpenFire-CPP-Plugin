package org.jivesoftware.openfire.plugin;

import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.Log;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class SDKConnection {
    private int PORT;
    private String ADDRESS;
    private SocketChannel SDKSockChannel;
    private Selector selector;
    private InPacketListener inPacketListener;
    private boolean status = false;

    public SDKConnection(String address, int port){
        this.PORT = port;
        this.ADDRESS = address;
    }

    /**
     * Start the connection
     */
    public boolean start(){
        assert inPacketListener != null;
        // attempt to create a socket channel connecting to C++ SDK
        try{
            SDKSockChannel = SocketChannel.open();
            SDKSockChannel.connect(new InetSocketAddress(ADDRESS, PORT));
            SDKSockChannel.configureBlocking(false);
            this.selector = Selector.open();
            SelectionKey key = SDKSockChannel.register(selector, SelectionKey.OP_READ);
            this.status = true;

            // start a new thread to listen for messages
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(status){
                        try{
                            int ready = selector.select();
                            Set<SelectionKey> selectionKeys = selector.selectedKeys();
                            for(SelectionKey key: selectionKeys){
                                if(key.isReadable()){
                                    // read from buffer
                                    ByteBuffer buffer = ByteBuffer.allocate(1000);
                                    SDKSockChannel.read(buffer);
                                    // invoke listener
                                    inPacketListener.onPacketReceived(buffer.array());
                                }
                            }
                        } catch(Exception e){
                            status = false;
                            Log.error("C++SDKIntegrationPlugin: Error -- " + e.getMessage() + " caused connection to close");
                            e.printStackTrace();
                        }
                    }
                    try{
                        Log.info("C++SDKIntegrationPlugin: Status false, Connection Closed");
                        SDKSockChannel.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e){
            e.printStackTrace();
            this.status = false;
            Log.error("C++SDKIntegrationPlugin: Error occurred when attempting connection " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Send a packet through the connection
     * @param packet packet to send out
     */
    public void send(byte[] packet){
        // if the switch is turned off, the socket is unavailable
        if (!this.status){
            Log.info("C++SDKIntegrationPlugin: Failed to send packet. Connection is not available");
            return;
        }

        ByteBuffer buffer = ByteBuffer.allocate(packet.length);
        buffer.clear();
        buffer.put(packet);
        buffer.flip();

        try{
            SDKSockChannel.write(buffer);
        } catch(Exception e){
            e.printStackTrace();
            Log.error("C++SDKIntegrationPlugin: Error occurred attempting to send packet: " + e.getMessage());
        }
    }

    /**
     * Close this connection by turing the switch off.
     */
    public void close(){
        this.status = false;
    }

    public void setInPacketListener(InPacketListener listener){
        this.inPacketListener = listener;
    }

    public boolean getStatus() {
        return status;
    }
}
