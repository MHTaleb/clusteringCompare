/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;


/**
 *
 * @author taleb
 */
public class ImageToCSVConverter {

    public static void convertToCSV() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File selectedImage = chooser.showOpenDialog(null);

        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().addAll(
            
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );

        List<List<Integer>> matriceCSV = new ArrayList();

        File toSaveCSV = chooser.showSaveDialog(null);
        if (selectedImage != null) {
            CSVPrinter csvPrinter = null;
           
            FileWriter fWriter = null;
            try {

               
                int rowNum = 0;
                if (toSaveCSV.createNewFile()) {
                    BufferedImage readImage = ImageIO.read(selectedImage);
                    final int colonsSize = readImage.getWidth();
                    ArrayList<String> headers = new ArrayList();

//                    for (int i = 0; i < colonsSize; i++) {
//                        headers.add("ATT" + i);
//                    }
                    headers.add("RED");
                    headers.add("GREEN");
                    headers.add("BLUE");

                    for (int i = 0; i < readImage.getHeight(); i++) { // ligne indice
                        for (int j = 0; j < colonsSize; j++) { // collone indice
                        List ligne = new ArrayList<>();
                        Color c = new Color(readImage.getRGB(j, i));
                        ligne.add(c.getRed());
                        ligne.add(c.getGreen());
                        ligne.add(c.getBlue());
                        matriceCSV.add(ligne);
                        }
                    }
                    
                    if(toSaveCSV.getName().endsWith("csv")){
                            CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator("\n").withDelimiter(';').withFirstRecordAsHeader();
                            fWriter = new FileWriter(toSaveCSV);
                            csvPrinter = new CSVPrinter(fWriter, csvFormat);
                            csvPrinter.printRecord(headers);
                            for (int i = 0; i < matriceCSV.size(); i++) {
                                csvPrinter.printRecord(matriceCSV.get(i));
                            }
                        
                    }else{
                        
                
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                     
                    if (fWriter != null) {
                        fWriter.flush();
                        fWriter.close();
                    }

                    if (csvPrinter != null) {
                        csvPrinter.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

}
