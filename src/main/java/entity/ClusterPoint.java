/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.List;

/**
 *
 * @author taleb
 */
public class ClusterPoint {
    private String playerIndex;
    private double x, y; // coordonée
    private int currentCluster;
    private int nearestIndex;

    public void setPlayerIndex(String playerIndex) {
        this.playerIndex = playerIndex;
    }

    public String getPlayerIndex() {
        return playerIndex;
    }

    
    
    public ClusterPoint() {
        this.x = Math.random() * 150;
        this.y = Math.random() * 150;
    }

    public int getCurrentCluster() {
        return currentCluster;
    }

    public void setCurrentCluster(int currentCluster) {
        this.currentCluster = currentCluster;
    }

    public ClusterPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "ClusterPoint{" + "x=" + x + ", y=" + y + ", currentCluster=" + currentCluster + ", nearestIndex=" + nearestIndex + '}';
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getNearestIndex() {
        return nearestIndex;
    }

    public void setNearestIndex(int nearestIndex) {
        this.nearestIndex = nearestIndex;
    }

    /**
     *
     * Cette methode permet de rattacher ce point a son plus proche centroid
     *
     * @param centroids la liste des centres
     *
     */
    public void attachToNearestIndex(List<ClusterPoint> centroids) {

        //initialiser la distance la plus courte
        double nearestDistance = Double.MAX_VALUE;
        // initialiser les indice
        int nearestLocalIndex = 0;
        int currentIndex = 0;
        //algorithme de recherche du plus proche centre
        for (ClusterPoint centroid : centroids) {
            // calcul de distance avec le centroid en cours
            double distance = Math.sqrt(Math.pow(centroid.getX() - this.getX(), 2) + Math.pow(centroid.y - this.getY(), 2));
            //algorithme d'election
//            System.out.println("----------");
//            System.out.println("distance = "+distance );
//            System.out.println("nearest = "+nearestDistance );
//            System.out.println("index = "+currentIndex );
//            System.out.println("----------");
            if (distance < nearestDistance) {
                //mettre a jour la distance elu ( la plus courte )
                nearestDistance = distance;
                //mettre a jour l indice du cluster elu
                nearestLocalIndex = currentIndex;
            }
            // incrementation de l indice de l iteration
            currentIndex++;
        }
        // ratacher ce noeud au cluster le plus proche en specifiant son indice
        this.nearestIndex = nearestLocalIndex;
        this.currentCluster = nearestLocalIndex;
//        System.out.println("nearest index "+nearestLocalIndex + this);
    }

    /**
     *
     * cette methode permet de calculer le nouveau centre d un cluster
     * 
     * @param members les membre de ce cluster en cours
     */
    public void updateCenter(List<ClusterPoint> members) {
        
        double sommeX = 0,sommeY =0;
        for (ClusterPoint member : members) {
            sommeX+=member.getX();
            sommeY+=member.getY();
        }
        
        sommeX/=members.size();
        sommeY/=members.size();
        
        this.setX(sommeX);
        this.setY(sommeY);
        
    }

    /**
     *
     * cette methode permet de comparer si deux point son les meme ( superposé )
     * 
     * @param comparasonPoint le point de comparaison
     * @return
     */
    public boolean isTheSameAs(ClusterPoint comparasonPoint) {
        
        final boolean state = (int)this.getX()*10000 == (int)comparasonPoint.getX()*10000 && (int)this.getY()*10000 == (int)comparasonPoint.getY() *10000;
//        System.out.println("X "+(int)this.getX()*10000 );
//        System.out.println("X1 "+(int)comparasonPoint.getX()*10000 );
//        System.out.println("Y "+(int)this.getY()*10000  );
//        System.out.println("Y1 "+(int)comparasonPoint.getY()*10000 );
        return state ;
        
    }

}
