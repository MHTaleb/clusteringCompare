/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithme;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author taleb
 */
public class THJAlgorithm {
    
    int seuil;
    // private final List<List<Float>> currentCentroid;

    private boolean wbChoice = true;    
    
    private final List<List<Float>> matriceCSV;
    
    private double bestWB;
    
    private List<Player> corbeil;
    
    private final List<Float> lambda;
    
    private Hashtable<Integer, Cluster> bestMap;
    
    private final List<Integer> classClustering;
    
    private final Hashtable<Integer, Cluster> clusterMap;
    private final double best_kCA;
    private int nombreMaxItere = 500;
    
    public THJAlgorithm(List<List<Float>> matriceCSV, int seuil, double best_kCA) {
        //this.currentCentroid = new ArrayList();
        this.matriceCSV = matriceCSV;
        this.classClustering = new ArrayList();
        lambda = new ArrayList();
        clusterMap = new Hashtable<>();
        bestMap = new Hashtable<>();
        corbeil = new ArrayList<>();
        this.seuil = seuil;
        this.best_kCA = best_kCA;
        
    }
    private int nombre_iteration;
    
    public void resolve(int k) {
        int moins = seuil / nombreMaxItere;
        System.out.println("start");
        // creation de la population
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < matriceCSV.size(); i++) {
            
            players.add(new Player("" + i, matriceCSV.get(i)));
        }
        System.out.println("players added");

        // choix k centre aleatoir
        for (int i = 0; i < k; i++) {
            final List<Float> centroid = players.get(i).getAttributes();
            //currentCentroid.add(centroid);
            clusterMap.put(i, new Cluster(centroid));
        }
        System.out.println("selected centers");

        // pour chaque joueur
        for (int j = 0; j < players.size(); j++) {
            Player player = players.get(j);
            
            int bestClusterIndex = 0;
            double minDistance = Double.MAX_VALUE;
            int index = 0;
            
            for (int l = 0; l < clusterMap.size(); l++) {
                Cluster cluster = clusterMap.get(l);
                final List<Float> clusterCoordinates = cluster.getClusterCoordinates();
                // System.out.println("clusterCoordinates " + clusterCoordinates);
                // System.out.println("player " + player);
                double currentDistance = player.calculerDistancePoint(clusterCoordinates);
                if (currentDistance < minDistance) {
                    minDistance = currentDistance;
                    
                    bestClusterIndex = index;
                }
                index++;
            }
            clusterMap.get(bestClusterIndex).putPlayer(player);
            
        }
        //-------------

