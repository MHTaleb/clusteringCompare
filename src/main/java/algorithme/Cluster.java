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
  

    public Cluster(float factor) {
        players = new ArrayList();
 
        this.vitrine = new ArrayList();
    }
    public Cluster(){
        this.clusterCoordinates = new ArrayList<>();
        this.players = new ArrayList();
        this.vitrine = new ArrayList();
    }

    public Cluster(List<Float> clusterCoordinate) {
       
        this.clusterCoordinates = clusterCoordinate;
        this.vitrine = new ArrayList();
        players = new ArrayList<>();
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
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
 
        if( players.size()>0){
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

    public Cluster(List<Player> vitrine, List<Player> players, List<Float> clusterCoordinates) {
        this.vitrine = vitrine;
        this.players = players;
        this.clusterCoordinates = clusterCoordinates;

    }

    @Override
    public String toString() {
        return "\n"+this.players;
    }

    public List<Player> getPlayerBeyondLimit(int seuil) {
        List<Player> corbeil_players = new ArrayList();
        for (int i = 0; i < players.size(); i++) {
            Player curent_player = players.get(i);
            double calculerDistancePoint = curent_player.calculerDistancePoint(clusterCoordinates);
           // System.out.println("calculerDistancePoint :::::::"+calculerDistancePoint);
            if(calculerDistancePoint > seuil){
                corbeil_players.add(curent_player);
                players.remove(curent_player);
            }
        }
        return corbeil_players;
    }

  
    
}
