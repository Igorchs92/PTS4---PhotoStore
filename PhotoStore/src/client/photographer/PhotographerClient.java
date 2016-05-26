/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.ClientConnector;
import static client.ClientConnector.socket;
import client.IClient;
import client.ui.ClientLoginController;
import client.ui.InterfaceCall;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.ClientType;
import shared.files.PictureGroup;

/**
 *
 * @author Igor
 */
public class PhotographerClient extends Application implements IClient {

    public static PhotographerClient client;
    private static PhotographerClientRunnable clientRunnable;
    private static final String title = "Photostore Photographer";
    private Stage primaryStage;
    private Scene sceneLogin;
    private Scene sceneMain;
    public LocalFileManager localfilemanager;
    private LocalDatabase ldb;
    private List<PictureGroup> pgl;
    private List<Integer> AvailableGroupID;
    private List<Integer> AvailablePersonalID;
    public File selectedDirectory;

    @Override
    public void start(Stage stage) throws Exception {
        client = this;
        ldb = new LocalDatabase();
        pgl = ldb.getPictureGroups();
        AvailablePersonalID = ldb.getPersonalID();
        AvailableGroupID = ldb.getGroupID();
        ldb.getPhotographer();
        ClientConnector.client = this;
        this.primaryStage = stage;
        sceneMain = new Scene(FXMLLoader.load(getClass().getResource("ui/PhotographerClient.fxml")));
        setSceneMain();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public boolean connectToServer() {
        try {
            ClientConnector clientConnector = new ClientConnector();
            if (clientConnector.connectToServer(ClientType.photographer)) {
                clientRunnable = new PhotographerClientRunnable(clientConnector.getSocket());
                ClientConnector.clientRunnable = clientRunnable;
                return true;
            } else {
                return false;
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PhotographerClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void setSceneLogin() {
        try {
            if (connectToServer()) {
                /*
                sceneLogin = new Scene(FXMLLoader.load(getClass().getResource("../ui/ClientLogin.fxml")));
                primaryStage.setScene(sceneLogin);
                primaryStage.setTitle(title + "Login");
                 */
                // Load the fxml file and create a new stage for the popup dialog.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(PhotographerClient.class.getResource("../ui/ClientLogin.fxml"));
                AnchorPane page = (AnchorPane) loader.load();
                // Create the dialog Stage.
                Stage stage = new Stage();
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(primaryStage);
                Scene scene = new Scene(page);
                stage.setScene(scene);
                stage.setTitle(title + " - Login");
                ClientLoginController controller = loader.getController();
                controller.setDialogStage(stage);
                // Show the dialog and wait until the user closes it
                controller.dialogMode();
                stage.showAndWait();
            } else {
                InterfaceCall.connectionFailed();
            }
        } catch (IOException ex) {
            Logger.getLogger(PhotographerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSceneMain() {
        primaryStage.setScene(sceneMain);
        primaryStage.setTitle(title);
    }

    @Override
    public void loggedIn() {

    }

    @Override
    public boolean login(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSceneRegister() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LocalDatabase getLocalDatabase() {
        return ldb;
    }

    public List<PictureGroup> getPictureGroupList() {
        return pgl;
    }

    public List<Integer> getAvailablGroupIDList() {
        return ldb.getGroupID();
    }

    public List<Integer> getAvailablePersonalIDList() {
        return ldb.getPersonalID();
    }

    public void CallFileUploader() {
        Task<List<PictureGroup>> tPgl = new FileUploader(socket, PhotographerClient.client.getPictureGroupList());
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

    public void savePersonalPictureIdList(List<Integer> ppl) {
        for (int pp : ppl) {
            ldb.savePersonalPictureId(pp);
        }
    }

    public void savePersonalPictureId(int pp) {
        ldb.savePersonalPictureId(pp);
    }
    
    public void removePersonalPictureId(int pp){
        ldb.removePersonalPictureId(pp);
    }

    public void savePictureGroupIdList(List<Integer> pgl) {
        for (int pg : pgl) {
            ldb.savePictureGroupId(pg);
        }
    }

    public void savePictureGroupId(int pg) {
        ldb.savePictureGroupId(pg);
    }
    
    public void removePictureGroupId(int pg){
        ldb.removePictureGroupId(pg);
    }

    public void savePictureGroupList(List<PictureGroup> pgg) {
        for (PictureGroup pg : pgg) {
            ldb.savePictureGroup(pg);
        }
    }

    public void savePictureGroup(PictureGroup pg) {
        ldb.savePictureGroup(pg);
    }

    public void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedDirectory = directoryChooser.showDialog(primaryStage);
        if (selectedDirectory != null) {
            localfilemanager = new LocalFileManager(selectedDirectory.toString());
        }
    }

    public void savePhotographer(String email, String password) {
        ldb.savePhotographer(email, password);
    }
}
