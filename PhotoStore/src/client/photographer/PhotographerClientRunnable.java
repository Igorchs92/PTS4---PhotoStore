/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

import client.IClientRunnable;
import java.io.IOException;
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

    private final SocketConnection socket;
    public static PhotographerClientRunnable clientRunnable;
    private final LocalDatabase ldb;
    private List<PictureGroup> pgl;

    public PhotographerClientRunnable(SocketConnection s) throws IOException, ClassNotFoundException {
        clientRunnable = this;
        this.socket = s;
        ldb = new LocalDatabase();
        pgl = ldb.getPictureGroups();
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

    public LocalDatabase getLocalDatabase() {
        return ldb;
    }

    public List<PictureGroup> getPictureGroupList() {
        return pgl;
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

    public boolean uploadPictureGroups(List<PictureGroup> pgl) {
        try {
            boolean saveRequired = false;
            socket.writeObject(PhotographerCall.upload);
            //filter picturegroup list on images that havent been uploaded yet and send this to the server.
            List<PictureGroup> fPgl = pgl;
            for (PictureGroup pg : fPgl) {
                for (Picture p : pg.getPictures()) {
                    if (p.isUploaded() || !p.getFile().exists()) {
                        //already uploaded, we can remove it
                        pg.removePicture(p);
                    }
                }
                //select each personalpicture
                for (PersonalPicture pp : pg.getPersonalPictures()) {
                    for (Picture p : pg.getPictures()) {
                        if (p.isUploaded() || !p.getFile().exists()) {
                            //already uploaded, we can remove it
                            pg.removePicture(p);
                        }
                    }
                    if (pp.getPictures().isEmpty()) {
                        //personalpicture doesnt contain any pictures, we can remove it
                        pg.removePersonalPicture(pp);
                    }
                }
                if (pg.getPersonalPictures().isEmpty() && pg.getPictures().isEmpty()) {
                    //picturegroup doesnt contain any pictures, we can remove it
                    fPgl.remove(pg);
                }
            }
            //send the filtered list to the server
            socket.writeObject(fPgl);
            for (PictureGroup pg : pgl) {

                for (Picture p : pg.getPictures()) {
                    if (!p.isUploaded() || p.getFile().exists()) {
                        //picture isnt uploaded, send it to the server
                        socket.writeFile(p.getFile());
                        //update the uploaded status on the picture
                        p.setUploaded(true);
                        saveRequired = true;
                    }
                }
                for (PersonalPicture pp : pg.getPersonalPictures()) {
                    for (Picture p : pp.getPictures()) {
                        if (!p.isUploaded() || p.getFile().exists()) {
                            //picture isnt uploaded, send it to the server
                            socket.writeFile(p.getFile());
                            //update the uploaded status on the picture
                            p.setUploaded(true);
                            saveRequired = true;
                        }
                    }
                }
                if (saveRequired) {
                    //save is required, save the new picturegroup on the local database
                    ldb.savePictureGroup(pg);
                }

            }
            return saveRequired;
        } catch (IOException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void CallFileUploader() {
        Task<List<PictureGroup>> tPgl = new FileUploader(socket, pgl);
        ProgressBar pb = new ProgressBar(); //just for the idea
        pb.progressProperty().bind(tPgl.progressProperty());
        new Thread(tPgl).start();
        Thread t = new Thread(() -> {
            {
                try {
                    pgl = tPgl.get();
                    Platform.runLater(() -> {
                        //PhotographerClient.client //Continue whatever we were doing
                    });
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t.start();

    }

}
