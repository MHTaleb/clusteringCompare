/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithme;

import entity.ClusterPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author taleb
 */
public class KmeansResolver {

    private String simulationName;
    
    private final List<ClusterPoint> POINTS;
    private final List<ClusterPoint> CENTROIDS;
    private List<ClusterPoint> previsouCentroids;

    /**
     *
     * @param numPoints la taille de la population
     * @param numClusters le nombre de cluster desiré
     */
    public KmeansResolver(int numPoints, int numClusters) {
        // creation de la population
        this.POINTS = new ArrayList<>(numPoints);
        for (int i = 0; i < numPoints; i++) {
            POINTS.add(new ClusterPoint());
        }
//        System.out.println(POINTS);
        // initialisation de l algorithme
        this.CENTROIDS = new ArrayList<>(numClusters);
        this.previsouCentroids = new ArrayList<>(numClusters);
        for (int i = 0; i < numClusters; i++) {
            CENTROIDS.add(POINTS.get(i));
        }
//        System.out.println(CENTROIDS);
        resolve();

    }

    public KmeansResolver(List<ClusterPoint> POINTS,int numClusters) {
        this.POINTS = new ArrayList<>(POINTS);
        this.CENTROIDS = new ArrayList<>();
        this.previsouCentroids = new ArrayList<>(numClusters);
        for (int i = 0; i < numClusters; i++) {
            CENTROIDS.add(this.POINTS.get(i));
        }
        resolve();
    }

    public String getSimulationName() {
        return simulationName;
    }

    public void setSimulationName(String simulationName) {
        this.simulationName = simulationName;
    }
    
    

 
    private final int FIN = 2;

    private int numOfRepeat;

    private void resolve() {
        this.numOfRepeat = 0;
        int step = 0;
        boolean resolved = false;

        while (!resolved) {

            //affecter chaque point a un cluster
            POINTS.stream().forEach(point -> {
                point.attachToNearestIndex(CENTROIDS);
            });

            //sauvegarder l ancien clustering
            previsouCentroids = new ArrayList<>();// vider l hitorique
            for (int i = 0; i < CENTROIDS.size(); i++) {
                ClusterPoint centroid = CENTROIDS.get(i);
                previsouCentroids.add(i, new ClusterPoint(centroid.getX(), centroid.getY())); // creer une sauvegarde
            }

            //calculer les nouveaux centroids
            for (int centroidIndex = 0; centroidIndex < CENTROIDS.size(); centroidIndex++) {
                ClusterPoint centroid = CENTROIDS.get(centroidIndex);
                final int finalCentroidIndex = centroidIndex;
                Predicate<ClusterPoint> prdct = point -> {
                    return point.getCurrentCluster() == finalCentroidIndex;
                };
                // mettre a jour
                centroid.updateCenter(POINTS.stream().filter(prdct).collect(Collectors.toList()));

            }

            // verifier le nouveau centre avec l ancien
            boolean endOrNot = true;
            for (int i = 0; i < CENTROIDS.size(); i++) {
                endOrNot = endOrNot && CENTROIDS.get(i).isTheSameAs(previsouCentroids.get(i));
            }
            // verifier le resultat de l iteration courante avec la precedente
//            System.out.println("repet num : " + numOfRepeat);
            numOfRepeat++;
            if (endOrNot) {
                step = FIN;
            }

            if (numOfRepeat > 1000) {
                resolved = true;
            }

            if (step == FIN) {
                resolved = true;
            }

        }

    }

    public List<ClusterPoint> getCentroids() {
        return CENTROIDS;
    }

    public List<ClusterPoint> getPoints() {
        return POINTS;
    }

    public List<ClusterPoint> getPrevisouCentroids() {
        return previsouCentroids;
    }

    public int getNumOfRepeat() {
        return numOfRepeat;
    }

}
