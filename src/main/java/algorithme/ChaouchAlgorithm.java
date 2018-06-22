/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithme;

import builder.ClusteringDataPair;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author taleb
 */
public class ChaouchAlgorithm {

    private List<Integer> classes;
    private List<List<Float>> matriceCSV; // matrice des csv i:ligne , j:collone

    public ChaouchAlgorithm(List<ClusteringDataPair> clusteringDataPairs) {

        this.matriceCSV = new ArrayList<>();

        int nombreDeLigne = clusteringDataPairs.get(0).getColumnPoints().size();
        int nombreDeCollone = clusteringDataPairs.size();

        for (int i = 0; i < nombreDeLigne; i++) {
            List<Float> line = new ArrayList<>();
            for (int j = 0; j < nombreDeCollone; j++) {
                final ClusteringDataPair column = clusteringDataPairs.get(j);
                if (!column.getColumnName().trim().toLowerCase().equals("class")) {
                    Float value = column.getColumnPoints().get(i).getValue();
                    line.add(value);
                }
            }
            if (!line.isEmpty()) {
                matriceCSV.add(i, line);
            }
        }

        classes = new ArrayList<>(clusteringDataPairs.get(0).getColumnPoints().size());
    }
    List<List<Float>> currentCentroid;

    public void resolve(int numberOfClasses, final int G) {

        long start = System.currentTimeMillis();

        List<Integer> previousClasses = new ArrayList();
        int loop = 0;
        currentCentroid = new ArrayList<>();

        for (int i = 0; i < numberOfClasses; i++) {
            currentCentroid.add(matriceCSV.get(i));
        }

        List<Integer> currentClasses = new ArrayList();
        do {
            loop++;
            for (List<Float> element : matriceCSV) {
                float minDistance = Float.MAX_VALUE;
                int selectedIndex = 0;
                int currentIndex = 0;
                for (List<Float> center : currentCentroid) {
                    float currentDistance = getDistance(element, center, G);
                    if (currentDistance <= minDistance) {
                        minDistance = currentDistance;
                        selectedIndex = currentIndex;
                    }
                    currentIndex++;
                }
                currentClasses.add(selectedIndex);

            }

            if (!isSame(currentClasses, previousClasses)) {
                previousClasses = new ArrayList<>();
                currentClasses.stream().forEach(previousClasses::add);
                currentClasses = new ArrayList<>();
            }

            currentCentroid = new ArrayList<>();

            // 1 2 .... 3  centroid
            for (int i = 0; i < numberOfClasses; i++) {
                int divide = 0;
                final ArrayList<Float> centroid = new ArrayList<>();
                for (int j = 0; j < matriceCSV.size(); j++) { // ligne
                    if (previousClasses.get(j) == i) {
                        divide++;
                        List<Float> member = matriceCSV.get(j);
                        int k = 0;
                        for (Float val : member) {
                            try {
                                centroid.set(k, centroid.get(k) + val);
                            } catch (Exception e) {
                                centroid.add(k, val);
                            }
                            k++;
                        }
                    }
                }
                for (int j = 0; j < centroid.size(); j++) {
                    centroid.set(j, centroid.get(j) / divide);
                }
                currentCentroid.add(centroid);
            }
            System.out.println("processing");
        } while (!isSame(currentClasses, previousClasses));
        currentClasses.stream().forEach(classes::add);
        long end = (System.currentTimeMillis() - start)/1000;
        System.out.println("end of clustering " + end + " seconds and " + loop + " repeats");
    }

    private float getDistance(List<Float> element, List<Float> center, int g) {
        float sum = 0;
        for (int i = 0; i < element.size(); i++) {
            sum += Math.pow(Math.sqrt(Math.pow(element.get(i) - center.get(i), 2)), g);
        }
        return (float) Math.sqrt(sum);
    }

