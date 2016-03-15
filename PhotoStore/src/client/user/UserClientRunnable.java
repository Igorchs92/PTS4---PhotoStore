/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import shared.Log;
import shared.SocketConnection;
import shared.user.UserCall;

/**
 *
 * @author Igor
 */
public class UserClientRunnable {

    private SocketConnection socket;
    
    public UserClientRunnable(SocketConnection socket) throws IOException, ClassNotFoundException {
        this.socket = socket;
        testConnection();
    }

    public void testConnection() throws IOException, ClassNotFoundException {
        socket.writeObject(UserCall.test);
        boolean send = false;
        socket.writeObject(send);
        Log.info("Message sent: {0}", send);
        boolean receive = (boolean) socket.readObject();
        Log.info("Message received: {0}", receive);
    }
}
