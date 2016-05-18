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
import client.ui.InterfaceCall;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import shared.ClientType;
import shared.files.PersonalPicture;
import shared.files.Picture;
import shared.files.PictureGroup;

/**
 *
 * @author Igor
 */
public class PhotographerClient extends Application implements IClient {

    public static PhotographerClient client;
    private static PhotographerClientRunnable clientRunnable;
    private ClientConnector clientConnector;
    private static final String title = "Photostore Photographer";
    private Stage primaryStage;
    private Scene sceneLogin;
    private Scene sceneMain;
    public LocalFileManager localfilemanager;
    private LocalDatabase ldb;
    private List<PictureGroup> pgl;
    private List<PersonalPicture> AvailablePP;

    @Override
    public void start(Stage stage) throws Exception {
        client = this;
        ldb = new LocalDatabase();
        pgl = ldb.getPictureGroups();
        AvailablePP = ldb.getPersonalPicture();
        //connectToServer();
        ClientConnector.client = this;
        this.primaryStage = stage;
        localfilemanager = new LocalFileManager("D:\\fotos");
        sceneLogin = new Scene(FXMLLoader.load(getClass().getResource("../ui/ClientLogin.fxml")));
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
            clientConnector = new ClientConnector();
            if (clientConnector.connectToServer(ClientType.photographer)) {
                clientRunnable = new PhotographerClientRunnable(clientConnector.getSocket());
                ClientConnector.clientRunnable = clientRunnable;
                return true;
            } else {
                InterfaceCall.connectionFailed();
                return false;
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PhotographerClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void setSceneLogin() {
        primaryStage.setScene(sceneLogin);
        primaryStage.setTitle(title + "Login");
    }

    public void setSceneMain() {
        primaryStage.setScene(sceneMain);
        primaryStage.setTitle(title);
    }

    @Override
    public void loggedIn() {
        InterfaceCall.showAlert(Alert.AlertType.INFORMATION, "logged in");
        PhotographerClient.client.setSceneMain();
    }

    @Override
    public boolean login(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // need to be fixed
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

    public List<PersonalPicture> getAvailablePersonalPictureList() {
        return AvailablePP;
    }

    public void addPhotoToPersonalPicture(int personalPictures_id, Picture pic) {
        for (PersonalPicture ppl : AvailablePP) {
            if (ppl.getId() == personalPictures_id) {
                ppl.addPicture(pic);
            }
        }
    }

    //change prize
    public void changePicturePrice(int pictureID, double price) {
        for (PersonalPicture pp : AvailablePP) {
            for (Picture pic : pp.getPictures()) {
                if (pic.getId() == pictureID) {
                    pic.setPrice(price);
                }
            }
        }
    }

    public void savePictureGroupsToLocal(List<PictureGroup> pgg) {
        for (PictureGroup pg : pgg) {
            ldb.savePictureGroup(pg);
        }
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
    
    public void savePersonalPictureToLocal(List<PersonalPicture> ppL) {
        for (PersonalPicture pp : ppL) {
            ldb.savePersonalPicture(pp);
        }
    }
}
