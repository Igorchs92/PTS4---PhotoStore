/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 * @author martijn
 */
public class LocalFileManager {
    
    String path;
    ArrayList<Path> imageFilePaths;
    
    public LocalFileManager(String path) {
        this.path = path;
        imageFilePaths = new ArrayList<>();
        FileScanner scanner = new FileScanner(path, imageFilePaths);
        // the arraylist in this instance is the same as the arraylist in de scanner.
        // as long as the arraylist is not initialised again the data will also be available in this instance.
        Thread t = new Thread(scanner);
        t.start();
    }
    
    
}
