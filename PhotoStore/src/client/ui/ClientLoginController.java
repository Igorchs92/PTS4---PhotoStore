/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui;

import client.ClientConnector;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Button btnLogIn;
    
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
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO make UI elements multi-language
    }    
    
    @FXML
    private void handleBtnLoginOnClick(ActionEvent event) {
        // TODO: login
        // set below boolean to true if login succeeds
        ClientConnector.loggedIn = false;
        // TODO: remove this window and go back to the old window
    }
    
    @FXML
    private void handleBtnRegisterOnClick(ActionEvent event) {
        // TODO: goto register ui
        switch (ClientConnector.clientType) {
            case producer: {
                
            }
            case photographer: {
                
            }
            case user: {
                
            }
        }
    }
    
}