    private boolean isSame(List<Integer> currentClasses, List<Integer> previousClasses) {
        if (currentClasses.size() != previousClasses.size()) {
            return false;
        }
        int size = currentClasses.size();
        for (int i = 0; i < size; i++) {
            if (!Objects.equals(currentClasses.get(i), previousClasses.get(i))) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> getClasses() {
        return classes;
    }

    public List<List<Float>> getCurrentCentroid() {
        return currentCentroid;
    }

    public List<List<Float>> getMatriceCSV() {
        return matriceCSV;
    }

    public double getWB() {
        System.out.println("getting wb for ca");
        double wb = 0;
        //ssw
        double ssw = 0;
        for (int i = 0; i < classes.size(); i++) {
            List<Float> xi = matriceCSV.get(i);
            List<Float> ci = currentCentroid.get(classes.get(i));
            ssw += minus(xi, ci);
        }
        System.out.println("ssw");
        //x_
        List<Float> x_ = new ArrayList<>();
        //init
        for (int i = 0; i < matriceCSV.get(0).size(); i++) {
            x_.add(Float.valueOf(0));
        }
        for (int i = 0; i < matriceCSV.size(); i++) {
            List<Float> xi = matriceCSV.get(i);
            for (int j = 0; j < xi.size(); j++) {
                x_.set(j, x_.get(j) + xi.get(j));
            }
        }
        for (int i = 0; i < x_.size(); i++) {
            x_.set(i, x_.get(i) / matriceCSV.size());
        }
        //ssb
        System.out.println("ssb");
        double ssb = 0;
        for (int i = 0; i < currentCentroid.size(); i++) {
            //ni de ci
            int ni = 0;
            for (int j = 0; j < classes.size(); j++) {
                if (classes.get(j) == i) {
                    ni++;
                }
            }

            List<Float> ci = currentCentroid.get(i);
            ssb += ni * minus(ci, x_);
        }
        System.out.println("wb");
        wb = currentCentroid.size() * ssw / ssb;
        return wb;
    }
    private Hashtable<Integer, Cluster> clusterMap;

    public double getDaviesBouldin(int k) {
        initClusterMap();
        double DB = 0;
        for (int i = 0; i < clusterMap.size(); i++) {
            DB += calculerD(i);
        }

        return DB / clusterMap.size();
    }

    public double calculerD(int i) {
        double di = Double.MIN_VALUE;

        for (int j = 0; j < clusterMap.size(); j++) {
            if (i != j) {
                Double calculerRij = calculerRij(i, j);
                if (di < calculerRij) {
                    di = calculerRij;
                }
            }
        }

        return di;
    }

    private Double calculerRij(int i, int j) {
        double rij = 0;

        double sj = calculerS(j);
        double si = calculerS(i);
        double mij = calculerM(i, j);

        rij = (si + sj) / mij;
        return rij;
    }

    private double calculerS(int i) {
        double si;
        double somme = 0;

        Cluster clusterI = clusterMap.get(i);
        List<Float> ai = clusterI.getClusterCoordinates();
        final List<Player> membreClusterI = clusterI.getPlayers();

        for (int j = 0; j < membreClusterI.size(); j++) {
            Player xj = membreClusterI.get(j);
            somme += Math.pow(Math.abs(minus(xj.getAttributes(), ai)), 2);
        }
        si = Math.pow(somme / membreClusterI.size(), 1 / 2);
        return si;
    }

    private double calculerM(int i, int j) {
        double mij = minus(clusterMap.get(i).getClusterCoordinates(), clusterMap.get(j).getClusterCoordinates());

        return mij;
    }

    @Override
    public String toString() {
        return "ChaouchAlgorithm{" + "classes=" + classes + "\n, matriceCSV=" + matriceCSV + "\n, currentCentroid=" + currentCentroid + '}';
    }

    private double minus(List<Float> xi, List<Float> ci) {
        double sw = 0;
        for (int i = 0; i < xi.size(); i++) {
            sw += Math.pow(Math.abs(xi.get(i) - ci.get(i)), 2);
        }
        return sw;
    }

    private void initClusterMap() {
        clusterMap = new Hashtable<>();
        // creation des cluster
        for (int i = 0; i < currentCentroid.size(); i++) {
            clusterMap.put(i, new Cluster(currentCentroid.get(i)));
        }

        // affectation des memebres
        for (int i = 0; i < matriceCSV.size(); i++) {
            List<Float> player = matriceCSV.get(i);
            Integer indiceCluster = classes.get(i);
            clusterMap.get(indiceCluster).putPlayer(new Player("" + i, player));
        }

    }

}
