/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.ObservableList;

/**
 *
 * @author martijn
 */
public class FileScanner implements Runnable {

    String directoryPath;
    ObservableList<Path> filePaths;

    public FileScanner(String directoryPath, ObservableList<Path> filePathsArrayList) {
        this.directoryPath = directoryPath;
        this.filePaths = filePathsArrayList;
    }

    @Override
    public void run() {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(directoryPath);
            dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

            System.out.println("Watch Service registered for dir: " + dir.getFileName());
            for (File f : dir.toFile().listFiles()) {
                addToList(f.toPath());
                System.out.println("Found an initial file: " + f.getPath());
            }
            System.out.println(filePaths.size());

            while (true) {
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();

                    System.out.println(kind.name() + ": " + fileName);
                    Platform.runLater(() -> {
                        if (kind == ENTRY_CREATE) {
                            addToList(fileName);
                            System.out.println(filePaths.size());
                        } else if (kind == ENTRY_DELETE) {
                            DeleteFromList(fileName);
                            System.out.println(filePaths.size());
                        } else if (kind == ENTRY_MODIFY) {
                            DeleteFromList(fileName);
                            addToList(new File(dir.toString() + "\\" + fileName.toString()).toPath());
                            System.out.println("My source file has changed!!!");
                            System.out.println(filePaths.size());
                        }
                    });
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private void addToList(Path p) {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.{BIN,TXT,JPG}");
        //TODO: change this to use the enum with supported path exstentions when it's available

        if (matcher.matches(p)) {
            if  (!filePaths.contains(p)){
                filePaths.add(p.getFileName());
            }
            System.out.println("path added to list: " + p);
        }
    }

    private void DeleteFromList(Path p) {
        filePaths.remove(p);
    }
}
