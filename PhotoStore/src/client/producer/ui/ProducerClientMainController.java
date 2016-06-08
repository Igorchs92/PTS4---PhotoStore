/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.producer.ui;

import client.producer.ProducerClient;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * FXML Controller class
 *
 * @author IGOR
 */
public class ProducerClientMainController implements Initializable {

    @FXML
    private Label lblPhotographerAmount;
    @FXML
    private Label lblUserAmount;
    @FXML
    private Label lblGroupAmount;
    @FXML
    private Label lblUIDAmount;
    @FXML
    private Label lblPictureAmount;
    @FXML
    private Label lblOrderAmount;
    @FXML
    private Label lblTotalIncome;
    @FXML
    private Label lblIncomeToday;
    @FXML
    private Label lblIncomeWeek;
    @FXML
    private Label lblIncomeMonth;
    @FXML
    private Label lblIncomeYear;
    @FXML
    private ProgressBar pbProgress2;
    @FXML
    private Button btRegisterPhotographer;
    @FXML
    private StackedAreaChart<?, ?> cIncomeFreq;
    @FXML
    private ProgressBar pbProgress1;
    @FXML
    private BarChart<?, ?> cPictureFreq;
    @FXML
    private ProgressBar pbProgress;
    @FXML
    private BarChart<?, ?> cPhotographerBestEarning;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) {
        ProducerClient.client.setSceneRegisterPhotographer();
    }
    
}
