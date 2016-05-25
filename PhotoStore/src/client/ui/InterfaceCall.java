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

    public static void showAlert(Alert.AlertType type, String header) {
        Alert alert = new Alert(type);
        alert.setTitle(Strings.getString("notification"));
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    public static void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(Strings.getString("notification"));
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void connectionFailed() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Connection failed");
        alert.setHeaderText("Failed to connect to the server");
        alert.setContentText("Check your internet connection and try again");
        alert.showAndWait();
    }

    public static boolean isValidEmailAddress(String s) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(s);
        return m.matches();
    }

    public static boolean doStringsMatch(String s1, String s2) {
        return s1.equals(s2);
    }

    public static boolean isNumeric(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c) || c == '-') {
                return false;
            }
        }
        return true;
    }
    
        public static boolean isDouble(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c) || c == '.') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isPhone(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c) || c == '-' || c == '+') {
                return false;
            }
        }
        return true;
    }
    
}
