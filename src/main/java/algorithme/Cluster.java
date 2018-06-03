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
public class Cluster {

    final private List<Player> vitrine;
    private List<Player> players;
    private List<Float> clusterCoordinates;
    private final double FACTOR;

    public Cluster(float factor) {
        players = new ArrayList();
        this.FACTOR = factor;
        this.vitrine = new ArrayList();
    }

    public Cluster(List<Float> clusterCoordinate, double factor) {
        this.FACTOR = factor;
        this.clusterCoordinates = clusterCoordinate;
        this.vitrine = new ArrayList();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> getVitrine() {
        return vitrine;
    }

    public void virer() {
       
    }

    public List<Float> getClusterCoordinates() {
        return clusterCoordinates;
    }

   

    void vendre() {
      
    }

    //error here
    public void updateCentroid() {
        this.clusterCoordinates = new ArrayList();
 
        for(int i = 0 ; i< players.get(0).getAttributes().size();i++){
            float att = 0;
            for(int j=0 ; j<players.size();j++){
                att+=players.get(j).getAttributes().get(i);
            }
            att/=players.size();
            clusterCoordinates.add(att);
        }
       

    }

    void achetter(Collection<Cluster> clusters) {
     
    }

    void putPlayer(Player player) {
        while (vitrine.contains(player)) {            
            vitrine.remove(player);
        }
        if(players == null)players = new ArrayList();
        while (players.contains(player)) {            
            players.remove(player);
        }
        players.add(player);
        
    }

    public Cluster(List<Player> vitrine, List<Player> players, List<Float> clusterCoordinates, double FACTOR) {
        this.vitrine = vitrine;
        this.players = players;
        this.clusterCoordinates = clusterCoordinates;
        this.FACTOR = FACTOR;
    }

    @Override
    public String toString() {
        return "\n"+this.players;
    }

  
    
}
