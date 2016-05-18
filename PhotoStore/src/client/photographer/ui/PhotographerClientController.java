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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import shared.files.PersonalPicture;
import shared.files.PictureGroup;

/**
 * FXML Controller class
 *
 * @author Robin
 */
public class PhotographerClientController implements Initializable {

    //WatchService part  +++++++
    ObservableList<Path> picturesPath;
    //WatchService part  +++++++

    PersonalPicture selectedPP;
    PictureGroup selectedPG;

    /*
     private ListView<PersonalPicture> lvBoundUniqueIDs = new ListView();
     private ListView<PictureGroup> lvGroups = new ListView();
     */
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
    private ListView lvImageSelect = new ListView();
    @FXML
    private HBox hbGroupPersonalPicture;
    @FXML
    private GridPane gpGroupInfo;
    @FXML
    private Button btnAddGroup;
    @FXML
    private GridPane gpGroups;
    @FXML
    private Button btnAddID;
    @FXML
    private Button btnRemoveID;
    @FXML
    private HBox hbPicture;
    @FXML
    private GridPane gpModifyPictureInfo;
    @FXML
    private TextField tfModifyPictureInfoName;
    @FXML
    private TextField tfModifyPictureInfoPrice;
    @FXML
    private TextField tfModifyPictureInfoFileLocation;
    @FXML
    private TextField tfModifyPictureInfoCreatedOn;
    @FXML
    private Button btnAddPicture;
    @FXML
    private GridPane gpImageSelect;
    @FXML
    private TextField tfImageSelectPathLocation;
    @FXML
    private GridPane gpSavedPictures;
    @FXML
    private ListView<?> lvSavedPictures;
    @FXML
    private Button btnRemovePicture;
    @FXML
    private Button btnSync;
    @FXML
    private Label lblToolBarEmail;
    @FXML
    private Label lblToolBarGroupsRemaining;
    @FXML
    private Label lblToolBarUIDRemaining;
    @FXML
    private Button btnSignOut;
    @FXML
    private ImageView ivImagePreview;
    @FXML
    private TreeView<?> tvGroupsAndUIDs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getGroupNumbersLocal();
        getPersonalUniqueCodeLocal();
        TreeItem ti = new TreeItem();
        tvGroupsAndUIDs.setRoot(ti);
        tvGroupsAndUIDs.setShowRoot(false);
        lvImageSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                System.out.println(lvImageSelect.getSelectionModel().getSelectedItem().toString());
                File file = new File(lvImageSelect.getSelectionModel().getSelectedItem().toString());
                Image img = new Image(file.toURI().toString());
                ivImagePreview.setImage(img);
            }
        });

        //on click change textfield name and description of selected group number
        tvGroupsAndUIDs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem() != null) {
                    if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PictureGroup) {
                        PictureGroup pg = (PictureGroup) tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue();
                        tfGroupInfoName.setText(pg.getName());
                        taGroupInfoDescription.setText(pg.getDescription());
                        selectedPG = pg;
                        uniqueNumbersSelected = selectedPG.getPersonalPictures();
                        ObservableList ob = FXCollections.observableArrayList(pg.getPictures());
                        lvSavedPictures.setItems(ob);
                    }
                }
                if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem() != null) {
                    if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PersonalPicture) {
                        PersonalPicture pp = (PersonalPicture) tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue();
                        ObservableList ob = FXCollections.observableArrayList(pp.getPictures());
                        lvSavedPictures.setItems(ob);
                        selectedPP = pp;
                    }
                }
                initviewUniqueID();
            }
        });
        initviewsEdit();

        /*
         lvBoundUniqueIDs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
         @Override
         public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
         //lvUniqueLocalIDS
         selectedPP = lvBoundUniqueIDs.getSelectionModel().getSelectedItem();
         }
         });
         */
        //lblUniqueIDsCount.setText(Integer.toString(uniqueNumbers.size()));
        //lblGroupsCount.setText(Integer.toString(groupNumbers.size()));
    }

    public void refresh() {
        // initviewsPictures();
        initviewsEdit();

        // reloadPicture();
    }

    public void initviewsEdit() {
        tvGroupsAndUIDs.getRoot().getChildren().clear();
        observableUniqueNumbers = FXCollections.observableArrayList(this.uniqueNumbers);
        observableGroupNumbers = FXCollections.observableArrayList(this.groupNumbers);
        observavbleUniqueNumbersSelected = FXCollections.observableArrayList(this.uniqueNumbersSelected);
        for (PictureGroup pg : PhotographerClient.client.getPictureGroupList()) {
            TreeItem ti = new TreeItem(pg);
            for (PersonalPicture pp : pg.getPersonalPictures()) {
                TreeItem cti = new TreeItem(pp);
                ti.getChildren().add(cti);
            }
            tvGroupsAndUIDs.getRoot().getChildren().add(ti);
        }
        /*
         this.lvGroups.setItems(this.observableGroupNumbers);
         this.lvBoundUniqueIDs.setItems(observavbleUniqueNumbersSelected);
         */
    }

    public void initviewUniqueID() {
        observavbleUniqueNumbersSelected = FXCollections.observableArrayList(this.uniqueNumbersSelected);

        //this.lvBoundUniqueIDs.setItems(observavbleUniqueNumbersSelected);
    }

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
        //lblUniqueIDsCount.setText(Integer.toString(uniqueNumbers.size()));
        initviewUniqueID();
    }

    public void removeBindGroupPicturePersonalPicture() {
        if (selectedPG != null && selectedPP != null) {
            selectedPG.removePersonalPicture(selectedPP);
            uniqueNumbers.add(selectedPP);
        }
        //lblUniqueIDsCount.setText(Integer.toString(uniqueNumbers.size()));
        initviewUniqueID();
    }

    public void createGroups() {
        /*
         groupNumbers = PhotographerClientRunnable.clientRunnable.createGroups(PhotographerInfo.photographerID);
         initviewsEdit();
         */
    }

    public void getGroupNumbersLocal() {
        groupNumbers = PhotographerClient.client.getPictureGroupList();
    }

    public void getPersonalUniqueCodeLocal() {
        uniqueNumbers = PhotographerClient.client.getAvailablePersonalPictureList();
    }

    //get UniqueNumbers from online servers
    public void getUniqueNumbersOnline() {

        System.out.println(uniqueNumbers.size());
        initviewsEdit();
    }

    public void uploadEverythingToOnline() {
        PhotographerClientRunnable.clientRunnable.uploadPictureGroups();
    }

    public void changeGroupInfo() {
        /*
         selectedPG = lvGroups.getSelectionModel().getSelectedItem();
         selectedPG.setName(tfGroupInfoName.getText());
         lvGroups.getSelectionModel().getSelectedItem().setName(tfGroupInfoName.getText());
         selectedPG.setDescription(taGroupInfoDescription.getText());
         lvGroups.getSelectionModel().getSelectedItem().setDescription(taGroupInfoDescription.getText());
         */

    }

    public void saveAllLocal() {
        PhotographerClient.client.savePictureGroupsToLocal(groupNumbers);

    }

    public void getStuffOnline() {
        PhotographerClientRunnable.clientRunnable.getUniqueNumbers(PhotographerInfo.photographerID);
        PhotographerClientRunnable.clientRunnable.createGroups(PhotographerInfo.photographerID);
    }

    @FXML
    public void chooseDirectory() {
        PhotographerClient.client.chooseDirectory();
        if (PhotographerClient.client.localfilemanager != null) {
            picturesPath = PhotographerClient.client.localfilemanager.getPicture();
            this.lvImageSelect.setItems(this.picturesPath);
            tfImageSelectPathLocation.setText(PhotographerClient.client.selectedDirectory.toString());
        }
    }

    public void addUID() {
        selectedPG.addPersonalPicture(uniqueNumbers.get(0));
        uniqueNumbers.remove(0);
        initviewsEdit();
    }

}
