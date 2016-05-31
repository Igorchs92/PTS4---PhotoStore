/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user.payment;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 *
 * @author Robin
 */
public class FXMLDocumentController implements Initializable, StoreGUI { 
    
    PaymentController pc;
    @FXML
    private Button submit;
    
    @FXML
    private Label ERROR;
    
    @FXML
    private TextField CCN, CVC, EXPM, EXPY;
    
    @FXML
    private ListView CARTLIST = new ListView();
    
    @FXML
    private Label CARTTOTAL;
    
    private StoreCart sc;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        //label.setText("Hello World!");
        pc.processPayment(CCN.getText(), CVC.getText(), EXPM.getText(), EXPY.getText());
        submit.setDisable(true);
        ERROR.setText("Processing card info, please wait.."); //#LOC
    }
    
    public void finishedRequest() {
        submit.setDisable(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    } 
    
    public void setStoreCart(StoreCart sc) {
        this.sc = sc;
        //GUI:
        submit.setDisable(true);
        CARTTOTAL.setText("Total $" + sc.getTotal());
        CARTLIST.getItems().setAll(sc.getList());
        pc = new PaymentController(this, sc);
        setErrorMessage("Connecting to payment processor..", Color.ORANGE); //#LOC
    }
    public void setErrorMessage(String text, Color col) {
        ERROR.setTextFill(col);
        ERROR.setText(text);
    }

    @Override
    public void showAttemptingCharge() {
        System.out.println("[GUI]: Attempting charge..");
        setErrorMessage("Attempting to charge your card..", Color.ORANGE); //#LOC
    }

    @Override
    public void showChargeSuccess() {
        System.out.println("[GUI]: Charge success");
        setErrorMessage("Charge successfull!", Color.GREEN); //#LOC
        
        //CLOSE THIS SCREEN
    }

    @Override
    public void showChargeError(String error) {
        System.out.println("[GUI]: Charge error: " + error);
        setErrorMessage(error, Color.RED);
        submit.setDisable(false);
    }

    @Override
    public void showTokenError(String error) {
        System.out.println("[GUI]: Token error: " + error);
        setErrorMessage(error, Color.RED);
        submit.setDisable(false);
    }

    @Override
    public void bridgeSetupComplete() {
        System.out.println("[GUI]: Bridge setup complete.");
        setErrorMessage("Connected to payment processor.", Color.GREEN); //#LOC
        submit.setDisable(false);
    }
    
}
