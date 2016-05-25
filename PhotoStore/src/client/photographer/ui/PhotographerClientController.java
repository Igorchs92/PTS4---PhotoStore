/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer.ui;

import client.IClientRunnable;
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
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
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
    @FXML
    private ProgressBar pbProgress;

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
                System.out.println(PhotographerClient.client.selectedDirectory.toString() + "\\" + lvImageSelect.getSelectionModel().getSelectedItem().toString());
                File file = new File(PhotographerClient.client.selectedDirectory.toString() + "\\" + lvImageSelect.getSelectionModel().getSelectedItem().toString());
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
                tfModifyPictureInfoFileLocation.setText(PhotographerClient.client.selectedDirectory.toString() + "\\" + lvImageSelect.getSelectionModel().getSelectedItem().toString());

                Path file = Paths.get(PhotographerClient.client.selectedDirectory.toString() + "\\" + lvImageSelect.getSelectionModel().getSelectedItem().toString());
                try {
                    BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                    tfModifyPictureInfoCreatedOn.setText(new java.util.Date(attr.creationTime().toMillis()).toGMTString());
                } catch (IOException ex) {
                    Logger.getLogger(PhotographerClientController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        lvSavedPictures.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                if (lvSavedPictures.getSelectionModel().getSelectedItem() != null) {
                    tfModifyPictureInfoFileLocation.setText(lvSavedPictures.getSelectionModel().getSelectedItem().getLocation());

                    File file = new File(lvSavedPictures.getSelectionModel().getSelectedItem().getLocation());
                    Image img = new Image(file.toURI().toString());
                    ivImagePreview.setImage(img);

                    Path filepath = Paths.get(lvSavedPictures.getSelectionModel().getSelectedItem().getLocation());
                    try {
                        BasicFileAttributes attr = Files.readAttributes(filepath, BasicFileAttributes.class);
                        tfModifyPictureInfoCreatedOn.setText(new java.util.Date(attr.creationTime().toMillis()).toGMTString());
                    } catch (IOException ex) {
                        Logger.getLogger(PhotographerClientController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    tfModifyPictureInfoName.setText("");
                    tfModifyPictureInfoPrice.setText("");
                    tfModifyPictureInfoFileLocation.setText("");
                    tfModifyPictureInfoCreatedOn.setText("");
                }

            }
        });

        lblToolBarUIDRemaining.setText(Integer.toString(personalIDS.size()));
        lblToolBarGroupsRemaining.setText(Integer.toString(groupIDS.size()));
        initviews();
    }

    public void initviews() {
        final Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                tvGroupsAndUIDs.getRoot().getChildren().clear();
                observablePictureGroups = FXCollections.observableArrayList(pictureGroups);
                for (PictureGroup pg : pictureGroups) {
                    TreeItem ti = new TreeItem(pg);
                    for (PersonalPicture pp : pg.getPersonalPictures()) {
                        TreeItem cti = new TreeItem(pp);
                        ti.getChildren().add(cti);
                    }
                    tvGroupsAndUIDs.getRoot().getChildren().add(ti);
                }
                Platform.runLater(() -> {
                    lblToolBarUIDRemaining.setText(Integer.toString(personalIDS.size()));
                    lblToolBarGroupsRemaining.setText(Integer.toString(groupIDS.size()));
                });
                return null;
            }
        };
        new Thread(task).start();
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
        if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem() != null) {
            if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PersonalPicture) {
                System.out.println(selectedPP.getId());
                personalIDS.add(selectedPP.getId());
                selectedPG.removePersonalPicture(selectedPP);
            } else if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PictureGroup) {
                System.out.println(selectedPG.getId());
                groupIDS.add(selectedPG.getId());
                pictureGroups.remove(selectedPP);
            }
        }
        saveAllLocal();
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
                    System.out.println("Add personal id working");
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
            if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PersonalPicture) {
                selectedPP.addPicture(p);
                ObservableList ob = FXCollections.observableArrayList(selectedPP.getPictures());
                lvSavedPictures.setItems(ob);
            } else if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PictureGroup) {
                selectedPG.addPicture(p);
                ObservableList ob = FXCollections.observableArrayList(selectedPG.getPictures());
                lvSavedPictures.setItems(ob);
            }
            saveAllLocal();
        } else {
            InterfaceCall.showAlert(Alert.AlertType.INFORMATION, "You have entered an invalid price.");
        }
    }

    @FXML
    public void removePicture() {
        if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PictureGroup) {
            if (selectedPG != null) {
                selectedPG.removePicture(lvSavedPictures.getSelectionModel().getSelectedItem());
            }
            ObservableList ob = FXCollections.observableArrayList(selectedPG.getPictures());
            lvSavedPictures.setItems(ob);
        } else if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PersonalPicture) {
            if (selectedPP != null) {
                selectedPP.removePicture(lvSavedPictures.getSelectionModel().getSelectedItem());
            }
            ObservableList ob = FXCollections.observableArrayList(selectedPP.getPictures());
            lvSavedPictures.setItems(ob);
        }
    }

    @FXML
    public void logout() {
        PhotographerClient.client.setSceneLogin();
    }

    public void autoLogin() {

        PhotographerClient.client.connectToServer();
        IClientRunnable clientRunnable;
        clientRunnable = PhotographerClientRunnable.clientRunnable;
        System.out.println(PhotographerInfo.photographerID + PhotographerInfo.photographerPass);
        clientRunnable.login(PhotographerInfo.photographerID, PhotographerInfo.photographerPass);
    }

    @FXML
    public void sync() {
        final Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                updateProgress(0, 5);
                System.out.println("autologin");
                autoLogin();
                updateProgress(1, 5);
                System.out.println("uploading picture group");
                PhotographerClientRunnable.clientRunnable.uploadPictureGroups();
                updateProgress(2, 5);
                System.out.println("get group id's");
                PhotographerClientRunnable.clientRunnable.getGroupIDs(PhotographerInfo.photographerID);
                updateProgress(3, 4);
                System.out.println("get personal id's");
                PhotographerClientRunnable.clientRunnable.getPersonalIDs(PhotographerInfo.photographerID);
                updateProgress(4, 5);
                System.out.println("initialise views");
                loadLocalDatabaseInformation();
                initviews();
                updateProgress(5, 5);
                return null;
            }
        };
        pbProgress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();

    }

    public void saveAllLocal() {
        final Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                updateProgress(0, 3);
                PhotographerClient.client.saveGroupIDToLocal(groupIDS);
                updateProgress(1, 3);
                PhotographerClient.client.savePersonalPictureToLocal(personalIDS);
                updateProgress(2, 3);
                PhotographerClient.client.savePictureGroupsToLocal(pictureGroups);
                updateProgress(3, 3);
                return null;
            }
        };
        pbProgress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();

    }

    public void loadLocalDatabaseInformation() {
        pictureGroups = PhotographerClient.client.getPictureGroupList();
        groupIDS = PhotographerClient.client.getAvailablGroupIDList();
        personalIDS = PhotographerClient.client.getAvailablePersonalIDList();
    }
}
