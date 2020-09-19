package org.jivesoftware.openfire.plugin;

// listener interface, all incoming packet listeners have to implement
// this interface
public interface InPacketListener {
    public void onPacketReceived(byte[] packet);
}
