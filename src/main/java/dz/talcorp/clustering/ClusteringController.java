/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.talcorp.clustering;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author taleb
 */
public class ClusteringController implements Initializable {

    @FXML
    private JFXSlider tailleNoeudKMeansAleatoir;
    @FXML
    private JFXSlider tailleClusterKmeansAleatoir;
    @FXML
    private JFXButton launch;
    @FXML
    private HBox nbrClassDetecter;
    @FXML
    private JFXTextField cheminKmeans;
    @FXML
    private JFXComboBox<?> DistanceKmeans;
    @FXML
    private JFXSlider nombreClusterKmeans;
    @FXML
    private TableView<TableClusterElement> popTableKmeans;
    @FXML
    private HBox nbrClassDetecter1;
    @FXML
    private HBox nbrClassDetecter11;
    @FXML
    private JFXProgressBar progressKMeans;
    @FXML
    private JFXTextField cheminVote;
    @FXML
    private JFXSlider nombreClusterVote;
    @FXML
    private TableView<TableClusterElement> popTableVote;
    @FXML
    private JFXProgressBar progressVote;
    @FXML
    private JFXTextField cheminTHJ;
    @FXML
    private JFXComboBox<?> indiceValiditeTHJ;
    @FXML
    private JFXSlider nombreClusterTHJ;
    @FXML
    private TableView<TableClusterElement> popTableTHJ;
    @FXML
    private JFXProgressBar progressTHJ;
    @FXML
    private TableColumn<TableClusterElement, String> membreAvantKmeans;
    @FXML
    private TableColumn<TableClusterElement, String> membreApresKmeans;
    @FXML
    private TableColumn<TableClusterElement, String> classColumnKmeans;
    @FXML
    private TableColumn<TableClusterElement, String> tauxKmeans;
    @FXML
    private TableColumn<TableClusterElement, String> membreAvantVote;
    @FXML
    private TableColumn<TableClusterElement, String> membreApresVote;
    @FXML
    private TableColumn<TableClusterElement, String> classColumnVote;
    @FXML
    private TableColumn<TableClusterElement, String> tauxVote;
    @FXML
    private TableColumn<TableClusterElement, String> membreAvantTHJ;
    @FXML
    private TableColumn<TableClusterElement, String> membreApresTHJ;
    @FXML
    private TableColumn<TableClusterElement, String> classColumnTHJ;
    @FXML
    private TableColumn<TableClusterElement, String> tauxTHJ;
    @FXML
    private HBox ConteneurKmeansAleatoir;
    @FXML
    private HBox ConteneurKmeansGlobal;
    @FXML
    private HBox ConteneurKmeans;
    @FXML
    private VBox conteneurTableauKmeans;
    @FXML
    private HBox conteneurVoteGlobal;
    @FXML
    private HBox conteneurVote;
    @FXML
    private VBox conteneurTableauVote;
    @FXML
    private HBox conteneurTHJGlobal;
    @FXML
    private HBox conteneurTHJ;
    @FXML
    private VBox conteneurTableauTHJ;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initCharts();
    }    

    @FXML
    private void Simule(ActionEvent event) {
    }

    @FXML
    private void openFileChooserKmeans(ActionEvent event) {
    }

    @FXML
    private void LancerSimulationKmeans(ActionEvent event) {
    }

    @FXML
    private void activerNombreDeclasseKmeans(ActionEvent event) {
    }

    @FXML
    private void openFileChooserVote(ActionEvent event) {
    }

    @FXML
    private void activerNombreDeClasseVote(ActionEvent event) {
    }

    @FXML
    private void LancerSimulationVote(ActionEvent event) {
    }

    @FXML
    private void openFileChooserTHJ(ActionEvent event) {
    }

    @FXML
    private void activerClasseTHJ(ActionEvent event) {
    }

    @FXML
    private void LancerSimulationTHJ(ActionEvent event) {
    }
    
    //**************************    Gestion des graphe  **********************//
    private ScatterChart<Number, Number> chartKmeansAleatoir;
    public void setupChartKmeansAleatoir(){
        Axis<Number> axisX = new NumberAxis("X", -500, +500,20);
        Axis<Number> axisY = new NumberAxis("X", -500, +500,20);
        axisX.setAnimated(true);
        axisX.setAutoRanging(true);
        axisY.setAnimated(true);
        axisY.setAutoRanging(true);
        chartKmeansAleatoir = new ScatterChart<Number,Number>(axisX,axisY);
       ConteneurKmeansAleatoir.getChildren().add(chartKmeansAleatoir);
        
    }
    
   

    private void initCharts() {
        setupChartKmeansAleatoir();
    }
    
    
}
