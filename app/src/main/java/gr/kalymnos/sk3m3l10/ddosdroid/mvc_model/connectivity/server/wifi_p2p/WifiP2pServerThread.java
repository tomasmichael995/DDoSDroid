package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.wifi_p2p;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server;

class WifiP2pServerThread implements Runnable {
    private static final String TAG = "WifiP2pServerThread";

    private Socket socket;
    private PrintWriter out;

    public WifiP2pServerThread(@NonNull Socket socket) {
        this.socket = socket;
        out = new PrintWriter(getOutputStreamFrom(socket), true);
    }

    private OutputStream getOutputStreamFrom(@NonNull Socket socket) {
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error obtaining outStream from socket", e);
        }
        return outputStream;
    }

    @Override
    public void run() {
        out.println(Server.RESPONSE);
        closeSocket();
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}