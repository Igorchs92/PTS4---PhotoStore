/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.producer.ui;

import client.producer.ProducerClient;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
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
    private BarChart<String, Number> cPictureFreq;
    @FXML
    private ProgressBar pbProgress;
    @FXML
    private BarChart<?, ?> cPhotographerBestEarning;

    CategoryAxis xAxis;
    NumberAxis yAxis;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        createIncomeChart();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        ProducerClient.client.setSceneRegisterPhotographer();
    }

    public void createIncomeChart() {
        System.out.println("incomechart werkt");
        String[] years = {"2014", "2015", "2016"};
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "$", null));
        cPictureFreq.setTitle("Top 10 Fotografaen");
        xAxis.setLabel("Year");
        xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(years)));
        yAxis.setLabel("Orders");
        System.out.println("incomechart werkt2");
        XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
        series1.setName("Jeroen");
        XYChart.Series<String, Number> series2 = new XYChart.Series<String, Number>();
        series2.setName("Igor");
        XYChart.Series<String, Number> series3 = new XYChart.Series<String, Number>();
        series3.setName("Hovsep");
        System.out.println("incomechart werkt3");
        series1.getData().add(new XYChart.Data<String, Number>(years[0], 567));
        series1.getData().add(new XYChart.Data<String, Number>(years[1], 1292));
        series1.getData().add(new XYChart.Data<String, Number>(years[2], 2180));
        series2.getData().add(new XYChart.Data<String, Number>(years[0], 956));
        series2.getData().add(new XYChart.Data<String, Number>(years[1], 1665));
        series2.getData().add(new XYChart.Data<String, Number>(years[2], 2450));
        series3.getData().add(new XYChart.Data<String, Number>(years[0], 900));
        series3.getData().add(new XYChart.Data<String, Number>(years[1], 1000));
        series3.getData().add(new XYChart.Data<String, Number>(years[2], 5800));
        System.out.println("incomechart werkt4");
        cPictureFreq.getData().add(series1);
        cPictureFreq.getData().add(series2);
        cPictureFreq.getData().add(series3);
        System.out.println("incomechart werkt5");
    }

}
