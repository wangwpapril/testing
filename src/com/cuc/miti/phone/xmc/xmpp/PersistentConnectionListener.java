package com.cuc.miti.phone.xmc.xmpp;

import org.jivesoftware.smack.ConnectionListener;

import android.util.Log;

/** 
 * A listener class for monitoring connection closing and reconnection events.
 *
 * @author SongQing
 */
public class PersistentConnectionListener implements ConnectionListener {

    private static final String LOGTAG = LogUtil.makeLogTag(PersistentConnectionListener.class);

    private final XmppManager xmppManager;

    public PersistentConnectionListener(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
    }

    public void connectionClosed() {
        Log.d(LOGTAG, "connectionClosed()...");
    }

    public void connectionClosedOnError(Exception e) {
        Log.d(LOGTAG, "connectionClosedOnError()...");
        if (xmppManager.getConnection() != null
                && xmppManager.getConnection().isConnected()) {
            			xmppManager.getConnection().disconnect();
        }
        xmppManager.startReconnectionThread();
    }

    public void reconnectingIn(int seconds) {
        Log.d(LOGTAG, "reconnectingIn()...");
    }

    public void reconnectionFailed(Exception e) {
        Log.d(LOGTAG, "reconnectionFailed()...");
    }

    public void reconnectionSuccessful() {
        Log.d(LOGTAG, "reconnectionSuccessful()...");
    }

}
