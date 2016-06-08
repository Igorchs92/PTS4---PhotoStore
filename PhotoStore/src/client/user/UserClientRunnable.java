/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user;

import client.IClientRunnable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.SocketConnection;
import shared.files.PersonalPicture;
import shared.files.Picture;
import shared.files.PictureGroup;
import shared.user.PictureModifies;
import shared.user.UserCall;

/**
 *
 * @author Igor
 */
public class UserClientRunnable implements IClientRunnable {

    private SocketConnection socket;
    public static UserClientRunnable clientRunnable;
    public Picture pictureToEdit;
    public ArrayList<PictureModifies> pictureModifiesList;

    public UserClientRunnable(SocketConnection socket) throws IOException, ClassNotFoundException {
        this.socket = socket;
        //testConnection();
        clientRunnable = this;
        pictureModifiesList = new ArrayList<>();
    }

    public void testConnection() throws IOException, ClassNotFoundException {
        socket.writeObject(UserCall.test);
        boolean send = false;
        socket.writeObject(send);
        Logger.getAnonymousLogger().log(Level.INFO, "Message sent: {0}", send);
        boolean receive = (boolean) socket.readObject();
        Logger.getAnonymousLogger().log(Level.INFO, "Message received: {0}", receive);
    }

    /**
     * @see user(email, password, name, address, zipcode, city, country, phone,
     * status)
     */
    public boolean registerUser(String email, String password, String name, String address, String zipcode, String city, String country, String phone) {
        try {
            socket.writeObject(UserCall.register);
            socket.writeObject(new String[]{email, password, name, address, zipcode, city, country, phone});
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(UserClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean login(String email, String password) {
        try {
            socket.writeObject(UserCall.login);
            socket.writeObject(new String[]{email, password});
            ClientInfo.clientID = email;
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(UserClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void attachCode(String s, int i) {
        try {
            socket.writeObject(UserCall.attachCodeToAccount);
            socket.writeObject(s);
            socket.writeObject(i);
        } catch (IOException ex) {
            Logger.getLogger(UserClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public  List<PictureGroup> download(){
        try {
            socket.writeObject(UserCall.download);
            List<PictureGroup> pgl = (List<PictureGroup>)socket.readObject();
            if (pgl == null) {
                return null;
            }
            for (PictureGroup pg : pgl) {
                File root_group = new File("resources\\user\\" + Integer.toString(pg.getId()) + "\\");
                for (Picture p : pg.getPictures()) {
                    File root_group_lowres = new File(root_group + "\\" + p.getRelativePath());
                    root_group_lowres.getParentFile().mkdirs();
                    socket.readFile(root_group_lowres);
                    p.setFile(root_group_lowres);
                }
                for (PersonalPicture pp : pg.getPersonalPictures()) {
                    for (Picture p : pp.getPictures()) {
                        File root_group_lowres = new File(root_group + "\\" + Integer.toString(pp.getId()) + "\\" + p.getRelativePath());
                        root_group_lowres.getParentFile().mkdirs();
                        socket.readFile(root_group_lowres);
                        p.setFile(root_group_lowres);
                    }
                }
            }
            System.out.println("Amount of picturegroups: " + pgl.size());
            return pgl;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(UserClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void upload(PictureModifies pm){
        try {
            socket.writeObject(UserCall.upload);
            socket.writeObject(pm);
        } catch (IOException ex) {
            Logger.getLogger(UserClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
