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
import client.photographer.ui.PhotographerClientRegisterController;
import client.ui.ClientLoginController;
import client.ui.InterfaceCall;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
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
    private Scene sceneMain;

    @Override
    public void start(Stage stage) throws Exception {
        client = this;
        this.primaryStage = stage;
        sceneMain = new Scene(FXMLLoader.load(getClass().getResource("ui/PhotographerClientMain.fxml")));
        setSceneMain();
        stage.show();
        LocalFileManager l = new LocalFileManager("C:\\hoi");
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
        if (connectToServer()) {
            try {
                ClientConnector.client = this;
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
                stage.setTitle(title + " - Register");
                ClientLoginController controller = loader.getController();
                controller.setDialogStage(stage);
                // Show the dialog and wait until the user closes it
                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(PhotographerClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void setSceneRegister() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PhotographerClient.class.getResource("../ui/ClientRegister.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            stage.setScene(scene);
            stage.setTitle(title + " - Register");
            PhotographerClientRegisterController controller = loader.getController();
            controller.setDialogStage(stage);
            // Show the dialog and wait until the user closes it
            stage.showAndWait();
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
        InterfaceCall.showAlert(Alert.AlertType.INFORMATION, "logged in");
    }

    @Override
    public boolean login(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
