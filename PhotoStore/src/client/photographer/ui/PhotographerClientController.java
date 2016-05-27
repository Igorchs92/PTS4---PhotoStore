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
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.FocusModel;
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
import javafx.util.Pair;
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
    PhotographerClient client;
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
    @FXML
    private Button btnSavePicture;
    @FXML
    private Button btnRemoveGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = PhotographerClient.client;
        loadLocalDatabaseInformation();
        if (PhotographerInfo.photographerID != null) {
            lblToolBarEmail.setText(PhotographerInfo.photographerID);
        }
        TreeItem ti = new TreeItem();
        tvGroupsAndUIDs.setRoot(ti);
        tvGroupsAndUIDs.setShowRoot(false);
        lvImageSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                System.out.println(client.selectedDirectory.toString() + "\\" + lvImageSelect.getSelectionModel().getSelectedItem().toString());
                File file = new File(client.selectedDirectory.toString() + "\\" + lvImageSelect.getSelectionModel().getSelectedItem().toString());
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
                        PictureGroup pg = (PictureGroup) tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getParent().getValue();
                        if (selectedPG != pg) {
                            tfGroupInfoName.setText(pg.getName());
                            taGroupInfoDescription.setText(pg.getDescription());
                            selectedPG = pg;
                        }
                    }
                }
            }
        });
        /*
        lvImageSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                
                tfModifyPictureInfoCreatedOn.setText(null);
                tfModifyPictureInfoName.setText(null);
                tfModifyPictureInfoPrice.setText(null);
                tfModifyPictureInfoFileLocation.setText(client.selectedDirectory.toString() + "\\" + lvImageSelect.getSelectionModel().getSelectedItem().toString());
                Path file = Paths.get(client.selectedDirectory.toString() + "\\" + lvImageSelect.getSelectionModel().getSelectedItem().toString());
                
            }
        });
         */
        lvSavedPictures.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                if (lvSavedPictures.getSelectionModel().getSelectedItem() != null) {

                    Picture p = lvSavedPictures.getSelectionModel().getSelectedItem();
                    tfModifyPictureInfoCreatedOn.setText(p.getCreatedString());
                    tfModifyPictureInfoFileLocation.setText(p.getLocation());
                    tfModifyPictureInfoName.setText(p.getName());
                    tfModifyPictureInfoPrice.setText(Double.toString(p.getPrice()));
                    File file = new File(lvSavedPictures.getSelectionModel().getSelectedItem().getLocation());
                    Image img = new Image(file.toURI().toString());
                    ivImagePreview.setImage(img);

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
        int selectedIndex = tvGroupsAndUIDs.getSelectionModel().getSelectedIndex();
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
        lblToolBarUIDRemaining.setText(Integer.toString(personalIDS.size()));
        lblToolBarGroupsRemaining.setText(Integer.toString(groupIDS.size()));
        tvGroupsAndUIDs.getSelectionModel().clearAndSelect(selectedIndex);
        /*
        final Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                Platform.runLater(() -> {

                });
                return null;
            }
        };
        new Thread(task).start();
         */
    }

    public void saveAllLocal() {
        final Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                updateProgress(0, 3);
                client.savePictureGroupIdList(groupIDS);
                updateProgress(1, 3);
                client.savePersonalPictureIdList(personalIDS);
                updateProgress(2, 3);
                client.savePictureGroupList(pictureGroups);
                updateProgress(3, 3);
                return null;
            }
        };
        pbProgress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();

    }

    public void loadLocalDatabaseInformation() {
        pictureGroups = client.getPictureGroupList();
        groupIDS = client.getAvailablGroupIDList();
        personalIDS = client.getAvailablePersonalIDList();
        lblToolBarUIDRemaining.setText(Integer.toString(personalIDS.size()));
        lblToolBarGroupsRemaining.setText(Integer.toString(groupIDS.size()));
    }

    @FXML
    public void chooseDirectory() {
        client.chooseDirectory();
        if (client.localfilemanager != null) {
            picturesPath = client.localfilemanager.getPicture();
            this.lvImageSelect.setItems(this.picturesPath);
            tfImageSelectPathLocation.setText(client.selectedDirectory.toString());
        }
    }

    @FXML
    public void addUID() {
        String result;
        if (selectedPG == null) {
            InterfaceCall.showAlert("Please select a group to add an UID.");
            return;
        }
        result = InterfaceCall.showInputDialog("Add UID's", "Enter the amount of UID's that you would like to add to '" + selectedPG.getName() + "'", "Amount of UID's: ", "1");
        if (result == null) {
            return;
        }
        if (!InterfaceCall.isInteger(result)) {
            InterfaceCall.showAlert("'" + result + "' is not a valid amount.");
            return;
        }
        int i = Integer.parseInt(result);
        if (personalIDS.size() < i) {
            InterfaceCall.showAlert("Sync with the server to retrieve more UID's.");
            return;
        }
        int count = 0;
        List<Integer> idl = new ArrayList<>();
        while (count < i) {
            PersonalPicture pp = new PersonalPicture(personalIDS.get(0));
            if (pp != null) {
                selectedPG.addPersonalPicture(pp);
                idl.add(personalIDS.get(0));
                personalIDS.remove(0);
            }
            count++;
        }
        client.removePersonalPictureIdList(idl);
        client.savePictureGroup(selectedPG);
        initviews();
    }

    public Pair<String, String> addGroupDialog() {
        // Create the custom dialog.
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Group");
        dialog.setHeaderText("Add a new group.");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(3);
        grid.setPadding(new Insets(20, 20, 20, 20));

        TextField name = new TextField();
        name.setPromptText("Name");
        TextArea description = new TextArea();
        description.setPromptText("Description");

        double width = 300;
        double height = 300;
        //name.setMaxHeight(value);
        name.setMaxWidth(width);
        description.setMaxHeight(height);
        description.setMaxWidth(width);

        Label lblName = new Label("Name");
        grid.add(lblName, 0, 1);
        grid.add(name, 0, 2);
        lblName.setOpacity(.5);
        Label lblDescription = new Label("Description");
        lblDescription.setOpacity(.5);
        grid.add(lblDescription, 0, 4);
        grid.add(description, 0, 5);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> name.requestFocus());

        if (dialog.showAndWait().get() != ButtonType.OK) {
            return null;
        } else {
            return new Pair<>(name.getText(), description.getText());
        }

    }

    @FXML
    public void addGroup() {
        PictureGroup pg = null;
        if (groupIDS.size() <= 0) {
            InterfaceCall.showAlert("Sync with the server to retrieve more Groups.");
            return;
        }
        Pair<String, String> result = addGroupDialog();
        if (result == null) {
            return;
        }
        pg = new PictureGroup(groupIDS.get(0));
        pg.setName(result.getKey());
        pg.setDescription(result.getValue());
        client.removePictureGroupId(groupIDS.get(0));
        groupIDS.remove(0);
        pictureGroups.add(pg);
        client.savePictureGroup(pg);
        initviews();
    }

    public Pair<String, String> addPictureDialog() {
        // Create the custom dialog.
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Picture");
        dialog.setHeaderText("Add a new picture.");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(3);
        grid.setPadding(new Insets(20, 20, 20, 20));

        TextField name = new TextField();
        name.setPromptText("Name");
        TextField price = new TextField();
        price.setPromptText("Price");

        double width = 200;
        double height = 300;
        //name.setMaxHeight(value);
        name.setMaxWidth(width);
        name.setPrefWidth(width);
        //price.setMaxHeight(height);
        price.setMaxWidth(width);
        price.setPrefWidth(width);

        Label lblName = new Label("Name");
        grid.add(lblName, 0, 0);
        grid.add(name, 1, 0);
        lblName.setOpacity(.5);
        Label lblPrice = new Label("Price");
        lblPrice.setOpacity(.5);
        grid.add(lblPrice, 0, 2);
        grid.add(price, 1, 2);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> name.requestFocus());

        if (dialog.showAndWait().get() != ButtonType.OK) {
            return null;
        } else {
            return new Pair<>(name.getText(), price.getText());
        }

    }

    @FXML
    public void addPicture() {
        if (lvImageSelect.getSelectionModel().isEmpty()) {
            InterfaceCall.showAlert("Please select an image from the list to add it as a picture.");
            return;
        }
        Pair<String, String> result = addPictureDialog();
        if (result == null) {
            return;
        }
        if (InterfaceCall.isDouble(result.getValue())) {
            String location = client.selectedDirectory.toString() + "\\" + lvImageSelect.getSelectionModel().getSelectedItem().toString();
            Picture p = new Picture(location, result.getKey(), Double.parseDouble(result.getValue()));
            if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PersonalPicture) {
                selectedPP.addPicture(p);
                ObservableList ob = FXCollections.observableArrayList(selectedPP.getPictures());
                lvSavedPictures.setItems(ob);
            } else if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PictureGroup) {
                selectedPG.addPicture(p);
                ObservableList ob = FXCollections.observableArrayList(selectedPG.getPictures());
                lvSavedPictures.setItems(ob);
            }
            client.savePictureGroup(selectedPG);
            //initviews();
            tvGroupsAndUIDs.refresh();
        } else {
            InterfaceCall.showAlert(Alert.AlertType.INFORMATION, "You have entered an invalid price.");
        }
    }

    @FXML
    private void savePicture() {
        if (lvSavedPictures.getSelectionModel().isEmpty()) {
            InterfaceCall.showAlert("Please select an picture from the list.");
            return;
        }
        if (!InterfaceCall.isDouble(tfModifyPictureInfoPrice.getText())) {
            InterfaceCall.showAlert("You have entered an invalid price.");
            return;
        }
        Picture p = lvSavedPictures.getSelectionModel().getSelectedItem();
        p.setName(tfModifyPictureInfoName.getText());
        p.setPrice(Double.parseDouble(tfModifyPictureInfoPrice.getText()));
        client.savePictureGroup(selectedPG);
        lvSavedPictures.refresh();
    }

    @FXML
    public void removePicture() {
        if (lvSavedPictures.getSelectionModel().isEmpty()) {
            InterfaceCall.showAlert("Please select an picture to remove it from the list.");
            return;
        }
        if (tvGroupsAndUIDs.getSelectionModel().isEmpty()) {
            InterfaceCall.showAlert("Please select an group or UID and a picture to remove it from the list.");
            return;
        }

        if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PictureGroup) {
            if (lvSavedPictures.getSelectionModel().getSelectedItem().isUploaded()) {
                InterfaceCall.showAlert("Picture cannot be removed once it has been uploaded.");
                return;
            }
            selectedPG.removePicture(lvSavedPictures.getSelectionModel().getSelectedItem());
            client.savePictureGroup(selectedPG);
            ObservableList ob = FXCollections.observableArrayList(selectedPG.getPictures());
            lvSavedPictures.setItems(ob);
        } else if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PersonalPicture) {
            if (selectedPP != null) {
                selectedPP.removePicture(lvSavedPictures.getSelectionModel().getSelectedItem());
                client.savePictureGroup(selectedPG);
            }
            ObservableList ob = FXCollections.observableArrayList(selectedPP.getPictures());
            lvSavedPictures.setItems(ob);
        }
        tvGroupsAndUIDs.refresh();
        lvSavedPictures.refresh();
    }

    @FXML
    public void removeUID() {
        if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem() != null && tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PersonalPicture) {
            if (selectedPP.getUploaded()) {
                InterfaceCall.showAlert("UID cannot be removed once it has been uploaded.");
                return;
            }
            System.out.println(selectedPP.getId());
            personalIDS.add(selectedPP.getId());
            Collections.sort(personalIDS);
            selectedPG.removePersonalPicture(selectedPP);
            client.savePictureGroup(selectedPG);
            client.savePersonalPictureId(selectedPP.getId());
            tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getParent().getChildren().remove(tvGroupsAndUIDs.getSelectionModel().getSelectedItem());
            tvGroupsAndUIDs.getSelectionModel().selectNext();
            lblToolBarUIDRemaining.setText(Integer.toString(personalIDS.size()));
        } else {
            InterfaceCall.showAlert("Please select an UID to remove it from the list.");
        }
    }

    @FXML
    private void removeGroup() {
        if (tvGroupsAndUIDs.getSelectionModel().getSelectedItem() != null && tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getValue() instanceof PictureGroup) {
            if (selectedPG.getUploaded()) {
                InterfaceCall.showAlert("Group cannot be removed once it has been uploaded.");
                return;
            }
            System.out.println(selectedPG.getId());
            groupIDS.add(selectedPG.getId());
            Collections.sort(groupIDS);
            client.removePictureGroup(selectedPG.getId());
            client.savePictureGroupId(selectedPG.getId());
            pictureGroups.remove(selectedPG);
            List<PersonalPicture> ppl = new ArrayList();
            ppl.addAll(selectedPG.getPersonalPictures());
            List<Integer> idl = new ArrayList<>();
            for (PersonalPicture pp : ppl) {
                idl.add(pp.getId());
                personalIDS.add(pp.getId());
                Collections.sort(personalIDS);
            }
            client.savePersonalPictureIdList(idl);
            tvGroupsAndUIDs.getSelectionModel().getSelectedItem().getParent().getChildren().remove(tvGroupsAndUIDs.getSelectionModel().getSelectedItem());
            tvGroupsAndUIDs.getSelectionModel().selectNext();
            lblToolBarGroupsRemaining.setText(Integer.toString(groupIDS.size()));
            lblToolBarUIDRemaining.setText(Integer.toString(personalIDS.size()));
        } else {
            InterfaceCall.showAlert("Please select an Group to remove it from the list.");
        }
    }

    @FXML
    public void logout() {
        client.setSceneLogin();
        lblToolBarEmail.setText(PhotographerInfo.photographerID);
    }

    public boolean autoLogin() {
        if (!client.connectToServer()) {
            Platform.runLater(() -> {
                InterfaceCall.connectionFailed();
            });
            return false;
        }
        if (PhotographerInfo.photographerID.isEmpty() || PhotographerInfo.photographerPass.isEmpty()) {
            client.setSceneLogin();
            return false;
        }
        IClientRunnable clientRunnable = PhotographerClientRunnable.clientRunnable;
        System.out.println(PhotographerInfo.photographerID + " - " + PhotographerInfo.photographerPass);
        clientRunnable.login(PhotographerInfo.photographerID, PhotographerInfo.photographerPass);
        return true;

    }

    @FXML
    public void sync() {
        final Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                updateProgress(0, 4);
                if (PhotographerClientRunnable.clientRunnable == null || PhotographerClientRunnable.clientRunnable.getSocket().isClosed()) {
                    System.out.println("autologin");
                    if (!autoLogin()) {
                        return null;
                    }
                }
                updateProgress(1, 4);
                System.out.println("uploading picture group");
                PhotographerClientRunnable.clientRunnable.uploadPictureGroups();
                updateProgress(2, 4);
                System.out.println("get group id's");
                PhotographerClientRunnable.clientRunnable.getGroupIDs(PhotographerInfo.photographerID);
                updateProgress(3, 4);
                System.out.println("get personal id's");
                PhotographerClientRunnable.clientRunnable.getPersonalIDs(PhotographerInfo.photographerID);
                updateProgress(4, 4);
                System.out.println("reload from local database");
                Platform.runLater(() -> {
                    loadLocalDatabaseInformation();
                    lvSavedPictures.refresh();
                    tvGroupsAndUIDs.refresh();
                });
                updateProgress(0, 4);
                return null;
            }
        };
        pbProgress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

}
