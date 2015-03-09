
package com.cuc.miti.phone.xmc.ftpserver;

import java.net.ServerSocket;
import java.net.Socket;

import com.cuc.miti.phone.xmc.services.FTPServerService;

import android.util.Log;

public class TcpListener extends Thread {
    private static final String TAG = TcpListener.class.getSimpleName();

    ServerSocket listenSocket;
    FTPServerService ftpServerService;

    public TcpListener(ServerSocket listenSocket, FTPServerService ftpServerService) {
        this.listenSocket = listenSocket;
        this.ftpServerService = ftpServerService;
    }

    public void quit() {
        try {
            listenSocket.close(); // if the TcpListener thread is blocked on accept,
                                  // closing the socket will raise an exception
        } catch (Exception e) {
            Log.d(TAG, "Exception closing TcpListener listenSocket");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket clientSocket = listenSocket.accept();
                Log.i(TAG, "New connection, spawned thread");
                SessionThread newSession = new SessionThread(clientSocket,
                        new NormalDataSocketFactory(), SessionThread.Source.LOCAL);
                newSession.start();
                ftpServerService.registerSessionThread(newSession);
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception in TcpListener");
        }
    }
}
