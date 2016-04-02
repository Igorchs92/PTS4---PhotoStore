/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.producer.ui;

import client.producer.ProducerClientRunnable;
import client.ui.InterfaceCall;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author IGOR
 */
public class ProducerClientRegisterController implements Initializable {

    private Stage stage;
    @FXML
    private TextField bEmail;
    @FXML
    private PasswordField bPassword;
    @FXML
    private TextField bName;
    @FXML
    private PasswordField bConfirmPassword;
    @FXML
    private ImageView imgCountryFlag;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleCancelButtonAction(MouseEvent event) {
        stage.close();
    }

        @FXML
    private void handleConfirmButtonAction(MouseEvent event) {
        if (validateForm()) {
            if (ProducerClientRunnable.clientRunnable.registerUser(bEmail.getText(), bPassword.getText(), bName.getText())) {
                InterfaceCall.showAlert(Alert.AlertType.CONFIRMATION, "Register completed", "You have registered with: " + bEmail.getText());
                stage.close();
            } else {
                InterfaceCall.showAlert(Alert.AlertType.INFORMATION, "You have already registered with the given e-mail address.");
            }
        }
    }

    public boolean validateForm() {
        if (bEmail.getText().isEmpty() || bPassword.getText().isEmpty() || bName.getText().isEmpty() ||  bConfirmPassword.getText().isEmpty()) {
            InterfaceCall.showAlert(Alert.AlertType.INFORMATION, "All of the forms are required in order to complete your registration.");
            return false;
        }
        if (!InterfaceCall.doStringsMatch(bPassword.getText(), bConfirmPassword.getText())){
            InterfaceCall.showAlert(Alert.AlertType.INFORMATION, "Password doesn't match.");
            return false;
        }
        return true;
    }

    public void setDialogStage(Stage stage) {
        this.stage = stage;
    }
}
