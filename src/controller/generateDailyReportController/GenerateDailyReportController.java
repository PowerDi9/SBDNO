/*
 * SBDNO - Small Business Delivery Note Organizer
 * 
 * Copyright (C) 2025 Adrián González Hermida
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package controller.generateDailyReportController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.dao.ClientsDAO;
import model.dao.DeliveryNoteDAO;
import model.dao.StoresDAO;
import model.dao.TrucksDAO;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import view.generateDailyReportView.GenerateDailyReportFrame;

public class GenerateDailyReportController {                                                                        //Controller for generate daily report view

    GenerateDailyReportFrame view;
    String currencyType, dailyReportFolderPath, personalBusinessHeaderPath = null;
    Date date = null;
    boolean putFinalAmount = false;
    ArrayList<Integer> cells = new ArrayList<>();

    public GenerateDailyReportController(GenerateDailyReportFrame view) {
        this.view = view;
        this.view.addCancelButtonAl(this.getCancelButtonActionListener());
        this.view.addGenerateDailyReportButtonAL(this.getGenerateDailyReportButtonActionListener());
        innitComponents();
    }

    private ActionListener getCancelButtonActionListener() {                                                        //Gives the Cancel button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    private ActionListener getGenerateDailyReportButtonActionListener() {                                                       //Generates a daily report
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");                                                      //Generates the simple date format I desire
                date = view.getDeliveryDateJCalendar().getDate();                                                               //Gets the selected date and formats it
                String simpleDate = sdf.format(date);
                try (BufferedReader br = new BufferedReader(new FileReader("./data/user_data/config.txt"))) {                   //Tries to get the user configuration
                    ArrayList<String> datos = new ArrayList<>();
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        datos.add(linea);
                    }
                    currencyType = datos.get(0);                                                                                //Sets the user configuration on the variables
                    dailyReportFolderPath = datos.get(2);
                    personalBusinessHeaderPath = datos.get(3);
                    br.close();
                } catch (IOException ioe) {
                    System.err.println("Error al leer datos: " + ioe.getMessage());
                }
                if (dailyReportFolderPath == null || dailyReportFolderPath.isEmpty()) {                                         //Veryfies the daily report folder path
                    JOptionPane.showMessageDialog(view, "Please select a Daily Report Folder in 'SBDNO' -> 'Configuration'");
                    return;
                }
                if (personalBusinessHeaderPath == null || personalBusinessHeaderPath.isEmpty()) {                               //Veryfies the personal busines header folder path
                    JOptionPane.showMessageDialog(view, "No Personal Business Header has been detected. The file will be generated without one.\nConsider selecting one on 'SBDNO' -> 'Configuration'");
                }
                if (date == null || simpleDate.isEmpty()) {                                                                     //Veryfies the given date
                    JOptionPane.showMessageDialog(view, "Please select a delivery date");
                }

                Workbook workbook = new XSSFWorkbook();                                                                         //Creates a Workbook wher the Excel data is going
                Sheet sheet = workbook.createSheet("Sheet1");                                                                   //Creates a sheet for the workbook
                CellStyle titleStyle = titleStyle(workbook);                                                                    //Creates the cell styles that are going to be used
                CellStyle clientPhoneStyle = clientPhoneStyle(workbook);
                CellStyle amountStyle = amountStyle(workbook);
                CellStyle totalStyle = totalStyle(workbook);

                if (personalBusinessHeaderPath != null && !personalBusinessHeaderPath.isEmpty()) {                              //If there's no header skips creating it
                    try (InputStream is = new FileInputStream(personalBusinessHeaderPath)) {                                    //Gets the header path and creates it
                        byte[] bytes = IOUtils.toByteArray(is);
                        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                        Drawing<?> drawing = sheet.createDrawingPatriarch();
                        CreationHelper helper = workbook.getCreationHelper();
                        ClientAnchor anchor = helper.createClientAnchor();

                        anchor.setCol1(0);                                                                                      //Sets the header boundaries
                        anchor.setRow1(0);
                        anchor.setCol2(5);
                        anchor.setRow2(9);
                        Picture pict = drawing.createPicture(anchor, pictureIdx);                                               //Sets the picture
                        is.close();                                                                                             //Closes the stream
                    } catch (IOException ex) {
                        System.err.println("Error al cargar la imagen: " + ex.getMessage());
                    }
                }
                sheet.addMergedRegion(new CellRangeAddress(9, 9, 3, 4));                                                        //Merges the cells of the Date Title

                Row dateRow = sheet.createRow(9);                                                                               //Creates the row of the Date Title
                Cell dateCell = dateRow.createCell(3);                                                                          //Creates the cell of the Date Title
                dateCell.setCellValue("DATE: " + simpleDate);                                                             
                dateCell.setCellStyle(titleStyle);

                int rowTracker = 9;                                                                                             //int for tracking the current row
                int startDataRow = 0;                                                                                           //int for tracking the start of the data
                try {
                    DeliveryNoteDAO dnDAO = new DeliveryNoteDAO();                                                              //Gets a connetion to the Delivery Notes table
                    ResultSet dnRS = dnDAO.listDeliveryNotesByDeliveryDate(simpleDate);                                         //Gets all delivery notes by a date
                    Integer truckIdTracker = null;                                                                              //Tracks the id of the currrent truck
                    Integer storeIdTracker = null;                                                                              //Tracks the id of the current store
                    while (dnRS.next()) {                                                                                       //Lists the data of the current delivery note until there is no more
                        if (truckIdTracker == null || truckIdTracker != dnRS.getInt("truck_id")) {                              //If the truck id changes sets the final amount for the preceding data row except for the first time
                            if (putFinalAmount) {
                                setFinalAmount(rowTracker, startDataRow, sheet, totalStyle);                                    //Sets the final amount of the preceding data row
                                putFinalAmount = false;
                                rowTracker++;
                            }
                            rowTracker = rowTracker + 2;                                                                        //Leaves a 2 row space from the previous truck 
                            Row truckRow = sheet.createRow(rowTracker);                                                         //Creates the row
                            sheet.addMergedRegion(new CellRangeAddress(rowTracker, rowTracker, 1, 6));                          //Merges the cells where the title is going
                            Cell truckCell = truckRow.createCell(1);                                                            //Creates the cell
                            TrucksDAO trDAO = new TrucksDAO();                                                                  //Gets a connection to the Trucks table
                            ResultSet trRS = trDAO.getTruckName(dnRS.getInt("truck_id"));                                       //Gets the truck name by id
                            truckCell.setCellValue(trRS.getString("name"));                                                     //Sets the name
                            truckCell.setCellStyle(titleStyle);                                                                 //Sets the cell style
                            trRS.close();                                                                                       //Closes the result set
                            rowTracker++;                                                                                       //Advances the row
                        }
                        if (storeIdTracker == null || truckIdTracker != dnRS.getInt("truck_id") || storeIdTracker != dnRS.getInt("store_id")) {     //If the store id changes sets the final amount for the preceding data row
                            if (putFinalAmount) {
                                setFinalAmount(rowTracker, startDataRow, sheet, totalStyle);                                    //Sets the final amount of the preceding data row
                                rowTracker++;
                            }
                            storeIdTracker = dnRS.getInt("store_id");                                                           //Updates the store id tracker
                            truckIdTracker = dnRS.getInt("truck_id");                                                           //Updates the truck id tracker
                            rowTracker++;                                                                                       //Leaves a row space from the previous store
                            Row storeRow = sheet.createRow(rowTracker);                                                         //Creates the row
                            sheet.addMergedRegion(new CellRangeAddress(rowTracker, rowTracker, 1, 2));                          //Merges the cells for the store name
                            Cell storeCell = storeRow.createCell(1);                                                            //Creates the cell
                            StoresDAO stDAO = new StoresDAO();                                                                  //Gets a connection to the Stores table
                            ResultSet stRS = stDAO.getStoreName(storeIdTracker);                                                //Gets the store name by id
                            storeCell.setCellValue(stRS.getString("name"));                                                     //Sets the name
                            storeCell.setCellStyle(titleStyle);                                                                 //Sets the cell style
                            stRS.close();                                                                                       //Closes the result set
                            rowTracker++;                                                                                       //Advances the row

                            Row headersRow = sheet.createRow(rowTracker);                                                       //Creates the headers row
                            sheet.addMergedRegion(new CellRangeAddress(rowTracker, rowTracker, 1, 3));                          //Merges the cells for the client name title
                            sheet.addMergedRegion(new CellRangeAddress(rowTracker, rowTracker, 4, 5));                          //Merges the cells for the client phone number title
                            Cell clientTitle = headersRow.createCell(1);                                                        //Creates the client title cell
                            clientTitle.setCellValue("Client Name");                                                            //Sets the name
                            clientTitle.setCellStyle(titleStyle);                                                               //Sets the style

                            Cell phoneNumberTitle = headersRow.createCell(4);                                                   //Creates the phone number title cell
                            phoneNumberTitle.setCellValue("Phone Number");                                                      //Sets the name
                            phoneNumberTitle.setCellStyle(titleStyle);                                                          //Sets the style

                            Cell amountTitle = headersRow.createCell(6);                                                        //Creates the amount title cell
                            amountTitle.setCellValue("Amount");                                                                 //Sets the name
                            amountTitle.setCellStyle(titleStyle);                                                               //Sets the style

                            rowTracker++;                                                                                       //Advances the row
                            startDataRow = rowTracker;                                                                          //Sets the start data row
                            putFinalAmount = true;                                                                              //Makes the for setting the final amount true
                        }

                        Row dataRow = sheet.createRow(rowTracker);                                                              //Creates the data row 
                        sheet.addMergedRegion(new CellRangeAddress(rowTracker, rowTracker, 1, 3));                              //Merges the cells of the client name
                        sheet.addMergedRegion(new CellRangeAddress(rowTracker, rowTracker, 4, 5));                              //Merges the cells of the client phone number
                        Cell clientNameCell = dataRow.createCell(1);                                                            //Creates the client name cell
                        ClientsDAO cDAO = new ClientsDAO();                                                                     //Gets a connection to the clients table
                        ResultSet cRS = cDAO.getClientNamePhone(dnRS.getInt("client_id"));                                      //Gets the clients name and phone number by id
                        clientNameCell.setCellValue(cRS.getString("name"));                                                     //Sets the name
                        clientNameCell.setCellStyle(clientPhoneStyle);                                                          //Sets the cell style

                        Cell clientPhoneNumberCell = dataRow.createCell(4);                                                     //Creates the client phone number cell
                        clientPhoneNumberCell.setCellValue(cRS.getString("phone"));                                             //Sets the phone number
                        clientPhoneNumberCell.setCellStyle(clientPhoneStyle);                                                   //Sets the cell style
                        cRS.close();                                                                                            //Closes the result set

                        Cell amountCell = dataRow.createCell(6);                                                                //Creates the cell for the amount
                        amountCell.setCellValue(dnRS.getDouble("amount"));                                                      //Sets the amount
                        amountCell.setCellStyle(amountStyle);                                                                   //Sets the cell style
                        rowTracker++;                                                                                           //Advances the row
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                setFinalAmount(rowTracker, startDataRow, sheet, totalStyle);                                                    //Sets the "final" final amount
                rowTracker++;                                                                                                   
                
                Row finalRow = sheet.createRow(rowTracker + 1);                                                                 //Creates the final total cell
                Cell totalLabelCell = finalRow.createCell(3);
                totalLabelCell.setCellValue("TOTAL");
                totalLabelCell.setCellStyle(totalStyle);

                Cell finalTotalAmount = finalRow.createCell(4);                                                                 //Creates the final amount cell
                StringBuilder formula = new StringBuilder("SUM(");                                                              //Gets the formula for the final SUM
                for (int i = 0; i < cells.size(); i++) {
                    formula.append("G").append(cells.get(i));
                    if (i < cells.size() - 1) {
                        formula.append(",");
                    }
                }
                formula.append(")");
                finalTotalAmount.setCellFormula(formula.toString());                                                            //Sets the formula
                finalTotalAmount.setCellStyle(totalStyle);                                                                      //Sets the style

                sheet.setColumnWidth(0, 3000);                                                                                  //Acjusts all columns width
                sheet.setColumnWidth(1, 3000);
                sheet.setColumnWidth(2, 3000);
                sheet.setColumnWidth(3, 3000);
                sheet.setColumnWidth(4, 3000);
                sheet.setColumnWidth(5, 3000);
                sheet.setColumnWidth(6, 3000);

                try (FileOutputStream os = new FileOutputStream(dailyReportFolderPath + "/" + simpleDate.replaceAll("/", ".") + "_DailyReport.xlsx")) {      //Gets the designated folder and name
                    workbook.write(os);                                                                                         //Writes it
                    os.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        workbook.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                view.dispose();
            }
        };
        return al;
    }

    private void setFinalAmount(int currentRow, int startDataRow, Sheet sheet, CellStyle totalStyle) {                          //Method for setting the final amount from the data rows
        Row finalAmountRow = sheet.createRow(currentRow);
        Cell totalLabelCell = finalAmountRow.createCell(5);
        totalLabelCell.setCellValue("Total: ");
        totalLabelCell.setCellStyle(totalStyle);

        Cell totalAmountCell = finalAmountRow.createCell(6);
        String finalAmountFormula = String.format("SUM(G%d:G%d)", startDataRow + 1, currentRow);
        totalAmountCell.setCellFormula(finalAmountFormula);
        totalAmountCell.setCellStyle(totalStyle);
        cells.add(currentRow + 1);
    }

    private CellStyle titleStyle(Workbook wk) {                                 //Makes the title style
        CellStyle style = wk.createCellStyle();
        Font font = wk.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle clientPhoneStyle(Workbook wk) {                           //Makes the Client and Phone style
        CellStyle style = wk.createCellStyle();
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }

    private CellStyle amountStyle(Workbook wk) {                                //Makes the Amount atyle
        CellStyle style = wk.createCellStyle();
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat(wk.createDataFormat().getFormat("#,##0.00" + currencyType));
        return style;
    }

    private CellStyle totalStyle(Workbook wk) {                                 //Makes the Total style
        CellStyle style = wk.createCellStyle();
        Font font = wk.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setBorderTop(BorderStyle.DOUBLE);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat(wk.createDataFormat().getFormat("#,##0.00" + currencyType));
        return style;
    }

    public void setIcon() {                                                     //Sets the app Icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    private void innitComponents() {                                            //Initializes the components
        setIcon();
        this.view.setDefaultCloseOperation();
        this.view.setTitle("Generate Daily Report");
    }

}
