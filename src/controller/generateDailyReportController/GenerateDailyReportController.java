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

public class GenerateDailyReportController {

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

    private ActionListener getCancelButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    private ActionListener getGenerateDailyReportButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                date = view.getDeliveryDateJCalendar().getDate();
                String simpleDate = sdf.format(date);
                try (BufferedReader br = new BufferedReader(new FileReader("./data/user_data/config.txt"))) {
                    ArrayList<String> datos = new ArrayList<>();
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        datos.add(linea);
                    }
                    currencyType = datos.get(0);
                    dailyReportFolderPath = datos.get(2);
                    personalBusinessHeaderPath = datos.get(3);
                    br.close();
                } catch (IOException ioe) {
                    System.err.println("Error al leer datos: " + ioe.getMessage());
                }
                if (dailyReportFolderPath == null || dailyReportFolderPath.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Please select a Daily Report Folder in 'SBDNO' -> 'Configuration'");
                    return;
                }
                if (personalBusinessHeaderPath == null || personalBusinessHeaderPath.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "No Personal Business Header has been detected. The file will be generated without one.\nConsider selecting one on 'SBDNO' -> 'Configuration'");
                }
                if (date == null || simpleDate.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Please select a delivery date");
                }

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Sheet1");
                CellStyle titleStyle = titleStyle(workbook);
                CellStyle clientPhoneStyle = clientPhoneStyle(workbook);
                CellStyle amountStyle = amountStyle(workbook);
                CellStyle totalStyle = totalStyle(workbook);

                if (personalBusinessHeaderPath != null && !personalBusinessHeaderPath.isEmpty()) {
                    try (InputStream is = new FileInputStream(personalBusinessHeaderPath)) {
                        byte[] bytes = IOUtils.toByteArray(is);
                        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                        Drawing<?> drawing = sheet.createDrawingPatriarch();
                        CreationHelper helper = workbook.getCreationHelper();
                        ClientAnchor anchor = helper.createClientAnchor();

                        anchor.setCol1(0);
                        anchor.setRow1(0);
                        anchor.setCol2(5);
                        anchor.setRow2(9);
                        Picture pict = drawing.createPicture(anchor, pictureIdx);
                        is.close();
                    } catch (IOException ex) {
                        System.err.println("Error al cargar la imagen: " + ex.getMessage());
                    }
                }
                sheet.addMergedRegion(new CellRangeAddress(9, 9, 3, 4));

                Row deliveredGoodsRow = sheet.createRow(9);
                Cell deliveredGoodsCell = deliveredGoodsRow.createCell(3);
                deliveredGoodsCell.setCellValue("DATE: " + simpleDate);
                deliveredGoodsCell.setCellStyle(titleStyle);

                Row emptyRow = sheet.createRow(10);
                int rowTracker = 9;
                int startDataRow = 0;
                try {
                    DeliveryNoteDAO dnDAO = new DeliveryNoteDAO();
                    ResultSet dnRS = dnDAO.listDeliveryNotesByDeliveryDate(simpleDate);
                    Integer truckIdTracker = null;
                    Integer storeIdTracker = null;
                    while (dnRS.next()) {
                        if (truckIdTracker == null || truckIdTracker != dnRS.getInt("truck_id")) {
                            if (putFinalAmount) {
                                setFinalAmount(rowTracker, startDataRow, sheet, totalStyle);
                                putFinalAmount = false;
                                rowTracker++;
                            }
                            rowTracker = rowTracker + 2;
                            Row truckRow = sheet.createRow(rowTracker);
                            sheet.addMergedRegion(new CellRangeAddress(rowTracker, rowTracker, 1, 6));
                            Cell truckCell = truckRow.createCell(1);
                            TrucksDAO trDAO = new TrucksDAO();
                            ResultSet trRS = trDAO.getTruckName(dnRS.getInt("truck_id"));
                            truckCell.setCellValue(trRS.getString("name"));
                            truckCell.setCellStyle(titleStyle);
                            trRS.close();
                            rowTracker++;
                        }
                        if (storeIdTracker == null || truckIdTracker != dnRS.getInt("truck_id") || storeIdTracker != dnRS.getInt("store_id")) {
                            if (putFinalAmount) {
                                setFinalAmount(rowTracker, startDataRow, sheet, totalStyle);
                                rowTracker++;
                            }
                            storeIdTracker = dnRS.getInt("store_id");
                            truckIdTracker = dnRS.getInt("truck_id");
                            rowTracker++;
                            Row storeRow = sheet.createRow(rowTracker);
                            sheet.addMergedRegion(new CellRangeAddress(rowTracker, rowTracker, 1, 2));
                            Cell storeCell = storeRow.createCell(1);
                            StoresDAO stDAO = new StoresDAO();
                            ResultSet stRS = stDAO.getStoreName(storeIdTracker);
                            storeCell.setCellValue(stRS.getString("name"));
                            storeCell.setCellStyle(titleStyle);
                            stRS.close();
                            rowTracker++;

                            Row headersRow = sheet.createRow(rowTracker);
                            sheet.addMergedRegion(new CellRangeAddress(rowTracker, rowTracker, 1, 3));
                            sheet.addMergedRegion(new CellRangeAddress(rowTracker, rowTracker, 4, 5));
                            Cell clientTitle = headersRow.createCell(1);
                            clientTitle.setCellValue("Client Name");
                            clientTitle.setCellStyle(titleStyle);

                            Cell phoneNumberTitle = headersRow.createCell(4);
                            phoneNumberTitle.setCellValue("Phone Number");
                            phoneNumberTitle.setCellStyle(titleStyle);

                            Cell amountTitle = headersRow.createCell(6);
                            amountTitle.setCellValue("Amount");
                            amountTitle.setCellStyle(titleStyle);

                            rowTracker++;
                            startDataRow = rowTracker;
                            putFinalAmount = true;
                        }

                        Row dataRow = sheet.createRow(rowTracker);
                        sheet.addMergedRegion(new CellRangeAddress(rowTracker, rowTracker, 1, 3));
                        sheet.addMergedRegion(new CellRangeAddress(rowTracker, rowTracker, 4, 5));
                        Cell clientNameCell = dataRow.createCell(1);
                        ClientsDAO cDAO = new ClientsDAO();
                        ResultSet cRS = cDAO.getClientNamePhone(dnRS.getInt("client_id"));
                        clientNameCell.setCellValue(cRS.getString("name"));
                        clientNameCell.setCellStyle(clientPhoneStyle);

                        Cell clientPhoneNumberCell = dataRow.createCell(4);
                        clientPhoneNumberCell.setCellValue(cRS.getString("phone"));
                        clientPhoneNumberCell.setCellStyle(clientPhoneStyle);
                        cRS.close();

                        Cell amountCell = dataRow.createCell(6);
                        amountCell.setCellValue(dnRS.getDouble("amount"));
                        amountCell.setCellStyle(amountStyle);
                        rowTracker++;
                    }
                    rowTracker++;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                setFinalAmount(rowTracker - 1, startDataRow, sheet, totalStyle);

                Row finalRow = sheet.createRow(rowTracker+1);
                Cell totalLabelCell = finalRow.createCell(3);
                totalLabelCell.setCellValue("TOTAL");
                totalLabelCell.setCellStyle(totalStyle);

                Cell finalTotalAmount = finalRow.createCell(4);
                StringBuilder formula = new StringBuilder("SUM(");
                for (int i = 0; i < cells.size(); i++) {
                    formula.append("G").append(cells.get(i));
                    if (i < cells.size() - 1) {
                        formula.append(",");
                    }
                }
                formula.append(")");
                finalTotalAmount.setCellFormula(formula.toString());
                System.out.println(formula.toString());
                finalTotalAmount.setCellStyle(totalStyle);

                sheet.setColumnWidth(0, 3000);
                sheet.setColumnWidth(1, 3000);
                sheet.setColumnWidth(2, 3000);
                sheet.setColumnWidth(3, 3000);
                sheet.setColumnWidth(4, 3000);
                sheet.setColumnWidth(5, 3000);
                sheet.setColumnWidth(6, 3000);

                try (FileOutputStream outputStream = new FileOutputStream(dailyReportFolderPath + "\\" + simpleDate.replaceAll("/", ".") + "_DailyReport.xlsx")) {
                    workbook.write(outputStream);
                    System.out.println("Excel file created correctly on: " + dailyReportFolderPath);
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

    private void setFinalAmount(int currentRow, int startDataRow, Sheet sheet, CellStyle totalStyle) {
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

    private CellStyle titleStyle(Workbook wk) {
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

    private CellStyle clientPhoneStyle(Workbook wk) {
        CellStyle style = wk.createCellStyle();
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }

    private CellStyle amountStyle(Workbook wk) {
        CellStyle style = wk.createCellStyle();
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat(wk.createDataFormat().getFormat("#,##0.00" + currencyType));
        return style;
    }

    private CellStyle totalStyle(Workbook wk) {
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

    private void innitComponents() {
        this.view.setDefaultCloseOperation();
    }

}
