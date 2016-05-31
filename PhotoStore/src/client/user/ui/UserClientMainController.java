/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user.ui;

import client.user.ClientInfo;
import client.user.UserClientRunnable;
import client.user.editphoto.EditPicureApplication;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shared.files.PersonalPicture;
import shared.files.Picture;
import shared.files.PictureGroup;

/**
 * FXML Controller class
 *
 * @author IGOR
 */
public class UserClientMainController implements Initializable {

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDownload;
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
    
    List<PictureGroup> pictureGroups;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        paneScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        paneScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);        
    }  
    
    @FXML
    public void attachCodeToAccount(){
        UserClientRunnable.clientRunnable.attachCode(ClientInfo.clientID, Integer.valueOf(tfPersonalCode.getText()));
    }
    
    @FXML
    public void redownloadPictures() throws FileNotFoundException {
        // first empty the screen
        paneScrollPane.setContent(null);
        listViewAlbums.setItems(null);
        
        // then download the pictures
        pictureGroups = UserClientRunnable.clientRunnable.download();
        
        // show the pictureGroups in the listview;
        
        ObservableList<PictureGroup> obsPictureGroups = FXCollections.observableArrayList(pictureGroups);
        listViewAlbums.setItems(obsPictureGroups);
    }
    
    @FXML
    public void listViewPictureGroupClicked(MouseEvent arg0) throws FileNotFoundException {
        PictureGroup pg = (PictureGroup)listViewAlbums.getSelectionModel().getSelectedItem();
        if (pg == null) return;
        showPictures(pg);
        
        
    }
    
    public void showPictures(PictureGroup pg) throws FileNotFoundException {
              
        List<ImageView> images = new ArrayList<ImageView>();

        for (Picture p : pg.getPictures()) {
            Image i = new Image(new FileInputStream(p.getLocation()));
                ImageView view = new ImageView(i);
                images.add(view);
                view.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        UserClientRunnable.clientRunnable.pictureToEdit = p;
                        
                        EditPicureApplication edit = new EditPicureApplication();
                        Stage stage = new Stage();
                        edit.start(stage);
                        stage.show();
                    }
            });
        }
         
        for (PersonalPicture pp: pg.getPersonalPictures()) {
            for (Picture p : pp.getPictures()) {
                Image i = new Image(new FileInputStream(p.getLocation()));
                ImageView view = new ImageView(i);
                images.add(view);
                view.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        UserClientRunnable.clientRunnable.pictureToEdit = p;
                        
                        EditPicureApplication edit = new EditPicureApplication();
                        Stage stage = new Stage();
                        edit.start(stage);
                        stage.show();
                    }
                });  
            }
        }
        
        putPicturesOnScreen(images.size(), images);
    }
    
    /**
     * 
     * @param amountImages the amount of images that must be shown
     * @param images the images that should be shown, the amount of images has to be equal to the first parameter
     */
    private void putPicturesOnScreen(int amountImages, List<ImageView> images) {
        try {
            ///// 4 images per row, as many rows as needed        
            List<Node> vBoxNodes = new ArrayList<Node>();
            for (int i = 0; i < amountImages/4d; i++) {
                List<Node> hBoxNodes = new ArrayList<Node>();
                for(int j = 0; j < Math.min(4, amountImages-(4d*i)); j++) {

                        ImageView view = images.get(i*4+j);
                        hBoxNodes.add(view);

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
        }
        catch(Exception ex){}
    }
            
}