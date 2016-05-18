/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user.ui;

import client.user.ClientInfo;
import client.user.UserClientRunnable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author IGOR
 */
public class UserClientMainController implements Initializable {

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnShoppingCart;
    @FXML
    private Button btnLogOut;
    @FXML
    private TextField tfPersonalCode;
    @FXML
    private ScrollPane paneScrollPane;
    @FXML
    private ListView listViewAlbums;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            
        /// below should be the amount of real images to show, this is a test amount
        int amountImages = 13;
        /// in the list below should be the images to show, the amount of images
        // must be exact the same as the integer above.
        List<ImageView> images = new ArrayList<ImageView>();
        
        for (int a = 0; a < amountImages; a++) {
            images.add(new ImageView(new Image(new FileInputStream("C:\\Users\\martijn\\Desktop\\test.png"))));
        }
        
        ///// 9 images per row, as many rows as needed        
        List<Node> vBoxNodes = new ArrayList<Node>();
        for (int i = 0; i < amountImages/4d; i++) {
            List<Node> hBoxNodes = new ArrayList<Node>();
            for(int j = 0; j < Math.min(4, amountImages-(4d*i)); j++) {
                
                    
                    hBoxNodes.add(images.get(i*4+j));
                
            }
            Node[] nodes = new Node[hBoxNodes.size()];
            for (int h = 0; h < hBoxNodes.size(); h++) {
                nodes[h] = hBoxNodes.get(h);
            }
            HBox h = new HBox(25, nodes);
            vBoxNodes.add(h);
        }
        
        Node[] nodess = new Node[vBoxNodes.size()];
            for (int h = 0; h < vBoxNodes.size(); h++) {
                nodess[h] = vBoxNodes.get(h);
            }
            VBox v = new VBox(25, nodess);
        
        paneScrollPane.setContent(v);
        paneScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        paneScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        } catch (FileNotFoundException ex) {
                    Logger.getLogger(UserClientMainController.class.getName()).log(Level.SEVERE, null, ex);
                }
    }  
    
    @FXML
    public void attachCodeToAccount(){
        UserClientRunnable.clientRunnable.attachCode(ClientInfo.clientID, Integer.valueOf(tfPersonalCode.getText()));
    }
}