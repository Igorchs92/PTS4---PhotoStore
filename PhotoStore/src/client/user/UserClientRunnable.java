/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import shared.ClientType;
import shared.Log;
import shared.SocketConnection;
import shared.user.Account;
import shared.user.UserCall;

/**
 *
 * @author Igor
 */
public class UserClientRunnable {

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
    
    public Account registerUser(String username, String password/*, String name, String adress, String phonenumber*/) throws IOException, ClassNotFoundException {
        //System.out.println("Start write");
        socket.writeObject(UserCall.register);
        socket.writeObject(username);
        socket.writeObject(password);
        
        Account acc = null;
        UserCall pass = (UserCall) socket.readObject();
        if(pass == pass.fail) {
            return null;
        }else if(pass == pass.login) {
            acc = (Account) socket.readObject();
        }
        return acc;
        //System.out.println("Wrote objs");
        /*
        socket.writeObject(name);
        socket.writeObject(adress);
        socket.writeObject(phonenumber);*/
    }
    
    public Account loginUser(String username, String password) throws IOException, ClassNotFoundException {
        socket.writeObject(UserCall.login);
        socket.writeObject(username);
        socket.writeObject(password);
        UserCall pass = (UserCall) socket.readObject();
        Account acc= null;
        if(pass == pass.fail) {
            return null;
        }else if(pass == pass.login) {
            acc = (Account) socket.readObject();
        }
        return acc;
    }
}
