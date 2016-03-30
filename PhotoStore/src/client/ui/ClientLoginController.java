/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui;

import client.ClientConnector;
import client.strings.Strings;
import static client.strings.Strings.getString;
import client.user.UserClientRunnable;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
    
    
    private UserClientRunnable serverCom;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO make UI elements multi-language
        serverCom = UserClientRunnable.clientRunnable;
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
    private void handleBtnLoginOnClick(ActionEvent event) throws IOException {
        if(txtUsername.getText().compareTo("") == 0 || txtPassword.getText().compareTo("") == 0) {
            showAlert(Strings.getString("enter_a_username_and_password"), AlertType.ERROR);
            return;
        }
        serverCom.loginUser(txtUsername.getText(), txtPassword.getText());
        // TODO: login
        // set below boolean to true if login succeeds
        ClientConnector.loggedIn = true;
        // TODO: remove this window and go back to the old window
    }
    
    @FXML
    private void handleBtnRegisterOnClick(ActionEvent event) throws IOException {
        if(txtUsername.getText().compareTo("") == 0 || txtPassword.getText().compareTo("") == 0) {
            showAlert(Strings.getString("enter_a_username_and_password"), AlertType.ERROR);
            return;
        }
        serverCom.registerUser(txtUsername.getText(), txtPassword.getText());
    }
    private void showAlert(String text, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(Strings.getString("notification"));
        alert.setContentText(text);
        alert.show();
    }
}
