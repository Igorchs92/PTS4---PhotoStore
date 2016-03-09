/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import server.user.UserServerRunnable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ClientType;

/**
 *
 * @author Igor
 */
public class Server {
    private static final Logger LOG = Logger.getLogger(Server.class.getName());
    
    public static void main(String[] args) throws ClassNotFoundException {
        try {
            // Establish server socket
            ServerSocket serverSocket = new ServerSocket(8189);
            LOG.log(Level.INFO, "Server is running. Listening on port: {0}", serverSocket.getLocalPort());
            while (true) {
                try {
                    // Wait for client connection
                    Socket socket = serverSocket.accept();
                    LOG.log(Level.INFO, "New Client Connected: {0}", socket.getInetAddress());
                    //Read input stream for ClientType type enum
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    ClientType client = (ClientType) in.readObject();
                    LOG.log(Level.INFO, "Client Type: {0}", client.name());
                    // Handle client request in a new thread
                    Thread thread;
                    if (client == ClientType.producer){
                        thread = new Thread(new UserServerRunnable(socket));
                        thread.start();
                    }
                    if (client == ClientType.photographer){
                        thread = new Thread(new UserServerRunnable(socket));
                        thread.start();
                    }
                    if (client == ClientType.user){
                        thread = new Thread(new UserServerRunnable(socket));
                        thread.start();
                    }                    
                    
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "IOException occurred: {0}", e.getMessage());
                }
            }

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
}
