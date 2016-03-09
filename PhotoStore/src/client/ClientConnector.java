/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ClientType;

/**
 *
 * @author Igor
 */
public class ClientConnector {

    private final Logger LOG = Logger.getLogger(ClientConnector.class.getName());
    private Socket socket;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    
    public ClientConnector(ClientType client) throws IOException{
        try {
        socket = new Socket("localhost", 8189);
        newOutputStream();
        out.writeObject(client);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } 
    }
    
    private void newInputStream() throws IOException{
        in = new ObjectInputStream(socket.getInputStream());
    }
    
    private void newOutputStream() throws IOException{
        out = new ObjectOutputStream(socket.getOutputStream());
    }    
    
    public Socket getSocket(){
        return socket;
    }
}
