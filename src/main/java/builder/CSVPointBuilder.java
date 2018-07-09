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
 * cette classe nous permet de lire un fichier csv et le mapper dans une java List de ClusteringDataPair
 * 
 * chaque clustering data pair est simplement une collone dans le fichier csv avec ses donnée par exemple
 * 
 * Age : 17, 18, 22, 25, 15, 3 ......  ( ceci est un objet de type ClusteringDataPair il contient une liste de valeur et le nom de la collone )
 * 
 * dans cette exemple chaque valuer d age est en faite du type ColumnMembreValue qui enfaite a deux atribut : [value,index]
 * value dans ce cas est par exemple 22, loquiment 22 a l index 2 (notation commence de 0)
 * de cette facon j ai une conscience de l'appartenance de la valeur a son individu
 * 
 * 
 * a la fin pour lire une colomn specifique on ecris  clusteringDataColumn.get(i)
 * et pour recuperer la valeur d un individu pour une collone specifique on ecris
 * clusteringDataColumn.get(i).get(j).getValue().   ( attribut i : age   ,  membre j : 7 eme joueur , getValue() la valeur de l atribut age pour le 7 eme joureur )
 * 
 * de cette facon on peut manipuler nos donnees et les introduire dans un algorithme au choix 
 * 
 * @author taleb
 */
public class CSVPointBuilder {

    private final String FILE_PATH;
    private String[] HEADERS;
    List<ClusteringDataPair> clusteringDataColumn;
    
    /**
     * @param charge si vrai on va preparé une map de tout les combinaison possible d attribut ( pour algorithme de  vote )
     * @param chemin_fichier  le chemin du fichier csv
     */
    public CSVPointBuilder(String chemin_fichier,boolean charge) throws IOException {
        System.out.println("begin");
        this.FILE_PATH = chemin_fichier;

        this.clusteringDataColumn = new ArrayList<>();

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
                clusteringDataColumn.add(new ClusteringDataPair(head, new ArrayList<>()));
            });
            
            for (CSVRecord csvRecord : csvRecords) {
                final long index = csvRecord.getRecordNumber();
                // Accessing values by Header names
//                System.out.println("csvRecord.getRecordNumber()   "+index);
                final Map<String, String> line = csvRecord.toMap();
//                System.out.println("csvRecord.toMap()     "+line);
                Set<Map.Entry<String, String>> entrySet = line.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    //System.out.println(entry.getKey());
                   // System.out.println(entry.getValue());
                    Predicate<? super ClusteringDataPair> prdct = clusterData ->{
                        return clusterData.getColumnName().equals(entry.getKey());
                    };
                    clusteringDataColumn.stream().filter(prdct).forEach(clusterData ->{
                        clusterData.getColumnPoints().add((int)index-1, new ColumnMembreValue(Float.parseFloat(entry.getValue()), (int)index));
                    });
                    
                }
                
            }
            System.out.println("");
            System.out.println("/////////////////////////////////////////////////////////");
            System.out.println("/////////////////////////////////////////////////////////");
            System.out.println("/////////////////////////////////////////////////////////");
            System.out.println("");
//            System.out.println(clusteringDataColumn);
            if(!charge)return;
            Map<String, List<ClusterPoint>> clusteringDimensions = getClusteringDimensions();
         
    }catch(Exception e){
        e.printStackTrace();
    }

}
    /**
     * une methode qui va nous former des paire du csv charger
     * @return liste des paire former depuis les collonmne
     */
    public Map<String,List<ClusterPoint>> getClusteringDimensions(){
        final Map<String,List<ClusterPoint>> clusterSuperpositions = new Hashtable();
        
        int size = clusteringDataColumn.size();
        for (int i = 0; i < size-1; i++) {
            for (int j = i; j < size; j++) {
            
                final ArrayList<ClusterPoint> clusterPoints = new ArrayList<>();
                final ClusteringDataPair firstPair = clusteringDataColumn.get(i);
                final ClusteringDataPair secondPair = clusteringDataColumn.get(j);
                final String firstPairID = firstPair.getColumnName();
                final String secondPairID = secondPair.getColumnName();
                
                if(!firstPairID.equals(secondPairID) && (!firstPairID.toLowerCase().equals("class"))&& (!secondPairID.toLowerCase().equals("class"))){
                
                int dimension = firstPair.getColumnPoints().size();
                for (int k = 0; k < dimension; k++) {
                       // System.out.println("processing loop i="+i+" j = "+j+"    k="+k);
                    clusterPoints.add(new ClusterPoint(firstPair.getColumnPoints().get(k).getValue(), secondPair.getColumnPoints().get(k).getValue()));
                }
                clusterSuperpositions.put(firstPairID+" "+secondPairID, clusterPoints);
                }
            }
        }
                System.out.println("end processing");
        
        return clusterSuperpositions;
    }

    
    /**
     * cette fonction retourne le csv sous frome de tableau avec le titre de l attribut
     * 
     * @return chaque collone du csv avec ses valeur
     */
    public List<ClusteringDataPair> getClusteringDataColumn() {
        System.out.println("getting datas");
        return clusteringDataColumn;
    }
    
    

}
