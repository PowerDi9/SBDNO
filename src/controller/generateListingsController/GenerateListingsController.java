package controller.generateListingsController;

import controller.generateListingsController.selectDeliveryFromDateController.SelectDeliveryFromDateController;
import controller.generateListingsController.selectDeliveryUntilDateController.SelectDeliveryUntilDateController;
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
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.dao.BusinessDAO;
import model.dao.ClientsDAO;
import model.dao.DeliveryNoteDAO;
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
import view.addDeliveryNoteView.selectDateDialog.SelectDateDialog;
import view.generateListingsView.GenerateListingsFrame;

public class GenerateListingsController {

    GenerateListingsFrame view;
    String currencyType, listingFolderPath, personalBusinessHeaderPath, deliveryFromDate, deliveryUntilDate, businessName = null;
    int businessId;
    double businessPercentage;

    public GenerateListingsController(GenerateListingsFrame view) {
        this.view = view;
        this.view.addBackButtonAL(this.getBackButtonActionListener());
        this.view.addSelectDeliveryFromDateButtonAL(this.getSelectDeliveryFromDateButtonActionListener());
        this.view.addSelectDeliveryUntilDateButtonAL(this.getSelectDeliveryUntilDateButtonActionListener());
        this.view.addGenerateListingButtonAL(this.getGenerateListingActionListener());
        innitComponents();
    }

