/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author taleb
 */
public class Player {

    private List<Float> attributes;

    private List<Float> distancesIntraCluster;

    public Player(int size) {
        attributes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            attributes.add(Float.valueOf(0));
        }
    }

    public Player(List<Float> attributes) {
        this.attributes = attributes;
        distancesIntraCluster = new ArrayList();
    }

    public double calculerDistancePoint(List<Float> coordinates) {
       
        long distance = 0;
        int diviseur = 0;
        for (int i = 0; i < coordinates.size(); i++) {
            distance += Math.pow(Math.abs(coordinates.get(i) - attributes.get(i)), 2);
            diviseur++;
        }
        if(diviseur == 0)diviseur =1;
        return distance / diviseur;
    }

    /**
     * verifié et validé
     * @param clusters list des clusters
     */
    public void calculerDistanceIntraCluster(List<Cluster> clusters) {
        distancesIntraCluster= new ArrayList<>();
        //System.out.println("calculerDistanceIntraCluster.clusters.size = "+clusters.size());
        for (int i = 0; i < clusters.size(); i++) {
            float distanceMoyenne = 0;
            Cluster currentCluster = clusters.get(i);
            int currentClusterSize = currentCluster.getPlayers().size() ;
            for (Player player : clusters.get(i).getPlayers()) {
                float distance = 0;
                for (int j = 0; j < player.attributes.size(); j++) {
                    distance += Math.abs(player.attributes.get(j) - this.attributes.get(j));
                }
                distanceMoyenne += distance;
            }
            distancesIntraCluster.add(distanceMoyenne / currentClusterSize);
        }
    }

    public int calculerIndexMeilleurCluster() {
        int winnerIndex = 0;
        float winnerValue = Float.MAX_VALUE;

        for (int i = 0; i < distancesIntraCluster.size(); i++) {
            final Float curentDistance = distancesIntraCluster.get(i);
            if (winnerValue > curentDistance) {
                winnerIndex = i;
                winnerValue = curentDistance;
            }
        }
        return winnerIndex;
    }

  

    public void setAttributes(List<Float> attributes) {
        this.attributes = attributes;
    }

    
    
    public List<Float> getAttributes() {
        return attributes;
    }

    public double calculerSW(Cluster cluster) {
        return 0;
    }

    void divideBy(int size) {
        List<Float> new_attributes = new ArrayList();
        for(int i = 0 ; i<attributes.size();i++){
            new_attributes.add(attributes.get(i)/size);
        }
        this.attributes.clear();
        this.attributes.addAll(new_attributes);
    }

    @Override
    public String toString() {
        return "\n "+this.attributes;
    }
    
    

   

}
