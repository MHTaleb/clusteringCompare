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
     * cette classe a comme objectif de resoudre le probleme de clustering avec kmeans pour un ensemble de données avec une cardinalité x,y
     * à la fin on aura une simulation finie ou on peu recuperer le resultat du clustering et les centroid avec les methode getCentroids() et
     * getPoints()
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
    /**
     * cette classe a comme objectif de resoudre le probleme de clustering avec kmeans pour un ensemble de données avec une cardinalité x,y
     * à la fin on aura une simulation finie ou on peu recuperer le resultat du clustering et les centroid avec les methode getCentroids() et
     * getPoints()
     * @param POINTS la population 
     * @param numClusters le nombre de cluster a former
     */
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
    
    /**
     * methode interne jamais exposer a l utilisateur elle est l implementation d kmean et la resolution du probleme
     */
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

    
    /**
     * la fonction qui donne le resultat sous forme de centre
     * 
     * @return liste des centres de chaque cluster
     */
    public List<ClusterPoint> getCentroids() {
        return CENTROIDS;
    }
    
    /**
     * cette fonction retourne la population total avec l indice d appartenance au centre
     * ou chaque individu va etre affecter a un cluster
     * @return population resolut
     */
    public List<ClusterPoint> getPoints() {
        return POINTS;
    }

    public List<ClusterPoint> getPrevisouCentroids() {
        return previsouCentroids;
    }

    public int getNumOfRepeat() {
        return numOfRepeat;
    }
    
    public float getWB(){
        float wb = 0 ;
        int TAILLE_CLUSTER = CENTROIDS.size();
        //ssw
        double ssw = 0 ;
        for(int i = 0 ; i<TAILLE_CLUSTER ; i++){
            ClusterPoint cpi = CENTROIDS.get(i);
            for(int j = 0 ; j<POINTS.size();j++){
                if(POINTS.get(j).getCurrentCluster() == i){
                    ClusterPoint xi = POINTS.get(j);
                    ssw+=minus(xi,cpi);
                }
            }
        }
        //x_
        ClusterPoint x_ = new ClusterPoint(0,0);
        for (int i = 0; i < POINTS.size(); i++) {
             x_.setX(x_.getX() + POINTS.get(i).getX());
             x_.setY(x_.getY() + POINTS.get(i).getY());
        }
        x_.setX(x_.getX() / POINTS.size());
        x_.setY(x_.getY() / POINTS.size());
        //ssb
        float ssb = 0;
        for (int i = 0; i < CENTROIDS.size(); i++) {
            ClusterPoint ci = CENTROIDS.get(i);
            Predicate<? super ClusterPoint> inCluster = new InClusterPredicate(i);
            long ni = POINTS.stream().filter(inCluster).count();
            ssb+=ni*minus(ci, x_);
        }
        wb = (float) (CENTROIDS.size() * ssw / ssb) ;
        return wb;
    }

    private float minus(ClusterPoint xi, ClusterPoint cpi) {
        float distance = 0;
        final double x_abs = Math.abs(xi.getX() - cpi.getX());
        final double y_abs = Math.abs(xi.getY() - cpi.getY());
        distance = (float) (x_abs*x_abs+y_abs*y_abs);
       return distance;
    }

    private static class InClusterPredicate implements Predicate<ClusterPoint> {

        private final int i;

        public InClusterPredicate(int i) {
            this.i = i;
        }

        @Override
        public boolean test(ClusterPoint t) {
            return t.getCurrentCluster() == i;
        }
    }

}
