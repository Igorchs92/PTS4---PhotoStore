/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.control.Dialog;
import shared.SocketConnection;
import shared.files.PersonalPicture;
import shared.files.Picture;
import shared.files.PictureGroup;
import shared.photographer.PhotographerCall;

/**
 *
 * @author IGOR
 */
public class FileUploader extends Task<List<PictureGroup>> {

    SocketConnection socket;
    List<PictureGroup> pgl;

    public FileUploader(SocketConnection socket, List<PictureGroup> pgl) {
        this.socket = socket;
        this.pgl = pgl;
        Dialog dg = new Dialog();
        
    }

    @Override
    protected List<PictureGroup> call() throws Exception {
        try {
            int pTotal = 0;
            socket.writeObject(PhotographerCall.upload);
            //filter picturegroup list on images that havent been uploaded yet and send this to the server.
            List<PictureGroup> fPgl = pgl;
            for (PictureGroup pg : fPgl) {
                for (Picture p : pg.getPictures()) {
                    if (p.isUploaded() || !p.getFile().exists()) {
                        //already uploaded, we can remove it
                        pg.removePicture(p);
                    } else {
                        pTotal++;
                    }
                }
                //select each personalpicture
                for (PersonalPicture pp : pg.getPersonalPictures()) {
                    for (Picture p : pg.getPictures()) {
                        if (p.isUploaded() || !p.getFile().exists()) {
                            //already uploaded, we can remove it
                            pg.removePicture(p);
                        } else {
                            pTotal++;
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
            int pUploaded = 0;
            updateProgress(pTotal, 0);
            for (PictureGroup pg : pgl) {
                for (Picture p : pg.getPictures()) {
                    if (!p.isUploaded() || p.getFile().exists()) {
                        //picture isnt uploaded, send it to the server
                        socket.writeFile(p.getFile());
                        //update the uploaded status on the picture
                        p.setUploaded();
                        updateProgress(pTotal, ++pUploaded);
                    }
                }
                for (PersonalPicture pp : pg.getPersonalPictures()) {
                    for (Picture p : pp.getPictures()) {
                        if (!p.isUploaded() || p.getFile().exists()) {
                            //picture isnt uploaded, send it to the server
                            socket.writeFile(p.getFile());
                            //update the uploaded status on the picture
                            p.setUploaded();
                            updateProgress(pTotal, ++pUploaded);
                        }
                    }
                }
            }
            this.succeeded();
            return pgl;
        } catch (IOException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
