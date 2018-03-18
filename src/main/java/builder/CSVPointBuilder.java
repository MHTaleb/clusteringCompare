/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

import entity.ClusterPoint;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author taleb
 */
public class CSVPointBuilder {

    private final String FILE_PATH;
    private String[] HEADERS;
    List<ClusteringDataPair> clusteringDataPairs;
    
    public CSVPointBuilder(String chemin_fichier) throws IOException {

        this.FILE_PATH = chemin_fichier;

        this.clusteringDataPairs = new ArrayList<>();

        Reader in = null;

        //preparation de la bibliotheque apache pour lecture du csv file
        //lecture du fichier csv
        in = new FileReader(FILE_PATH);
        //lecture des entetes
        Iterable<CSVRecord> records;
        
        try (
            Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withDelimiter(';')
                    .withTrim());
        ) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            //initialiser les map de clusters
            csvRecords.iterator().next().toMap().keySet().forEach(head->{
                clusteringDataPairs.add(new ClusteringDataPair(head, new ArrayList<>()));
            });
            
            for (CSVRecord csvRecord : csvRecords) {
                final long index = csvRecord.getRecordNumber();
                // Accessing values by Header names
//                System.out.println("csvRecord.getRecordNumber()   "+index);
                final Map<String, String> line = csvRecord.toMap();
//                System.out.println("csvRecord.toMap()     "+line);
                Set<Map.Entry<String, String>> entrySet = line.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
//                    System.out.println(entry.getKey());
//                    System.out.println(entry.getValue());
                    Predicate<? super ClusteringDataPair> prdct = clusterData ->{
                        return clusterData.getColumn().equals(entry.getKey());
                    };
                    clusteringDataPairs.stream().filter(prdct).forEach(clusterData ->{
                        clusterData.getPoints().add((int)index-1, new Line(Float.parseFloat(entry.getValue()), (int)index));
                    });
                    
                }
                
            }
//            System.out.println("");
//            System.out.println("/////////////////////////////////////////////////////////");
//            System.out.println("/////////////////////////////////////////////////////////");
//            System.out.println("/////////////////////////////////////////////////////////");
//            System.out.println("");
//            System.out.println(clusteringDataPairs);
            
            Hashtable<String, List<ClusterPoint>> clusteringDimensions = getClusteringDimensions();
            clusteringDimensions.entrySet().forEach(set->{
//                System.out.println("");
//                System.out.println("/////////////////////////////////////////////////////////");
//                System.out.println("/////////////////////////////////////////////////////////");
//                System.out.println("/////////////////////////////////////////////////////////");
//                System.out.println("");
//                System.out.println("set = "+set.getKey());
//                System.out.println("values = "+set.getValue());
            });
    }

}
    
    public Hashtable<String,List<ClusterPoint>> getClusteringDimensions(){
        final Hashtable<String,List<ClusterPoint>> clusterSuperpositions = new Hashtable();
        
        int size = clusteringDataPairs.size();
        for (int i = 0; i < size-1; i++) {
            for (int j = i; j < size; j++) {
                final ArrayList<ClusterPoint> clusterPoints = new ArrayList<>();
                final ClusteringDataPair firstPair = clusteringDataPairs.get(i);
                final ClusteringDataPair secondPair = clusteringDataPairs.get(j);
                final String firstPairID = firstPair.getColumn();
                final String secondPairID = secondPair.getColumn();
                
                if(!firstPairID.equals(secondPairID)){
                
                int dimension = firstPair.getPoints().size();
                for (int k = 0; k < dimension; k++) {
                    clusterPoints.add(new ClusterPoint(firstPair.getPoints().get(k).getValue(), secondPair.getPoints().get(k).getValue()));
                }
                clusterSuperpositions.put(firstPairID+" "+secondPairID, clusterPoints);
                }
            }
        }
        
        return clusterSuperpositions;
    }

}
