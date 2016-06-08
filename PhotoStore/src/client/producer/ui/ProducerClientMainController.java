/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.producer.ui;

import client.producer.ProducerClient;
import client.producer.ProducerClientRunnable;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Pair;

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
    private BarChart<String, Number> cIncomeFreq;
    @FXML
    private ProgressBar pbProgress1;
    @FXML
    private BarChart<String, Number> cPictureFreq;
    @FXML
    private ProgressBar pbProgress;
    @FXML
    private BarChart<String, Number> cPhotographerBestEarning;

    Toolkit toolkit;
    Timer timer;
    CategoryAxis xAxis;
    NumberAxis yAxis;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        ProducerClientRunnable.clientRunnable.reloadStats();
        refreshOverview();
        createcIncomeFrequency24H();
        createPictureFrequency7D();
        PhotographerTop30D();
        timer = new Timer();
        timer.scheduleAtFixedRate(new refreshTask(), 0, 10000);
    }

    class refreshTask extends TimerTask {

        public void run() {
            ProducerClientRunnable.clientRunnable.reloadStats();
            Platform.runLater(() -> {
                refreshOverview();
                createcIncomeFrequency24H();
                createPictureFrequency7D();
                PhotographerTop30D();
            });

        }
    }

    public void refreshOverview() {
        HashMap<String, String> hmap = ProducerClientRunnable.clientRunnable.getStats();
        lblUserAmount.setText(hmap.get("users"));
        lblPhotographerAmount.setText(hmap.get("photographers"));
        lblGroupAmount.setText(hmap.get("users"));
        lblUIDAmount.setText(hmap.get("uids"));
        lblPictureAmount.setText(hmap.get("pictures"));
        lblOrderAmount.setText(hmap.get("orders"));
        lblIncomeToday.setText(hmap.get("24h"));
        lblIncomeWeek.setText(hmap.get("7d"));
        lblIncomeMonth.setText(hmap.get("30d"));
        lblIncomeYear.setText(hmap.get("365d"));
        lblTotalIncome.setText(hmap.get("alltime"));
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        ProducerClient.client.setSceneRegisterPhotographer();
    }

    public void createcIncomeFrequency24H() {
        cIncomeFreq.getData().clear();
        if (ProducerClientRunnable.clientRunnable.getIncome24h() != null) {
            XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
            for (Pair<String, Double> p : ProducerClientRunnable.clientRunnable.getIncome24h()) {
                series1.getData().add(new XYChart.Data<String, Number>(p.getKey(), p.getValue()));
            }
            cIncomeFreq.getData().add(series1);
        }
    }

    public void createPictureFrequency7D() {
        cPictureFreq.getData().clear();

        if (ProducerClientRunnable.clientRunnable.getPictures7d() != null) {
            XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
            for (Pair<String, Integer> p : ProducerClientRunnable.clientRunnable.getPictures7d()) {
                series1.getData().add(new XYChart.Data<String, Number>(p.getKey(), p.getValue()));
            }
            cPictureFreq.getData().add(series1);
        }
    }

    public void PhotographerTop30D() {
        cPhotographerBestEarning.getData().clear();
        if (ProducerClientRunnable.clientRunnable.getPhotographers30d() != null) {
            for (Pair<String, Double> p : ProducerClientRunnable.clientRunnable.getPhotographers30d()) {
                XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
                series1.setName(p.getKey());
                series1.getData().add(new XYChart.Data<String, Number>("", p.getValue()));
                cPhotographerBestEarning.getData().add(series1);

            }
        }
    }
}
