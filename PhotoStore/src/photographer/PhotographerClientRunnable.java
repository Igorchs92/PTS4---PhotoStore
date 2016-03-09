/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photographer;

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
public class PhotographerClientRunnable {

    private final Logger LOG = Logger.getLogger(PhotographerClient.class.getName());
    private final Socket socket;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    public PhotographerClientRunnable(Socket socket) throws IOException {
        this.socket = socket;
        newOutputStream();
        out.writeObject(ClientType.photographer);
    }
         
    private void newInputStream() throws IOException{
        in = new ObjectInputStream(socket.getInputStream());
    }
    
    private void newOutputStream() throws IOException{
        out = new ObjectOutputStream(socket.getOutputStream());
    }    
    
    public void testConnection() throws IOException, ClassNotFoundException {
        boolean send = false;
        newOutputStream();
        out.writeObject(send);
        LOG.log(Level.INFO, "Message sent: {0}", send);
        newInputStream();
        boolean receive = (boolean) in.readObject();
        LOG.log(Level.INFO, "Message received: {0}", receive);
    }
}
