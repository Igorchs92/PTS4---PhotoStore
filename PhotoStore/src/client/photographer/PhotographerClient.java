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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.ClientType;

/**
 *
 * @author Igor
 */
public class PhotographerClient extends Application {

    private static final Logger LOG = Logger.getLogger(PhotographerClient.class.getName());
    private static PhotographerClientRunnable clientRunnable;
    private static final String title = "Photostore Photographer";
    private Stage stage;
    private Scene sceneLogin;
    private Scene sceneRegister;
    private Scene sceneMain;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        sceneLogin = new Scene(FXMLLoader.load(getClass().getResource("PhotographerClientLogin.fxml")));
        setSceneLogin();
        stage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            ClientConnector clientConnector = new ClientConnector(ClientType.photographer);
            clientRunnable = new PhotographerClientRunnable(clientConnector.getSocket());
            launch(args);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public void setSceneLogin() {
        stage.setScene(sceneLogin);
        stage.setTitle(title + " - Login");
    }

    public void setSceneRegister() {
        stage.setScene(sceneRegister);
        stage.setTitle(title + " - Register");
    }

    public void setSceneMain() {
        stage.setScene(sceneMain);
        stage.setTitle(title);
    }

}
