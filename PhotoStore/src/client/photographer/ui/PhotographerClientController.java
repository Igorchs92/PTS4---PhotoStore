/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer.ui;

import client.photographer.FileScanner;
import client.photographer.PhotographerClient;
import client.photographer.PhotographerClientRunnable;
import client.photographer.PhotographerInfo;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Robin
 */
public class PhotographerClientController implements Initializable {

    @FXML
    private TextField tfName;
    @FXML
    private TextArea tfDescription;
    @FXML
    private ListView lvUniqueCodes = new ListView();
    @FXML
    private ListView lvGroupNumber = new ListView();

    List<Integer> uniqueNumbers = new ArrayList();
    List<Integer> groupNumbers = new ArrayList();
    ObservableList observableUniqueNumbers;
    ObservableList observableGroupNumbers;

    //WatchService part  +++++++
    @FXML
    private ListView lvPictures = new ListView();
    @FXML
    private ImageView ivPictures = new ImageView();
    ArrayList<Path> picturesPath = new ArrayList();

    ObservableList observablePicture;
    //WatchService part  +++++++

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        picturesPath = PhotographerClient.client.localfilemanager.getPicture();
        System.out.println("Aantal pictures: " + picturesPath.size());
        initviewsEdit();
        initviewsPictures();

        lvPictures.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {

                File file = new File(lvPictures.getSelectionModel().getSelectedItem().toString());
                Image img = new Image(file.toURI().toString());
                ivPictures.setImage(img);
            }
        });
    }

    public void reloadPicture() {
        File file = new File(lvPictures.getSelectionModel().getSelectedItem().toString());
        Image img = new Image(file.toURI().toString());
        ivPictures.setImage(img);
    }

    public void refresh() {
        initviewsPictures();
        initviewsEdit();
        reloadPicture();
    }

    public void initviewsPictures() {
        observablePicture = FXCollections.observableArrayList(this.picturesPath);
        this.lvPictures.setItems(this.observablePicture);
    }

    public void initviewsEdit() {
        observableUniqueNumbers = FXCollections.observableArrayList(this.uniqueNumbers);
        observableGroupNumbers = FXCollections.observableArrayList(this.groupNumbers);
        this.lvUniqueCodes.setItems(this.observableUniqueNumbers);
        this.lvGroupNumber.setItems(this.observableGroupNumbers);

        //tfName.setText(lvGroupNumber.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void createGroups() {
        groupNumbers = PhotographerClientRunnable.clientRunnable.createGroups(PhotographerInfo.photographerID);
        initviewsEdit();
    }

    @FXML
    public void addGroupToUniqueNumber() {
        PhotographerClientRunnable.clientRunnable.addGroupToUniqueNumber(Integer.valueOf(lvGroupNumber.getSelectionModel().getSelectedItem().toString()), Integer.valueOf(lvUniqueCodes.getSelectionModel().getSelectedItem().toString()));
    }

    @FXML
    public void getUniqueNumbers() {
        uniqueNumbers = PhotographerClientRunnable.clientRunnable.getUniqueNumbers(PhotographerInfo.photographerID);
        System.out.println(uniqueNumbers.size());
        initviewsEdit();
    }
}
