/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui;

import client.ClientConnector;
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

/**
 * FXML Controller class
 *
 * @author Igor
 */
public class ClientLoginController implements Initializable {

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;
    
    private IClientRunnable client;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO make UI elements multi-language
        client = ClientConnector.iClient;
        setUITexts();
    }

    private void setUITexts() {

        lblUsername.setText(getString("username"));
        lblPassword.setText(getString("password"));
        txtUsername.setPromptText(getString("username"));
        txtPassword.setPromptText(getString("password"));
        btnLogin.setText(getString("log_in"));
        btnRegister.setText(getString("register"));

    }

    @FXML
    private void handleBtnLoginOnClick(ActionEvent event) throws IOException, ClassNotFoundException {
        if (txtUsername.getText().compareTo("") == 0 || txtPassword.getText().compareTo("") == 0) {
            InterfaceCall.showAlert(Strings.getString("enter_a_username_and_password"), AlertType.ERROR);
            return;
        }
        if(client.login(txtUsername.getText(), txtPassword.getText())) {
            InterfaceCall.showAlert("You logged in", AlertType.CONFIRMATION);
            ClientConnector.account_Id = txtUsername.getText();
            ClientConnector.loggedIn = true;
        }else {
            InterfaceCall.showAlert("Error while logging in!", AlertType.ERROR);
            ClientConnector.loggedIn = false;
        }
        // TODO: login
        // set below boolean to true if login succeeds
        
        // TODO: remove this window and go back to the old window
    }

    @FXML
    private void handleBtnRegisterOnClick(ActionEvent event) throws IOException, ClassNotFoundException {
        if (txtUsername.getText().compareTo("") == 0 || txtPassword.getText().compareTo("") == 0) {
            InterfaceCall.showAlert(Strings.getString("enter_a_username_and_password"), AlertType.ERROR);
            return;
        }
        /*
        if(client.registerUser(txtUsername.getText(), txtPassword.getText())) {
            String alertstr = "You logged in as";
            showAlert(alertstr, AlertType.CONFIRMATION);
        } else {
            showAlert("Error while registering!", AlertType.ERROR);
        }*/
    }
    

}
