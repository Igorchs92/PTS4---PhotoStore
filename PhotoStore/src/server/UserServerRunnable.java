/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Igor
 */
public class UserServerRunnable implements Observer, Runnable {

    private static final Logger LOG = Logger.getLogger(UserServerRunnable.class.getName());
    private Socket socket = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    public UserServerRunnable(Socket socket) {
        this.socket = socket;
    }

    private void newInputStream() throws IOException {
        in = new ObjectInputStream(socket.getInputStream());
    }

    private void newOutputStream() throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void update(Observable o, Object arg) {
        LOG.log(Level.INFO, "Oberservable");
    }

    @Override
    public void run() {
        testConnection();
    }

    public void testConnection() {
        try {
            while (!socket.isClosed()) {
                newInputStream();
                boolean receive = (boolean) in.readObject();
                LOG.log(Level.INFO, "Message received: {0}", receive);
                boolean send = true;
                newOutputStream();
                out.writeObject(send);
                LOG.log(Level.INFO, "Message sent: {0}", send);
            }
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOG.log(Level.INFO, "User client disconnected: {0}", socket.getInetAddress());
        }
    }
}
