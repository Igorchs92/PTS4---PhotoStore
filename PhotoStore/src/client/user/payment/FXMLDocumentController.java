/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user.payment;
import client.user.UserClientRunnable;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
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
    
    
    //INT:
    String lang;
    String country;
    String lastError="";
    
    Locale curLoc;
    ResourceBundle msgs;
    
    @FXML
    TitledPane LOCoverview, LOCdetails;
    @FXML
    Label LOClblCCN, LOClblCVC, LOClblEXP, CARTADDREM;
    @FXML
    Button btnEN, btnNL;
    @FXML
    Button CARTADD, CARTREMOVE;
    
    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        StoreItem i = (StoreItem) CARTLIST.getSelectionModel().getSelectedItem();
        int idx = CARTLIST.getSelectionModel().getSelectedIndex();
        if(i == null)
            return;
        i.setQuantity(i.getQuantity()+1);
        CARTLIST.getItems().setAll(StoreCart.getList());
        CARTTOTAL.setText(msgs.getString("TOTAL") + StoreCart.getTotal());
        CARTLIST.getSelectionModel().select(idx);
    }
    
    @FXML
    private void handleRemoveButtonAction(ActionEvent event) {
        StoreItem i = (StoreItem) CARTLIST.getSelectionModel().getSelectedItem();
        int idx = CARTLIST.getSelectionModel().getSelectedIndex();
        if(i == null)
            return;
        if(i.getQuantity()-1 <= 0) {
            StoreCart.removeFromCart(i);
        }else {
            i.setQuantity(i.getQuantity()-1);
        }
        CARTLIST.getItems().setAll(StoreCart.getList());
        CARTTOTAL.setText(msgs.getString("TOTAL") + StoreCart.getTotal());
        CARTLIST.getSelectionModel().select(idx);
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        //System.out.println("You clicked me!");
        //label.setText("Hello World!");
        //ERROR.setText("Processing card info, please wait.."); //#LOC
        submit.setDisable(true);
        pc.processPayment(CCN.getText(), CVC.getText(), EXPM.getText(), EXPY.getText());
    }
    
    @FXML
    private void handleENClick(ActionEvent event) {
        btnEN.setDisable(true);
        btnNL.setDisable(false);
        setLanguage("en", "EN");
    }
    
    @FXML
    private void handleNLClick(ActionEvent event) {
        btnEN.setDisable(false);
        btnNL.setDisable(true);
        setLanguage("nl", "NL");
    }
    
    public void setLanguage(String lang, String country) {
        this.lang= lang;
        this.country=country;
        
        curLoc = new Locale(lang, country);
        msgs = ResourceBundle.getBundle("client/ErrorBundle", curLoc);
        
        LOCoverview.setText(msgs.getString("LOCoverview"));
        LOCdetails.setText(msgs.getString("LOCdetails"));
        
        LOClblCCN.setText(msgs.getString("LOClblCCN"));
        LOClblCVC.setText(msgs.getString("LOClblCVC"));
        LOClblEXP.setText(msgs.getString("LOClblEXP"));
        
        CCN.setPromptText(msgs.getString("CCN"));
        CVC.setPromptText(msgs.getString("CVC"));
        EXPM.setPromptText(msgs.getString("EXP_M"));
        EXPY.setPromptText(msgs.getString("EXP_Y"));
        
        CARTTOTAL.setText(msgs.getString("TOTAL") + StoreCart.getTotal()); //@#LOC
        
        submit.setText(msgs.getString("SUBMIT"));
        
        CARTADDREM.setText(msgs.getString("CARTADDREM"));
        
        if(msgs.containsKey(lastError)) {
            Color c= (Color) ERROR.getTextFill();
            setErrorMessage(msgs.getString(lastError), c);
        }
            
        pc.setLanguage(lang, country);
    }
    
    public void finishedRequest() { 
        submit.setDisable(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    } 
    
    public void setStoreCart() {
        //GUI:
        submit.setDisable(true);
        CARTLIST.getItems().setAll(StoreCart.getList());
        pc = new PaymentController(this);
        setErrorMessage("Connecting..", Color.ORANGE); //#LOC
        setLanguage("en", "EN");
        btnEN.setDisable(true);
        btnNL.setDisable(false);
    }
    public void setErrorMessage(String text, Color col) {
        ERROR.setTextFill(col);
        ERROR.setText(text);
    }

    @Override
    public void showAttemptingCharge(String msg) {
        lastError= "attemptingCharge";
        System.out.println("[GUI]: Attempting charge..");
        setErrorMessage(msg, Color.ORANGE); //#LOC
    }

    @Override
    public void showChargeSuccess(String msg) {
        lastError= "chargeSuccess";
        System.out.println("[GUI]: Charge success");
        setErrorMessage(msg, Color.GREEN); //#LOC
        UserClientRunnable.clientRunnable.upload();
        StoreCart.clear();        
    }

    @Override
    public void showChargeError(String error) {
        //Not able to localize
        System.out.println("[GUI]: Charge error: " + error);
        setErrorMessage(error, Color.RED);
        submit.setDisable(false);
    }

    @Override
    public void showTokenError(String error) {
        //Not able to localize
        System.out.println("[GUI]: Token error: " + error);
        setErrorMessage(error, Color.RED);
        submit.setDisable(false);
    }

    @Override
    public void bridgeSetupComplete(String msg) {
        lastError= "setupComplete";
        System.out.println("[GUI]: Bridge setup complete.");
        setErrorMessage(msg, Color.GREEN); //#LOC
        submit.setDisable(false);
    }

    @Override
    public void showValidationError(String error, String loc) {
        lastError=loc;
        System.out.println("[GUI]: Validation error!");
        setErrorMessage(error, Color.RED);
        submit.setDisable(false);
    }

    @Override
    public void showAttemptingProcessing(String msg) {
        lastError="attemptingProcessing";
        System.out.println("[GUI]: Attempting processing.");
        setErrorMessage(msg, Color.ORANGE);
    }
    
}
