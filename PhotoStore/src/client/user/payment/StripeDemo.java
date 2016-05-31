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
        //MOCK STORECART:
        StoreCart sc = new StoreCart();
        StoreItem i_0= new MockItem("ultra-rare pepe", 20f, 1);
        StoreItem i_1= new MockItem("semi-rare pepe", 10f, 2);
        StoreItem i_2= new MockItem("medium-rare pepe", 5f, 5);
        StoreItem i_3= new MockItem("common-rare pepe", 2.5f, 13);
        //MOCK STORECART:
        sc.addToCart(i_0);
        sc.addToCart(i_1);
        sc.addToCart(i_2);
        sc.addToCart(i_3);
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = (Parent) loader.load();
        FXMLDocumentController controller = loader.getController();
        
        controller.setStoreCart(sc);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
