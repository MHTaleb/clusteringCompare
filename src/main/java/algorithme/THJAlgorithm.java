/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithme;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author taleb
 */
public class THJAlgorithm {

    private final List<List<Float>> currentCentroid;

    private final List<List<Float>> matriceCSV;

    private double bestWB;

    private final List<Float> lambda;

    private Hashtable<Integer, Cluster> bestMap;

    private final List<Integer> classClustering;

    private final Hashtable<Integer, Cluster> clusterMap;

    public THJAlgorithm(List<List<Float>> matriceCSV) {
        this.currentCentroid = new ArrayList();
        this.matriceCSV = matriceCSV;
        this.classClustering = new ArrayList();
        lambda = new ArrayList();
        clusterMap = new Hashtable<>();
        bestMap = new Hashtable<>();

    }
    private int nombre_iteration;

    public void resolve(int k) {

        // creation de la population
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < matriceCSV.size(); i++) {

            players.add(new Player(matriceCSV.get(i)));
        }

        // choix k centre aleatoir
        for (int i = 0; i < k; i++) {
            final List<Float> centroid = players.get(i).getAttributes();
            currentCentroid.add(centroid);
            clusterMap.put(i, new Cluster(centroid, 0.8));
        }

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
                long currentDistance = player.calculerDistancePoint(clusterCoordinates);
                if (currentDistance < minDistance) {
                    minDistance = currentDistance;
                    bestClusterIndex = index;
                }
                index++;
            }
            clusterMap.get(bestClusterIndex).putPlayer(player);

        }
        //-------------

        bestWB = Double.MAX_VALUE;
        bestMap = new Hashtable<>();
        boolean state = true;
        for (int iteration = 0; iteration < 20 && state; iteration++) {
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
                for (int j = 0; j < cluster.getPlayers().size(); j++) {
                    Player player = cluster.getPlayers().get(j);

                    final int calculerIndexMeilleurCluster = player.calculerIndexMeilleurCluster() % k;
                    //System.out.println("calculerIndexMeilleurCluster    " + calculerIndexMeilleurCluster);
                    final List<Player> clusterMembers = clusterMap.get(calculerIndexMeilleurCluster).getPlayers();
                    //System.out.println(player + " ----- " + clusterMembers.size());
                    cluster.getPlayers().remove(player);
                    clusterMembers.add(player);

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
            };
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
                if (wb == bestWB) {
                    state = false;
                }
                
            }
            nombre_iteration = iteration;

        }

        System.out.println("Best is " + bestWB);
        System.out.println("BEST MAP ***************************");
        bestMap.entrySet().stream().forEach((set) -> {
            System.out.println("cluster = " + set.getKey());
         //   System.out.println(" members  " + set.getValue());
        });
        System.out.println("BEST MAP ***************************");

    }

    private double minus(List<Float> attributes, List<Float> cpi) {
        double result = 0;
        for (int i = 0; i < attributes.size(); i++) {
            result += Math.pow(attributes.get(i) - cpi.get(i), 2);
        }
        return result;
    }

    public int getIteration() {
       return nombre_iteration;
    }

    private static class SWCalculator implements Consumer<Player> {

        private double sw;
        private final Cluster cluster;

        public SWCalculator(Cluster cluster) {
            this.sw = 0;
            this.cluster = cluster;
        }

        @Override
        public void accept(Player player) {
            sw += player.calculerSW(cluster);
        }

        public double getSw() {
            return sw;
        }

    }

    private static class SSWCalculator implements Consumer<Cluster> {

        private double ssw;

        public SSWCalculator() {
            this.ssw = 0;
        }

        @Override
        public void accept(Cluster cluster) {
            final SWCalculator swCalculator = new SWCalculator(cluster);
            cluster.getPlayers().forEach(swCalculator);
            ssw += swCalculator.getSw();
        }

        public double getSsw() {
            return ssw;
        }

    }

    public double getBestWB() {
        return bestWB;
    }

    public Hashtable<Integer, Cluster> getBestMap() {
        return bestMap;
    }

}
