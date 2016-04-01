/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui;

import client.strings.Strings;
import javafx.scene.control.Alert;

/**
 *
 * @author IGOR
 */
public class InterfaceCall {
    
    public static void showAlert(String text, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(Strings.getString("notification"));
        alert.setContentText(text);
        alert.show();
    }
    
}
