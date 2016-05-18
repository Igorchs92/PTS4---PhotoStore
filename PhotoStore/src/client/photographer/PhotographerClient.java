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
import client.IClient;
import client.ui.InterfaceCall;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import shared.ClientType;

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

    @Override
    public void start(Stage stage) throws Exception {
        client = this;
        connectToServer();
        ClientConnector.client = this;
        this.primaryStage = stage;
        localfilemanager  = new LocalFileManager("D:\\fotos");
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

}
