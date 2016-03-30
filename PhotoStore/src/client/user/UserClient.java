/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user;

import client.ClientConnector;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class UserClient extends Application {

    private static final Logger LOG = Logger.getLogger(UserClient.class.getName());
    private static UserClientRunnable clientRunnable;
    private static final String title = "Photostore User";
    private Stage stage;
    private Scene sceneLogin;
    private Scene sceneRegister;
    private Scene sceneMain;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("../ui/UserClientLogin.fxml"));
        sceneLogin = new Scene(root);
//        sceneRegister = new Scene(FXMLLoader.load(getClass().getResource("UserClientRegister.fxml")));
//        sceneMain = new Scene(FXMLLoader.load(getClass().getResource("UserClientMain.fxml")));
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
            ClientConnector clientConnector = new ClientConnector(ClientType.user);
            clientRunnable = new UserClientRunnable(clientConnector.getSocket());
            ClientConnector.iClient = clientRunnable;
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
