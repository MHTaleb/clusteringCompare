package dz.talcorp.clustering;

import algorithme.ChaouchAlgorithm;
import algorithme.Cluster;
import algorithme.GameTheoryResolver;
import algorithme.KmeansResolver;
import algorithme.Player;
import algorithme.THJAlgorithm;
import builder.CSVPointBuilder;
import builder.ClusteringDataPair;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import tools.ImageToCSVConverter;

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
    private JFXTextField SeuilField;
    
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

    private JFXDrawer drawer;

    @FXML
    private JFXButton launch;

    @FXML
    private JFXTextField chemin;

    @FXML
    private JFXHamburger hamburger;

    //--- declaration des graphe
    private ScatterChart<Number, Number> chart;
    private ScatterChart<Number, Number> chartCSV;
    private ScatterChart<Number, Number> chartCSVInitial;
    private ScatterChart<Number, Number> chartCSVBruit;

    private LineChart<Number, Number> kItereChart;
    String title;

    //--- fin declaration graphe
    //--- declaration liste observable de la table des resultat
    private ObservableList<TableClusterElement> tableStudyResultList;
    //--- fin declaration liste observable de la table des resultat

    @FXML
    private JFXRadioButton algo2;
    @FXML
    private JFXRadioButton algo1;
    @FXML
    private HBox nbrClassDetecter;
    @FXML
    private ToggleGroup group;
    @FXML
    private JFXComboBox<DistanceCBD> gValue;
    @FXML
    private JFXRadioButton algoTHJ;
    @FXML
    private VBox tableContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeChart();
        initializeTable();

        gValue.getItems().add(new DistanceCBD("Manhattan", 1));
        gValue.getItems().add(new DistanceCBD("Euclidean", 2));
        gValue.getItems().add(new DistanceCBD("p-norme3", 3));
        gValue.getItems().add(new DistanceCBD("p-norme4", 4));
        gValue.getItems().add(new DistanceCBD("p-norme5", 5));
        gValue.getItems().add(new DistanceCBD("Chebyshev - 25", 25));
        gValue.getItems().add(new DistanceCBD("Chebyshev - 50", 50));

        // lier l etat de visibilité du text field contenant le G a l etat du cochage 
        gValue.disableProperty().bind(algo1.selectedProperty().not());

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
                // selectedChart.getData().add(seriesCentre);
                i++;

            }
        });
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

                //selectedChart.getData().add(seriesMembre);
                selectedChart.getData().add(seriesCentre);
                i++;

            }
        });
    }

    private void initialzeThjChart() {

        csvMaps.getChildren().remove(kItereChart);
        csvMaps.getChildren().remove(chartCSVInitial);
        csvMaps.getChildren().remove(chartCSV);
        csvMaps.getChildren().remove(chartCSVBruit);
        mapDiabete.getChildren().remove(tableContainer);

        NumberAxis axisX = new NumberAxis(-500, 500, 10);
        NumberAxis axisY = new NumberAxis(-500, 500, 10);

        axisX.setAutoRanging(true);
        axisX.setLabel("Nombre Cluster ");
        axisX.setForceZeroInRange(false);
        axisY.setAutoRanging(true);
        axisY.setLabel("Taux");
        axisY.setForceZeroInRange(false);

        kItereChart = new LineChart<>(axisX, axisY);
        kItereChart.setTitle("Graphe des WB de l algorithm THJ");

        kItereChart.autosize();
        kItereChart.prefWidthProperty().bind(csvMaps.widthProperty());
        kItereChart.prefHeightProperty().bind(csvMaps.heightProperty());
        csvMaps.getChildren().add(kItereChart);

        axisX = new NumberAxis(-500, 500, 10);
        axisY = new NumberAxis(-500, 500, 10);

        axisX.setAutoRanging(true);
        axisX.setLabel("X");
        axisX.setForceZeroInRange(false);
        axisY.setAutoRanging(true);
        axisY.setLabel("Y");
        axisY.setForceZeroInRange(false);

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

        chartCSVInitial = new ScatterChart<>(axisX, axisY);
        chartCSVInitial.setTitle("clustering initial");
        chartCSVInitial.autosize();
        chartCSVInitial.prefWidthProperty().bind(csvMaps.widthProperty());
        chartCSVInitial.prefHeightProperty().bind(csvMaps.heightProperty());
        csvMaps.getChildren().add(chartCSVInitial);
    }

    private void initializeChart() {
        csvMaps.getChildren().remove(kItereChart);
        csvMaps.getChildren().remove(chartCSVInitial);
        csvMaps.getChildren().remove(chartCSV);
        csvMaps.getChildren().remove(chartCSVBruit);
        mapDiabete.getChildren().remove(tableContainer);
        //----------------           normal kmeans chart           ----------------
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
        //----------------  fin initialisation graphe kmeans page 1 ----------------

        //---------------- debut initalisation graphe resultat final clustering ----------------
        axisX = new NumberAxis(-500, 500, 10);
        axisY = new NumberAxis(-500, 500, 10);

        axisX.setAutoRanging(true);
        axisX.setLabel("X");
        axisX.setForceZeroInRange(false);
        axisY.setAutoRanging(true);
        axisY.setLabel("Y");
        axisY.setForceZeroInRange(false);

        chartCSV = new ScatterChart<>(axisX, axisY);
        chartCSV.setTitle("clustering final");
        chartCSV.autosize();
        chartCSV.prefWidthProperty().bind(csvMaps.widthProperty());
        chartCSV.prefHeightProperty().bind(csvMaps.heightProperty());
        csvMaps.getChildren().add(chartCSV);
        //----------------  fin initalisation graphe resultat final clustering  ----------------

        //---------------- debut initalisation graphe resultat bruit clustering ----------------
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
        //----------------  fin initalisation graphe resultat bruit clustering  ----------------

        //--------------  debut initalisation graphe resultat initial clustering  --------------
        axisX = new NumberAxis(-500, 500, 10);
        axisY = new NumberAxis(-500, 500, 10);

        axisX.setAutoRanging(true);
        axisX.setLabel("X");
        axisX.setForceZeroInRange(false);
        axisY.setAutoRanging(true);
        axisY.setLabel("Y");
        axisY.setForceZeroInRange(false);
        chartCSVInitial = new ScatterChart<>(axisX, axisY);
        chartCSVInitial.setTitle("clustering Initial");
        chartCSVInitial.autosize();
        chartCSVInitial.prefWidthProperty().bind(csvMaps.widthProperty());
        chartCSVInitial.prefHeightProperty().bind(csvMaps.heightProperty());
        csvMaps.getChildren().add(chartCSVInitial);
        //--------------   fin initalisation graphe resultat initial clustering   --------------

        try {

            mapDiabete.getChildren().add(tableContainer);
        } catch (Exception e) {
        }
    }

    //--- fichier csv Selectionner
    private File selectedFile;

    //--- convertisseur csv en point pluri-dimensionel
    private CSVPointBuilder csvpb;

    //--- 
    private List<KmeansResolver> simulations;

    @FXML
    void openFileChooser(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        selectedFile = chooser.showOpenDialog(null);
        chemin.setText(selectedFile.getAbsolutePath());
    }

    @FXML
    void LancerSimulationcsv(ActionEvent event) {

        List<ClusteringDataPair> listeDesAttribusAvecValeur = null;
        try {
            csvpb = new CSVPointBuilder(selectedFile.getAbsolutePath());
            listeDesAttribusAvecValeur = csvpb.getClusteringDataColumn();
            System.out.println(listeDesAttribusAvecValeur); // matrice des données

        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // choix de collone du graphe
        final int PARAM_AFFICHAGE_1 = 3;
        final int PARAM_AFFICHAGE_2 = 5;

        // notre algo
        if (algo2.isSelected()) {

            algoBinome(listeDesAttribusAvecValeur, PARAM_AFFICHAGE_1, PARAM_AFFICHAGE_2);
        }
        // chaouch algo
        if (algo1.isSelected()) {
            algoChaouech(listeDesAttribusAvecValeur, PARAM_AFFICHAGE_1, PARAM_AFFICHAGE_2);

        }
        if (algoTHJ.isSelected()) {
            int valeurG = 2;
            algorithmTHJ(listeDesAttribusAvecValeur, valeurG, PARAM_AFFICHAGE_1, PARAM_AFFICHAGE_2);

        }

    }
    String tit1e;

    public void algoChaouech(List<ClusteringDataPair> listeDesAttribusAvecValeur, final int PARAM_AFFICHAGE_1, final int PARAM_AFFICHAGE_2) throws NumberFormatException {
        ChaouchAlgorithm algorithm = new ChaouchAlgorithm(listeDesAttribusAvecValeur);
        algorithm.resolve((int) nombreClusterDiabete.getValue(), gValue.getSelectionModel().getSelectedItem().getDistanceValue());
        System.out.println("algorithme " + algorithm);

        tableStudyResultList.clear();

        Predicate<? super ClusteringDataPair> classFilter = cdp -> {
            return cdp.getColumnName().toLowerCase().trim().equals("class");
        };

        ClusteringDataPair classColumnAlgo2 = listeDesAttribusAvecValeur.stream().filter(classFilter).findFirst().get();

        final List<Integer> centersPrevious = new ArrayList();

        classColumnAlgo2.getColumnPoints().stream().forEach(center -> {
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
            currentWellClassedSize += wellClassedSize;
            currentBenchMarkSize += benchmarkSize;
        }
        tableStudyResultList.add(new TableClusterElement("" + (currentBenchMarkSize),
                "" + currentBenchMarkSize, "" + currentWellClassedSize, "" + ((float) currentWellClassedSize / (float) currentBenchMarkSize) * 100));

        System.out.println("classesAlgorithme" + classesAlgorithme);

        chartCSVInitial.dataProperty().get().clear();
        chartCSV.dataProperty().get().clear();
        chartCSVBruit.dataProperty().get().clear();
        for (int i = 0; i < nombreClusterDiabete.getValue(); i++) {
//------------------------
            final ObservableList<XYChart.Data<Number, Number>> observableArrayList = FXCollections.observableArrayList();

            for (int j = 0; j < centersPrevious.size(); j++) {
                //if (Objects.equals(centersPrevious.get(j), classesAlgorithme.get(j)) && centersPrevious.get(j) == i) {
                if (classesAlgorithme.get(j) == i) {
                    final Float x = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1).getColumnPoints().get(j).getValue();
                    final Float y = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2).getColumnPoints().get(j).getValue();
                    System.out.println("x = " + x + "   y = " + y + "  clust");
                    observableArrayList.add(new XYChart.Data<>(Double.valueOf(x), Double.valueOf(y)));
                }
            }

            final XYChart.Series<Number, Number> series = new XYChart.Series<>(observableArrayList);
            series.setName("class" + i);
            chartCSV.dataProperty().get().add(series);
//-----------------------------------
            final ObservableList<XYChart.Data<Number, Number>> observableArrayListBruit = FXCollections.observableArrayList();

            for (int j = 0; j < centersPrevious.size(); j++) {
                if (centersPrevious.get(j) == i) {
                    final Float x = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1).getColumnPoints().get(j).getValue();
                    final Float y = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2).getColumnPoints().get(j).getValue();
                    System.out.println("x = " + x + "   y = " + y + "  noise");
                    observableArrayListBruit.add(new XYChart.Data<>(Double.valueOf(x), Double.valueOf(y)));
                }
            }

            final XYChart.Series<Number, Number> seriesBruit = new XYChart.Series<>(observableArrayListBruit);
            seriesBruit.setName("class" + i);
            chartCSVInitial.dataProperty().get().add(seriesBruit);

            // affichage bruit
            final ObservableList<XYChart.Data<Number, Number>> observableArrayListBruitReel = FXCollections.observableArrayList();

            for (int j = 0; j < centersPrevious.size(); j++) {
                if (centersPrevious.get(j) == i) {
                    System.out.println("centersPrevious.get(j).intValue() " + centersPrevious.get(j).intValue());
                    System.out.println("classesAlgorithme.get(j).intValue() " + classesAlgorithme.get(j).intValue());
                    if (centersPrevious.get(j).intValue() != classesAlgorithme.get(j).intValue()) {
                        final Float x = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1).getColumnPoints().get(j).getValue();
                        final Float y = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2).getColumnPoints().get(j).getValue();
                        System.out.println("x = " + x + "   y = " + y + "  noise");
                        observableArrayListBruitReel.add(new XYChart.Data<>(Double.valueOf(x), Double.valueOf(y)));
                    }
                }
            }
            final XYChart.Series<Number, Number> seriesBruitReel = new XYChart.Series<>(observableArrayListBruitReel);
            seriesBruitReel.setName("class" + i);
            chartCSVBruit.dataProperty().get().add(seriesBruitReel);

        }
    }

    public void algoBinome(List<ClusteringDataPair> listeDesAttribusAvecValeur, final int PARAM_AFFICHAGE_1, final int PARAM_AFFICHAGE_2) throws NumberFormatException {
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
        ChaouchAlgorithm algorithm = new ChaouchAlgorithm(listeDesAttribusAvecValeur);
        GameTheoryResolver gameTheoryResolver = new GameTheoryResolver(simulations, algorithm.getMatriceCSV());

        // preparé une classe qui va organiser les resultats
        PFEDataFormator pfeDataFormator = new PFEDataFormator(gameTheoryResolver.getPairResults());

        // specifier le point de depart (on lui donne la classification du csv --> derniere collone class )
        pfeDataFormator.setClassData(listeDesAttribusAvecValeur);
        //JOptionPane.showConfirmDialog(null, "wait");
        //pfeDataFormator

        tableStudyResultList.clear();

        int benchMark = 0;
        int bienClassee = 0;

        // on recupere le clustering final
        HashMap<Integer, List> members = pfeDataFormator.getClassifications();
        //pour chaque individu
        for (int i = 0; i < members.size(); i++) {

            final int tailleBiensClassee = pfeDataFormator.getBiensClassifiee().get(i).size();
            final int tailleBenchMark = pfeDataFormator.getClassificationBenchMark().get(i).size();
            tableStudyResultList.add(new TableClusterElement("" + tailleBenchMark,
                    "" + pfeDataFormator.getClassifications().get(i).size(), "" + tailleBiensClassee, "" + ((float) tailleBiensClassee / (float) tailleBenchMark) * 100));
            benchMark += tailleBenchMark;
            bienClassee += tailleBiensClassee;
        }
        tableStudyResultList.add(new TableClusterElement("" + (benchMark),
                "" + benchMark, "" + bienClassee, "" + ((float) bienClassee / (float) benchMark) * 100));

        // ajouter resumer depuis le reste des information introduite
        chartCSVInitial.dataProperty().get().clear();
        chartCSV.dataProperty().get().clear();
        chartCSVBruit.dataProperty().get().clear();
        for (int i = 0; i < nombreClusterDiabete.getValue(); i++) {

            final ObservableList<XYChart.Data<Number, Number>> observableArrayList = FXCollections.observableArrayList();

            for (int j = 0; j < pfeDataFormator.getClassifications().get(i).size(); j++) {
                String node = (String) pfeDataFormator.getClassifications().get(i).get(j);
                int nodeIndex = Integer.parseInt(node.replace("node", ""));

                observableArrayList.add(new XYChart.Data<>(Double.valueOf(listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1).getColumnPoints().get(nodeIndex).getValue()), Double.valueOf(listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2).getColumnPoints().get(nodeIndex).getValue())));
            }

            final XYChart.Series<Number, Number> series = new XYChart.Series<>(observableArrayList);
            series.setName("class" + i);
            chartCSV.dataProperty().get().add(series);

            final ObservableList<XYChart.Data<Number, Number>> observableArrayListBruit = FXCollections.observableArrayList();

            for (int j = 0; j < pfeDataFormator.getClassificationBenchMark().get(i).size(); j++) {
                String node = (String) pfeDataFormator.getClassificationBenchMark().get(i).get(j);
                int nodeIndex = Integer.parseInt(node.replace("node", "")) - 1;
                System.out.println("node index " + nodeIndex + " noise");
                observableArrayListBruit.add(new XYChart.Data<>(Double.valueOf(listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1).getColumnPoints().get(nodeIndex).getValue()), Double.valueOf(listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2).getColumnPoints().get(nodeIndex).getValue())));
            }

            final XYChart.Series<Number, Number> seriesBruit = new XYChart.Series<>(observableArrayListBruit);
            seriesBruit.setName("class" + i);
            chartCSVInitial.dataProperty().get().add(seriesBruit);

            final ObservableList<XYChart.Data<Number, Number>> observableArrayListBruitReel = FXCollections.observableArrayList();

            for (int j = 0; j < pfeDataFormator.getClassificationBenchMark().get(i).size(); j++) {
                String node = (String) pfeDataFormator.getClassificationBenchMark().get(i).get(j);
                boolean contains = pfeDataFormator.getClassifications().get(i).contains(pfeDataFormator.getClassificationBenchMark().get(i).get(j));
                int nodeIndex = Integer.parseInt(node.replace("node", "")) - 1;
                System.out.println("node index " + nodeIndex + " noise");
                if (contains) {
                    observableArrayListBruitReel.add(new XYChart.Data<>(Double.valueOf(listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1).getColumnPoints().get(nodeIndex).getValue()), Double.valueOf(listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2).getColumnPoints().get(nodeIndex).getValue())));
                }
            }

            final XYChart.Series<Number, Number> seriesBruitreel = new XYChart.Series<>(observableArrayListBruitReel);
            seriesBruitreel.setName("class" + i);
            chartCSVBruit.dataProperty().get().add(seriesBruitreel);

        }
    }

    private void initializeTable() {
//        clusterColumn.setCellValueFactory(new PropertyValueFactory<TableClusterElement, String>("cluster"));
//        indvColumn.setCellValueFactory(new PropertyValueFactory<TableClusterElement, String>("individu"));
        // tablePopulationObservableList = FXCollections.observableArrayList();

        membreAvant.setCellValueFactory(new PropertyValueFactory<>("individu"));
        membreApres.setCellValueFactory(new PropertyValueFactory<>("cluster"));
        classColumn.setCellValueFactory(new PropertyValueFactory<>("noise"));
        taux.setCellValueFactory(new PropertyValueFactory<>("taux"));
        tableStudyResultList = FXCollections.observableArrayList();
        popTable.setItems(tableStudyResultList);

    }

    private void algorithmTHJ(List<ClusteringDataPair> listeDesAttribusAvecValeur, int valeurG, int PARAM_AFFICHAGE_1, int PARAM_AFFICHAGE_2) {
        
        
        
        List<ChaouchAlgorithm> cas = new ArrayList<>();
        for (int k = 2; k < nombreClusterDiabete.getValue()+1; k++) {
            ChaouchAlgorithm ca = new ChaouchAlgorithm(listeDesAttribusAvecValeur);
            ca.resolve(k, 2);
            cas.add(ca);
        }

        int indexCA = 0;
        double best_kCA = Double.MAX_VALUE;
        for (int j = 0; j < cas.size(); j++) {
            ChaouchAlgorithm ca = cas.get(j);
            double bestWB = ca.getWB();
            if (best_kCA > bestWB) {
                indexCA = j;
                best_kCA = bestWB;
            }
        }
        
        
        tit1e = "THJ";
        List<THJAlgorithm> tHJAlgorithms = new ArrayList<>();

        ChaouchAlgorithm algorithm = new ChaouchAlgorithm(listeDesAttribusAvecValeur);
        for (int k = 2; k < nombreClusterDiabete.getValue()+1; k++) {
            THJAlgorithm thja = new THJAlgorithm(algorithm.getMatriceCSV(), Integer.parseInt(SeuilField.getText()) , cas.get(k-2).getWB() );
            thja.resolve(k);
            tHJAlgorithms.add(thja);

            System.out.println("best wb for " + k + " is : " + thja.getBestWB());
            System.out.println("best composition for " + k + " is : " + thja.getBestMap().values());
        }

        int index = 0;
        double best_k = Double.MAX_VALUE;
        for (int i = 0; i < tHJAlgorithms.size(); i++) {
            THJAlgorithm tHJAlgorithm = tHJAlgorithms.get(i);
            double bestWB = tHJAlgorithm.getBestWB();
            if (best_k > bestWB) {
                index = i;
                best_k = bestWB;
            }
        }
        THJAlgorithm elu = tHJAlgorithms.get(index);
        Hashtable<Integer, Cluster> bestMap = elu.getBestMap();
        List<ClusterPoint> centroids = new ArrayList<>();
        List<ClusterPoint> points = new ArrayList<>();
        int i = 0;
        System.out.println("best map size = " + bestMap.size());
        for (int j = 0; j < bestMap.size(); j++) {

            Cluster cluster = bestMap.get(j);
            System.out.println("cluster members size = " + cluster.getPlayers().size());
            List<Float> clusterCoordinates = cluster.getClusterCoordinates();
            centroids.add(new ClusterPoint(clusterCoordinates.get(PARAM_AFFICHAGE_1), clusterCoordinates.get(PARAM_AFFICHAGE_2)));
            for (Player player : cluster.getPlayers()) {
                final ClusterPoint playerPoint = new ClusterPoint(player.getAttributes().get(PARAM_AFFICHAGE_1), player.getAttributes().get(PARAM_AFFICHAGE_2));
                playerPoint.setCurrentCluster(i);
                points.add(playerPoint);
            }
            i++;
        }

        title = "kmeans";
        kItereChart.getData().clear();
        final XYChart.Series series = new XYChart.Series();
        series.setName("Theorie des jeu");
        tHJAlgorithms.stream().forEach(experiance -> {
            series.getData().add(new XYChart.Data(series.getData().size() + 2, experiance.getBestWB()));
        });
        kItereChart.getData().add(series);

        drawChart(elu.getIteration(), centroids, points, chartCSV);

        ChaouchAlgorithm eluCA = cas.get(indexCA);

        Predicate<? super ClusteringDataPair> classFilter = cdp -> {
            return cdp.getColumnName().toLowerCase().trim().equals("class");
        };

        ClusteringDataPair classColumnAlgo2 = listeDesAttribusAvecValeur.stream().filter(classFilter).findFirst().get();
        final List<Integer> centersPrevious = new ArrayList();

        classColumnAlgo2.getColumnPoints().stream().forEach(center -> {
            centersPrevious.add(center.getValue().intValue());
        });

        List<Integer> classesAlgorithme = eluCA.getClasses();
        chartCSVInitial.dataProperty().get().clear();
        for (int l = 0; l < indexCA ; l++) { //+ (int) nombreClusterDiabete.getValue()

            final ObservableList<XYChart.Data<Number, Number>> observableArrayList = FXCollections.observableArrayList();

            for (int j = 0; j < centersPrevious.size(); j++) {
                //if (Objects.equals(centersPrevious.get(j), classesAlgorithme.get(j)) && centersPrevious.get(j) == i) {
                if (classesAlgorithme.get(j) == l) {
                    final Float x = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1).getColumnPoints().get(j).getValue();
                    final Float y = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2).getColumnPoints().get(j).getValue();
                    System.out.println("x = " + x + "   y = " + y + "  clust");
                    observableArrayList.add(new XYChart.Data<>(Double.valueOf(x), Double.valueOf(y)));
                }
            }

            final XYChart.Series<Number, Number> caSerieDraw = new XYChart.Series<>(observableArrayList);
            caSerieDraw.setName("class" + l);
            chartCSVInitial.dataProperty().get().add(caSerieDraw);
        }
        final XYChart.Series seriesCAK = new XYChart.Series();
        seriesCAK.setName("Kmeans");
        cas.stream().forEach(experiance -> {
            seriesCAK.getData().add(new XYChart.Data(seriesCAK.getData().size() + 2, experiance.getWB()));
        });
        kItereChart.getData().add(seriesCAK);

    }

    @FXML
    private void setChart(ActionEvent event) {
        initializeChart();
    }

    @FXML
    private void setTHJChart(ActionEvent event) {
        initialzeThjChart();
    }

    @FXML
    private void convert(ActionEvent event) throws IOException {
        ImageToCSVConverter.convertToCSV();
    }

    private static class DistanceCBD {

        private final StringProperty distanceName = new SimpleStringProperty();
        private final IntegerProperty distanceValue = new SimpleIntegerProperty();

        public int getDistanceValue() {
            return distanceValue.get();
        }

        public void setDistanceValue(int value) {
            distanceValue.set(value);
        }

        public IntegerProperty distanceValueProperty() {
            return distanceValue;
        }

        public String getDistanceName() {
            return distanceName.get();
        }

        public void setDistanceName(String value) {
            distanceName.set(value);
        }

        public StringProperty distanceNameProperty() {
            return distanceName;
        }

        public DistanceCBD(String distanceName, int value) {
            this.distanceName.setValue(distanceName);
            distanceValue.setValue(value);
        }

        @Override
        public String toString() {
            return distanceName.getValue();
        }

    }

}
