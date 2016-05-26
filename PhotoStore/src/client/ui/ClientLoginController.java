/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui;

import client.ClientConnector;
import client.IClient;
import client.IClientRunnable;
import client.strings.Strings;
import static client.strings.Strings.getString;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Igor
 */
public class ClientLoginController implements Initializable {

    private Stage stage;
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private Text lblUsername;

    @FXML
    private Text lblPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    private IClient client;
    private IClientRunnable clientRunnable;
    @FXML
    private ImageView imgCountryFlag;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO make UI elements multi-language
        client = ClientConnector.client;
        clientRunnable = ClientConnector.clientRunnable;
        setUITexts();
    }

    private void setUITexts() {
        //lblUsername.setText(getString("username"));
        lblPassword.setText(getString("password"));
        //txtUsername.setPromptText(getString("username"));
        //txtPassword.setPromptText(getString("password"));
        btnLogin.setText(getString("log_in"));
        btnRegister.setText(getString("register"));

    }

    @FXML
    private void handleBtnLoginOnClick(ActionEvent event) throws IOException, ClassNotFoundException {
        if (txtUsername.getText().compareTo("") == 0 || txtPassword.getText().compareTo("") == 0 || !InterfaceCall.isValidEmailAddress(txtUsername.getText())) {
            InterfaceCall.showAlert(AlertType.INFORMATION, Strings.getString("enter_a_username_and_password"));
        } else if (clientRunnable.login(txtUsername.getText(), txtPassword.getText())) {
            //InterfaceCall.showAlert(AlertType.CONFIRMATION, "You have logged in!");
            ClientConnector.account_Id = txtUsername.getText();
            ClientConnector.loggedIn = true;
            client.loggedIn();
            if (stage != null){
                stage.close();
            }
        } else {
            InterfaceCall.showAlert(AlertType.INFORMATION, "E-mail does not match the password.");
        }
    }

    @FXML
    private void handleBtnRegisterOnClick(ActionEvent event) throws IOException, ClassNotFoundException {
        if (stage != null){
            stage.close();
        } else {
            ClientConnector.client.setSceneRegister();
        }
    }

    public void setDialogStage(Stage stage) {
        this.stage = stage;
    }
    
    public void dialogMode(){
        btnRegister.setText(getString("cancel"));
    }

}
