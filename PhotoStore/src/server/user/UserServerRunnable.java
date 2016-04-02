/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.user;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Databasemanager;
import shared.ClientType;
import shared.SocketConnection;
import shared.user.UserCall;

/**
 *
 * @author Igor
 */
public class UserServerRunnable implements Observer, Runnable {

    private SocketConnection socket;
    private Databasemanager dbm;

    public UserServerRunnable(SocketConnection socket) {
        this.socket = socket;
        dbm = new Databasemanager();
    }

    @Override
    public void update(Observable o, Object arg) {
        Logger.getAnonymousLogger().log(Level.INFO, "Oberservable");
    }

    @Override
    public void run() {
        String[] args;
        boolean result;
        try {
            while (!socket.isClosed()) {
                UserCall call = (UserCall) socket.readObject();
                switch (call) {
                    case test: {
                        testConnection();
                        break;
                    }
                    case register: {
                        args = (String[]) socket.readObject();
                        result = dbm.registerUser(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
                        socket.writeObject(result);
                        break;
                    }
                    case login: {
                        args = (String[]) socket.readObject();
                        result = dbm.login(ClientType.user, args[0], args[1]);
                        socket.writeObject(result);
                        break;
                    }
                    case logout: {

                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getAnonymousLogger().log(Level.INFO, "User client disconnected: {0}", socket.getInetAddress());
        }
    }

    public void testConnection() {
        try {
            boolean receive = (boolean) socket.readObject();
            Logger.getAnonymousLogger().log(Level.INFO, "Message received: {0}", receive);
            boolean send = true;
            socket.writeObject(send);
            Logger.getAnonymousLogger().log(Level.INFO, "Message sent: {0}", send);
        } catch (ClassNotFoundException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, null,  ex);
        } catch (IOException ex) {
            Logger.getAnonymousLogger().log(Level.INFO, "User client disconnected: {0}", socket.getInetAddress());
        }
    }
}
