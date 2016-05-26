/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

import client.IClientRunnable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
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

    private SocketConnection socket;
    public static PhotographerClientRunnable clientRunnable;
    private List<PictureGroup> grps = null;

    public PhotographerClientRunnable(SocketConnection s) throws IOException, ClassNotFoundException {
        clientRunnable = this;
        this.socket = s;
    }

    public void testConnection() throws IOException, ClassNotFoundException {
        socket.writeObject(PhotographerCall.test);
        boolean send = false;
        socket.writeObject(send);
        Logger.getAnonymousLogger().log(Level.INFO, "Message sent: {0}", send);
        boolean receive = (boolean) socket.readObject();
        Logger.getAnonymousLogger().log(Level.INFO, "Message received: {0}", receive);
    }

    public SocketConnection getSocket() {
        return socket;
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
            PhotographerInfo.photographerPass = password;
            PhotographerClient.client.savePhotographer(email, password);
            socket.writeObject(PhotographerCall.login);
            socket.writeObject(new String[]{email, password});
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean uploadPictureGroups() {
        try {
            boolean saveRequired = false;
            socket.writeObject(PhotographerCall.upload);
            //send the filtered list to the server
            System.out.println("Sending filtered picture group");
            socket.writeObject(PhotographerClient.client.getPictureGroupList());
            System.out.println("Sent picture group");
            for (PictureGroup pg : PhotographerClient.client.getPictureGroupList()) {
                for (Picture p : pg.getPictures()) {
                    if (!p.isUploaded() && p.getFile().exists()) {
                        //picture isnt uploaded, send it to the server
                        p.setId((int) socket.readObject());
                        System.out.println(p.getId());
                        socket.writeFile(p.getFile());
                        //update the uploaded status on the picture
                        p.setUploaded(true);
                        saveRequired = true;
                    }
                }
                for (PersonalPicture pp : pg.getPersonalPictures()) {
                    for (Picture p : pp.getPictures()) {
                        if (!p.isUploaded() && p.getFile().exists()) {
                            //picture isnt uploaded, send it to the server
                            p.setId((int) socket.readObject());
                            socket.writeFile(p.getFile());
                            //update the uploaded status on the picture
                            p.setUploaded(true);
                            saveRequired = true;
                        }
                    }
                }
                if (saveRequired) {
                    //save is required, save the new picturegroup on the local database
                    PhotographerClient.client.getLocalDatabase().savePictureGroup(pg);
                }

            }
            return saveRequired;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //create maximum ammount of groups and return the ids as a list.
    //save it to the local database.
    public void getGroupIDs(String photographer_id) {

        List<Integer> groupID = new ArrayList<>();

        try {
            socket.writeObject(PhotographerCall.createTonsOfGroups);
            socket.writeObject(photographer_id);
            Object obj = socket.readObject();
            groupID = (ArrayList) obj;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
        PhotographerClient.client.savePictureGroupIdList(groupID);
    }

    //create maximum ammount of personal unique codes and return the ids as list
    //save it to the local database
    public void getPersonalIDs(String photographer_id) {
        List<Integer> personalID = new ArrayList<>();
        try {
            socket.writeObject(PhotographerCall.getUniqueNumbers);
            socket.writeObject(photographer_id);
            personalID = (ArrayList) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
        PhotographerClient.client.savePersonalPictureIdList(personalID);
    }

}
