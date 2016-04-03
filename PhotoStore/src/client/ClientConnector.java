/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.ui.InterfaceCall;
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

    static public IClientRunnable clientRunnable;
    static public IClient client;
    static public ClientType clientType;
    static public boolean loggedIn = false;
    static public String account_Id;
    static public SocketConnection socket;

    public ClientConnector() throws IOException {
    }
    
    public boolean connectToServer(ClientType client) {
        clientType = client;
        try {
            socket = new SocketConnection(new Socket("localhost", 8189));
            socket.writeObject(client);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ClientConnector.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public SocketConnection getSocket() {
        return socket;
    }
}
