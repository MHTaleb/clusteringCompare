/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithme;

import builder.ClusteringDataPair;
import java.util.ArrayList;
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

        int nombreDeLigne = clusteringDataPairs.get(0).getPoints().size();
        int nombreDeCollone = clusteringDataPairs.size();

        for (int i = 0; i < nombreDeLigne; i++) {
            List<Float> line = new ArrayList<>();
            for (int j = 0; j < nombreDeCollone; j++) {
                final ClusteringDataPair column = clusteringDataPairs.get(j);
                if (!column.getColumn().trim().toLowerCase().equals("class")) {
                    Float value = column.getPoints().get(i).getValue();
                    line.add(value);
                }
            }
            if (!line.isEmpty()) {
                matriceCSV.add(i, line);
            }
        }

        classes = new ArrayList<>(clusteringDataPairs.get(0).getPoints().size());
    }
    List<List<Float>> currentCentroid;

    public void resolve(int numberOfClasses) {

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
                    float currentDistance = getDistance(element, center);
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
        long end = System.currentTimeMillis() - start;
        System.out.println("end of clustering " + end + " seconds and " + loop + " repeats");
    }

    private float getDistance(List<Float> element, List<Float> center) {
        float sum = 0;
        for (int i = 0; i < element.size(); i++) {
            sum += Math.pow(element.get(i) - center.get(i), 2);
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
    
    

    @Override
    public String toString() {
        return "ChaouchAlgorithm{" + "classes=" + classes + "\n, matriceCSV=" + matriceCSV + "\n, currentCentroid=" + currentCentroid + '}';
    }


}
