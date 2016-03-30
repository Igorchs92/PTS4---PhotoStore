/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.producer;

import client.IClientRunnable;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import shared.Log;
import shared.SocketConnection;
import shared.producer.ProducerCall;
import shared.user.Account;

/**
 *
 * @author Igor
 */
public class ProducerClientRunnable implements IClientRunnable{

    private SocketConnection socket;
    
    public ProducerClientRunnable(SocketConnection socket) throws IOException, ClassNotFoundException {
        this.socket = socket;
        testConnection();
    }

    public void testConnection() throws IOException, ClassNotFoundException {
        socket.writeObject(ProducerCall.test);
        boolean send = false;
        socket.writeObject(send);
        Log.info("Message sent: {0}", send);
        boolean receive = (boolean) socket.readObject();
        Log.info("Message received: {0}", receive);
    }

    @Override
    public Account registerUser(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Account loginUser(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
