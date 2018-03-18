package dz.talcorp.clustering;

import algorithme.GameTheoryResolver;
import algorithme.KmeansResolver;
import builder.CSVPointBuilder;
import com.github.javafx.charts.zooming.ZoomManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import entity.ClusterPoint;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class FXMLController implements Initializable {

    @FXML
    private JFXButton next;

    @FXML
    private JFXButton previous;

    @FXML
    private TableView<TableClusterElement> popTable;

    private ObservableList<TableClusterElement> tablePopulationObservableList;

    @FXML
    private TableColumn<TableClusterElement, String> indvColumn;

    @FXML
    private TableColumn<TableClusterElement, String> clusterColumn;

    @FXML
    private VBox csvMaps;

    @FXML
    private JFXSlider tailleCluster;

    @FXML
    private HBox chartBox;

    @FXML
    private JFXSlider tailleNoeud;

    @FXML
    private HBox mapDiabete;

    @FXML
    private JFXSlider nombreClusterDiabete;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXButton launch;

    @FXML
    private JFXTextField chemin;

    @FXML
    private JFXHamburger hamburger;

    private ScatterChart<Number, Number> chart;
    private ScatterChart<Number, Number> chartCSV;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeDrawer();
        initializeChart();
        initializeTable();

    }

    @FXML
    private void Simule(ActionEvent event) {
        //lancement du simulateur
        KmeansResolver kmeansResolver = new KmeansResolver((int) tailleNoeud.getValue(), (int) tailleCluster.getValue());

        //recuperation du resultat
        List<ClusterPoint> centroids = kmeansResolver.getCentroids();
        int numOfRepeat = kmeansResolver.getNumOfRepeat();
        List<ClusterPoint> points = kmeansResolver.getPoints();//pop
        drawChart(numOfRepeat, centroids, points, chart);

    }

    private void drawChart(int numOfRepeat, List<ClusterPoint> centroids, List<ClusterPoint> points, ScatterChart<Number, Number> selectedChart) {
        //preparation du graph
        selectedChart.setTitle("Carte de résolution \n nombre d'itération : " + numOfRepeat);
        selectedChart.getData().clear();
        centroids.stream().forEach(new Consumer<ClusterPoint>() {
            int i = 0;// indice du cluster courant

            @Override
            public void accept(ClusterPoint center) {
                // creer uen nouvelle serie
                XYChart.Series seriesCentre = new XYChart.Series();
                seriesCentre.setName("Centre cluster " + i);
                // filtrer que les point membre de ce cluster
                Predicate<? super ClusterPoint> prdct = point -> {
                    return point.getCurrentCluster() == i;
                };
                // ajouter le centre
                seriesCentre.getData().add(new XYChart.Data<>(center.getX(), center.getY()));

                final XYChart.Series seriesMembre = new XYChart.Series();
                seriesMembre.setName("membre du cluster " + i);
                // ajouter la population
                points.stream().filter(prdct).forEach((ClusterPoint point) -> {
                    seriesMembre.getData().add(new XYChart.Data<>(point.getX() / 1, point.getY() / 1));
                });
//
//                System.out.println(seriesCentre);
//                System.out.println(seriesMembre);

                selectedChart.getData().add(seriesMembre);
                selectedChart.getData().add(seriesCentre);
                i++;
                
            }
        });
    }

    private void initializeDrawer() {

        try {
            VBox menu = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
            menu.setPrefWidth(0);
            ((VBox) menu.getChildren().get(0)).setPrefWidth(0);
            drawer.setSidePane(menu);
            drawer.setPrefWidth(0);
            HamburgerBackArrowBasicTransition transaction = new HamburgerBackArrowBasicTransition(hamburger);
            transaction.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_RELEASED, ect -> {
                transaction.setRate(transaction.getRate() * -1);
                transaction.play();
                if (drawer.isHidden() || drawer.isHiding()) {
                    menu.setPrefWidth(150);
                    ((VBox) menu.getChildren().get(0)).setPrefWidth(150);
                    drawer.setPrefWidth(150);
                    drawer.open();
                } else {
                    drawer.close();
                    drawer.setPrefWidth(0);
                    menu.setPrefWidth(0);
                    ((VBox) menu.getChildren().get(0)).setPrefWidth(0);
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initializeChart() {
        // normal kmeans chart
        NumberAxis axisX = new NumberAxis( -500, 500, 10);
        NumberAxis axisY = new NumberAxis( -500, 500, 10);
        
        axisX.setAutoRanging(true);
        axisX.setLabel("X");
	axisX.setForceZeroInRange(false);
	axisY.setAutoRanging(true);
	axisY.setLabel("Y");
	axisY.setForceZeroInRange(false);
        
        chart = new ScatterChart<Number, Number>(axisX, axisY);
        chart.autosize();
        chart.prefWidthProperty().bind(chartBox.widthProperty());
        chartBox.getChildren().add(chart);

        // game theory chart
        chartCSV = new ScatterChart<>(axisX, axisY);
        chartCSV.autosize();
        chartCSV.prefWidthProperty().bind(csvMaps.widthProperty());
        chartCSV.prefHeightProperty().bind(csvMaps.heightProperty());
        csvMaps.getChildren().add(chartCSV);
        
       
        
    }

    private File selectedFile;

    private CSVPointBuilder csvpb;

    private List<KmeansResolver> simulations;

    @FXML
    void openFileChooser(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        selectedFile = chooser.showOpenDialog(null);
        chemin.setText(selectedFile.getAbsolutePath());
    }

    private int sizeOfElements;
    private int currentGraphIndex;

    @FXML
    void LancerSimulationcsv(ActionEvent event) {
        
        try {
            csvpb = new CSVPointBuilder(selectedFile.getAbsolutePath());
            Hashtable<String, List<ClusterPoint>> clusteringDimensions = csvpb.getClusteringDimensions();
            simulations = new ArrayList<>();

            clusteringDimensions.keySet().stream().forEach(key -> {
                final KmeansResolver kmeansResolver = new KmeansResolver(clusteringDimensions.get(key), (int) nombreClusterDiabete.getValue());
                //System.out.println("simulation " + key + " is done");
                kmeansResolver.setSimulationName(key);
                simulations.add(kmeansResolver);
            });
                System.out.println("simulation  is done");

            final KmeansResolver kmeansResolver = simulations.get(currentGraphIndex);

            List<ClusterPoint> centroids = kmeansResolver.getCentroids();
            int numOfRepeat = kmeansResolver.getNumOfRepeat();
            List<ClusterPoint> points = kmeansResolver.getPoints();//pop
            drawChart(numOfRepeat, centroids, points, chartCSV);

        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        currentGraphIndex = 0;
        
        tablePopulationObservableList.clear();
        sizeOfElements = simulations.get(0).getPoints().size();
        for (int i = 0; i < sizeOfElements; i++) {
            tablePopulationObservableList.add(new TableClusterElement("none", "element n°=" + i));
        }

    }

    private void initializeTable() {
        clusterColumn.setCellValueFactory(new PropertyValueFactory<TableClusterElement, String>("cluster"));
        indvColumn.setCellValueFactory(new PropertyValueFactory<TableClusterElement, String>("individu"));
        tablePopulationObservableList = FXCollections.observableArrayList();
        popTable.setItems(tablePopulationObservableList);
        currentGraphIndex = 0;

    }

    @FXML
    public void showPrevious(ActionEvent e) {
        if (currentGraphIndex > 0) {
            currentGraphIndex--;


        }else
        {
            currentGraphIndex = 0;
        }
            final KmeansResolver kmeansResolver = simulations.get(currentGraphIndex);
            List<ClusterPoint> centroids = kmeansResolver.getCentroids();
            int numOfRepeat = kmeansResolver.getNumOfRepeat();
            List<ClusterPoint> points = kmeansResolver.getPoints();//pop
            drawChart(numOfRepeat, centroids, points, chartCSV);
            chartCSV.setTitle("graphe de simulation entre :"+simulations.get(currentGraphIndex).getSimulationName());
    }

    @FXML
    public void showNext(ActionEvent e) {
        if (sizeOfElements-1 != currentGraphIndex) {
            currentGraphIndex++;


        }else{
            currentGraphIndex = 0;
        }
        if (currentGraphIndex >= simulations.size()) {
            currentGraphIndex = simulations.size()-1;
        }
            final KmeansResolver kmeansResolver = simulations.get(currentGraphIndex);
            List<ClusterPoint> centroids = kmeansResolver.getCentroids();
            int numOfRepeat = kmeansResolver.getNumOfRepeat();
            List<ClusterPoint> points = kmeansResolver.getPoints();//pop
            drawChart(numOfRepeat, centroids, points, chartCSV);
            chartCSV.setTitle("graphe de simulation entre :"+simulations.get(currentGraphIndex).getSimulationName());
        
    }
    
    @FXML
    void lancerAnalyse(ActionEvent event) {
        
            GameTheoryResolver gameTheoryResolver = new GameTheoryResolver(simulations);
        final ClusterDetector clusterDetector = new ClusterDetector(0);
            gameTheoryResolver.getPairResults().stream().forEach(clusterDetector);
            
    }

    private class ClusterDetector implements Consumer<GameTheoryResolver.PairResult> {

        private  int i;

        public ClusterDetector(int i) {
            this.i = i;
        }

        @Override
        public void accept(GameTheoryResolver.PairResult pair) {
            tablePopulationObservableList.get(i).setCluster(pair.getDetectedCluster()+"");
            System.out.println("i = "+i);
            i++;
        }
    }
}
