/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Igor
 */
public class SocketConnection {

    private Socket socket = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    
    public SocketConnection(Socket s){
        socket = s;
    }
    
    public Socket getSocket(){
        return socket;
    }
    
    public boolean isClosed(){
        return socket.isClosed();
    }
    
    public InetAddress getInetAddress(){
        return socket.getInetAddress();
    }
    
    public Object readObject() throws IOException, ClassNotFoundException {
        in = new ObjectInputStream(socket.getInputStream());
        return in.readObject();
    }

    public void writeObject(Object obj) throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(obj);
    }
}
