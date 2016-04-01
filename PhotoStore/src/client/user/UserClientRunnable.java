/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user;

import client.IClientRunnable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.SocketConnection;
import shared.user.UserCall;

/**
 *
 * @author Igor
 */
public class UserClientRunnable implements IClientRunnable {

    private SocketConnection socket;
    public static UserClientRunnable clientRunnable;

    public UserClientRunnable(SocketConnection socket) throws IOException, ClassNotFoundException {
        this.socket = socket;
        //testConnection();
        clientRunnable = this;
    }

    public void testConnection() throws IOException, ClassNotFoundException {
        socket.writeObject(UserCall.test);
        boolean send = false;
        socket.writeObject(send);
        Logger.getAnonymousLogger().log(Level.INFO, "Message sent: {0}", send);
        boolean receive = (boolean) socket.readObject();
        Logger.getAnonymousLogger().log(Level.INFO, "Message received: {0}", receive);
    }

    public boolean registerUser(String email, String password, String name, String phone, String address, String zipcode, String city, String country) {
        try {
            socket.writeObject(UserCall.register);
            socket.writeObject(new String[]{email, password, name, phone, address, zipcode, city, country});
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(UserClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean login(String email, String password) {
        try {
            socket.writeObject(UserCall.login);
            socket.writeObject(new String[]{email, password});
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(UserClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
