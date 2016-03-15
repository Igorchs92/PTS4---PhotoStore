/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.producer;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import shared.Log;
import shared.SocketConnection;
import shared.producer.ProducerCall;

/**
 *
 * @author Igor
 */
public class ProducerServerRunnable implements Observer, Runnable {

    private SocketConnection socket;

    public ProducerServerRunnable(SocketConnection socket) {
        this.socket = socket;
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.info("Oberservable");
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                ProducerCall call = (ProducerCall) socket.readObject();
                switch (call) {
                    case test: {
                        testConnection();
                        break;
                    }
                    case login: {

                        break;
                    }
                    case logout: {

                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Log.exception(ex);
        } catch (IOException ex) {
            Log.info("User client disconnected: {0}", socket.getInetAddress());
        }
    }

    public void testConnection() {
        try {
            while (!socket.isClosed()) {
                boolean receive = (boolean) socket.readObject();
                Log.info("Message received: {0}", receive);
                boolean send = true;
                socket.writeObject(send);
                Log.info("Message sent: {0}", send);
            }
        } catch (ClassNotFoundException ex) {
            Log.exception(ex);
        } catch (IOException ex) {
            Log.info("Producer client disconnected: {0}", socket.getInetAddress());
        }
    }
}
