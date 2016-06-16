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
    public void showAttemptingProcessing(String msg);
    public void showAttemptingCharge(String msg); //Called while attempting a charge
    public void showChargeSuccess(String msg); //Called when charge successfull
    public void showChargeError(String error); //Called when error during charge
    public void showValidationError(String error, String loc);
    public void showTokenError(String error); //Called when error regarding token creation
    public void bridgeSetupComplete(String msg); //Called when bridge is set up and ready to use
    
}
