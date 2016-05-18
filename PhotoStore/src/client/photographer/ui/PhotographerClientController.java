/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer.ui;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import shared.files.PersonalPicture;
import shared.files.PictureGroup;

/**
 * FXML Controller class
 *
 * @author Robin
 */
public class PhotographerClientController implements Initializable {

    @FXML
    private ListView<PersonalPicture> lvBoundUniqueIDs = new ListView();
    @FXML
    private ListView<PictureGroup> lvGroups = new ListView();

    List<PersonalPicture> uniqueNumbers = new ArrayList();
    List<PictureGroup> groupNumbers = new ArrayList();
    List<PersonalPicture> uniqueNumbersSelected = new ArrayList();

    ObservableList observableUniqueNumbers;
    ObservableList observableGroupNumbers;
    ObservableList observavbleUniqueNumbersSelected;

    @FXML
    private TextField tfGroupInfoName = new TextField();

    @FXML
    private TextArea taGroupInfoDescription = new TextArea();
    @FXML
    private Label lblGroupsCount = new Label();
    @FXML
    private Label lblUniqueIDsCount = new Label();

    //WatchService part  +++++++
    @FXML
    private ListView lvImageSelect = new ListView();
    @FXML
    private ImageView ivPictures = new ImageView();
    ArrayList<Path> picturesPath = new ArrayList();
    //WatchService part  +++++++
    PersonalPicture selectedPP;
    PictureGroup selectedPG;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getGroupNumbersLocal();
        getPersonalUniqueCodeLocal();
        initviewsEdit();
        initviewsPictures();

        lvImageSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                System.out.println(lvImageSelect.getSelectionModel().getSelectedItem().toString());
                File file = new File(lvImageSelect.getSelectionModel().getSelectedItem().toString());
                Image img = new Image(file.toURI().toString());
                ivPictures.setImage(img);
            }
        });

        //on click change textfield name and description of selected group number
        lvGroups.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                if (lvGroups.getSelectionModel().getSelectedItem() != null) {
                    tfGroupInfoName.setText(lvGroups.getSelectionModel().getSelectedItem().getName());
                    taGroupInfoDescription.setText(lvGroups.getSelectionModel().getSelectedItem().getDescription());
                    selectedPG = lvGroups.getSelectionModel().getSelectedItem();
                    uniqueNumbersSelected = selectedPG.getPersonalPictures();
                }
                initviewUniqueID();
            }
        });

        lvBoundUniqueIDs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                //lvUniqueLocalIDS
                selectedPP = lvBoundUniqueIDs.getSelectionModel().getSelectedItem();
            }
        });
        lblUniqueIDsCount.setText(Integer.toString(uniqueNumbers.size()));
        lblGroupsCount.setText(Integer.toString(groupNumbers.size()));
    }

    public void reloadPicture() {
        File file = new File(lvImageSelect.getSelectionModel().getSelectedItem().toString());
        Image img = new Image(file.toURI().toString());
        ivPictures.setImage(img);
    }

    public void refresh() {
        // initviewsPictures();
        initviewsEdit();

        // reloadPicture();
    }

    public void initviewsPictures() {
        this.lvImageSelect.setItems(PhotographerClient.client.localfilemanager.getPicture());
    }

    public void initviewsEdit() {

        observableUniqueNumbers = FXCollections.observableArrayList(this.uniqueNumbers);
        observableGroupNumbers = FXCollections.observableArrayList(this.groupNumbers);
        observavbleUniqueNumbersSelected = FXCollections.observableArrayList(this.uniqueNumbersSelected);

        this.lvGroups.setItems(this.observableGroupNumbers);
        this.lvBoundUniqueIDs.setItems(observavbleUniqueNumbersSelected);
    }

    public void initviewUniqueID() {
        observavbleUniqueNumbersSelected = FXCollections.observableArrayList(this.uniqueNumbersSelected);
        this.lvBoundUniqueIDs.setItems(observavbleUniqueNumbersSelected);
    }

    @FXML
    public void bindGroupPicturePersonalPicture() {
        PersonalPicture pp = null;
        if (selectedPG != null) {
            if (uniqueNumbers.size() > 0) {
                int i = 0;
                while (i < 1) {
                    pp = uniqueNumbers.get(i);
                    uniqueNumbers.remove(i);
                    i++;
                }
                if (pp != null) {
                    selectedPG.addPersonalPicture(pp);
                }
            }
        }
        lblUniqueIDsCount.setText(Integer.toString(uniqueNumbers.size()));
        initviewUniqueID();
    }

    @FXML
    public void removeBindGroupPicturePersonalPicture() {
        if (selectedPG != null && selectedPP != null) {
            selectedPG.removePersonalPicture(selectedPP);
            uniqueNumbers.add(selectedPP);
        }
        lblUniqueIDsCount.setText(Integer.toString(uniqueNumbers.size()));
        initviewUniqueID();
    }

    @FXML
    public void createGroups() {
        groupNumbers = PhotographerClientRunnable.clientRunnable.createGroups(PhotographerInfo.photographerID);
        initviewsEdit();
    }

    @FXML
    public void getGroupNumbersLocal() {
        groupNumbers = PhotographerClientRunnable.clientRunnable.getPictureGroupList();
    }

    @FXML
    public void getPersonalUniqueCodeLocal() {
        uniqueNumbers = PhotographerClientRunnable.clientRunnable.getAvailablePersonalPictureList();
    }

    //get UniqueNumbers from online servers
    @FXML
    public void getUniqueNumbersOnline() {

        System.out.println(uniqueNumbers.size());
        initviewsEdit();
    }

    @FXML
    public void uploadEverythingToOnline() {
        PhotographerClientRunnable.clientRunnable.uploadPictureGroups();
    }

    @FXML
    public void changeGroupInfo() {
        selectedPG = lvGroups.getSelectionModel().getSelectedItem();
        selectedPG.setName(tfGroupInfoName.getText());
        lvGroups.getSelectionModel().getSelectedItem().setName(tfGroupInfoName.getText());
        selectedPG.setDescription(taGroupInfoDescription.getText());
        lvGroups.getSelectionModel().getSelectedItem().setDescription(taGroupInfoDescription.getText());

    }

    @FXML
    public void saveAllLocal() {
        PhotographerClientRunnable.clientRunnable.savePictureGroupsToLocal(groupNumbers);

    }

    @FXML
    public void getStuffOnline() {
        PhotographerClientRunnable.clientRunnable.getUniqueNumbers(PhotographerInfo.photographerID);
        PhotographerClientRunnable.clientRunnable.createGroups(PhotographerInfo.photographerID);
    }
}
