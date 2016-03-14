/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import server.producer.ProducerServerRunnable;
import server.photographer.PhotographerServerRunnable;
import server.user.UserServerRunnable;
import shared.ClientType;
import shared.SocketConnection;

/**
 *
 * @author Igor
 */
public class Server extends SocketConnection {

    public void clientConnect() throws ClassNotFoundException {
        try {
            // Establish server socket
            ServerSocket serverSocket = new ServerSocket(8189);
            LOG.log(Level.INFO, "Server is running. Listening on port: {0}", serverSocket.getLocalPort());
            while (true) {
                try {
                    // Wait for client connection
                    socket = serverSocket.accept();
                    LOG.log(Level.INFO, "New Client Connected: {0}", socket.getInetAddress());
                    //Read input stream for ClientType type enum
                    ClientType client = (ClientType) readObject();
                    LOG.log(Level.INFO, "Client Type: {0}", client.name());
                    // Handle client request in a new thread
                    Thread thread;
                    switch (client) {
                        case producer:
                            thread = new Thread(new ProducerServerRunnable(socket));
                            thread.start();
                            break;
                        case photographer:
                            thread = new Thread(new PhotographerServerRunnable(socket));
                            thread.start();
                            break;
                        case user:
                            thread = new Thread(new UserServerRunnable(socket));
                            thread.start();
                            break;
                        default:
                            break;
                    }
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "IOException occurred: {0}", e.getMessage());
                }
            }

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        (new Server()).clientConnect();
    }

}
