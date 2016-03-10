/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import shared.ClientType;
import shared.SocketConnection;

/**
 *
 * @author Igor
 */
public class ClientConnector extends SocketConnection{

    public ClientConnector(ClientType client) throws IOException {
        try {
            socket = new Socket("localhost", 8189);
            writeObject(client);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
