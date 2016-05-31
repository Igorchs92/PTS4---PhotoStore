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
import javafx.scene.image.Image;
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

    public Filesystem(SocketConnection socket, Databasemanager dbsm) {
        this.socket = socket;
        this.dbsm = dbsm;
        this.root = new File("resources\\FileSystem\\");
        this.highres = "high\\";
        this.lowres = "low\\";
        if (!root.exists()) {
            root.mkdirs();
        }
    }

    public void compressPicture(File fileInput, File fileOutput) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            File input = fileInput;
            File output = fileOutput;
            bis = new BufferedInputStream(new FileInputStream(input));
            bos = new BufferedOutputStream(new FileOutputStream(output));
            SeekableStream ss = SeekableStream.wrapInputStream(bis, true);
            RenderedOp image = JAI.create("stream", ss);
            ((OpImage) image.getRendering()).setTileCache(null);
            RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            Image i = new Image(bis);
            double height = i.getHeight();
            double scaling = 100/height;
            RenderedOp resizedImage = JAI.create("SubsampleAverage", image, scaling, scaling, qualityHints);
            JAI.create("encode", resizedImage, bos, "JPEG", null);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filesystem.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bis.close();
                bos.close();
            } catch (IOException ex) {
                Logger.getLogger(Filesystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void upload(List<PictureGroup> pgl) {
        for (PictureGroup pg : pgl) {
            dbsm.modifyGroupPictureInfo(pg);
            File root_group = new File(root + "\\" + Integer.toString(pg.getId()) + "\\");
            //add group pictures
            for (Picture p : pg.getPictures()) {
                if (!p.isUploaded()) {
                    try {
                        p.setId(dbsm.addOriginalPicture(p));
                        socket.writeObject(p.getId());
                        if (p.getId() != 0) {
                            dbsm.addGroupPicturesPicture(pg, p);
                            File root_group_highres = new File(root_group + "\\" + this.highres + p.getRelativePath());
                            File root_group_lowres = new File(root_group + "\\" + this.lowres + p.getRelativePath());
                            root_group_highres.getParentFile().mkdirs();
                            root_group_lowres.getParentFile().mkdirs();
                            socket.readFile(root_group_highres);
                            compressPicture(root_group_highres, root_group_lowres);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Filesystem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (!p.isUpdated()) {
                    dbsm.modifyPicture(p);
                }
            }
            for (PersonalPicture pp : pg.getPersonalPictures()) {
                dbsm.modifyPersonalPicture(pg, pp);
                for (Picture p : pp.getPictures()) {
                    if (!p.isUploaded()) {
                        try {
                            p.setId(dbsm.addOriginalPicture(p));
                            socket.writeObject(p.getId());
                            if (p.getId() != 0) {
                                dbsm.addPersonalPicturesPicture(pp, p);
                                File root_group_highres = new File(root_group + "\\" + Integer.toString(pp.getId()) + "\\" + this.highres + p.getRelativePath());
                                File root_group_lowres = new File(root_group + "\\" + Integer.toString(pp.getId()) + "\\" + this.lowres + p.getRelativePath());
                                root_group_highres.getParentFile().mkdirs();
                                root_group_lowres.getParentFile().mkdirs();
                                socket.readFile(root_group_highres);
                                compressPicture(root_group_highres, root_group_lowres);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Filesystem.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (!p.isUpdated()) {
                        dbsm.modifyPicture(p);
                    }

                }
            }
        }

    }

    public void download(String uid) {
        try {
            List<PictureGroup> pgl = dbsm.getUserPictureGroup(uid);
            if (pgl == null) {
                socket.writeObject(null);
                return;
            }
            socket.writeObject(pgl);
            for (PictureGroup pg : pgl) {
                File root_group = new File(root + "\\" + Integer.toString(pg.getId()) + "\\");
                for (Picture p : pg.getPictures()) {
                    File root_group_lowres = new File(root_group + "\\" + this.lowres + p.getRelativePath());
                    socket.writeFile(root_group_lowres);
                }
                for (PersonalPicture pp : pg.getPersonalPictures()) {
                    for (Picture p : pp.getPictures()) {
                        File root_group_lowres = new File(root_group + "\\" + Integer.toString(pp.getId()) + "\\" + this.lowres + p.getRelativePath());
                        socket.writeFile(root_group_lowres);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Filesystem.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
