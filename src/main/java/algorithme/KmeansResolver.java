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

    private final List<ClusterPoint> points;
    private final List<ClusterPoint> centroids;
    private List<ClusterPoint> previsouCentroids;

    /**
     *
     * @param numPoints la taille de la population
     * @param numClusters le nombre de cluster desiré
     */
    public KmeansResolver(int numPoints, int numClusters) {
        // creation de la population
        this.points = new ArrayList<>(numPoints);
        for (int i = 0; i < numPoints; i++) {
            points.add(new ClusterPoint());
        }
        System.out.println(points);
        // initialisation de l algorithme
        this.centroids = new ArrayList<>(numClusters);
        this.previsouCentroids = new ArrayList<>(numClusters);
        for (int i = 0; i < numClusters; i++) {
            centroids.add(points.get(i));
        }
        System.out.println(centroids);
        resolve();

    }

    private final int ETAPE_RECHERCHE = 0;
    private final int ETAPE_VALIDATION = 1;
    private final int FIN = 2;

    private int numOfRepeat;

    private void resolve() {
        this.numOfRepeat = 0;
        int step = 0 ;
        boolean resolved = false;
        
        while (!resolved) {

            //affecter chaque point a un cluster
            points.stream().forEach(point -> {
                point.attachToNearestIndex(centroids);
            });

            //sauvegarder l ancien clustering
            previsouCentroids = new ArrayList<>();// vider l hitorique
            for (int i = 0; i < centroids.size(); i++) {
                ClusterPoint centroid = centroids.get(i);
                previsouCentroids.add(i,new ClusterPoint(centroid.getX(), centroid.getY())); // creer une sauvegarde
            }
            

            //calculer les nouveaux centroids
            for (int centroidIndex = 0; centroidIndex < centroids.size(); centroidIndex++) {
                ClusterPoint centroid = centroids.get(centroidIndex);
                final int finalCentroidIndex = centroidIndex;
                Predicate<ClusterPoint> prdct = point -> {
                    return point.getCurrentCluster() == finalCentroidIndex;
                };
                // mettre a jour
                centroid.updateCenter(points.stream().filter(prdct).collect(Collectors.toList()));
                
            }
        
           
            // verifier le nouveau centre avec l ancien
            boolean endOrNot = true;
            for (int i = 0; i < centroids.size(); i++) {
                endOrNot = endOrNot && centroids.get(i).isTheSameAs(previsouCentroids.get(i));
            }
            // verifier le resultat de l iteration courante avec la precedente
            System.out.println("repet num : " + numOfRepeat);
            numOfRepeat++;
            if (endOrNot) {
                step = FIN;
            }

            if (numOfRepeat > 100) {
                resolved = true;
            }
            
            if (step == FIN) {
                resolved = true;
            }

        }

    }

    public List<ClusterPoint> getCentroids() {
        return centroids;
    }

    public List<ClusterPoint> getPoints() {
        return points;
    }

    public List<ClusterPoint> getPrevisouCentroids() {
        return previsouCentroids;
    }

    public int getNumOfRepeat() {
        return numOfRepeat;
    }

}
