/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user.payment;

/**
 *
 * @author Robin
 */
public interface StoreGUI {
    public void showAttemptingCharge(); //Called while attempting a charge
    public void showChargeSuccess(); //Called when charge successfull
    public void showChargeError(String error); //Called when error during charge
    //public void showValidationError(String error);
    public void showTokenError(String error); //Called when error regarding token creation
    public void bridgeSetupComplete(); //Called when bridge is set up and ready to use
    
}
