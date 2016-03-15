/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IGOR
 */
public class Log {
    protected static final Logger LOG = Logger.getAnonymousLogger();
    
    public static void exception(Exception ex){
        LOG.log(Level.SEVERE, null, ex);
    }
    
    public static void info(String s, Object obj){
        LOG.log(Level.INFO, s, obj);
    }
    
    public static void info(Object obj){
        LOG.log(Level.INFO, null, obj);
    }
    
    public static void warning(String s, Object obj){
        LOG.log(Level.WARNING, s, obj);
    }
    
    public static void warning(Object obj){
        LOG.log(Level.WARNING, null, obj);
    }
}
