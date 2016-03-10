/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.producer;

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
public class ProducerClient extends Application {

    private static final Logger LOG = Logger.getLogger(ProducerClient.class.getName());
    private static ProducerClientRunnable clientRunnable;
    private static final String title = "Photostore Producer";
    private static final String loginFXML = "ProducerClientLogin.fxml";
    private static final String menuFXML = "ProducerClientMenu.fxml";
    private static final String registerFXML = "ProducerClientRegister.fxml";

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
            ClientConnector clientConnector = new ClientConnector(ClientType.producer);
            clientRunnable = new ProducerClientRunnable(clientConnector.getSocket());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

}
