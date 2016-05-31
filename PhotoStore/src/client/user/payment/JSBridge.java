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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robin
 */
public class JSBridge {
    StoreGUI gui;
 
    public JSBridge(StoreGUI gui) {
        this.gui = gui;
    }
    
    public void setupComplete() { //Call this to let the GUI know the bridge can be used
        gui.bridgeSetupComplete();
    }
    
    public void recieveError(String error) { //Errors while contacting stripe.js to format token
        System.out.println("Error recieved: " + error);
        gui.showTokenError(error);
    }
    
    public void recieveToken(String token) { //Called when formatted token is recieved
            System.out.println("Token recieved: " + token);
            System.out.println("Attempting charge");
            gui.showAttemptingCharge();
            try {
                Map<String, Object> chargeParams = new HashMap<String, Object>();
                int am = Math.round(StoreCart.getTotal()*100);
                chargeParams.put("amount", am); // amount in cents, again
                chargeParams.put("currency", "eur");
                chargeParams.put("source", token);
                chargeParams.put("description", "Example charge");

                Charge charge = Charge.create(chargeParams);
                gui.showChargeSuccess();
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
