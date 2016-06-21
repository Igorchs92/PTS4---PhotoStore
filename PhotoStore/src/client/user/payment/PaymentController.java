/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user.payment;

import com.stripe.Stripe;
import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

/**
 *
 * @author Robin
 */
public class PaymentController {
    
    //GUI & StoreCart
    StoreGUI gui;
    
    //JS:
    WebEngine webEngine;
    JSObject window;
    JSBridge bridge;
    
    public PaymentController(StoreGUI gui) {
        this.gui= gui;
        
        //API key:
        Stripe.apiKey = "sk_test_TDfA9LcsPq3kYOhoND6PK4TZ";
        
        //JS:
        bridge = new JSBridge(gui);
        WebView browser = new WebView();
        webEngine = browser.getEngine();
        webEngine.load(new File("src\\client\\processcard.html").toURI().toString());
        webEngine.getLoadWorker().stateProperty().addListener(
                    new ChangeListener<Worker.State>(){
                         
                        @Override
                        public void changed(ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) {
                            if(newState == Worker.State.SUCCEEDED){
                                JSObject window = (JSObject)webEngine.executeScript("window");
                                window.setMember("javaApp", bridge);
                                System.out.println("Bridge set up");
                                bridge.setupComplete();
                            }
                        }
        });
        
        
    }
    
    public void setLanguage(String lang, String country) {
        bridge.setLanguage(lang, country);
    }
    
    
    public void processPayment(String CCN, String CVC, String exp_M, String exp_Y) {
        String exec = "handlePayment('"+CCN+"','" + CVC + "','" + exp_M + "','" + exp_Y + "')";
        System.out.println("STR: " + exec);
        webEngine.executeScript(exec);
    }
}