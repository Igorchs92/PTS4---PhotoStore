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
    private static final String loginFXML = "UserClientLogin.fxml";
    private static final String menuFXML = "UserClientMenu.fxml";
    private static final String registerFXML = "UserClientRegister.fxml";
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(loginFXML));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        launch(args);
        try {
            ClientConnector clientConnector = new ClientConnector(ClientType.user);
            clientRunnable = new UserClientRunnable(clientConnector.getSocket());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

}
