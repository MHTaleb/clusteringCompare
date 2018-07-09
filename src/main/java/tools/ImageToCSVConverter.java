/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
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
 * this is my api to convert a specific image to a csv data array
 * this class store the current converted image dimensions 
 * 
 * this file is under the apache 2 licence is free to use and redestribute
 * hail opensource
 * 
 * @author taleb mohammed housseyn
 */
public class ImageToCSVConverter {

    public static int width, height;

    /**
     * a generic use case : this method will call the javafx file chooser to load 
     * a specific image file under the following extensions : JPG,BMP,PNG
     * then it will ask you to choose where to store the csv image conversion results
     * in a csv with headers RED GREEN BLUE CLASS
     * 
     * all the classes will be set to zero
     * this representation is better seen by the voting based algorithm
     * 
     * @return a csv rgb separated values representation of the selected image file
     */
    public static void convertTo3DCSV() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File selectedImage = chooser.showOpenDialog(null);

        imageToCSV3D(selectedImage);

    } 
    
     /**
     * a generic use case : this method will call the javafx file chooser to load 
     * a specific image file under the following extensions : JPG,BMP,PNG
     * then it will ask you to choose where to store the csv image conversion results
     * in a csv with headers Color CLASS
     * 
     * in this case the result is a binary shift conversion of a pixel color
     * this representation is better seen by kmeans
     * 
     * all the classes will be set to zero
     * 
     * @return a csv rgb integer value representation of the selected image file
     */
    public static void convertTo1DCSV() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File selectedImage = chooser.showOpenDialog(null);

        imageToCSV1D(selectedImage);

    }

    
    /**
     * this is the facade methode that handles the converting to 1 value csv
     *  1- we choose where to store the csv file
     *  2- filling a matrix with the read values from the buffered image
     *  3- using the csvPrinter to write to the csv file
     *  4- flushing and closing streams
     * 
     * @param selectedImage  the file object of the selected image
     * @return the csv file created from this image
     */
    public static File imageToCSV1D(File selectedImage){
         FileChooser chooser = new FileChooser();
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

                BufferedImage readImage = ImageIO.read(selectedImage);
                ImageToCSVConverter.width = readImage.getWidth();
                ImageToCSVConverter.height = readImage.getHeight();
                int rowNum = 0;
                if (toSaveCSV.createNewFile()) {

                    final int colonsSize = readImage.getWidth();
                    ArrayList<String> headers = new ArrayList();

//                    for (int i = 0; i < colonsSize; i++) {
//                        headers.add("ATT" + i);
//                    }
                    headers.add("color");
                    headers.add("class");

                    System.out.println("ImageToCSVConverter.height " + ImageToCSVConverter.height);
                    System.out.println("ImageToCSVConverter.width " + ImageToCSVConverter.width);
                    for (int i = 0; i < readImage.getHeight(); i++) { // ligne indice
                        for (int j = 0; j < colonsSize; j++) { // collone indice
                            List ligne = new ArrayList<>();
                            Color c = new Color(readImage.getRGB(j, i));
                            ligne.add(c.getRGB());
                            ligne.add(0);
                            matriceCSV.add(ligne);
                        }
                    }

                    if (toSaveCSV.getName().endsWith("csv")) {
                        CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator("\n").withDelimiter(';').withFirstRecordAsHeader();
                        fWriter = new FileWriter(toSaveCSV);
                        csvPrinter = new CSVPrinter(fWriter, csvFormat);
                        csvPrinter.printRecord(headers);
                        for (int i = 0; i < matriceCSV.size(); i++) {
                            csvPrinter.printRecord(matriceCSV.get(i));
                        }

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
        return toSaveCSV;
    }
    
      /**
     * this is the facade methode that handles the converting to 3 value csv
     *  1- we choose where to store the csv file
     *  2- filling a matrix with the read values from the buffered image
     *  3- using the csvPrinter to write to the csv file
     *  4- flushing and closing streams
     * 
     * @param selectedImage  the file object of the selected image
     * @return the csv file created from this image
     */
    public static File imageToCSV3D(File selectedImage) {
        FileChooser chooser = new FileChooser();
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

                BufferedImage readImage = ImageIO.read(selectedImage);
                ImageToCSVConverter.width = readImage.getWidth();
                ImageToCSVConverter.height = readImage.getHeight();
                int rowNum = 0;
                if (toSaveCSV.createNewFile()) {

                    final int colonsSize = readImage.getWidth();
                    ArrayList<String> headers = new ArrayList();

//                    for (int i = 0; i < colonsSize; i++) {
//                        headers.add("ATT" + i);
//                    }
                    headers.add("RED");
                    headers.add("GREEN");
                    headers.add("BLUE");
                    headers.add("class");

                    System.out.println("ImageToCSVConverter.height " + ImageToCSVConverter.height);
                    System.out.println("ImageToCSVConverter.width " + ImageToCSVConverter.width);
                    for (int i = 0; i < readImage.getHeight(); i++) { // ligne indice
                        for (int j = 0; j < colonsSize; j++) { // collone indice
                            List ligne = new ArrayList<>();
                            Color c = new Color(readImage.getRGB(j, i));
                            ligne.add(c.getRed());
                            ligne.add(c.getGreen());
                            ligne.add(c.getBlue());
                            ligne.add(0);
                            matriceCSV.add(ligne);
                        }
                    }

                    if (toSaveCSV.getName().endsWith("csv")) {
                        CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator("\n").withDelimiter(';').withFirstRecordAsHeader();
                        fWriter = new FileWriter(toSaveCSV);
                        csvPrinter = new CSVPrinter(fWriter, csvFormat);
                        csvPrinter.printRecord(headers);
                        for (int i = 0; i < matriceCSV.size(); i++) {
                            csvPrinter.printRecord(matriceCSV.get(i));
                        }

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
        return toSaveCSV;
    }

}
