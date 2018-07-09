/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.talcorp.clustering;

import algorithme.Cluster;
import algorithme.GameTheoryResolver;
import algorithme.KmeansG;
import algorithme.KmeansResolver;
import algorithme.Player;
import algorithme.THJAlgorithm;
import builder.CSVPointBuilder;
import builder.ClusteringDataPair;
import builder.ColumnMembreValue;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import entity.ClusterPoint;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import tools.ImageToCSVConverter;

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
    private JFXComboBox<DistanceCBD> DistanceKmeans;
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
    private JFXComboBox<String> indiceValiditeTHJ;
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
    @FXML
    private JFXButton launchKmeansButton;
    @FXML
    private JFXButton launchVoteButton;
    @FXML
    private JFXButton launchTHJButton;
    @FXML
    private JFXCheckBox classeKmeansCheck;
    @FXML
    private JFXCheckBox classVoteCheck;
    @FXML
    private JFXCheckBox classTHJCheck;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initCharts();
        initControlsStates();
        initTables();
        initComboBoxes();
        initButtons();
    }

    @FXML
    private void Simule(ActionEvent event) {
        long temp_debut = System.currentTimeMillis();
        KmeansResolver kmeansResolver = new KmeansResolver((int) tailleNoeudKMeansAleatoir.getValue(), (int) tailleClusterKmeansAleatoir.getValue());
        long temp_fin = System.currentTimeMillis();
        long temp_execution = temp_fin - temp_debut;
        final int nombre_iteration = kmeansResolver.getNumOfRepeat();
        String title = "Simulation Kmeans Aleatoire";
        List<ClusterPoint> population = kmeansResolver.getPoints();
        List<ClusterPoint> classes = kmeansResolver.getCentroids();

        setChartContent(chartKmeansAleatoir, classes, population, title, temp_execution, nombre_iteration);

    }

    private File kmeansSelectedFile;

    @FXML
    private void openFileChooserKmeans(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        kmeansSelectedFile = chooser.showOpenDialog(null);
        if (kmeansSelectedFile != null) {
            cheminKmeans.setText(kmeansSelectedFile.getName());
            DistanceKmeans.setDisable(false);
            classeKmeansCheck.setDisable(false);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez choisir un fichier valide", ButtonType.OK);
            alert.initOwner(((Node) event.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    @FXML
    private void LancerSimulationKmeans(ActionEvent event) throws IOException {
        switch (checkType(kmeansSelectedFile)) {
            case "image":
                processKmeansImaging();
                break;
            case "csv":
                processKmeansCSV();
                break;
            default:
                showUnsupportedProcessing(event);
                break;
        }
    }

    boolean etatNombreDeClasseKmeans;

    @FXML
    private void activerNombreDeclasseKmeans(ActionEvent event) {

        resetOneContainers(ConteneurKmeans);
        if (etatNombreDeClasseKmeans) {
            showTableau(conteneurTableauKmeans, ConteneurKmeansGlobal);
            chartKmeansBenchMarkInitial = setupChart(ConteneurKmeans);
            chartKmeansBenchMarkBruit = setupChart(ConteneurKmeans);
            chartKmeansBenchMarkFinal = setupChart(ConteneurKmeans);
        } else {
            hideTableau(conteneurTableauKmeans, ConteneurKmeansGlobal);
            chartKmeansKIndicePlotWB = setupPlot(ConteneurKmeans);
            chartKmeansKIndicePlotDB = setupPlot(ConteneurKmeans);
            chartKmeansBestKPlotWB = setupChart(ConteneurKmeans);
            chartKmeansBestKPlotDB = setupChart(ConteneurKmeans);
        }
        etatNombreDeClasseKmeans = !etatNombreDeClasseKmeans;
        ConteneurKmeans.autosize();

    }

    private File voteSelectedFile;

    @FXML
    private void openFileChooserVote(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        voteSelectedFile = chooser.showOpenDialog(null);
        if (voteSelectedFile != null) {
            cheminVote.setText(voteSelectedFile.getName());
            nombreClusterVote.disableProperty().bind(classVoteCheck.selectedProperty().not());

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez choisir un fichier valide", ButtonType.OK);
            alert.initOwner(((Node) event.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    boolean etatNombreDeClasseVote;

    @FXML
    private void activerNombreDeClasseVote(ActionEvent event) {

        resetOneContainers(conteneurVote);
        if (etatNombreDeClasseVote) {
            showTableau(conteneurTableauVote, conteneurVoteGlobal);
            chartVoteBenchMarkInitial = setupChart(conteneurVote);
            chartVoteBenchMarkBruit = setupChart(conteneurVote);
            chartVoteBenchMarkFinal = setupChart(conteneurVote);
        } else {
            hideTableau(conteneurTableauVote, conteneurVoteGlobal);
            chartVoteKIndicePlotWB = setupPlot(conteneurVote);
            chartVoteKIndicePlotDB = setupPlot(conteneurVote);
            chartVoteBestKPlotWB = setupChart(conteneurVote);
            chartVoteBestKPlotDB = setupChart(conteneurVote);
        }
        etatNombreDeClasseVote = !etatNombreDeClasseVote;
        conteneurVote.autosize();

    }

    @FXML
    private void LancerSimulationVote(ActionEvent event) throws IOException {

        switch (checkType(voteSelectedFile)) {
            case "image":
                processVoteImaging();
                break;
            case "csv":
                processVoteCSV();
                break;
            default:
                showUnsupportedProcessing(event);
                break;
        }

    }

    private File thjSelectedFile;

    @FXML
    private void openFileChooserTHJ(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        thjSelectedFile = chooser.showOpenDialog(null);
        if (thjSelectedFile != null) {
            cheminTHJ.setText(thjSelectedFile.getName());
            nombreClusterTHJ.disableProperty().bind(classTHJCheck.selectedProperty().not());
            indiceValiditeTHJ.setDisable(false);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez choisir un fichier valide", ButtonType.OK);
            alert.initOwner(((Node) event.getSource()).getScene().getWindow());
            alert.showAndWait();
        }

    }

    private boolean etatNombreDeClasseTHJ;

    @FXML
    private void activerClasseTHJ(ActionEvent event) {

        resetOneContainers(conteneurTHJ);
        if (etatNombreDeClasseTHJ) {
            showTableau(conteneurTableauTHJ, conteneurTHJGlobal);
            chartTHJBenchMarkInitial = setupChart(conteneurTHJ);
            chartTHJBenchMarkBruit = setupChart(conteneurTHJ);
            chartTHJBenchMarkFinal = setupChart(conteneurTHJ);
        } else {
            hideTableau(conteneurTableauTHJ, conteneurTHJGlobal);
            chartTHJKIndicePlotWB = setupPlot(conteneurTHJ);
            chartTHJKIndicePlotDB = setupPlot(conteneurTHJ);
            chartTHJBestKPlotWB = setupChart(conteneurTHJ);
            chartTHJBestKPlotDB = setupChart(conteneurTHJ);
        }
        etatNombreDeClasseTHJ = !etatNombreDeClasseTHJ;
        conteneurTHJ.autosize();

    }

    @FXML
    private void LancerSimulationTHJ(ActionEvent event) throws IOException {

        switch (checkType(thjSelectedFile)) {
            case "image":
                processTHJImaging();
                break;
            case "csv":
                processTHJCSV();
                break;
            default:
                showUnsupportedProcessing(event);
                break;
        }
    }

    //**************************    Gestion des graphe  **********************//
    private static ScatterChart<Number, Number> chartKmeansAleatoir;

    //**************************           Kmeans Benchmark          **********************//
    private ScatterChart<Number, Number> chartKmeansBenchMarkInitial;
    private ScatterChart<Number, Number> chartKmeansBenchMarkBruit;
    private ScatterChart<Number, Number> chartKmeansBenchMarkFinal;

    private ScatterChart<Number, Number> chartKmeansBestKPlotWB;
    private LineChart<Number, Number> chartKmeansKIndicePlotWB;
    private ScatterChart<Number, Number> chartKmeansBestKPlotDB;
    private LineChart<Number, Number> chartKmeansKIndicePlotDB;

    //**************************           Kmeans Benchmark          **********************//
    //**************************           Vote Benchmark          **********************//
    private ScatterChart<Number, Number> chartVoteBenchMarkInitial;
    private ScatterChart<Number, Number> chartVoteBenchMarkBruit;
    private ScatterChart<Number, Number> chartVoteBenchMarkFinal;

    private ScatterChart<Number, Number> chartVoteBestKPlotWB;
    private LineChart<Number, Number> chartVoteKIndicePlotWB;
    private ScatterChart<Number, Number> chartVoteBestKPlotDB;
    private LineChart<Number, Number> chartVoteKIndicePlotDB;
    //**************************           Vote Benchmark          **********************//

    //**************************           THJ Benchmark          **********************//
    private ScatterChart<Number, Number> chartTHJBenchMarkInitial;
    private ScatterChart<Number, Number> chartTHJBenchMarkBruit;
    private ScatterChart<Number, Number> chartTHJBenchMarkFinal;

    private ScatterChart<Number, Number> chartTHJBestKPlotWB;
    private LineChart<Number, Number> chartTHJKIndicePlotWB;
    private ScatterChart<Number, Number> chartTHJBestKPlotDB;
    private LineChart<Number, Number> chartTHJKIndicePlotDB;
    //**************************           Vote Benchmark          **********************//

    public LineChart<Number, Number> setupPlot(HBox container) {
        LineChart<Number, Number> chart;
        Axis<Number> axisX = new NumberAxis("X", -500, +500, 20);
        Axis<Number> axisY = new NumberAxis("X", -500, +500, 20);
        axisX.setAnimated(true);
        axisX.setAutoRanging(true);
        axisY.setAnimated(true);
        axisY.setAutoRanging(true);
        chart = new LineChart<Number, Number>(axisX, axisY);
        chart.getData().add(new XYChart.Series<>(FXCollections.observableArrayList()));
        container.getChildren().add(chart);
        return chart;
    }

    public ScatterChart<Number, Number> setupChart(HBox container) {
        ScatterChart<Number, Number> chart;
        Axis<Number> axisX = new NumberAxis("X", -500, +500, 20);
        Axis<Number> axisY = new NumberAxis("X", -500, +500, 20);
        axisX.setAnimated(true);
        axisX.setAutoRanging(true);
        axisY.setAnimated(true);
        axisY.setAutoRanging(true);
        chart = new ScatterChart<Number, Number>(axisX, axisY);
        container.getChildren().add(chart);
        return chart;
    }

    private void initCharts() {
        resetAllContainers();

        chartKmeansAleatoir = setupChart(ConteneurKmeansAleatoir);

        chartKmeansBenchMarkInitial = setupChart(ConteneurKmeans);
        chartKmeansBenchMarkBruit = setupChart(ConteneurKmeans);
        chartKmeansBenchMarkFinal = setupChart(ConteneurKmeans);

        chartVoteBenchMarkInitial = setupChart(conteneurVote);
        chartVoteBenchMarkBruit = setupChart(conteneurVote);
        chartVoteBenchMarkFinal = setupChart(conteneurVote);

        chartTHJBenchMarkInitial = setupChart(conteneurTHJ);
        chartTHJBenchMarkBruit = setupChart(conteneurTHJ);
        chartTHJBenchMarkFinal = setupChart(conteneurTHJ);

    }

    private void showTableau(VBox container, HBox globalContainer) {
        container.setVisible(true);
        container.getChildren().stream().forEach(node -> {
            node.setVisible(true);
        });
        globalContainer.getChildren().add(container);
    }

    private void hideTableau(VBox container, HBox globalContainer) {
        container.setVisible(false);
        globalContainer.getChildren().remove(container);
    }

    private void resetAllContainers() {
        HBox[] boxes = new HBox[]{ConteneurKmeansAleatoir, ConteneurKmeans, conteneurTHJ, conteneurVote};
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].getChildren().clear();
        }
    }

    private void resetOneContainers(HBox container) {
        HBox[] boxes = new HBox[]{container};
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].getChildren().clear();
        }
    }

    private void setChartContent(ScatterChart<Number, Number> chart, List<ClusterPoint> classes, List<ClusterPoint> population, String title, long temp_execution, int nombre_iteration) {

        title += "\nnombre iteration " + nombre_iteration;
        title += "\ntemps d'execution " + temp_execution + "ms";
        chart.setTitle(title);

        chart.getData().clear();

        classes.stream().forEach(new Consumer<ClusterPoint>() {
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
                population.stream().filter(prdct).forEach((ClusterPoint point) -> {
                    seriesMembre.getData().add(new XYChart.Data<>(point.getX() / 1, point.getY() / 1));
                });
//
//                System.out.println(seriesCentre);
//                System.out.println(seriesMembre);

                chart.getData().add(seriesMembre);
                // selectedChart.getData().add(seriesCentre);
                i++;

            }
        });
        classes.stream().forEach(new Consumer<ClusterPoint>() {
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
                population.stream().filter(prdct).forEach((ClusterPoint point) -> {
                    seriesMembre.getData().add(new XYChart.Data<>(point.getX() / 1, point.getY() / 1));
                });
//
//                System.out.println(seriesCentre);
//                System.out.println(seriesMembre);

                //selectedChart.getData().add(seriesMembre);
                chart.getData().add(seriesCentre);
                i++;

            }
        });

    }
    //**************************    Gestion des graphes  **********************//

    private void initControlsStates() {
        // button
        launchKmeansButton.setDisable(true);
        launchVoteButton.setDisable(true);
        launchTHJButton.setDisable(true);

        //selectable
        DistanceKmeans.setDisable(true);
        classeKmeansCheck.setDisable(true);
        nombreClusterKmeans.setDisable(true);

        //
        nombreClusterKmeans.disableProperty().bind(classeKmeansCheck.selectedProperty().not());
        nombreClusterVote.disableProperty().bind(classVoteCheck.selectedProperty().not());
        nombreClusterTHJ.disableProperty().bind(classTHJCheck.selectedProperty().not());
    }

    private void initComboBoxes() {
        DistanceKmeans.getItems().add(new DistanceCBD("Manhattan", 1));
        DistanceKmeans.getItems().add(new DistanceCBD("Euclidean", 2));
        DistanceKmeans.getItems().add(new DistanceCBD("p-norme3", 3));
        DistanceKmeans.getItems().add(new DistanceCBD("p-norme4", 4));
        DistanceKmeans.getItems().add(new DistanceCBD("p-norme5", 5));
        DistanceKmeans.getItems().add(new DistanceCBD("Chebyshev - 25", 25));
        DistanceKmeans.getItems().add(new DistanceCBD("Chebyshev - 50", 50));

        indiceValiditeTHJ.getItems().add("Davies Bouldin");
        indiceValiditeTHJ.getItems().add("WB");
    }

    private void initButtons() {
        launchKmeansButton.disableProperty().bind(DistanceKmeans.selectionModelProperty().get().selectedIndexProperty().lessThan(0).or(cheminKmeans.textProperty().isEmpty()).or(DistanceKmeans.disableProperty()));
        launchTHJButton.disableProperty().bind(cheminTHJ.textProperty().length().lessThan(4).or(indiceValiditeTHJ.selectionModelProperty().get().selectedIndexProperty().lessThan(0)));
        launchVoteButton.disableProperty().bind(cheminVote.textProperty().length().lessThan(4));
    }

    private String checkType(File file) {
        //CSV FORMAT
        if (file.getName().endsWith(".csv")) {
            return "csv";
        }
        if (file.getName().endsWith(".txt")) {
            return "csv";
        }
        if (file.getName().endsWith(".xsls")) {
            return "csv";
        }
        if (file.getName().endsWith(".xsl")) {
            return "csv";
        }
        //IMAGE FORMAT
        if (file.getName().endsWith(".png")) {
            return "image";
        }
        if (file.getName().endsWith(".jpg")) {
            return "image";
        }
        if (file.getName().endsWith(".bmp")) {
            return "image";
        }

        return "unprocessed";
    }

    private void processKmeansImaging() throws IOException {
        //transformer le vecteur de l'image en csv
        File csvImageFile = ImageToCSVConverter.imageToCSV3D(kmeansSelectedFile);
        CSVPointBuilder csvPointBuilder = new CSVPointBuilder(csvImageFile.getAbsolutePath(), false);
        System.out.println("constrructed image csv from image");
        List<ClusteringDataPair> clusteringDataPairs = csvPointBuilder.getClusteringDataColumn();

        // System.out.println("clusteringDataPairs :::: " + clusteringDataPairs);
        KmeansG kmeansG = new KmeansG(clusteringDataPairs);
        System.out.println("continued");
        kmeansG.resolve((int) nombreClusterKmeans.getValue(), DistanceKmeans.getSelectionModel().getSelectedItem().getDistanceValue());
        JFXAlert alert = new JFXAlert();
        final HBox chartContainer = new HBox();
        chartContainer.setAlignment(Pos.CENTER);

        List<ClusterPoint> points = new ArrayList<>();
        final List<Integer> classes = kmeansG.getClasses();
        System.out.println("classes.size() = " + classes.size());
        for (int i = 0; i < classes.size(); i++) {
            final List<Float> individu = kmeansG.getMatriceCSV().get(i);
            final ClusterPoint clusterPoint = new ClusterPoint(individu.get(0), individu.get(1));
            clusterPoint.setCurrentCluster(classes.get(i));
            points.add(clusterPoint);
        }

        //drawing image
        ImageView drawImage = drawImage(points);
        chartContainer.setSpacing(15);
        chartContainer.getChildren().add(drawImage);
        alert.setContent(chartContainer);
        alert.show();
    }

    private ImageView drawImage(List<ClusterPoint> points) throws IOException {
        int image_width = ImageToCSVConverter.height;
        int image_height = ImageToCSVConverter.width;
        BufferedImage bi = new BufferedImage(image_width, image_height, BufferedImage.TYPE_INT_ARGB);
        File myNewJPegFile = new File("ClusteredImage.jpg");
        for (int i = 0; i < image_width; i++) {
            for (int j = 0; j < image_height; j++) {
                try {
                    int playerID = image_height * i + j;
                    int currentCluster = points.get(playerID).getCurrentCluster();
                    Color c = new Color(100, 120, 130);
                    switch (currentCluster) {
                        case 0:
                            c = Color.RED;
                            break;
                        case 1:
                            c = Color.BLACK;
                            break;
                        case 2:
                            c = Color.GREEN;
                            break;
                        case 3:
                            c = Color.BLUE;
                            break;
                        case 4:
                            c = Color.ORANGE;
                            break;
                        case 5:
                            c = Color.PINK;
                            break;
                        case 6:
                            c = Color.MAGENTA;
                            break;
                        case 7:
                            c = new Color(127, 170, 133);
                            break;
                    }

                    bi.setRGB(i, j, c.getRGB());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("image processed");
        ImageIO.write(bi, "jpg", myNewJPegFile);
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(new FileInputStream(myNewJPegFile)));
        return imageView;
    }

    private void processKmeansCSV() throws IOException {
        // formating csv file

        progressKMeans.setProgress(0);
        progressKMeans.setProgress(0.5);
        CSVPointBuilder csvPointBuilder = new CSVPointBuilder(kmeansSelectedFile.getAbsolutePath(), false);
        List<ClusteringDataPair> listeDesAttribusAvecValeur = csvPointBuilder.getClusteringDataColumn();
        progressKMeans.setProgress(0.10);
        // starting algorithme for benchmark processing
        if (!etatNombreDeClasseKmeans) {

            long start_time = System.currentTimeMillis();

            KmeansG algorithm = new KmeansG(listeDesAttribusAvecValeur);
            progressKMeans.setProgress(0.15);
            progressKMeans.setProgress(0.20);
            progressKMeans.setProgress(0.25);
            progressKMeans.setProgress(0.30);

            int nombre_de_class = (int) nombreClusterKmeans.getValue();
            int distance = (int) DistanceKmeans.getSelectionModel().getSelectedItem().getDistanceValue();

            algorithm.resolve(nombre_de_class, distance);
            progressKMeans.setProgress(0.35);
            progressKMeans.setProgress(0.40);
            progressKMeans.setProgress(0.45);
            progressKMeans.setProgress(0.50);

            long end_time = System.currentTimeMillis() - start_time;

            ObservableList<TableClusterElement> tableStudyResultList = FXCollections.observableArrayList();

            Predicate<? super ClusteringDataPair> classFilter = cdp -> {
                return cdp.getColumnName().toLowerCase().trim().equals("class");
            };

            ClusteringDataPair classColumnAlgo2 = listeDesAttribusAvecValeur.stream().filter(classFilter).findFirst().get();
            progressKMeans.setProgress(0.55);
            progressKMeans.setProgress(0.60);

            final List<Integer> centersPrevious = new ArrayList();

            classColumnAlgo2.getColumnPoints().stream().forEach(center -> {
                centersPrevious.add(center.getValue().intValue());
            });

            progressKMeans.setProgress(0.65);
            List<Integer> classesAlgorithme = algorithm.getClasses();

            int currentBenchMarkSize = 0;
            int currentWellClassedSize = 0;
            for (int i = 0; i < nombreClusterKmeans.getValue(); i++) {
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
            progressKMeans.setProgress(0.70);
            tableStudyResultList.add(new TableClusterElement("" + (currentBenchMarkSize),
                    "" + currentBenchMarkSize, "" + currentWellClassedSize, "" + ((float) currentWellClassedSize / (float) currentBenchMarkSize) * 100));

            chartKmeansBenchMarkInitial.dataProperty().get().clear();
            chartKmeansBenchMarkInitial.setTitle("Initial");
            chartKmeansBenchMarkFinal.dataProperty().get().clear();
            chartKmeansBenchMarkBruit.dataProperty().get().clear();
            chartKmeansBenchMarkBruit.setTitle("Bruit");

            int PARAM_AFFICHAGE_1 = 1;
            int PARAM_AFFICHAGE_2 = 2;
            progressKMeans.setProgress(0.75);

            for (int i = 0; i < nombreClusterKmeans.getValue(); i++) {
//------------------------
                final ObservableList<XYChart.Data<Number, Number>> observableArrayList = FXCollections.observableArrayList();

                for (int j = 0; j < centersPrevious.size(); j++) {
                    //if (Objects.equals(centersPrevious.get(j), classesAlgorithme.get(j)) && centersPrevious.get(j) == i) {
                    if (classesAlgorithme.get(j) == i) {
                        final Float x = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(j).getValue();
                        final Float y = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(j).getValue();
                        System.out.println("x = " + x + "   y = " + y + "  clust");
                        observableArrayList.add(new XYChart.Data<>(Double.valueOf(x), Double.valueOf(y)));
                    }
                }

                final XYChart.Series<Number, Number> series = new XYChart.Series<>(observableArrayList);
                series.setName("class" + i);
                chartKmeansBenchMarkFinal.dataProperty().get().add(series);
                chartKmeansBenchMarkFinal.setTitle("Final \nNombre d'iteration "+algorithm.getIterations()+"\n"
                        + "Temps : "+end_time+" ms");
//-----------------------------------
                final ObservableList<XYChart.Data<Number, Number>> observableArrayListBruit = FXCollections.observableArrayList();

                for (int j = 0; j < centersPrevious.size(); j++) {
                    if (centersPrevious.get(j) == i) {
                        final Float x = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(j).getValue();
                        final Float y = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(j).getValue();
                        System.out.println("x = " + x + "   y = " + y + "  noise");
                        observableArrayListBruit.add(new XYChart.Data<>(Double.valueOf(x), Double.valueOf(y)));
                    }
                }

                final XYChart.Series<Number, Number> seriesBruit = new XYChart.Series<>(observableArrayListBruit);
                seriesBruit.setName("class" + i);
                chartKmeansBenchMarkInitial.dataProperty().get().add(seriesBruit);

                // affichage bruit
                final ObservableList<XYChart.Data<Number, Number>> observableArrayListBruitReel = FXCollections.observableArrayList();

                for (int j = 0; j < centersPrevious.size(); j++) {
                    if (centersPrevious.get(j) == i) {
                        if (centersPrevious.get(j).intValue() != classesAlgorithme.get(j).intValue()) {
                            final Float x = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(j).getValue();
                            final Float y = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(j).getValue();
                            observableArrayListBruitReel.add(new XYChart.Data<>(Double.valueOf(x), Double.valueOf(y)));
                        }
                    }
                }
                final XYChart.Series<Number, Number> seriesBruitReel = new XYChart.Series<>(observableArrayListBruitReel);
                chartKmeansBenchMarkBruit.dataProperty().get().add(seriesBruitReel);
                progressKMeans.setProgress(100);
                popTableKmeans.getItems().clear();
                popTableKmeans.getItems().addAll(tableStudyResultList);
            }

        } else {
            // starting algorithme for unsupervised processing

            KmeansG bestWB = null;
            KmeansG bestDB = null;

            int best_wb_indice;
            int best_db_indice;

            int PARAM_AFFICHAGE_1 = 1, PARAM_AFFICHAGE_2 = 2;

            double db_min = Double.MAX_VALUE;
            double wb_min = Double.MAX_VALUE;

            long start_time = System.currentTimeMillis();

            chartKmeansKIndicePlotDB.getData().get(0).getData().clear();
            chartKmeansKIndicePlotDB.getData().get(0).setName("DB");
            chartKmeansKIndicePlotWB.getData().get(0).getData().clear();
            chartKmeansKIndicePlotWB.getData().get(0).setName("WB");

            for (int i = 2; i < 10; i++) {

                KmeansG algorithm = new KmeansG(listeDesAttribusAvecValeur);

                algorithm.resolve(i, 2);

                progressKMeans.setProgress(0.10 * i + 0.1);

                double wb = algorithm.getWB();
                if (wb < wb_min) {
                    wb_min = wb;
                    bestWB = algorithm;
                }
                double db = algorithm.getDaviesBouldin(i);
                if (db < db_min) {
                    db_min = db;
                    bestDB = algorithm;
                }

                chartKmeansKIndicePlotDB.getData().get(0).getData().add(new XYChart.Data<>(i, db));
                chartKmeansKIndicePlotWB.getData().get(0).getData().add(new XYChart.Data<>(i, wb));

            }

            if (bestWB != null) {

                List<ClusterPoint> centroids = new ArrayList<>();
                for (int i = 0; i < bestWB.getCurrentCentroid().size(); i++) {
                    centroids.add(
                            new ClusterPoint(
                                    bestWB.getCurrentCentroid().get(i).get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()),
                                    bestWB.getCurrentCentroid().get(i).get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size())
                            )
                    );
                }
                List<ClusterPoint> points = new ArrayList<>();

                for (int i = 0; i < bestWB.getClasses().size(); i++) {
                    final ClusterPoint clusterPoint = new ClusterPoint(
                            bestWB.getMatriceCSV().get(i).get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()),
                            bestWB.getMatriceCSV().get(i).get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size())
                    );
                    clusterPoint.setCurrentCluster(bestWB.getClasses().get(i));
                    points.add(clusterPoint);
                }

                drawChart(bestWB.getIterations(), centroids, points, chartKmeansBestKPlotWB);
            }

            if (bestDB != null) {

                List<ClusterPoint> centroids = new ArrayList<>();
                for (int i = 0; i < bestDB.getCurrentCentroid().size(); i++) {
                    centroids.add(
                            new ClusterPoint(
                                    bestDB.getCurrentCentroid().get(i).get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()),
                                    bestDB.getCurrentCentroid().get(i).get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size())
                            )
                    );
                }
                List<ClusterPoint> points = new ArrayList<>();

                for (int i = 0; i < bestDB.getClasses().size(); i++) {
                    final ClusterPoint clusterPoint = new ClusterPoint(
                            bestDB.getMatriceCSV().get(i).get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()),
                            bestDB.getMatriceCSV().get(i).get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size())
                    );
                    clusterPoint.setCurrentCluster(bestDB.getClasses().get(i));
                    points.add(clusterPoint);
                }

                drawChart(bestDB.getIterations(), centroids, points, chartKmeansBestKPlotDB);
            }

        }

    }

    int counter;

    private void drawChart(int numOfRepeat, List<ClusterPoint> centroids, List<ClusterPoint> points, ScatterChart<Number, Number> selectedChart) {
        //preparation du graph
        selectedChart.setTitle("Carte de résolution \n nombre d'itération : " + numOfRepeat);
        selectedChart.getData().clear();
        counter = 0;
        centroids.stream().forEach(new Consumer<ClusterPoint>() {
            int i = 0;// indice du cluster courant

            @Override
            public void accept(ClusterPoint center) {
                // creer uen nouvelle serie
//                XYChart.Series seriesCentre = new XYChart.Series();
//                seriesCentre.setName("Centre cluster " + i);
                // filtrer que les point membre de ce cluster
                Predicate<? super ClusterPoint> prdct = point -> {
                    return point.getCurrentCluster() == i;
                };
                // ajouter le centre
//                seriesCentre.getData().add(new XYChart.Data<>(center.getX(), center.getY()));

                final XYChart.Series seriesMembre = new XYChart.Series();
                seriesMembre.setName("membre du cluster " + i);
                // ajouter la population
                points.stream().filter(prdct).limit(1000).forEach((ClusterPoint point) -> {
                    seriesMembre.getData().add(new XYChart.Data<>(point.getX() / 1, point.getY() / 1));
                    System.out.println("addind plot" + counter);
                    counter++;
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
//                points.stream().filter(prdct).forEach((ClusterPoint point) -> {
//                    seriesMembre.getData().add(new XYChart.Data<>(point.getX() / 1, point.getY() / 1));
//                });
//
//                System.out.println(seriesCentre);
//                System.out.println(seriesMembre);

                //selectedChart.getData().add(seriesMembre);
                selectedChart.getData().add(seriesCentre);
                i++;

            }
        });
    }

    private void showUnsupportedProcessing(ActionEvent event) {
        Window parent = ((Node) event.getSource()).getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.ERROR, "Le fichier selectioner est inaproprié", ButtonType.OK);
        alert.initOwner(parent);
        alert.showAndWait();
    }

    private void initTables() {
        classColumnKmeans.setCellValueFactory(new PropertyValueFactory<>("noise"));
        classColumnTHJ.setCellValueFactory(new PropertyValueFactory<>("noise"));
        classColumnVote.setCellValueFactory(new PropertyValueFactory<>("noise"));
        membreApresKmeans.setCellValueFactory(new PropertyValueFactory<>("cluster"));
        membreApresTHJ.setCellValueFactory(new PropertyValueFactory<>("cluster"));
        membreApresVote.setCellValueFactory(new PropertyValueFactory<>("cluster"));
        membreAvantKmeans.setCellValueFactory(new PropertyValueFactory<>("individu"));
        membreAvantTHJ.setCellValueFactory(new PropertyValueFactory<>("individu"));
        membreAvantVote.setCellValueFactory(new PropertyValueFactory<>("individu"));
        tauxKmeans.setCellValueFactory(new PropertyValueFactory<>("taux"));
        tauxTHJ.setCellValueFactory(new PropertyValueFactory<>("taux"));
        tauxVote.setCellValueFactory(new PropertyValueFactory<>("taux"));
    }

    private void processVoteImaging() throws IOException {
        File csvImageFile = ImageToCSVConverter.imageToCSV3D(voteSelectedFile);
        CSVPointBuilder csvPointBuilder = new CSVPointBuilder(csvImageFile.getAbsolutePath(), false);
        System.out.println("constrructed image csv from image");
        List<ClusteringDataPair> clusteringDataColumn = csvPointBuilder.getClusteringDataColumn();

        System.out.println("simulation  is done");
        Map<String, List<ClusterPoint>> clusteringDimensions = csvPointBuilder.getClusteringDimensions();
        List<KmeansResolver> simulations;
        simulations = new ArrayList<>();

        clusteringDimensions.keySet().stream().forEach(key -> {
            if (!(key.toLowerCase().trim().startsWith("class ")
                    || //key.toLowerCase().trim().startsWith("id") ||
                    key.toLowerCase().trim().endsWith(" class")
                    || key.toLowerCase().trim().endsWith("id")
                    || key.toLowerCase().trim().equals("id"))) {
                final KmeansResolver kmeansResolver = new KmeansResolver(clusteringDimensions.get(key), (int) nombreClusterVote.getValue());
                System.out.println("simulation " + key + " is done");
                kmeansResolver.setSimulationName(key);
                simulations.add(kmeansResolver);

            }
        });
        System.out.println("simulation  is done");
        KmeansG algorithm = new KmeansG(clusteringDataColumn);

        GameTheoryResolver gameTheoryResolver = new GameTheoryResolver(simulations, algorithm.getMatriceCSV());
        // preparé une classe qui va organiser les resultats
        PFEDataFormator pfeDataFormator = new PFEDataFormator(gameTheoryResolver.getPairResults());
        System.out.println("simulation  is done");

        // specifier le point de depart (on lui donne la classification du csv --> derniere collone class )
        pfeDataFormator.setClassData(clusteringDataColumn);
        System.out.println("simulation  is done");
        JFXAlert alert = new JFXAlert();
        System.out.println("simulation  is done");
        final HBox chartContainer = new HBox();
        System.out.println("simulation  is done");
        chartContainer.setAlignment(Pos.CENTER);
        System.out.println("simulation  is done");

        List<ClusterPoint> points = new ArrayList<>();
        final List<Integer> classes = gameTheoryResolver.getClasses();
        System.out.println("classes.size() = " + classes.size());
        
        for (int i = 0; i < classes.size(); i++) {
            final List<Float> individu = algorithm.getMatriceCSV().get(i);
            final ClusterPoint clusterPoint = new ClusterPoint(individu.get(0), individu.get(1));
            clusterPoint.setCurrentCluster(classes.get(i));
            points.add(clusterPoint);
            System.out.println("i");
        }
        System.out.println("simulation  is done");

        ImageView drawImage = drawImage(points);
        chartContainer.setSpacing(15);
        System.out.println("simulation  is done");
        chartContainer.getChildren().add(drawImage);
        alert.setContent(chartContainer);
        alert.show();
    }

    private void processVoteCSV() throws IOException {
        if (!etatNombreDeClasseVote) {
            // vote supervisé
            CSVPointBuilder csvPointBuilder = new CSVPointBuilder(voteSelectedFile.getAbsolutePath(), true);
            List<ClusteringDataPair> clusteringDataColumn = csvPointBuilder.getClusteringDataColumn();

            long startTime = System.currentTimeMillis();
            Map<String, List<ClusterPoint>> clusteringDimensions = csvPointBuilder.getClusteringDimensions();
            List<KmeansResolver> simulations;
            simulations = new ArrayList<>();

            clusteringDimensions.keySet().stream().forEach(key -> {
                if (!(key.toLowerCase().trim().startsWith("class ")
                        || //key.toLowerCase().trim().startsWith("id") ||
                        key.toLowerCase().trim().endsWith(" class")
                        || key.toLowerCase().trim().endsWith("id")
                        || key.toLowerCase().trim().equals("id"))) {
                    final KmeansResolver kmeansResolver = new KmeansResolver(clusteringDimensions.get(key), (int) nombreClusterVote.getValue());
                    System.out.println("simulation " + key + " is done");
                    kmeansResolver.setSimulationName(key);
                    simulations.add(kmeansResolver);

                }
            });
            System.out.println("simulation  is done");
            KmeansG algorithm = new KmeansG(clusteringDataColumn);

            GameTheoryResolver gameTheoryResolver = new GameTheoryResolver(simulations, algorithm.getMatriceCSV());

            long endTime = (System.currentTimeMillis() - startTime);

            System.out.println("temps d execution methode de vote est  :  " + endTime + "ms avec un nombre d iteration " + gameTheoryResolver.getIterations());

            // preparé une classe qui va organiser les resultats
            PFEDataFormator pfeDataFormator = new PFEDataFormator(gameTheoryResolver.getPairResults());

            // specifier le point de depart (on lui donne la classification du csv --> derniere collone class )
            pfeDataFormator.setClassData(clusteringDataColumn);
            //JOptionPane.showConfirmDialog(null, "wait");
            //pfeDataFormator

            ObservableList<TableClusterElement> tableStudyResultList = FXCollections.observableArrayList();;

            tableStudyResultList.clear();

            int PARAM_AFFICHAGE_1 = 1, PARAM_AFFICHAGE_2 = 2;

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

            popTableVote.getItems().clear();
            popTableVote.getItems().addAll(tableStudyResultList);
            // ajouter resumer depuis le reste des information introduite
            chartVoteBenchMarkInitial.dataProperty().get().clear();
            chartVoteBenchMarkFinal.dataProperty().get().clear();
            chartVoteBenchMarkBruit.dataProperty().get().clear();
            chartVoteBenchMarkInitial.setTitle("Initial");
            chartVoteBenchMarkBruit.setTitle("Bruit");
            chartVoteBenchMarkFinal.setTitle("Final \nnombre d'iterations : "+gameTheoryResolver.getIterations()+"\n"
                    + "temps "+endTime+" ms");
            for (int i = 0; i < nombreClusterVote.getValue(); i++) {

                final ObservableList<XYChart.Data<Number, Number>> observableArrayList = FXCollections.observableArrayList();

                for (int j = 0; j < pfeDataFormator.getClassifications().get(i).size(); j++) {
                    String node = (String) pfeDataFormator.getClassifications().get(i).get(j);
                    int nodeIndex = Integer.parseInt(node.replace("node", ""));

                    observableArrayList.add(new XYChart.Data<>(Double.valueOf(clusteringDataColumn.get(PARAM_AFFICHAGE_1 % clusteringDataColumn.size()).getColumnPoints().get(nodeIndex).getValue()), Double.valueOf(clusteringDataColumn.get(PARAM_AFFICHAGE_2 % clusteringDataColumn.size()).getColumnPoints().get(nodeIndex).getValue())));
                }

                final XYChart.Series<Number, Number> series = new XYChart.Series<>(observableArrayList);
                series.setName("class" + i);
                chartVoteBenchMarkFinal.dataProperty().get().add(series);

                final ObservableList<XYChart.Data<Number, Number>> observableArrayListBruit = FXCollections.observableArrayList();

                for (int j = 0; j < pfeDataFormator.getClassificationBenchMark().get(i).size(); j++) {
                    String node = (String) pfeDataFormator.getClassificationBenchMark().get(i).get(j);
                    int nodeIndex = Integer.parseInt(node.replace("node", "")) - 1;
                    System.out.println("node index " + nodeIndex + " noise");
                    observableArrayListBruit.add(new XYChart.Data<>(Double.valueOf(clusteringDataColumn.get(PARAM_AFFICHAGE_1 % clusteringDataColumn.size()).getColumnPoints().get(nodeIndex).getValue()), Double.valueOf(clusteringDataColumn.get(PARAM_AFFICHAGE_2 % clusteringDataColumn.size()).getColumnPoints().get(nodeIndex).getValue())));
                }

                final XYChart.Series<Number, Number> seriesBruit = new XYChart.Series<>(observableArrayListBruit);
                seriesBruit.setName("class" + i);
                chartVoteBenchMarkInitial.dataProperty().get().add(seriesBruit);

                final ObservableList<XYChart.Data<Number, Number>> observableArrayListBruitReel = FXCollections.observableArrayList();

                for (int j = 0; j < pfeDataFormator.getClassificationBenchMark().get(i).size(); j++) {
                    String node = (String) pfeDataFormator.getClassificationBenchMark().get(i).get(j);
                    boolean contains = pfeDataFormator.getClassifications().get(i).contains(pfeDataFormator.getClassificationBenchMark().get(i).get(j));
                    int nodeIndex = Integer.parseInt(node.replace("node", "")) - 1;
                    System.out.println("node index " + nodeIndex + " noise");
                    if (contains) {
                        observableArrayListBruitReel.add(new XYChart.Data<>(Double.valueOf(clusteringDataColumn.get(PARAM_AFFICHAGE_1 % clusteringDataColumn.size()).getColumnPoints().get(nodeIndex).getValue()), Double.valueOf(clusteringDataColumn.get(PARAM_AFFICHAGE_2 % clusteringDataColumn.size()).getColumnPoints().get(nodeIndex).getValue())));
                    }
                }

                final XYChart.Series<Number, Number> seriesBruitreel = new XYChart.Series<>(observableArrayListBruitReel);
                seriesBruitreel.setName("class" + i);
                chartVoteBenchMarkBruit.dataProperty().get().add(seriesBruitreel);

            }

        } else {
            //vote non supervisé

            List<ClusteringDataPair> listeDesAttribusAvecValeur = null;
            CSVPointBuilder csvpb = new CSVPointBuilder(voteSelectedFile.getAbsolutePath(), false);
            listeDesAttribusAvecValeur = csvpb.getClusteringDataColumn();

            // choix de collone du graphe
            final int PARAM_AFFICHAGE_1 = 2;
            final int PARAM_AFFICHAGE_2 = 1;

            int iteration = 0;
            long currentTimeMillis = System.currentTimeMillis();
            long timeFinal = 0;

            GameTheoryResolver best_gameTheoryResolverWB = null;
            GameTheoryResolver best_gameTheoryResolverDB = null;
            double min_wb = Double.MAX_VALUE;
            double min_db = Double.MAX_VALUE;
            int winner_KWB = 2;
            int winner_KDB = 2;
            for (int i = 2; i <= 10; i++) {
                Map<String, List<ClusterPoint>> clusteringDimensions = csvpb.getClusteringDimensions();
                final Integer I = i;
                List<KmeansResolver> simulations;
                simulations = new ArrayList<>();

                clusteringDimensions.keySet().stream().forEach(key -> {
                    if (!(key.toLowerCase().trim().startsWith("class ")
                            || //key.toLowerCase().trim().startsWith("id") ||
                            key.toLowerCase().trim().endsWith(" class")
                            || key.toLowerCase().trim().endsWith("id")
                            || key.toLowerCase().trim().equals("id"))) {
                        final KmeansResolver kmeansResolver = new KmeansResolver(clusteringDimensions.get(key), I);
                        System.out.println("simulation " + key + " is done");
                        kmeansResolver.setSimulationName(key);
                        simulations.add(kmeansResolver);

                    }
                });
                System.out.println("simulation  is done");
                KmeansG algorithm = new KmeansG(listeDesAttribusAvecValeur);

                GameTheoryResolver gameTheoryResolver;
                gameTheoryResolver = new GameTheoryResolver(simulations, algorithm.getMatriceCSV());
                final double wb = gameTheoryResolver.wbCalculation(i);

                iteration += gameTheoryResolver.getIterations();
                if (wb < min_wb) {
                    min_wb = wb;
                    best_gameTheoryResolverWB = gameTheoryResolver;
                    winner_KWB = i;
                }

                gameTheoryResolver = new GameTheoryResolver(simulations, algorithm.getMatriceCSV());
                final double db = gameTheoryResolver.daviesBouldin(i);
                if (db < min_db) {
                    min_db = db;
                    best_gameTheoryResolverDB = gameTheoryResolver;
                    winner_KDB = i;
                }

                chartVoteKIndicePlotDB.getData().get(0).getData().add(new XYChart.Data<>(i, db));
                chartVoteKIndicePlotWB.getData().get(0).getData().add(new XYChart.Data<>(i, wb));
                System.out.println(" i = " + i + "  WB = " + wb);
                System.out.println(" i = " + i + "  DBI = " + db);
                long time = System.currentTimeMillis();
                timeFinal = (time - currentTimeMillis) / 1000;
//            iteration += ca.getIterations();
                chartVoteKIndicePlotDB.setTitle("Davies Bouldin");
                chartVoteKIndicePlotWB.setTitle("WB");

            }
            if (best_gameTheoryResolverDB != null) {
                //drawBest
                PFEDataFormator pfeDataFormator = new PFEDataFormator(best_gameTheoryResolverDB.getPairResults());

                // specifier le point de depart (on lui donne la classification du csv --> derniere collone class )
                pfeDataFormator.setClassData(listeDesAttribusAvecValeur);
                chartVoteBestKPlotDB.getData().clear();
                for (int i = 0; i < winner_KDB; i++) {

                    final ObservableList<XYChart.Data<Number, Number>> observableArrayList = FXCollections.observableArrayList();

                    for (int j = 0; j < pfeDataFormator.getClassifications().get(i).size(); j++) {
                        String node = (String) pfeDataFormator.getClassifications().get(i).get(j);
                        int nodeIndex = Integer.parseInt(node.replace("node", ""));

                        observableArrayList.add(new XYChart.Data<>(Double.valueOf(listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(nodeIndex).getValue()), Double.valueOf(listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(nodeIndex).getValue())));
                    }

                    final XYChart.Series<Number, Number> series = new XYChart.Series<>(observableArrayList);
                    series.setName("class" + i);
                    chartVoteBestKPlotDB.dataProperty().get().add(series);
                    chartVoteBestKPlotDB.setTitle("nombre iteration : " + best_gameTheoryResolverDB.getIterations());

                }
            }

            if (best_gameTheoryResolverWB != null) {
                PFEDataFormator pfeDataFormator = new PFEDataFormator(best_gameTheoryResolverWB.getPairResults());

                // specifier le point de depart (on lui donne la classification du csv --> derniere collone class )
                pfeDataFormator.setClassData(listeDesAttribusAvecValeur);
                chartVoteBestKPlotWB.getData().clear();
                for (int i = 0; i < winner_KWB; i++) {

                    final ObservableList<XYChart.Data<Number, Number>> observableArrayList = FXCollections.observableArrayList();

                    for (int j = 0; j < pfeDataFormator.getClassifications().get(i).size(); j++) {
                        String node = (String) pfeDataFormator.getClassifications().get(i).get(j);
                        int nodeIndex = Integer.parseInt(node.replace("node", ""));

                        observableArrayList.add(new XYChart.Data<>(Double.valueOf(listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(nodeIndex).getValue()), Double.valueOf(listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(nodeIndex).getValue())));
                    }

                    final XYChart.Series<Number, Number> series = new XYChart.Series<>(observableArrayList);
                    series.setName("class" + i);
                    chartVoteBestKPlotWB.dataProperty().get().add(series);
                    chartVoteBestKPlotWB.setTitle("Nombre d'iterations : " + best_gameTheoryResolverWB.getIterations());
                }
            }
        }

    }

    private void processTHJImaging() {
//        if (ImageCheck.isSelected()) {
//                thja.setNombreiterationMax(80);
//            }
    }

    private void processTHJCSV() throws IOException {
        if (!etatNombreDeClasseTHJ) {
            // thj supervisé
            CSVPointBuilder csvpb = new CSVPointBuilder(thjSelectedFile.getAbsolutePath(), false);
            List<ClusteringDataPair> listeDesAttribusAvecValeur = csvpb.getClusteringDataColumn();

            long start = System.currentTimeMillis();

            int k = (int) nombreClusterTHJ.getValue();
            int PARAM_AFFICHAGE_1 = 1;
            int PARAM_AFFICHAGE_2 = 2;
            KmeansG ca = new KmeansG(listeDesAttribusAvecValeur);
            ca.resolve(k, 2);

            String tit1e = "THJ indice " + indiceValiditeTHJ.getSelectionModel().getSelectedItem();
            double wb = ca.getWB();
            THJAlgorithm thja = new THJAlgorithm(ca.getMatriceCSV(), 12000, wb);
            if (indiceValiditeTHJ.getSelectionModel().getSelectedItem().equals("Davies Bouldin")) {
                thja.setDaviesBouldinCalculator();
            }
            thja.resolve(k);

            System.out.println("best wb for " + k + " is : " + thja.getBestWB());
            System.out.println("best composition for " + k + " is : " + thja.getBestMap().values());

            Hashtable<Integer, Cluster> bestMap = thja.getBestMap();
            List<ClusterPoint> centroids = new ArrayList<>();
            List<ClusterPoint> points = new ArrayList<>();
            int i = 0;
            System.out.println("best map size = " + bestMap.size());
            for (int j = 0; j < bestMap.size(); j++) {

                Cluster cluster = bestMap.get(j);
                System.out.println("cluster members size = " + cluster.getPlayers().size());
                List<Float> clusterCoordinates = cluster.getClusterCoordinates();
                centroids.add(new ClusterPoint(clusterCoordinates.get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()), clusterCoordinates.get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size())));
                for (Player player : cluster.getPlayers()) {
                    final ClusterPoint playerPoint = new ClusterPoint(player.getAttributes().get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()), player.getAttributes().get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size()));
                    playerPoint.setCurrentCluster(i);
                    playerPoint.setPlayerIndex(player.getName());
                    points.add(playerPoint);
                }
                i++;
            }

            long end = (System.currentTimeMillis() - start);
            //------------------------- Affichage
            chartTHJBenchMarkInitial.dataProperty().get().clear();
            chartTHJBenchMarkInitial.setTitle("Initial");
            chartTHJBenchMarkFinal.dataProperty().get().clear();
            chartTHJBenchMarkBruit.dataProperty().get().clear();
            chartTHJBenchMarkBruit.setTitle("Bruit");
            chartTHJBenchMarkFinal.setTitle("Final\nNombre d'iteration : "+thja.getIteration()+"\n"
                    + "temps "+end+" ms");

            Predicate<? super ClusteringDataPair> classFilter = cdp -> {
                return cdp.getColumnName().toLowerCase().trim().equals("class");
            };

            ClusteringDataPair classColumnAlgo2 = listeDesAttribusAvecValeur.stream().filter(classFilter).findFirst().get();

            final List<Integer> centersPrevious = new ArrayList();

            classColumnAlgo2.getColumnPoints().stream().forEach(center -> {
                System.out.println("center foudn   " + center.getValue().intValue());
                centersPrevious.add(center.getValue().intValue());
            });

            //-----------------------------------
            for (int l = 0; l < k; l++) {
                final ObservableList<XYChart.Data<Number, Number>> observableArrayListBruit = FXCollections.observableArrayList();

                for (int j = 0; j < centersPrevious.size(); j++) {
                    if (centersPrevious.get(j) == l) {
                        final Float x = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(j).getValue();
                        final Float y = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(j).getValue();
                        System.out.println("x = " + x + "   y = " + y + "  noise");
                        observableArrayListBruit.add(new XYChart.Data<>(Double.valueOf(x), Double.valueOf(y)));
                    }
                }

                final XYChart.Series<Number, Number> seriesBruit = new XYChart.Series<>(observableArrayListBruit);
                seriesBruit.setName("class" + l);
                chartTHJBenchMarkInitial.dataProperty().get().add(seriesBruit);

            }

            for (int l = 0; l < k; l++) {
                final ObservableList<XYChart.Data<Number, Number>> observableArrayListBruit = FXCollections.observableArrayList();

                for (int j = 0; j < centersPrevious.size(); j++) {
                    Predicate<? super ClusterPoint> prdct;
                    final int J = j;
                    prdct = (ClusterPoint cp) -> {
                        return cp.getPlayerIndex().equals("" + J);
                    };
                    try {

                        if (centersPrevious.get(j) == l && centersPrevious.get(j) == points.stream().filter(prdct).findFirst().get().getCurrentCluster()) {
                            final Float x = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_1 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(j).getValue();
                            final Float y = listeDesAttribusAvecValeur.get(PARAM_AFFICHAGE_2 % listeDesAttribusAvecValeur.size()).getColumnPoints().get(j).getValue();
                            System.out.println("x = " + x + "   y = " + y + "  noise");
                            observableArrayListBruit.add(new XYChart.Data<>(Double.valueOf(x), Double.valueOf(y)));
                        }
                    } catch (Exception e) {
                        System.out.println("element a la corbeil " + j);
                    }
                }

                final XYChart.Series<Number, Number> seriesBruit = new XYChart.Series<>(observableArrayListBruit);
                seriesBruit.setName("class" + l);
                chartTHJBenchMarkBruit.dataProperty().get().add(seriesBruit);

            }
            ///affichage final
            drawChart(thja.getIteration(), centroids, points, chartTHJBenchMarkFinal);
            System.out.println("nombre d iteration " + thja.getIteration() + " \n temps d execution " + end + " ms");
            chartTHJBenchMarkFinal.setTitle("nombre d iteration " + thja.getIteration() + " \n temps d execution " + end + " ms");
            /// affichage tableau
            List<Integer> classesAlgorithme = new ArrayList();
            ObservableList<TableClusterElement> tableStudyResultList = FXCollections.observableArrayList();
            int populationSize = centersPrevious.size();
            for (int j = 0; j < populationSize; j++) {
                classesAlgorithme.add(-1);
            }

            for (int j = 0; j < populationSize; j++) {
                final int J = j;
                Predicate<? super ClusterPoint> elementId = cp -> {
                    return cp.getPlayerIndex().equals("" + J);
                };
                try {
                    final ClusterPoint cp = points.stream().filter(elementId).findFirst().get();
                    String index = cp.getPlayerIndex();
                    int currentCluster = cp.getCurrentCluster();
                    classesAlgorithme.remove(Integer.parseInt(index));
                    classesAlgorithme.add(Integer.parseInt(index), currentCluster);

                } catch (Exception e) {
                    System.out.println("element a la corbeil " + j);
                }
            }
            System.out.println("class algo " + classesAlgorithme);
            System.out.println("centersPrevious" + centersPrevious);
            int currentBenchMarkSize = 0;
            int currentWellClassedSize = 0;
            for (int l = 0; l < nombreClusterTHJ.getValue(); l++) {
                int count = 0;
                for (Integer integer : classesAlgorithme) {
                    if (integer == l) {
                        count++;
                    }
                }
                int wellClassedSize = 0;
                for (int j = 0; j < centersPrevious.size(); j++) {
                    if (Objects.equals(centersPrevious.get(j), classesAlgorithme.get(j)) && centersPrevious.get(j) == l) {
                        wellClassedSize++;
                    }
                }
                final int benchmarkSize = centersPrevious.lastIndexOf(l) - currentBenchMarkSize + 1;
                tableStudyResultList.add(new TableClusterElement("" + (benchmarkSize),
                        "" + count, "" + wellClassedSize, "" + ((float) wellClassedSize / (float) benchmarkSize) * 100));
                currentWellClassedSize += wellClassedSize;
                currentBenchMarkSize += benchmarkSize;
            }
            tableStudyResultList.add(new TableClusterElement("" + (currentBenchMarkSize),
                    "" + currentBenchMarkSize, "" + currentWellClassedSize, "" + ((float) currentWellClassedSize / (float) currentBenchMarkSize) * 100));

            popTableTHJ.getItems().clear();
            popTableTHJ.getItems().addAll(tableStudyResultList);

        } else {
            // thj non supervisé
            CSVPointBuilder csvpb = new CSVPointBuilder(thjSelectedFile.getAbsolutePath(), false);
            List<ClusteringDataPair> listeDesAttribusAvecValeur = csvpb.getClusteringDataColumn();

            // choix de collone du graphe
            final int PARAM_AFFICHAGE_1 = 2;
            final int PARAM_AFFICHAGE_2 = 1;

            List<KmeansG> cas = new ArrayList<>();
            List<THJAlgorithm> thjas = new ArrayList<>();
            int iteration = 0;
            int bestK_WB = 0;
            int bestK_DB = 0;
            long currentTimeMillis = System.currentTimeMillis();
            long timeFinal = 0;
            double min_db = Double.MAX_VALUE;
            double min_wb = Double.MAX_VALUE;
            THJAlgorithm thja_db = null;
            THJAlgorithm thja_wb = null;
            for (int i = 2; i <= 10; i++) {
                KmeansG ca = new KmeansG(listeDesAttribusAvecValeur);
                ca.resolve(i, 2);
                cas.add(ca);
                System.out.println("ca rendred starting thj");
                double wb = ca.getWB();
                THJAlgorithm thja;
                thja = new THJAlgorithm(ca.getMatriceCSV(), 12000, wb);
                thja.setWbCalculator();
                thja.resolve(i);
                double thjWB = thja.getBestWB();
                if (thjWB < min_wb) {
                    thja_wb = thja;
                    min_wb = thjWB;
                    bestK_WB = i;
                }
                chartTHJKIndicePlotWB.getData().get(0).getData().add(new XYChart.Data<>(i, thjWB));
                thja = new THJAlgorithm(ca.getMatriceCSV(), 12000, wb);

                thja.setDaviesBouldinCalculator();
                thja.resolve(i);
                double thjDB = thja.getBestWB();
                if (thjDB < min_db) {
                    thja_db = thja;
                    min_db = thjDB;
                    bestK_DB = i;
                }
                chartTHJKIndicePlotDB.getData().get(0).getData().add(new XYChart.Data<>(i, thjDB));

            }

            if (thja_db != null) {
                List<ClusterPoint> centroids = new ArrayList();
                List<ClusterPoint> points = new ArrayList();

                Hashtable<Integer, Cluster> bestMap = thja_db.getBestMap();
                for (int i = 0; i < bestMap.size(); i++) {
                    Cluster cluster = bestMap.get(i);
                    final ClusterPoint clusterPointCenter = new ClusterPoint(cluster.getClusterCoordinates().get(PARAM_AFFICHAGE_1), cluster.getClusterCoordinates().get(PARAM_AFFICHAGE_2));
                    clusterPointCenter.setCurrentCluster(i);
                    centroids.add(clusterPointCenter);

                    List<Player> players = cluster.getPlayers();
                    for (int j = 0; j < players.size(); j++) {
                        Player player = players.get(j);
                        final ClusterPoint clusterPoint = new ClusterPoint(player.getAttributes().get(PARAM_AFFICHAGE_1), player.getAttributes().get(PARAM_AFFICHAGE_2));
                        clusterPoint.setCurrentCluster(i);
                        points.add(clusterPoint);
                    }
                }
                drawChart(thja_db.getIteration(), centroids, points, chartTHJBestKPlotDB);
            }
            if (thja_wb != null) {
                List<ClusterPoint> centroids = new ArrayList();
                List<ClusterPoint> points = new ArrayList();

                Hashtable<Integer, Cluster> bestMap = thja_wb.getBestMap();
                for (int i = 0; i < bestMap.size(); i++) {
                    Cluster cluster = bestMap.get(i);
                    final ClusterPoint clusterPointCenter = new ClusterPoint(cluster.getClusterCoordinates().get(PARAM_AFFICHAGE_1), cluster.getClusterCoordinates().get(PARAM_AFFICHAGE_2));
                    clusterPointCenter.setCurrentCluster(i);
                    centroids.add(clusterPointCenter);

                    List<Player> players = cluster.getPlayers();
                    for (int j = 0; j < players.size(); j++) {
                        Player player = players.get(j);
                        final ClusterPoint clusterPoint = new ClusterPoint(player.getAttributes().get(PARAM_AFFICHAGE_1), player.getAttributes().get(PARAM_AFFICHAGE_2));
                        clusterPoint.setCurrentCluster(i);
                        points.add(clusterPoint);
                    }
                }
                drawChart(thja_wb.getIteration(), centroids, points, chartTHJBestKPlotWB);
            }

        }
    }
}
