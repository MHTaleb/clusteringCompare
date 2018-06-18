/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
                new FileChooser.ExtensionFilter("XLS", "*.xlsx")
        );

        List<List<Integer>> matriceCSV = new ArrayList();

        File toSaveCSV = chooser.showSaveDialog(null);
        if (selectedImage != null) {
            CSVPrinter csvPrinter = null;
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileWriter fWriter = null;
            try {

                XSSFSheet sheet = workbook.createSheet("Datatypes in Java");
                int rowNum = 0;
                if (toSaveCSV.createNewFile()) {
                    BufferedImage readImage = ImageIO.read(selectedImage);
                    final int colonsSize = readImage.getWidth();
                    ArrayList<String> headers = new ArrayList();

                    for (int i = 0; i < colonsSize; i++) {
                        headers.add("ATT" + i);
                    }

                    for (int i = 0; i < readImage.getHeight(); i++) { // ligne indice
                        List ligne = new ArrayList<>();
                        matriceCSV.add(ligne);
                        for (int j = 0; j < colonsSize; j++) { // collone indice
                            ligne.add(readImage.getRGB(j, i));
                        }
                    }
//                    CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator("\n").withDelimiter(',').withFirstRecordAsHeader();
//                    fWriter = new FileWriter(toSaveCSV);
//                    csvPrinter = new CSVPrinter(fWriter, csvFormat);
//                    csvPrinter.printRecord(headers);
//                    for (int i = 0; i < matriceCSV.size(); i++) {
//                        csvPrinter.printRecord(matriceCSV.get(i));
//                    }
                    Row row = sheet.createRow(rowNum++);
                    int colNum = 0;
                    for (int i = 0 ; i<headers.size() ; i++) {
                        Cell headerCell = row.createCell(colNum++);
                        headerCell.setCellValue(headers.get(i));
                    }
                    
                    for (int i = 0; i < matriceCSV.size(); i++) {
                        XSSFRow dataRow = sheet.createRow(rowNum++);
                        for (int j = 0; j < matriceCSV.get(0).size(); j++) {
                            XSSFCell dataCell = dataRow.createCell(j);
                            dataCell.setCellValue(matriceCSV.get(i).get(j));
                        }
                    }
             FileOutputStream outputStream = new FileOutputStream(toSaveCSV);
             workbook.write(outputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                     workbook.close();
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