    private ActionListener getGenerateListingActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (BufferedReader br = new BufferedReader(new FileReader("./data/user_data/config.txt"))) {
                    ArrayList<String> datos = new ArrayList<>();
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        datos.add(linea);
                    }
                    currencyType = datos.get(0);
                    listingFolderPath = datos.get(1);
                    personalBusinessHeaderPath = datos.get(3);
                    br.close();
                } catch (IOException ioe) {
                    System.err.println("Error al leer datos: " + ioe.getMessage());
                }
                if (listingFolderPath == null || listingFolderPath.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Please select a Listing Folder in 'SBDNO' -> 'Configuration'");
                    return;
                }
                if (personalBusinessHeaderPath == null || personalBusinessHeaderPath.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "No Personal Business Header has been detected. The file will be generated without one.\nConsider selecting one on 'SBDNO' -> 'Configuration'");
                }
                if (deliveryFromDate == null || deliveryFromDate.isEmpty()) {
                    JOptionPane.showConfirmDialog(view, "Please select a starting delivery date");
                    return;
                }
                if (deliveryUntilDate == null || deliveryUntilDate.isEmpty()) {
                    JOptionPane.showConfirmDialog(view, "Please select an ending delivery date");
                    return;
                }
                businessId = Integer.parseInt(view.getSelectBusinessComboBox().getSelectedItem().toString().split(",")[0]);
                businessName = view.getSelectBusinessComboBox().getSelectedItem().toString().split(",")[1];
                try {
                    BusinessDAO bDAO = new BusinessDAO();
                    ResultSet rs = bDAO.getBusinessPercentage(businessId);
                    businessPercentage = rs.getDouble("percentage");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Sheet1");
                CellStyle titleStyle = titleStyle(workbook);
                CellStyle dateClientStyle = dateClientStyle(workbook);
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
                    } catch (IOException ex) {
                        System.err.println("Error al cargar la imagen: " + ex.getMessage());
                    }
                }
                sheet.addMergedRegion(new CellRangeAddress(9, 9, 1, 6));
                sheet.addMergedRegion(new CellRangeAddress(11, 11, 2, 4));
                sheet.addMergedRegion(new CellRangeAddress(10, 10, 1, 2));
                sheet.addMergedRegion(new CellRangeAddress(10, 10, 3, 6));

                Row deliveredGoodsRow = sheet.createRow(9);
                Cell deliveredGoodsCell = deliveredGoodsRow.createCell(1);
                deliveredGoodsCell.setCellValue("DELIVERED GOODS");
                deliveredGoodsCell.setCellStyle(titleStyle);

                Row businessRow = sheet.createRow(10);
                Cell businessLaberCell = businessRow.createCell(1);
                businessLaberCell.setCellValue("BUSINESS");
                businessLaberCell.setCellStyle(titleStyle);

                Cell businessNameCell = businessRow.createCell(3);
                businessNameCell.setCellValue(businessName);
                businessNameCell.setCellStyle(titleStyle);

                Row headersRow = sheet.createRow(11);
                Cell dateHeaderCell = headersRow.createCell(1);
                dateHeaderCell.setCellValue("DATE");
                dateHeaderCell.setCellStyle(titleStyle);

                Cell clientHeaderCell = headersRow.createCell(2);
                clientHeaderCell.setCellValue("CLIENTS");
                clientHeaderCell.setCellStyle(titleStyle);

                Cell amountHeaderCell = headersRow.createCell(5);
                amountHeaderCell.setCellValue("AMOUNT");
                amountHeaderCell.setCellStyle(titleStyle);

                Cell percentageHeaderCell = headersRow.createCell(6);
                percentageHeaderCell.setCellValue(businessPercentage + "%");
                percentageHeaderCell.setCellStyle(titleStyle);

                int startedLoopRow = 12;
                int startDataRow = 12;
                try {
                    DeliveryNoteDAO dnDAO = new DeliveryNoteDAO();
                    ResultSet dnRS = dnDAO.listDeliveryNotesByBusinessIdAndDeliveryDate(deliveryFromDate, deliveryUntilDate, businessId);
                    while (dnRS.next()) {
                        int currentRow = startDataRow++;
                        Row dataRow = sheet.createRow(currentRow);
                        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 2, 4));
                        Cell dateCell = dataRow.createCell(1);
                        dateCell.setCellValue(dnRS.getString("delivery_date"));
                        dateCell.setCellStyle(dateClientStyle);

                        ClientsDAO cDAO = new ClientsDAO();
                        ResultSet cRS = cDAO.getClientName(dnRS.getInt("client_id"));
                        Cell clientNameCell = dataRow.createCell(2);
                        clientNameCell.setCellValue(cRS.getString("name"));
                        clientNameCell.setCellStyle(dateClientStyle);

                        Cell amountCell = dataRow.createCell(5);
                        amountCell.setCellValue(dnRS.getDouble("amount"));
                        amountCell.setCellStyle(amountStyle);

                        Cell percentageCell = dataRow.createCell(6);
                        String formula = String.format("F%d*" + (businessPercentage / 100), currentRow + 1);
                        percentageCell.setCellFormula(formula);
                        percentageCell.setCellStyle(amountStyle);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                Row finalRow = sheet.createRow(startDataRow);
                Cell totalLabelCell = finalRow.createCell(4);
                totalLabelCell.setCellValue("TOTAL");
                totalLabelCell.setCellStyle(totalStyle);

                Cell totalAmountCell = finalRow.createCell(5);
                String finalAmountFormula = String.format("SUM(F%d:F%d)", startedLoopRow + 1, startDataRow);
                totalAmountCell.setCellFormula(finalAmountFormula);
                totalAmountCell.setCellStyle(totalStyle);

                Cell totalPercentaceCell = finalRow.createCell(6);
                String finalPercentageFormula = String.format("SUM(G%d:G%d)", startedLoopRow + 1, startDataRow);
                totalPercentaceCell.setCellFormula(finalPercentageFormula);
                totalPercentaceCell.setCellStyle(totalStyle);

                sheet.setColumnWidth(0, 3000);
                sheet.setColumnWidth(1, 3000);
                sheet.setColumnWidth(2, 3000);
                sheet.setColumnWidth(3, 3000);
                sheet.setColumnWidth(4, 3000);
                sheet.setColumnWidth(5, 3000);
                sheet.setColumnWidth(6, 3000);

                try (FileOutputStream outputStream = new FileOutputStream(listingFolderPath + "\\" + businessName + "-" + deliveryFromDate.replaceAll("/", ".") + "-" + deliveryUntilDate.replaceAll("/", ".") + ".xlsx")) {
                    workbook.write(outputStream);
                    System.out.println("Excel file created correctly on: " + listingFolderPath);
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
            }
        };
        return al;
    }

    private ActionListener getBackButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    private ActionListener getSelectDeliveryFromDateButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenerateListingsController ctrl = GenerateListingsController.this;
                SelectDateDialog sdd = new SelectDateDialog(view, true);
                SelectDeliveryFromDateController sdfdc = new SelectDeliveryFromDateController(sdd, ctrl);
                sdd.setLocationRelativeTo(view);
                sdd.setVisible(true);
            }
        };
        return al;
    }

    private ActionListener getSelectDeliveryUntilDateButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenerateListingsController ctrl = GenerateListingsController.this;
                SelectDateDialog sdd = new SelectDateDialog(view, true);
                SelectDeliveryUntilDateController sdfdc = new SelectDeliveryUntilDateController(sdd, ctrl);
                sdd.setLocationRelativeTo(view);
                sdd.setVisible(true);
            }
        };
        return al;
    }

    private void setBusinessComboBox() {
        DefaultComboBoxModel<String> businessModel = new DefaultComboBoxModel<>();
        try {
            BusinessDAO dao = new BusinessDAO();
            ResultSet rs = dao.listBusinessesIdName();
            while (rs.next()) {
                String businessId = String.valueOf(rs.getInt("business_id"));
                String name = rs.getString("name");
                String str = businessId + "," + name;
                businessModel.addElement(str);
            }
            view.getSelectBusinessComboBox().setModel(businessModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDeliveryFromDate(String str) {
        this.deliveryFromDate = str;
        this.view.setSelectDeliveryFromDateText(str);
    }

    public void setDeliveryUntilDate(String str) {
        this.deliveryUntilDate = str;
        this.view.setSelectDeliveryUntilDateText(str);
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

    private CellStyle dateClientStyle(Workbook wk) {
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

    public void setIcon() {
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    private void innitComponents() {
        setIcon();
        view.setTitle("Generate Listings");
        setBusinessComboBox();
        view.setDefaultCloseOperation();
    }

}
