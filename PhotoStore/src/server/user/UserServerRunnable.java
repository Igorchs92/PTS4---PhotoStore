/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.user;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Databasemanager;
import shared.Log;
import shared.SocketConnection;
import shared.user.Account;
import shared.user.Klant;
import shared.user.Producent;
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
        Log.info("Oberservable");
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                UserCall call = (UserCall) socket.readObject();
                switch (call) {
                    case test:
                        testConnection();
                        break;
                    case register:
                        Log.info("register");
                        String uname = (String) socket.readObject();
                        String pword = (String) socket.readObject();
                        Account acc = null;
                        try {
                            dbm.registreren(uname, pword, "", "", "", "");
                            //TODO: pas deze regel aan zodat inloggen werkt
                            acc = dbm.inloggen(uname, pword);
                            //acc = new Klant("bert", "", "", "");
                            System.out.println("ACCOUNT:" + acc);
                            //Log.info("Account: " + acc);
                        } catch (SQLException ex) {
                            Logger.getLogger(UserServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
                            socket.writeObject(UserCall.fail);
                            return;
                        }
                        socket.writeObject(UserCall.login);
                        socket.writeObject(acc);
                        System.out.println("U: " + uname + " PW:" + pword);
                        break;
                    case login: {
                        Log.info("login");
                        String name = (String) socket.readObject();
                        String word = (String) socket.readObject();
                        Account acclogin = null;
                        if (dbm.loginProducer(name, word)) {
                            socket.writeObject(UserCall.login);
                            acclogin = new Producent(name, "", word, "", "", "");
                            socket.writeObject(acclogin);
                        } else {
                            socket.writeObject(UserCall.fail);
                        }
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
            boolean receive = (boolean) socket.readObject();
            Log.info("Message received: {0}", receive);
            boolean send = true;
            socket.writeObject(send);
            Log.info("Message sent: {0}", send);
        } catch (ClassNotFoundException ex) {
            Log.exception(ex);
        } catch (IOException ex) {
            Log.info("User client disconnected: {0}", socket.getInetAddress());
        }
    }
}
