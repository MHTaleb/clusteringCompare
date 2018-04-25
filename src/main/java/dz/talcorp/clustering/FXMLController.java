package dz.talcorp.clustering;

import algorithme.ChaouchAlgorithm;
import algorithme.GameTheoryResolver;
import algorithme.KmeansResolver;
import builder.CSVPointBuilder;
import builder.ClusteringDataPair;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.sun.javafx.collections.ElementObservableListDecorator;
import entity.ClusterPoint;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
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
    private TableView<TableClusterElement> popTable;

    @FXML
    private TableColumn<TableClusterElement, String> membreAvant;

    @FXML
    private TableColumn<TableClusterElement, String> taux;

    @FXML
    private TableColumn<TableClusterElement, String> classColumn;

    @FXML
    private TableColumn<TableClusterElement, String> membreApres;

    @FXML
    private HBox csvMaps;

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
    private ScatterChart<Number, Number> chartCSVBruit;
    private ObservableList<TableClusterElement> tableStudyResultList;
    @FXML
    private JFXRadioButton algo2;
    @FXML
    private JFXRadioButton algo1;
    @FXML
    private HBox nbrClassDetecter;

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
        NumberAxis axisX = new NumberAxis(-500, 500, 10);
        NumberAxis axisY = new NumberAxis(-500, 500, 10);

        axisX.setAutoRanging(true);
        axisX.setLabel("X");
        axisX.setForceZeroInRange(false);
        axisY.setAutoRanging(true);
        axisY.setLabel("Y");
        axisY.setForceZeroInRange(false);

        chart = new ScatterChart<>(axisX, axisY);
        chart.autosize();
        chart.prefWidthProperty().bind(chartBox.widthProperty());
        chartBox.getChildren().add(chart);

         axisX = new NumberAxis(-500, 500, 10);
         axisY = new NumberAxis(-500, 500, 10);

        axisX.setAutoRanging(true);
        axisX.setLabel("X");
        axisX.setForceZeroInRange(false);
        axisY.setAutoRanging(true);
        axisY.setLabel("Y");
        axisY.setForceZeroInRange(false);
        // game theory chart
        chartCSV = new ScatterChart<>(axisX, axisY);
        chartCSV.setTitle("clustering final");
        chartCSV.autosize();
        chartCSV.prefWidthProperty().bind(csvMaps.widthProperty());
        chartCSV.prefHeightProperty().bind(csvMaps.heightProperty());
        csvMaps.getChildren().add(chartCSV);
        
         axisX = new NumberAxis(-500, 500, 10);
         axisY = new NumberAxis(-500, 500, 10);

        axisX.setAutoRanging(true);
        axisX.setLabel("X");
        axisX.setForceZeroInRange(false);
        axisY.setAutoRanging(true);
        axisY.setLabel("Y");
        axisY.setForceZeroInRange(false);
        chartCSVBruit = new ScatterChart<>(axisX, axisY);
        chartCSVBruit.setTitle("clustering Bruit");
        chartCSVBruit.autosize();
        chartCSVBruit.prefWidthProperty().bind(csvMaps.widthProperty());
        chartCSVBruit.prefHeightProperty().bind(csvMaps.heightProperty());
        csvMaps.getChildren().add(chartCSVBruit);

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

    @FXML
    void LancerSimulationcsv(ActionEvent event) {

        List<ClusteringDataPair> clusteringDataPairs = null;
        try {
            csvpb = new CSVPointBuilder(selectedFile.getAbsolutePath());
            clusteringDataPairs = csvpb.getClusteringDataPairs();
            System.out.println(clusteringDataPairs); // matrice des données

        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        final int PARAM_AFFICHAGE_1 = 1;
        final int PARAM_AFFICHAGE_2 = 4;
        // notre algo
        if (algo1.isSelected()) {
            
            Hashtable<String, List<ClusterPoint>> clusteringDimensions = csvpb.getClusteringDimensions();
            simulations = new ArrayList<>();

            clusteringDimensions.keySet().stream().forEach(key -> {
                if (!(key.toLowerCase().trim().startsWith("class ")
                        || //key.toLowerCase().trim().startsWith("id") || 
                        key.toLowerCase().trim().endsWith(" class")
                        || key.toLowerCase().trim().endsWith("id")
                        || key.toLowerCase().trim().equals("id"))) {
                    final KmeansResolver kmeansResolver = new KmeansResolver(clusteringDimensions.get(key), (int) nombreClusterDiabete.getValue());
                    System.out.println("simulation " + key + " is done");
                    kmeansResolver.setSimulationName(key);
                    simulations.add(kmeansResolver);

                }
            });
            System.out.println("simulation  is done");
//
//            final KmeansResolver kmeansResolver = simulations.get(currentGraphIndex);
//
//            List<ClusterPoint> centroids = kmeansResolver.getCentroids();
//            int numOfRepeat = kmeansResolver.getNumOfRepeat();
//            List<ClusterPoint> points = kmeansResolver.getPoints();//pop
            //drawChart(numOfRepeat, centroids, points, chartCSV);

            //  tablePopulationObservableList.clear();
//        sizeOfElements = simulations.get(0).getPoints().size();
//        for (int i = 0; i < sizeOfElements; i++) {
//            tablePopulationObservableList.add(new TableClusterElement("none", "element n°=" + i));
//        }
            GameTheoryResolver gameTheoryResolver = new GameTheoryResolver(simulations);
            //System.out.println("game thoer" + gameTheoryResolver.getPairResults());
            // debut nouveau modif
            PFEDataFormator pfeDataFormator = new PFEDataFormator(gameTheoryResolver.getPairResults());
            pfeDataFormator.setClassData(clusteringDataPairs);
            //JOptionPane.showConfirmDialog(null, "wait");
            //pfeDataFormator

            tableStudyResultList.clear();

            int currentBenchMarkSize = 0;
            int currentWellClassedSize = 0;
            HashMap<Integer, List> members = pfeDataFormator.getMembers();
            for (int i = 0; i < members.size(); i++) {
                final int wellClassedSize = pfeDataFormator.getWellClassedMembers().get(i).size();
                final int benchmarkSize = pfeDataFormator.getPreviousMembers().get(i).size();
                tableStudyResultList.add(new TableClusterElement("" + benchmarkSize,
                        "" + pfeDataFormator.getMembers().get(i).size(), "" + wellClassedSize, "" + ((float) wellClassedSize / (float) benchmarkSize) * 100));
                currentBenchMarkSize+=benchmarkSize;
                currentWellClassedSize+=wellClassedSize;
            }
             tableStudyResultList.add(new TableClusterElement("" + (currentBenchMarkSize),
                            "" + currentBenchMarkSize, "" + currentWellClassedSize, "" + ((float) currentWellClassedSize / (float) currentBenchMarkSize) * 100));
                

            // ajouter resumer depuis le reste des information introduite
            chartCSVBruit.dataProperty().get().clear();
            chartCSV.dataProperty().get().clear();
            for (int i = 0; i < nombreClusterDiabete.getValue(); i++) {

                final ObservableList<XYChart.Data<Number, Number>> observableArrayList = FXCollections.observableArrayList();

                for (int j = 0; j < pfeDataFormator.getMembers().get(i).size(); j++) {
                    String node = (String) pfeDataFormator.getMembers().get(i).get(j);
                    int nodeIndex = Integer.parseInt(node.replace("node", ""));
                    
                    observableArrayList.add(new XYChart.Data<>(Double.valueOf(clusteringDataPairs.get(PARAM_AFFICHAGE_1).getPoints().get(nodeIndex).getValue()), Double.valueOf(clusteringDataPairs.get(PARAM_AFFICHAGE_2).getPoints().get(nodeIndex).getValue())));
                }

                final XYChart.Series<Number, Number> series = new XYChart.Series<>(observableArrayList);
                series.setName("class" + i);
                chartCSV.dataProperty().get().add(series);

                final ObservableList<XYChart.Data<Number, Number>> observableArrayListBruit = FXCollections.observableArrayList();

                for (int j = 0; j < pfeDataFormator.getPreviousMembers().get(i).size(); j++) {
                    String node = (String) pfeDataFormator.getPreviousMembers().get(i).get(j);
                    int nodeIndex = Integer.parseInt(node.replace("node", ""))-1;
                    System.out.println("node index "+nodeIndex+" noise");
                    observableArrayListBruit.add(new XYChart.Data<>(Double.valueOf(clusteringDataPairs.get(PARAM_AFFICHAGE_1).getPoints().get(nodeIndex).getValue()), Double.valueOf(clusteringDataPairs.get(PARAM_AFFICHAGE_2).getPoints().get(nodeIndex).getValue())));
                }

                final XYChart.Series<Number, Number> seriesBruit = new XYChart.Series<>(observableArrayListBruit);
                seriesBruit.setName("class" + i);
                chartCSVBruit.dataProperty().get().add(seriesBruit);

            }
        }
        // chaouch algo
            if (algo2.isSelected()) {
                ChaouchAlgorithm algorithm = new ChaouchAlgorithm(clusteringDataPairs);
                algorithm.resolve((int) nombreClusterDiabete.getValue());
                System.out.println("algorithme " + algorithm);

                tableStudyResultList.clear();

                Predicate<? super ClusteringDataPair> classFilter = cdp -> {
                    return cdp.getColumn().toLowerCase().trim().equals("class");
                };

                ClusteringDataPair classColumnAlgo2 = clusteringDataPairs.stream().filter(classFilter).findFirst().get();

                final List<Integer> centersPrevious = new ArrayList();

                classColumnAlgo2.getPoints().stream().forEach(center -> {
                    centersPrevious.add(center.getValue().intValue());
                });

                List<Integer> classesAlgorithme = algorithm.getClasses();

                System.out.println("centersPrevious" + centersPrevious);
                int currentBenchMarkSize = 0;
                int currentWellClassedSize = 0;
                for (int i = 0; i < nombreClusterDiabete.getValue(); i++) {
                    int count = 0;
                    for (Integer integer : classesAlgorithme) {
                        if (integer == i) {
                            count++;
                        }
                    }
                    int wellClassedSize = 0;
                    for (int j = 0; j < centersPrevious.size(); j++) {
                        if (Objects.equals(centersPrevious.get(j), classesAlgorithme.get(j)) && centersPrevious.get(j) == i) {
                            wellClassedSize++;
                        }
                    }
                    final int benchmarkSize = centersPrevious.lastIndexOf(i) - currentBenchMarkSize + 1;
                    tableStudyResultList.add(new TableClusterElement("" + (benchmarkSize),
                            "" + count, "" + wellClassedSize, "" + ((float) wellClassedSize / (float) benchmarkSize) * 100));
                    currentWellClassedSize+=wellClassedSize;
                    currentBenchMarkSize+= benchmarkSize;
                }
                    tableStudyResultList.add(new TableClusterElement("" + (currentBenchMarkSize),
                            "" + currentBenchMarkSize, "" + currentWellClassedSize, "" + ((float) currentWellClassedSize / (float) currentBenchMarkSize) * 100));
                
                
                
                System.out.println("classesAlgorithme" + classesAlgorithme);

                chartCSVBruit.dataProperty().get().clear();
                chartCSV.dataProperty().get().clear();
                for (int i = 0; i < nombreClusterDiabete.getValue(); i++) {

                    final ObservableList<XYChart.Data<Number, Number>> observableArrayList = FXCollections.observableArrayList();

                    for (int j = 0; j < centersPrevious.size(); j++) {
                        //if (Objects.equals(centersPrevious.get(j), classesAlgorithme.get(j)) && centersPrevious.get(j) == i) {
                        if (classesAlgorithme.get(j) == i) {
                            final Float x = clusteringDataPairs.get(PARAM_AFFICHAGE_1).getPoints().get(j).getValue();
                            final Float y = clusteringDataPairs.get(PARAM_AFFICHAGE_2).getPoints().get(j).getValue();
                            System.out.println("x = "+x+"   y = "+y +"  clust");
                            observableArrayList.add(new XYChart.Data<>(Double.valueOf(x), Double.valueOf(y)));
                        }
                    }

                    final XYChart.Series<Number, Number> series = new XYChart.Series<>(observableArrayList);
                    series.setName("class" + i);
                    chartCSV.dataProperty().get().add(series);

                    final ObservableList<XYChart.Data<Number, Number>> observableArrayListBruit = FXCollections.observableArrayList();

                    for (int j = 0; j < centersPrevious.size(); j++) {
                        if (centersPrevious.get(j) == i) {
                            final Float x = clusteringDataPairs.get(PARAM_AFFICHAGE_1).getPoints().get(j).getValue();
                            final Float y = clusteringDataPairs.get(PARAM_AFFICHAGE_2).getPoints().get(j).getValue();
                            System.out.println("x = "+x+"   y = "+y +"  noise");
                            observableArrayListBruit.add(new XYChart.Data<>(Double.valueOf(x), Double.valueOf(y)));
                        }
                    }

                    final XYChart.Series<Number, Number> seriesBruit = new XYChart.Series<>(observableArrayListBruit);
                    seriesBruit.setName("class" + i);
                    chartCSVBruit.dataProperty().get().add(seriesBruit);

                }

            }
            
            
        
    }

    

    private void initializeTable() {
//        clusterColumn.setCellValueFactory(new PropertyValueFactory<TableClusterElement, String>("cluster"));
//        indvColumn.setCellValueFactory(new PropertyValueFactory<TableClusterElement, String>("individu"));
        // tablePopulationObservableList = FXCollections.observableArrayList();

        membreAvant.setCellValueFactory(new PropertyValueFactory<TableClusterElement, String>("individu"));
        membreApres.setCellValueFactory(new PropertyValueFactory<TableClusterElement, String>("cluster"));
        classColumn.setCellValueFactory(new PropertyValueFactory<TableClusterElement, String>("noise"));
        taux.setCellValueFactory(new PropertyValueFactory<TableClusterElement, String>("taux"));
        tableStudyResultList = FXCollections.observableArrayList();
        popTable.setItems(tableStudyResultList);

    }

}
