/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.photographer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Databasemanager;
import shared.ClientType;
import shared.SocketConnection;
import shared.photographer.PhotographerCall;

/**
 *
 * @author Igor
 */
public class PhotographerServerRunnable implements Observer, Runnable {

    private SocketConnection socket;
    private Databasemanager dbm;

    public PhotographerServerRunnable(SocketConnection socket) {
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
                PhotographerCall call = (PhotographerCall) socket.readObject();
                switch (call) {
                    case test: {
                        testConnection();
                        break;
                    }
                    case createTonsOfGroups: {
                        String s = socket.readObject().toString();
                        dbm.makeTonsOfGroup(s);
                        List groupNumbers = dbm.getGroups(s);
                        socket.writeObject(groupNumbers);
                        break;
                    }
                    case addUniqueNumberToGroup: {
                        int groupid = (int)socket.readObject();
                        int personalid = (int)socket.readObject();
                        dbm.addGroupToUniqueNumber(groupid, personalid);
                        System.out.println("we got here");
                        break;
                    }
                    case getUniqueNumbers: {
                        String s = socket.readObject().toString();
                        List uniqueNumbersList = dbm.getUniqueNumbers(s);
                        socket.writeObject(uniqueNumbersList);
                        break;
                    }
                    case login: {
                        args = (String[]) socket.readObject();
                        result = dbm.login(ClientType.photographer, args[0], args[1]);
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
        } catch (SQLException ex) {
            Logger.getLogger(PhotographerServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getAnonymousLogger().log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getAnonymousLogger().log(Level.INFO, "Photographer client disconnected: {0}", socket.getInetAddress());
        }
    }

}
