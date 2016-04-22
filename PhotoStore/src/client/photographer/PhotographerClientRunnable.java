/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

import client.IClientRunnable;
import static client.user.UserClientRunnable.clientRunnable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.SocketConnection;
import shared.files.PersonalPicture;
import shared.files.Picture;
import shared.files.PictureGroup;
import shared.photographer.PhotographerCall;

/**
 *
 * @author Igor
 */
public class PhotographerClientRunnable implements IClientRunnable {

    private final SocketConnection socket;
    public static PhotographerClientRunnable clientRunnable;
    private final LocalDatabase ld;
    private final List<PictureGroup> pgl;
    
    public PhotographerClientRunnable(SocketConnection s) throws IOException, ClassNotFoundException {
        clientRunnable = this;
        this.socket = s;
        ld = new LocalDatabase();
        pgl = ld.getPictureGroups();
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
            socket.writeObject(PhotographerCall.login);
            socket.writeObject(new String[]{email, password});
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean uploadFile(File file){
        try{
            socket.writeObject(PhotographerCall.upload);
            socket.writeObject(file);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean uploadPictureGroups(List<PictureGroup> pgl) {
        try {
            socket.writeObject(PhotographerCall.upload);
            socket.writeObject(pgl);
            for (PictureGroup pg : pgl) {
                //add group pictures
                for (Picture p : pg.getGroupPictures()) {
                    socket.writeFile(p.getFile());
                }
                for (PersonalPicture pp : pg.getPersonalPictures()) {
                    for (Picture p : pp.getPersonalPictures()) {
                        socket.writeFile(p.getFile());
                    }
                }
            }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
