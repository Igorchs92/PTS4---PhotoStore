/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user;

import client.ClientConnector;
import client.IClient;
import client.ui.InterfaceCall;
import client.user.ui.UserClientRegisterController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.ClientType;

/**
 *
 * @author Igor
 */
public class UserClient extends Application implements IClient {

    private static UserClientRunnable clientRunnable;
    private static final String title = "Photostore User";
    private Stage primaryStage;
    private Scene sceneLogin;
    private Scene sceneRegister;
    private Scene sceneMain;

    @Override
    public void start(Stage stage) throws Exception {
        connectToServer();
        ClientConnector.client = this;
        this.primaryStage = stage;
        stage.setTitle(title);
        Parent root = FXMLLoader.load(getClass().getResource("../ui/ClientLogin.fxml"));
        sceneLogin = new Scene(root);
        sceneRegister = new Scene(FXMLLoader.load(getClass().getResource("ui/UserClientRegister.fxml")));
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
        launch(args);
    }

    public void connectToServer() {
        try {
            ClientConnector clientConnector = new ClientConnector();
            if (!clientConnector.connectToServer(ClientType.user)) {
                InterfaceCall.connectionFailed();
                System.exit(0);
            }
            clientRunnable = new UserClientRunnable(clientConnector.getSocket());
            ClientConnector.clientRunnable = clientRunnable;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(UserClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSceneLogin() {
        primaryStage.setScene(sceneLogin);
        //primaryStage.setTitle(title + " - login");
    }

    @Override
    public void setSceneRegister() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserClient.class.getResource("ui/UserClientRegister.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            stage.setScene(scene);
            stage.setTitle(title + " - Register");
            UserClientRegisterController controller = loader.getController();
            controller.setDialogStage(stage);
            // Show the dialog and wait until the user closes it
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(UserClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSceneMain() {
        primaryStage.setScene(sceneMain);
        primaryStage.setTitle(title);
    }

    @Override
    public void loggedIn() {
        System.exit(0);
    }

}
