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
class PFEDataFormator {

    private HashMap<Integer, List> members;
    private final HashMap<Integer, List> previousMembers;
    private final HashMap<Integer, List> wellClassedMembers;
    

    public HashMap<Integer, List> getPreviousMembers() {
        return previousMembers;
    }

    public HashMap<Integer, List> getWellClassedMembers() {
        return wellClassedMembers;
    }
     
    PFEDataFormator(List<GameTheoryResolver.PairResult> pairResults) {
        members = new HashMap<>();
        previousMembers = new HashMap<>();
        wellClassedMembers = new HashMap<>();
        pairResults.stream().forEach(pair->{
            
            int detectedCluster = pair.getDetectedCluster();
            boolean containsKey = members.containsKey(detectedCluster);
            if(!containsKey){
                members.put(detectedCluster, new ArrayList());
            }
            members.get(detectedCluster).add(pair.getNodeLabel());
                });
        System.out.println("membres"+members);
        response = new StringBuilder();
    }

    private final StringBuilder response ;

    public HashMap<Integer, List> getMembers() {
        return members;
    }
    
    @Override
    public String toString() {
        final Set<Map.Entry<Integer, List>> entrySet = members.entrySet();
        entrySet.stream().forEach(cnsmr->{
        response.append("cluster ").append(cnsmr.getKey()).append("members size = ").append(cnsmr.getValue().size()).append("\n").append(cnsmr.getValue()).append(" \n");
                });
        return response.toString();//To change body of generated methods, choose Tools | Templates.
    }

    void setClassData(List<ClusteringDataPair> clusteringDataPairs) {
        Predicate<? super ClusteringDataPair> selectClass = cdp -> {
          return cdp.getColumn().trim().toLowerCase().equals("class");
        };
        clusteringDataPairs.stream().filter(selectClass).forEach(dataList->{
            final List<Line> points = dataList.getPoints();
            points.stream().forEach(point->{
                Float clusterIndex = point.getValue();
                if(!previousMembers.containsKey(clusterIndex.intValue())){
                    previousMembers.put(clusterIndex.intValue(), new ArrayList());
                    wellClassedMembers.put(clusterIndex.intValue(), new ArrayList());
                }
                previousMembers.get(clusterIndex.intValue()).add("node"+(point.getIndex()));
            });
            System.out.println("previous \n"+previousMembers);
            
            previousMembers.forEach((index,mmbrs)->{
                mmbrs.forEach(member->{
                    if(members.get(index).contains(member))wellClassedMembers.get(index).add(member);
                });
            });
            System.out.println("wellclassed \n"+wellClassedMembers);
        });
    }
    
    
    
}
