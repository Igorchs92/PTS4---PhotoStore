/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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

    public SocketConnection(Socket s) {
        socket = s;
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public InetAddress getInetAddress() {
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

    public void writeFile(File file) {
        try {
            byte[] byteArray = new byte[(int) file.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(byteArray, 0, byteArray.length);
            OutputStream os = socket.getOutputStream();
            os.write(byteArray, 0, byteArray.length);
            os.flush();
        } catch (IOException ex) {
            Log.exception(ex);
        }
    }

    public void readFile(File file) {
        try {
            byte[] byteArray = new byte[1024];
            InputStream is = socket.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int bytesRead = is.read(byteArray, 0, byteArray.length);
            bos.write(byteArray, 0, bytesRead);
            bos.close();
        } catch (IOException ex) {
            Log.exception(ex);
        }
    }
}
