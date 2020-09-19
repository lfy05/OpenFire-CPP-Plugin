package com.OF.protocol;

import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import java.util.Arrays;

// static utility class to help manage OF packets
public class Protocol {
    private static final byte MESSAGE_FLAG = Byte.parseByte("1C", 16);

    public enum OF_PACKET_TYPES {
        MESSAGE, PRESENCE, INVALID, INCOMPLETE
    }

    public static OF_PACKET_TYPES getType(byte[] packet){
        assert packet != null;

        if(packet[1] == MESSAGE_FLAG) return OF_PACKET_TYPES.MESSAGE;

        return OF_PACKET_TYPES.INVALID;
    }

    /**
     * Get an XMPP packet from the byte traffic implementing ACOF protocol
     * @param traffic   byte traffic representation of the packet
     * @return  the constructed packet
     */
    public static Message getXMPPPacket(byte[] traffic){
        // if the bytes traffic represents a MESSAGE packet
        if (Protocol.getType(traffic) == OF_PACKET_TYPES.MESSAGE){
            // construct a org.XMPP.packet.Message packet
            Message msgPacket = new Message();

            // read sender ID from traffic
            byte[] fromIDBytes = Arrays.copyOfRange(traffic, 3, 66);
            msgPacket.setFrom(new String(fromIDBytes).trim());

            // read receiver ID from traffic
            byte[] toIDBytes = Arrays.copyOfRange(traffic, 67, 130);
            msgPacket.setTo(new String(toIDBytes).trim());

            // read message body from traffic
            byte[] msgBodyBytes = Arrays.copyOfRange(traffic, 131, 299);
            msgPacket.setBody(new String(msgBodyBytes));

            return msgPacket;
        }

        return null;
    }

    /**
     * Get the byte array implementing ACOF Protocol from byte traffic
     * @param packet    packet whose byte array is needed
     * @return  byte array of packet in ACOF Protocol
     */
    public static byte[] getToCPPSDKBytes(Packet packet){
        if(packet instanceof Message){
            byte[] OFBytes = new byte[300];

            // set headers
            Utilities.addToByteArray(OFBytes, 0, Utilities.hexStringToByteArray("BB1C2B"));

            // set sender ID
            Utilities.addToByteArray(OFBytes, 3, packet.getFrom().toString().getBytes());

            // set receiver ID
            Utilities.addToByteArray(OFBytes, 67, packet.getTo().toString().getBytes());

            // set message body
            Utilities.addToByteArray(OFBytes, 131, ((Message) packet).getBody().getBytes());

            // set footer
            Utilities.addToByteArray(OFBytes, 299, Utilities.hexStringToByteArray("EE"));

            return OFBytes;
        }

        return null;
    }

}
