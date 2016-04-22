/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.sun.media.jai.codec.SeekableStream;
import java.awt.RenderingHints;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.JAI;
import javax.media.jai.OpImage;
import javax.media.jai.RenderedOp;
import shared.SocketConnection;
import shared.files.PersonalPicture;
import shared.files.Picture;
import shared.files.PictureGroup;

/**
 *
 * @author Igor
 */
public class Filesystem {

    SocketConnection socket;
    Databasemanager dbsm;
    File root;
    String highres;
    String lowres;

    public Filesystem(SocketConnection socket) {
        this.socket = socket;
        this.dbsm = new Databasemanager();
        this.root = new File("resources/FileSystem/");
        this.highres = "high/";
        this.lowres = "low/";
        if (!root.exists()) {
            root.mkdirs();
        }
    }

    public void compressPicture(File fileInput, File fileOutput) {
        BufferedInputStream bis = null;
        try {
//////////////////////// THE FOLLOWING PART WAS ON THE BRANCH "upload-groups-pictures"
            File input = fileInput;
            File output = fileOutput;
            bis = new BufferedInputStream(new FileInputStream(input));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output));
            SeekableStream ss = SeekableStream.wrapInputStream(bis, true);
            RenderedOp image = JAI.create("stream", ss);
            ((OpImage) image.getRendering()).setTileCache(null);
            RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            RenderedOp resizedImage = JAI.create("SubsampleAverage", image, 0.5, 0.5, qualityHints);
            JAI.create("encode", resizedImage, bos, "JPEG", null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filesystem.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                Logger.getLogger(Filesystem.class.getName()).log(Level.SEVERE, null, ex);
            }
///////////////////////UNTIL HERE


////////////////THE FOLLOWING PART WAS ON THE MASTER BRANCH BEFORE MERGING
            //receive groupId(0), uniqueId(1) and fileName(2)
            String[] arg = (String[]) socket.readObject();
            //create a new file with the right directories
            String pictureId /*= getFromDatabase()*/ = "0"; 
            File file = new File(arg[0] + "/" + arg[1] + "/" + pictureId + ".jpg");
            file.mkdirs();
            socket.readFile(file);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, null,  ex);
/////////////////// UNTIL HERE
        }
    }

    public void upload(List<PictureGroup> pgl) {
        for (PictureGroup pg : pgl) {
            dbsm.modifyGroupPictureInfo(pg);
            File root_group = new File(Integer.toString(pg.getId()) + "/");
            //add group pictures
            for (Picture p : pg.getGroupPictures()) {
                p.setId(dbsm.addOriginalPicture(p));
                if (p.getId() != 0) {
                    dbsm.addGroupPicturesPicture(pg, p);
                    File root_group_highres = new File(root_group + this.highres + p.toString());
                    File root_group_lowres = new File(root_group + this.lowres + p.toString());
                    socket.readFile(root_group_highres);
                    compressPicture(root_group_highres, root_group_lowres);
                }
            }
            for (PersonalPicture pp : pg.getPersonalPictures()) {
                dbsm.modifyPersonalPicture(pg, pp);
                for (Picture p : pp.getPersonalPictures()) {
                    p.setId(dbsm.addOriginalPicture(p));
                    if (p.getId() != 0) {
                        dbsm.addPersonalPicturesPicture(pp, p);
                        File root_group_highres = new File(root_group + Integer.toString(pg.getId()) + this.highres + p.toString());
                        File root_group_lowres = new File(root_group + Integer.toString(pg.getId()) + this.lowres + p.toString());
                        socket.readFile(root_group_highres);
                        compressPicture(root_group_highres, root_group_lowres);
                    }
                }
            }
        }

    }

}
