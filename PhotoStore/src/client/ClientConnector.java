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
import shared.Log;
import shared.SocketConnection;

/**
 *
 * @author Igor
 */
public class ClientConnector{
    
    SocketConnection socket;
    
    public ClientConnector(ClientType client) throws IOException {
        try {
            socket = new SocketConnection(new Socket("localhost", 8189));
            socket.writeObject(client);
        } catch (IOException ex) {
            Log.exception(ex);
        }
    }

    public SocketConnection getSocket() {
        return socket;
    }
}
