package dz.talcorp.clustering;

import algorithme.KmeansResolver;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import entity.ClusterPoint;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class FXMLController implements Initializable {

    @FXML
    private JFXSlider tailleCluster;

    @FXML
    private JFXSlider tailleNoeud;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXButton launch;

    @FXML
    private ScatterChart<Number, Number> chart;

    @FXML
    private JFXHamburger hamburger;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeDrawer();

    }

    @FXML
    void Simule(ActionEvent event) {
        //lancement du simulateur
        KmeansResolver kmeansResolver = new KmeansResolver((int) tailleNoeud.getValue(), (int) tailleCluster.getValue());
        //recuperation du resultat
        List<ClusterPoint> centroids = kmeansResolver.getCentroids();
        int numOfRepeat = kmeansResolver.getNumOfRepeat();
        List<ClusterPoint> points = kmeansResolver.getPoints();
        //preparation du graph
        chart.setTitle("Carte de résolution \n nombre d'itération : "+numOfRepeat);
        chart.setTitleSide(Side.TOP);
        centroids.stream().forEach(new Consumer<ClusterPoint>() {
            int i = 0;// indice du cluster courant
            @Override
            public void accept(ClusterPoint center) {
                // creer uen nouvelle serie
                XYChart.Series<Number,Number> seriesCentre = new XYChart.Series();
                seriesCentre = new XYChart.Series();
                seriesCentre.setName("Centre cluster "+i);
                // filtrer que les point membre de ce cluster
                Predicate<? super ClusterPoint> prdct = point ->{
                    return point.getCurrentCluster() == i;
                };
                // ajouter le centre
                seriesCentre.getData().add(new XYChart.Data<>(center.getX(), center.getY()));
                
                final XYChart.Series<Number,Number> seriesMembre = new XYChart.Series();
                seriesMembre.setName("membre du cluster "+i);
                // ajouter la population
                points.stream().filter(prdct).forEach((ClusterPoint point) -> {
                    seriesMembre.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
                    System.out.println("point "+point);
                });
                chart.getData().add(seriesCentre);
                chart.getData().add(seriesMembre);
                i++;
                System.out.println("graphe itere");
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

}
