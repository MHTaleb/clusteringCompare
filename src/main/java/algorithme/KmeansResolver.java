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
    private final List<ClusterPoint> previsouCentroids;

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
        // initialisation de l algorithme
        this.centroids = new ArrayList<>(numClusters);
        this.previsouCentroids = new ArrayList<>(numClusters);
        for (int i = 0; i < numClusters; i++) {
            centroids.add(points.get(i));
        }

        resolve();

    }

    private final int ETAPE_RECHERCHE = 0;
    private final int ETAPE_VALIDATION = 1;
    private final int FIN = 2;

    private int numOfRepeat;

    private void resolve() {
        this.numOfRepeat = 0;
        int step = ETAPE_RECHERCHE;
        boolean resolved = false;
        while (!resolved) {
         
                    //affecter chaque point a un cluster
                    points.stream().forEach(point -> {
                        point.attachToNearestIndex(centroids);
                    });

                    //sauvegarder l ancien clustering
                    previsouCentroids.clear(); // vider l hitorique
                    centroids.stream().forEach(centroid -> {
                        previsouCentroids.add(new ClusterPoint(centroid.getX(), centroid.getY())); // creer une sauvegarde
                    });

                    //calculer les nouveaux centroids
                    int centroidIndex = 0;
                    centroids.forEach(centroid -> {
                        // filtre qui permet de recuperer que les membres du cluster
                        Predicate<ClusterPoint> prdct = point -> {
                            return point.getCurrentCluster() == centroidIndex;
                        };
                        // mettre a jour
                        centroid.updateCenter(points.stream().filter(prdct).collect(Collectors.toList()));
                    });
                    step = ETAPE_VALIDATION;
         
                    
                    boolean endOrNot = true;
                    for (int i = 0; i < centroids.size(); i++) {
                        endOrNot = endOrNot && centroids.get(i).isTheSameAs(previsouCentroids.get(i));
                    }
                    // verifier le resultat de l iteration courante avec la precedente
                    System.out.println("repet num : "+numOfRepeat);
                    numOfRepeat++;
                    if (endOrNot) {
                        step = FIN;
                    } else {
                        step = ETAPE_RECHERCHE;
                    }

          
                    if (step == FIN )resolved = true;
                    
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
