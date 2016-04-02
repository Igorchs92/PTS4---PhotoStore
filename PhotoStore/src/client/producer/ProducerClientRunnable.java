/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.producer;

import client.IClientRunnable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.SocketConnection;
import shared.producer.ProducerCall;

/**
 *
 * @author Igor
 */
public class ProducerClientRunnable implements IClientRunnable {

    private SocketConnection socket;
    public static ProducerClientRunnable clientRunnable;
    
    public ProducerClientRunnable(SocketConnection socket) throws IOException, ClassNotFoundException {
        clientRunnable = this;
        this.socket = socket;
        testConnection();
    }

    public void testConnection() throws IOException, ClassNotFoundException {
        socket.writeObject(ProducerCall.test);
        boolean send = false;
        socket.writeObject(send);
        Logger.getAnonymousLogger().log(Level.INFO, "Message sent: {0}", send);
        boolean receive = (boolean) socket.readObject();
        Logger.getAnonymousLogger().log(Level.INFO, "Message received: {0}", receive);
    }

    public boolean registerUser(String email, String password, String name) {
        try {
            socket.writeObject(ProducerCall.register);
            socket.writeObject(new String[]{email, password, name});
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ProducerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean login(String email, String password) {
        try {
            socket.writeObject(ProducerCall.login);
            socket.writeObject(new String[]{email, password});
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ProducerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
