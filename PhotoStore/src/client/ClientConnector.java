/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ClientType;
import shared.SocketConnection;

/**
 *
 * @author Igor
 */
public class ClientConnector {

    static public IClientRunnable iClient;
    static public ClientType clientType;
    static public boolean loggedIn;
    static public String account_Id;
    SocketConnection socket;

    public ClientConnector(ClientType client) throws IOException {
        clientType = client;
        loggedIn = false;
        try {
            socket = new SocketConnection(new Socket("localhost", 8189));
            socket.writeObject(client);
        } catch (IOException ex) {
            Logger.getLogger(ClientConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SocketConnection getSocket() {
        return socket;
    }
}
