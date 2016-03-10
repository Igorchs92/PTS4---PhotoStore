/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import shared.SocketConnection;

/**
 *
 * @author Igor
 */
public class PhotographerClientRunnable extends SocketConnection{

    public PhotographerClientRunnable(Socket socket) throws IOException, ClassNotFoundException {
        this.socket = socket;
        testConnection();
    }

    public void testConnection() throws IOException, ClassNotFoundException {
        boolean send = false;
        writeObject(send);
        LOG.log(Level.INFO, "Message sent: {0}", send);
        boolean receive = (boolean) readObject();
        LOG.log(Level.INFO, "Message received: {0}", receive);
    }
}
