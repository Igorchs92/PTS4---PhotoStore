/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer.ui;

import client.photographer.PhotographerClient;
import client.photographer.PhotographerClientRunnable;
import client.photographer.PhotographerInfo;
import client.ui.InterfaceCall;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import shared.files.Picture;
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
    List<Integer> groupIDS = new ArrayList();
    List<Integer> personalIDS = new ArrayList();
    List<PictureGroup> pictureGroups = new ArrayList();
    List<PersonalPicture> selectedPersonalIDS = new ArrayList();

    ObservableList observablePersonalIDS;
    ObservableList observablePictureGroups;

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
    private ListView<Picture> lvSavedPictures;
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
        loadLocalDatabaseInformation();
        lblToolBarEmail.setText(PhotographerInfo.photographerID);
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
                        selectedPersonalIDS = selectedPG.getPersonalPictures();
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
            }
        });

        lvImageSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                tfModifyPictureInfoFileLocation.setText(lvImageSelect.getSelectionModel().getSelectedItem().toString());

                Path file = Paths.get(lvImageSelect.getSelectionModel().getSelectedItem().toString());
                try {
                    BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                    tfModifyPictureInfoCreatedOn.setText(attr.creationTime().toString());
                } catch (IOException ex) {
                    Logger.getLogger(PhotographerClientController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        lblToolBarUIDRemaining.setText(Integer.toString(personalIDS.size()));
        lblToolBarGroupsRemaining.setText(Integer.toString(groupIDS.size()));
        initviews();
    }

    public void initviews() {
        tvGroupsAndUIDs.getRoot().getChildren().clear();
        observablePictureGroups = FXCollections.observableArrayList(this.pictureGroups);
        for (PictureGroup pg : pictureGroups) {
            TreeItem ti = new TreeItem(pg);
            for (PersonalPicture pp : pg.getPersonalPictures()) {
                TreeItem cti = new TreeItem(pp);
                ti.getChildren().add(cti);
            }
            tvGroupsAndUIDs.getRoot().getChildren().add(ti);
        }
        lblToolBarUIDRemaining.setText(Integer.toString(personalIDS.size()));
        lblToolBarGroupsRemaining.setText(Integer.toString(groupIDS.size()));
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

    @FXML
    public void chooseDirectory() {
        PhotographerClient.client.chooseDirectory();
        if (PhotographerClient.client.localfilemanager != null) {
            picturesPath = PhotographerClient.client.localfilemanager.getPicture();
            this.lvImageSelect.setItems(this.picturesPath);
            tfImageSelectPathLocation.setText(PhotographerClient.client.selectedDirectory.toString());
        }
    }

    @FXML
    public void removeUID() {
        if (selectedPG != null && selectedPP != null) {
            System.out.println(selectedPP.getId());
            personalIDS.add(selectedPP.getId());
            selectedPG.removePersonalPicture(selectedPP);
        }
        initviews();
    }

    @FXML
    public void addUID() {
        PersonalPicture pp = null;
        if (selectedPG != null) {
            if (personalIDS.size() > 0) {
                int i = 0;
                while (i < 1) {
                    pp = new PersonalPicture(personalIDS.get(i));
                    PhotographerClient.client.getLocalDatabase().deletePersonalID(personalIDS.get(i));
                    personalIDS.remove(i);
                    i++;
                }
                if (pp != null) {
                    selectedPG.addPersonalPicture(pp);
                }
            }
        }
        initviews();
        saveAllLocal();
    }

    @FXML
    public void addGroup() {
        PictureGroup pg = null;
        if (groupIDS.size() > 0) {
            int i = 0;
            while (i < 1) {
                pg = new PictureGroup(groupIDS.get(i));
                pg.setName(tfGroupInfoName.getText());
                pg.setDescription(taGroupInfoDescription.getText());
                PhotographerClient.client.getLocalDatabase().deleteGroupID(groupIDS.get(i));
                groupIDS.remove(i);
                i++;
            }
            if (pg != null) {
                pictureGroups.add(pg);
                tfGroupInfoName.setText("");
                taGroupInfoDescription.setText("");
                System.out.println("Add group working");
            }
        }
        initviews();
        saveAllLocal();
    }

    @FXML
    public void savePicture() {

        if (InterfaceCall.isDouble(tfModifyPictureInfoPrice.getText())) {
            String location = tfModifyPictureInfoFileLocation.getText();
            String name = tfModifyPictureInfoName.getText();
            double price = Double.valueOf(tfModifyPictureInfoPrice.getText());
            Picture p = new Picture(location, name, price);
            selectedPP.addPicture(p);
            saveAllLocal();
        }

    }

    @FXML
    public void removePicture() {
        if (selectedPP != null) {
            selectedPP.removePicture(lvSavedPictures.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    public void logout() {
        PhotographerClient.client.setSceneLogin();
    }

    
    public void autoLogin(){
        
    }
    
    @FXML
    public void sync() {
        PhotographerClientRunnable.clientRunnable.uploadPictureGroups();
        PhotographerClientRunnable.clientRunnable.getGroupIDs(PhotographerInfo.photographerID);
        PhotographerClientRunnable.clientRunnable.getPersonalIDs(PhotographerInfo.photographerID);
    }

    public void saveAllLocal() {
        PhotographerClient.client.saveGroupIDToLocal(groupIDS);
        PhotographerClient.client.savePersonalPictureToLocal(personalIDS);
        PhotographerClient.client.savePictureGroupsToLocal(pictureGroups);

    }

    public void loadLocalDatabaseInformation() {
        pictureGroups = PhotographerClient.client.getPictureGroupList();
        groupIDS = PhotographerClient.client.getAvailablGroupIDList();
        personalIDS = PhotographerClient.client.getAvailablePersonalIDList();
    }
}