        int limitCorbeil = 50;
        bestWB = Double.MAX_VALUE;
        bestMap = new Hashtable<>();
        boolean state = true;
        for (int iteration = 0; iteration < nombreMaxItere && state; iteration++) {

            //vider la corbeil
            for (int i = 0; i < corbeil.size(); i++) {
                Player candidat_retour = corbeil.get(i);
                candidat_retour.calculerDistanceIntraCluster(new ArrayList<>(clusterMap.values()));
                int calculerIndexMeilleurCluster = candidat_retour.calculerIndexMeilleurCluster();
                Cluster selectedCluster = clusterMap.get(calculerIndexMeilleurCluster);
                if (candidat_retour.calculerDistancePoint(selectedCluster.getClusterCoordinates()) < seuil * 2.3) {
                    selectedCluster.getPlayers().add(candidat_retour);
                    corbeil.remove(candidat_retour);
                }
            }
            
            System.out.println(" clusterMap.size() -->" + clusterMap.size());
            // calculer lambda pour tout les clusters par joueur
            for (int l = 0; l < clusterMap.size(); l++) {
                Cluster cluster = clusterMap.get(l);
                
                for (int m = 0; m < cluster.getPlayers().size(); m++) {
                    Player player = cluster.getPlayers().get(m);
                    final List<Cluster> clustersCollection = clusterMap.values().stream().collect(Collectors.toList());
                    //System.out.println("clustersCollection.size()  : " + clustersCollection.size());
                    player.calculerDistanceIntraCluster(clustersCollection);
                    
                };
            }

            // chaque joueur decide de rester ou d aller a un autre cluster celon la distance moyenne intra lui et les membres de chaque cluster ( virer ou inscrire )
            for (int i = 0; i < clusterMap.size(); i++) {
                Cluster cluster = clusterMap.get(i);
                System.out.println("calcul de meilleur indice");
                for (int j = 0; j < cluster.getPlayers().size(); j++) {
                    Player player = cluster.getPlayers().get(j);
                    
                    final int calculerIndexMeilleurCluster = player.calculerIndexMeilleurCluster();
                    //System.out.println("calculerIndexMeilleurCluster    " + calculerIndexMeilleurCluster);
                    final List<Player> clusterMembers = clusterMap.get(calculerIndexMeilleurCluster).getPlayers();
                    //System.out.println(player + " ----- " + clusterMembers.size());
                    cluster.getPlayers().remove(player);
                    clusterMembers.add(player);
                    clusterMap.get(calculerIndexMeilleurCluster).setPlayers(clusterMembers);
                    
                }
            }

            // preparer les centre pourl e calcul de wb
            for (int i = 0; i < clusterMap.size(); i++) {
                Cluster cluster = clusterMap.get(i);
                try {
                    cluster.updateCentroid();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //Corbeil
            // remplir corbeil
            for (int i = 0; i < clusterMap.size(); i++) {
                if(corbeil.size()<limitCorbeil){
                    
                Cluster cluster_courant = clusterMap.get(i);
                List<Player> playerBeyondLimit = cluster_courant.getPlayerBeyondLimit(seuil);
                corbeil.addAll(playerBeyondLimit);
                cluster_courant.updateCentroid();
                }
            }
            
            for (int i = 0; i < clusterMap.size(); i++) {
                Cluster cluster = clusterMap.get(i);
                try {
                    cluster.updateCentroid();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            double wb = 0;
            if (wbChoice) {
                
                wb = wbCalculation(k);
            } else {
                wb = daviesBouldin(k);
            }
            
            if (wb < bestWB) {
                
                bestWB = wb;
                bestMap = new Hashtable<>(clusterMap);
                
            } else {
                if (iteration == 2) {
                    bestMap.entrySet().stream().forEach((set) -> {
                        //  System.out.println("cluster iteration 2 = " + set.getKey());
                        //  System.out.println("\n members iteration 2   " + set.getValue());
                    });
                }
                if ((int) (wb * 10) == (int) (bestWB * 10) && (bestWB < best_kCA) && (iteration > 20)) {
                    state = false;
                }
                
            }
            nombre_iteration = iteration;
            if (wb > best_kCA) {
                seuil -= moins;
                limitCorbeil++;
                System.out.println("nouveau seuil " + seuil);
            }
        }
        
        System.out.println("Best is " + bestWB);
        System.out.println("BEST MAP ***************************");
        bestMap.entrySet().stream().forEach((set) -> {
            System.out.println("cluster = " + set.getKey());
            //   System.out.println(" members  " + set.getValue());
        });
        System.out.println("BEST MAP ***************************");
        
    }
    
    public double wbCalculation(int k) {
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
    
    public int getIteration() {
        return nombre_iteration;
    }
    
    public void setWbCalculator() {
        wbChoice = true;
    }
    
    public void setDaviesBouldinCalculator() {
        wbChoice = false;
    }
    
    public double getBestWB() {
        return bestWB;
    }
    
    public Hashtable<Integer, Cluster> getBestMap() {
        return bestMap;
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
    
    private double daviesBouldin(int k) {
        
        double DB = 0;
        for (int i = 0; i < clusterMap.size(); i++) {
            DB += calculerD(i);
        }
        
        return DB / clusterMap.size();
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

    public void setNombreiterationMax(int nombreIteration) {
        this.nombreMaxItere = nombreIteration;
    }
    
}
