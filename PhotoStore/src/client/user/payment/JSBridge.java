/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user.payment;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robin
 */
public class JSBridge {
    StoreGUI gui;
 
    String lang;
    String country;
    
    Locale curLoc;
    ResourceBundle msgs;
    
    public JSBridge(StoreGUI gui) {
        this.gui = gui;
        this.lang= "en";
        this.country = "EN";
        
        curLoc = new Locale(lang, country);
        msgs = ResourceBundle.getBundle("client/ErrorBundle", curLoc);
    }
    
    public void attemptingProcessing() {
        gui.showAttemptingProcessing(msgs.getString("attemptingProcessing"));
    }
    
    public void setLanguage(String lang, String country) {
        this.lang = lang;
        this.country= country;
        curLoc = new Locale(lang, country);
        msgs = ResourceBundle.getBundle("client/ErrorBundle", curLoc);
    }
    
    public void setupComplete() { //Call this to let the GUI know the bridge can be used
        gui.bridgeSetupComplete(msgs.getString("setupComplete"));
    }
    
    public void recieveError(String error) { //Errors while contacting stripe.js to format token
        System.out.println("Error recieved: " + error);
        gui.showTokenError(error);
    }
    
    public void recieveValidationError(int err) {
        switch(err) {
            case 0://Card number
                gui.showValidationError(msgs.getString("validationCard"),"validationCard");
                break;
            case 1://CVC
                gui.showValidationError(msgs.getString("validationCVC"), "validationCVC");
                break;
            case 2://Expiry
                gui.showValidationError(msgs.getString("validationExp"), "validationExp");
                break;
        }
    }
    
    public void recieveToken(String token) { //Called when formatted token is recieved
            gui.showAttemptingCharge(msgs.getString("attemptingCharge"));
            System.out.println("Token recieved: " + token);
            System.out.println("Attempting charge");
            try {
                Map<String, Object> chargeParams = new HashMap<String, Object>();
                int am = Math.round(StoreCart.getTotal()*100);
                chargeParams.put("amount", am); // amount in cents, again
                chargeParams.put("currency", "eur");
                chargeParams.put("source", token);
                chargeParams.put("description", "Example charge");

                Charge charge = Charge.create(chargeParams);
                gui.showChargeSuccess(msgs.getString("chargeSuccess"));
            } catch (CardException e) {
                System.out.println("Charge failed");
                Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, null, e);
                gui.showChargeError(e.getLocalizedMessage());
            } catch (AuthenticationException ex) {
                Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, null, ex);
                gui.showChargeError(ex.getLocalizedMessage());
            } catch (InvalidRequestException ex) {
                Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, null, ex);
                gui.showChargeError(ex.getLocalizedMessage());
            } catch (APIConnectionException ex) {
                Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, null, ex);
                gui.showChargeError(ex.getLocalizedMessage());
            } catch (APIException ex) {
                Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, null, ex);
                gui.showChargeError(ex.getLocalizedMessage());
            }
    }
}
