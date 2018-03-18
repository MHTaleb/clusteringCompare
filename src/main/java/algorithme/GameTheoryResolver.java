/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithme;

import entity.ClusterPoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author taleb
 */
public class GameTheoryResolver {

    private List<KmeansResolver> kmeanResults;

    private List<PairResult> pairResults;
    private String i;

    public GameTheoryResolver() {
    }

    public GameTheoryResolver(List<KmeansResolver> kmeanResults) {
        this.kmeanResults = new ArrayList<>(kmeanResults);
        pairResults = new ArrayList<>();

        resolve();
    }

    public void setKmeanResults(List<KmeansResolver> kmeanResults) {
        this.kmeanResults = kmeanResults;
    }

    private void resolve() {
        final RecordsHandeler recordsHandeler = new RecordsHandeler();

        kmeanResults.get(0).getPoints().stream().forEach(recordsHandeler);

        kmeanResults.stream().forEach(clusteringResult -> {
            System.out.println(clusteringResult.getSimulationName());
            final OnePointRecorder onePointRecorder = new OnePointRecorder(clusteringResult);
            clusteringResult.getPoints().forEach(onePointRecorder);
        });

        System.out.println(pairResults);
    }

    public class PairResult {

        private String nodeLabel;
        private final Hashtable<String, Integer> results;

        public PairResult() {
            results = new Hashtable();
        }

        public void setNodeLabel(String nodeLabel) {
            this.nodeLabel = nodeLabel;
        }

        public String getNodeLabel() {
            return nodeLabel;
        }

        public void AddResult(String key, int value) {
            this.results.put(key, value);
        }

        @Override
        public String toString() {
            return "\nlabel = " + nodeLabel + " \n " + results.entrySet();
        }

        public int getDetectedCluster() {
            final Collection<Integer> values = results.values();
            return findFrequentNumber(values.toArray(new Integer[values.size()]));
        }

        private int findFrequentNumber(Integer[] inputArr) {

            //create a hashmap to store the count of each element . key is number and value is count for the number
            HashMap<Integer, Integer> numberMap = new HashMap<>();

            int result = -1; //result will hold the most frequent element for the given array
            int frequency = -1; //frequency is the count for the most frequent element

            int value;

            for (int i = 0; i < inputArr.length; i++) { //scan all elements of the array one by one 
                value = -1; //set value as -1 
                if (numberMap.containsKey(inputArr[i])) {
                    value = numberMap.get(inputArr[i]);
// if the element is in the map, get the count 
                }
                if (value != -1) { // value is not -1 , that means the element is in the map. Increment the value and check if it is // greater than the maximum 
                    value += 1;
                    if (value > frequency) { //if the value is greater than frequency, it means it is the maximum value
                        // till now. store it
                        frequency = value;
                        result = inputArr[i];
                    }

                    numberMap.put(inputArr[i], value); // put the updated value in the map
                } else {
                    //element is not in the map. put it with value or count as 1
                    numberMap.put(inputArr[i], 1);
                }

            }

            if (frequency == 1) {
                return -1;
            }

            return result;
        }

    }

    private class RecordsHandeler implements Consumer<ClusterPoint> {

        public RecordsHandeler() {
        }
        int i = 0;

        @Override
        public void accept(ClusterPoint indv) {
            final PairResult pairResult = new PairResult();
            pairResult.setNodeLabel("node" + i);
            i++;
            pairResults.add(pairResult);

        }
    }

    private class OnePointRecorder implements Consumer<ClusterPoint> {

        private final KmeansResolver clusteringResult;

        public OnePointRecorder(KmeansResolver clusteringResult) {
            this.clusteringResult = clusteringResult;
        }
        int i = 0;

        @Override
        public void accept(ClusterPoint point) {
            pairResults.get(i).AddResult(clusteringResult.getSimulationName(), point.getCurrentCluster());
            i++;
        }
    }

    public List<PairResult> getPairResults() {
        return pairResults;
    }

}
