/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer.ui;

import client.photographer.PhotographerClientRunnable;
import client.photographer.PhotographerInfo;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initview();

// TODO
    }

    public void initview() {
        observableUniqueNumbers = FXCollections.observableArrayList(this.uniqueNumbers);
        observableGroupNumbers = FXCollections.observableArrayList(this.groupNumbers);
        this.lvUniqueCodes.setItems(this.observableUniqueNumbers);
        this.lvGroupNumber.setItems(this.observableGroupNumbers);
        
        //tfName.setText(lvGroupNumber.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void createGroups() {  
        groupNumbers = PhotographerClientRunnable.clientRunnable.createGroups(PhotographerInfo.photographerID);
        initview();
    }

    @FXML
    public void addGroupToUniqueNumber() {

        //todo
        PhotographerClientRunnable.clientRunnable.addGroupToUniqueNumber(Integer.valueOf(lvGroupNumber.getSelectionModel().getSelectedItem().toString()), Integer.valueOf(lvUniqueCodes.getSelectionModel().getSelectedItem().toString()));    
    }

    @FXML
    public void getUniqueNumbers() {
        uniqueNumbers = PhotographerClientRunnable.clientRunnable.getUniqueNumbers(PhotographerInfo.photographerID);
        System.out.println(uniqueNumbers.size());
        initview();
    }
}
