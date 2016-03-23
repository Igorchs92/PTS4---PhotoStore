/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.IOException;
import shared.FileSystemCall;
import shared.Log;
import shared.SocketConnection;

/**
 *
 * @author Igor
 */
public class FileSystem {

    SocketConnection socket;
    File root;

    public FileSystem(SocketConnection socket) {
        this.socket = socket;
        this.root = new File("resources/FileSystem/");
        if (!root.exists()) {
            root.mkdirs();
        }
    }

    public void receiveFile() {
        try {
            //receive groupId(0), uniqueId(1) and fileName(2)
            String[] fileInfo = (String[]) socket.readObject();
            //create a new file with the right directories
            File file = new File(fileInfo[0] + "/" + fileInfo[1] + "/" + fileInfo[2] + ".jpg");
            file.mkdirs();
            socket.readFile(file);
        } catch (IOException | ClassNotFoundException ex) {
            Log.exception(ex);
        }
    }

    public void sendFiles(String groupId, String uniqueId) {
        try {
            //load the existing File out of the groupId
            File map = new File(root + groupId + "/");
            //does the groupId exist yet?
            if (map.exists()) {
                //load all files from the groupId File
                File[] files = map.listFiles();
                for (File file : files) {
                    //we only want to retrieve the group photo's
                    if (file.isFile()) {
                        //we want to send a file now
                        socket.writeObject(FileSystemCall.file);
                        //~~send file (needs testing)
                        socket.writeFile(file);
                    }
                }
            }
            //load the existing File out of the unique number
            map = new File(map + uniqueId + "/");
            //does the groupId exist?
            if (map.exists()) {
                //load all files from the groupId File
                File[] files = map.listFiles();
                for (File file : files) {
                    //we only want to retrieve the files (not that we wont find a groupId)
                    if (file.isFile()) {
                        //we want to send out a file now
                        socket.writeObject(FileSystemCall.file);
                        //~~send file (needs testing)
                        socket.writeFile(file);
                    }
                }
            }
            socket.writeObject(FileSystemCall.end);
        } catch (IOException ex) {
            Log.exception(ex);
        }
    }
    
}
