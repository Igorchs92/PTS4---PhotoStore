/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user.payment;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Robin
 */
public class StripeDemo extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {       
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = (Parent) loader.load();
        FXMLDocumentController controller = loader.getController();
        
        StoreCart.addToCart(new Item("test", 1.50f, 1));
        
        controller.setStoreCart();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }

    /*
    public static void main(String[] args) {
        launch(args);
    } 
    */
}
