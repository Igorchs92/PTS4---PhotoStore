/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import shared.FileSystemCall;
import shared.Log;
import shared.SocketConnection;

/**
 *
 * @author Hovsep
 */
public class FileSystem {

    SocketConnection socket;
    File root;

    public FileSystem(SocketConnection socket) {
        this.socket = socket;
        this.root = new File("resources/FileSystem/");
        if (!root.exists()) {
            root.mkdir();
        }
    }

    public void saveFile() {
        try {
            File map = root;
            boolean end = false;
            while (!end) {
                switch ((FileSystemCall) socket.readObject()) {
                    case map:
                        map = new File(root + (String) socket.readObject());
                        if (!map.exists()) {
                            map.mkdir();
                        }
                        break;
                    case file:
                        String name = (String) socket.readObject();
                        File file = new File(map + name + ".jpg");
                        ImageIcon image = (ImageIcon) socket.readObject();
                        ImageIO.write((BufferedImage) image.getImage(), "jpg", file);
                        break;
                    case end:
                        end = true;
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Log.exception(ex);
        }
    }

    
    public void sendFile() {
        try {
            //receive unique number
            String uniqueNumber = (String) socket.readObject();
            //load the existing File out of the unique number, first 5 characters are the group map name
            File map = new File(root + uniqueNumber.substring(0, 5) + "/");
            //does the map exist yet?
            if (map.exists()) {
                //load all files from the map File
                File[] files = map.listFiles();
                for (File file : files) {
                    //we only want to retrieve the group photo's
                    if (file.isFile()) {
                        //we want to send a file now
                        socket.writeObject(FileSystemCall.file);
                        //~~send file (needs testing)
                        socket.writeObject(fileToImageIcon(file));
                    }
                }
            }
            //load the existing File out of the unique number, last 5 characters are the personal map
            map = new File(map + uniqueNumber.substring(5, 10) + "/");
            //does the map exist?
            if (map.exists()) {
                //load all files from the map File
                File[] files = map.listFiles();
                for (File file : files) {
                    //we only want to retrieve the files (not that we wont find a map)
                    if (file.isFile()) {
                        //we want to send out a file now
                        socket.writeObject(FileSystemCall.file);
                        //~~send file (needs testing)
                        socket.writeObject(fileToImageIcon(file));
                        
                    }
                }
            }
            socket.writeObject(FileSystemCall.end);
        } catch (IOException | ClassNotFoundException ex) {
            Log.exception(ex);
        }
    }
    
    private ImageIcon fileToImageIcon(File file) throws IOException{
        ImageIcon imageIcon = new ImageIcon();
        imageIcon.setImage(ImageIO.read(file));
        return imageIcon;
    }
    
    public void uploadFile() {

    }
}
