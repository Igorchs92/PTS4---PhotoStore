/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user.ui;

import client.user.ClientInfo;
import client.user.UserClientRunnable;
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
 * @author IGOR
 */
public class UserClientMainController implements Initializable {

    @FXML
    private Button btAccept;
    @FXML
    private TextField tfPersonalCode;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }
    
    @FXML
    public void attachCodeToAccount(){
        UserClientRunnable.clientRunnable.attachCode(ClientInfo.clientID, Integer.valueOf(tfPersonalCode.getText()));
    }
}