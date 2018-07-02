/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithme;

import entity.ClusterPoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;

/**
 * une classe qui va resoudre le probleme de comparaison aveugle et kmeans paire 
 * a paire et implemente la theorie des jeux en moderation des resultat de tout les simulation
 * @author taleb
 */
public class GameTheoryResolver {

    private List<List<Float>> matriceCSV;
    
    private List<KmeansResolver> kmeanResults;

    private List<PairResult> pairResults;

    public Integer getIterations() {
        return iterations;
    }
   

    
    
    public GameTheoryResolver() {
    }
    
    /**
     * instancier un resoneur qui va decider de la classification dans tt les cas possible est choisir le cas optimal
     * 
     * @param kmeanResults liste de simulations
     */
    public GameTheoryResolver(List<KmeansResolver> kmeanResults,List<List<Float>> matriceCSV) {
        this.kmeanResults = new ArrayList<>(kmeanResults);
        pairResults = new ArrayList<>();
        this.matriceCSV = matriceCSV;
        resolve();
    }

    public void setKmeanResults(List<KmeansResolver> kmeanResults) {
        this.kmeanResults = kmeanResults;
    }

    /**
     * L algoritme de resolution une methode interne caché a tt etulisation externe
     * elle represente la methode decrite dans le document de l iris sepia et les forme 
     * rectangle cercle ect ...
     * 
     */
    Integer iterations =0;
    private void resolve() {
        final RecordsHandeler recordsHandeler = new RecordsHandeler();

        kmeanResults.get(0).getPoints().stream().forEach(recordsHandeler);

        kmeanResults.stream().forEach(clusteringResult -> {
            System.out.println(clusteringResult.getSimulationName());
            final OnePointRecorder onePointRecorder = new OnePointRecorder(clusteringResult);
            clusteringResult.getPoints().forEach(onePointRecorder);
            iterations++;
        });

        System.out.println("********************/*/*/*/***********************");
        System.out.println(pairResults);
        System.out.println("********************/*/*/*/***********************");
        
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
        /**
         * permet de detecter le cluster qui ete le plus apparant pour un individu dans tout les simulations effectué
         */
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
            iterations++;

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

    /**
     * @return retourne le resultat du clustering de tout les paire combiner entre les atribut de la population
     */
    public List<PairResult> getPairResults() {
        return pairResults;
    }

    public List<Integer> getClasses(){
        List<Integer> classes  = new ArrayList();
        
        
        for (int i = 0; i < pairResults.size(); i++) {
            classes.add(pairResults.get(i).getDetectedCluster());
        }
        
        return classes;
    }
    
    private double minus(List<Float> attributes, List<Float> cpi) {
        double result = 0;
        try {
        for (int i = 0; i < attributes.size(); i++) {
            result += Math.pow(attributes.get(i) - cpi.get(i), 2);
        }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private  Hashtable<Integer, Cluster> clusterMap;
    
    private void init(){
        clusterMap = new Hashtable<>();
        // remplir tout les element dans leur class respective
        // si la class n existe pas creer class sinon remplir
        List<Integer> listeDesClasses = getClasses();
        for (int i = 0; i < matriceCSV.size(); i++) {
            List<Float> playerI = matriceCSV.get(i);
            Integer indicePlayerI = listeDesClasses.get(i);
            if(!clusterMap.containsKey(indicePlayerI) && indicePlayerI >=0){
                System.out.println("indicePlayerI "+indicePlayerI);
                clusterMap.put(indicePlayerI, new Cluster());
            }
            if(indicePlayerI >=0)
            clusterMap.get(indicePlayerI).getPlayers().add(new Player(""+i, playerI));
        }
        
        for (int i = 0; i < clusterMap.size(); i++) {
            clusterMap.get(i).updateCentroid();
            
        }
        
    }
    
    public double daviesBouldin(int k) {
    
        init();
        
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
            somme+=Math.pow(Math.abs(minus(xj.getAttributes(), ai)),2);
        }
        si = Math.pow(somme/membreClusterI.size(), 1/2);
        return si;
    }
    
    private double calculerM(int i, int j) {
        double mij = minus(clusterMap.get(i).getClusterCoordinates(), clusterMap.get(j).getClusterCoordinates()) ;
        
        
        return mij;
    }

    
    public double wbCalculation(int k) {
    
        init();
        
        //calculer WB
        //  calculer ssw
        double ssw = 0;
        for (int i = 0; i < clusterMap.size(); i++) {
            double sw = 0;
            Cluster ci = clusterMap.get(i);
            List<Float> cpi = ci.getClusterCoordinates();
            final List<Player> joueursCI = ci.getPlayers();
            for (int j = 0; j < joueursCI.size(); j++) {
                Player xi = joueursCI.get(j);
                sw += minus(xi.getAttributes(), cpi);
            }
            ssw += sw;
        }
        //calculer ssb
        Player x_ = new Player(matriceCSV.get(0).size());
        for (int i = 0; i < matriceCSV.size(); i++) {
            List<Float> joueur_i_attribut = matriceCSV.get(i);
            List<Float> x_attribut = x_.getAttributes();
            List<Float> x_temp = new ArrayList();
            
            for (int j = 0; j < x_attribut.size(); j++) {
                x_temp.add(joueur_i_attribut.get(j) + x_attribut.get(j));
            }
            x_.setAttributes(x_temp);
        }
        x_.divideBy(matriceCSV.size());
        double ssb = 0;
        for (int i = 0; i < clusterMap.size(); i++) {
            Cluster ci = clusterMap.get(i);
            int ni = ci.getPlayers().size();
            ssb += ni * minus(ci.getClusterCoordinates(), x_.getAttributes());
        }
        double wb = k * ssw / ssb;
        //   System.out.println("wb(" + iteration + ") = " + wb);
        return wb;
    }
    
    
    
}
