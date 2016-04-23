/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

import client.IClientRunnable;
import static client.user.UserClientRunnable.clientRunnable;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.SocketConnection;
import shared.photographer.PhotographerCall;

/**
 *
 * @author Igor
 */
public class PhotographerClientRunnable implements IClientRunnable {

    private SocketConnection socket;
    public static PhotographerClientRunnable clientRunnable;

    public PhotographerClientRunnable(SocketConnection s) throws IOException, ClassNotFoundException {
        clientRunnable = this;
        this.socket = s;
        //testConnection();
    }

    public void testConnection() throws IOException, ClassNotFoundException {
        socket.writeObject(PhotographerCall.test);
        boolean send = false;
        socket.writeObject(send);
        Logger.getAnonymousLogger().log(Level.INFO, "Message sent: {0}", send);
        boolean receive = (boolean) socket.readObject();
        Logger.getAnonymousLogger().log(Level.INFO, "Message received: {0}", receive);
    }

    public boolean registerUser(String email, String password, String name, String phone, String address, String zipcode, String city, String country, String kvk, String auth) {
        try {
            socket.writeObject(PhotographerCall.register);
            socket.writeObject(new String[]{email, password, name, phone, address, zipcode, city, country, kvk, auth});
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean login(String email, String password) {
        try {
            PhotographerInfo.photographerID = email;
            socket.writeObject(PhotographerCall.login);
            socket.writeObject(new String[]{email, password});
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean uploadFile(File file) {
        try {
            socket.writeObject(PhotographerCall.upload);
            socket.writeObject(file);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Integer> createGroups(String photographer_id) {
        List<Integer> groupNumbers = new ArrayList<>();
        
        try {
            socket.writeObject(PhotographerCall.createTonsOfGroups);
            socket.writeObject(photographer_id);
            
            Object obj = socket.readObject();
            groupNumbers = (ArrayList) obj;
            
        } catch (IOException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return groupNumbers;
    }

    public void addGroupToUniqueNumber(int group_id, int personalPictures_id) {
        try {
            socket.writeObject(PhotographerCall.addUniqueNumberToGroup);
            socket.writeObject (group_id);
            socket.writeObject(personalPictures_id);
            
            System.out.println("We sended the call");
        } catch (IOException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public List<Integer> getUniqueNumbers(String photographer_id) {
        List<Integer> uniqueNumbers = new ArrayList<>();
        try {
            socket.writeObject(PhotographerCall.getUniqueNumbers);
            socket.writeObject(photographer_id);
            Object obj = socket.readObject();
            uniqueNumbers = (ArrayList) obj;
            
            
        } catch (IOException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
        //todo
        return uniqueNumbers;
    }

}
