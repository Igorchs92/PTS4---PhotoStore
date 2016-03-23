/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.strings;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author martijn
 */
public final class Strings {
    private static Locale l;
    private static ResourceBundle bundle;
    private static Strings strings = null;
    
    private Strings() {
        l = Locale.getDefault();
        try {
        bundle = ResourceBundle.getBundle("client.strings.StringBundle", l);
        }
        catch (MissingResourceException ex) {
            bundle = ResourceBundle.getBundle("StringBundle");
        }
    }
    
    /**
     * Gets a string for the given key from the resource bundle in the systems language (if existing).
     * @param key the key for the desired string
     * @return the string for the given key
     */
    public static String getString(String key) {
        if (strings == null) strings = new Strings();
        try {
            return bundle.getString(key);
        }
        catch(Exception ex){
            return "";
        }
    }
}
