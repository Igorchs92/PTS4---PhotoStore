/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user;

import client.IClientRunnable;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ClientType;
import shared.Log;
import shared.SocketConnection;
import shared.user.UserCall;

/**
 *
 * @author Igor
 */
public class UserClientRunnable implements IClientRunnable{

    private SocketConnection socket;
    public static UserClientRunnable clientRunnable;
    
    public UserClientRunnable(SocketConnection socket) throws IOException, ClassNotFoundException {
        this.socket = socket;
        testConnection();
        clientRunnable = this;
    }

    public void testConnection() throws IOException, ClassNotFoundException {
        socket.writeObject(UserCall.test);
        boolean send = false;
        socket.writeObject(send);
        Log.info("Message sent: {0}", send);
        boolean receive = (boolean) socket.readObject();
        Log.info("Message received: {0}", receive);
    }
    
    @Override
    public void registerUser(String username, String password) {
        try {
            //System.out.println("Start write");
            socket.writeObject(UserCall.register);
            socket.writeObject(username);
            socket.writeObject(password);
            //System.out.println("Wrote objs");
            /*
            socket.writeObject(name);
            socket.writeObject(adress);
            socket.writeObject(phonenumber);*/
        } catch (IOException ex) {
            Logger.getLogger(UserClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void loginUser(String username, String password) {
        try {
            socket.writeObject(UserCall.login);
            socket.writeObject(username);
            socket.writeObject(password);
        } catch (IOException ex) {
            Logger.getLogger(UserClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
