/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.talcorp.clustering;

import algorithme.GameTheoryResolver;
import builder.ClusteringDataPair;
import builder.Line;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 *
 * @author taleb
 */
public class PFEDataFormator {

    private HashMap<Integer, List> classifications;
    private final HashMap<Integer, List<String>> classificationBenchMark;
    private final HashMap<Integer, List> biensClassifiee;
    

    public HashMap<Integer, List<String>> getClassificationBenchMark() {
        return classificationBenchMark;
    }

    public HashMap<Integer, List> getBiensClassifiee() {
        return biensClassifiee;
    }
    /**
     * permet de presenter les donner d une facon bcp plus simple est etulisable dans l interface graphique
     * @param pairResults elle exige le resultat du lancement de notre algorithme 
     */ 
    public PFEDataFormator(List<GameTheoryResolver.PairResult> pairResults) {
        classifications = new HashMap<>();
        classificationBenchMark = new HashMap<>();
        biensClassifiee = new HashMap<>();
        pairResults.stream().forEach(pair->{
            
            int detectedCluster = pair.getDetectedCluster();
            boolean containsKey = classifications.containsKey(detectedCluster);
            if(!containsKey){
                classifications.put(detectedCluster, new ArrayList());
            }
            classifications.get(detectedCluster).add(pair.getNodeLabel());
                });
        System.out.println("membres"+classifications);
        response = new StringBuilder();
    }

    private final StringBuilder response ;

    public HashMap<Integer, List> getClassifications() {
        return classifications;
    }
    
    @Override
    public String toString() {
        final Set<Map.Entry<Integer, List>> entrySet = classifications.entrySet();
        entrySet.stream().forEach(cnsmr->{
        response.append("cluster ").append(cnsmr.getKey()).append("members size = ").append(cnsmr.getValue().size()).append("\n").append(cnsmr.getValue()).append(" \n");
                });
        return response.toString();//To change body of generated methods, choose Tools | Templates.
    }

    void setClassData(List<ClusteringDataPair> clusteringDataPairs) {
        Predicate<? super ClusteringDataPair> selectClass = cdp -> {
          return cdp.getColumnName().trim().toLowerCase().equals("class");
        };
        clusteringDataPairs.stream().filter(selectClass).forEach(dataList->{
            final List<Line> points = dataList.getColumnPoints();
            points.stream().forEach(point->{
                Float clusterIndex = point.getValue();
                if(!classificationBenchMark.containsKey(clusterIndex.intValue())){
                    classificationBenchMark.put(clusterIndex.intValue(), new ArrayList());
                    biensClassifiee.put(clusterIndex.intValue(), new ArrayList());
                }
                classificationBenchMark.get(clusterIndex.intValue()).add("node"+(point.getIndex()));
            });
            System.out.println("previous \n"+classificationBenchMark);
            
            classificationBenchMark.forEach((index,mmbrs)->{
                mmbrs.forEach(member->{
                    try {
                        
                        if(classifications.get(index).contains(member))biensClassifiee.get(index).add(member);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
            System.out.println("wellclassed \n"+biensClassifiee);
        });
    }

    public List<Integer> getSimpleClasses() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
