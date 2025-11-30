package controller.searchManageDeliveryNoteController.editDeliveryNoteController;

import controller.searchManageDeliveryNoteController.editDeliveryNoteController.selectDateController.SelectDateController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.BusinessDAO;
import model.dao.ClientsDAO;
import model.dao.DeliveryNoteDAO;
import model.dao.SellersDAO;
import model.dao.StoresDAO;
import model.dao.TrucksDAO;
import view.addDeliveryNoteView.selectDateDialog.SelectDateDialog;
import view.searchManageDeliveryNoteView.SearchManageDeliveryNoteFrame;
import view.searchManageDeliveryNoteView.editDeliveryNoteView.EditDeliveryNoteFrame;

public class EditDeliveryNoteController {

    EditDeliveryNoteFrame view;
    SearchManageDeliveryNoteFrame view2;
    boolean ignoreComboChange = false;
    String deliveryId, deliveryDate, pdfRoute, date, businessId, storeId, sellerId, truckId, amount, clientId = null;
    String newPDFRoute, newDeliveryDate, newBusinessId, newStoreId, newSellerId, newTruckId;
    Double newAmount;
    
    public EditDeliveryNoteController(EditDeliveryNoteFrame view,SearchManageDeliveryNoteFrame view2, String deliveryId, String introductionDate, String deliveryDate, String clientId, String businessId, String storeId, String sellerId, String truckId, String amount, String pdfPath) {
        this.view = view;
        this.view2 = view2;
        this.deliveryId = deliveryId;
        this.deliveryDate = deliveryDate;
        this.pdfRoute = pdfPath;
        this.date = introductionDate;
        this.businessId = businessId;
        this.storeId = storeId;
        this.sellerId = sellerId;
        this.truckId = truckId;
        this.clientId = clientId;
        this.amount = amount;
        this.innitComponents();
        this.view.addBackButtonAL(this.getBackButtonActionListener());
        this.view.addResetEntriesButtonAL(this.getResetEntriesButtonActionListener());
        this.view.addSelectBusinesComboBoxAL(this.getSelectBusinessComboBoxActionListener());
        this.view.addSelectStoreComboBoxAL(this.getSelectStoreComboBoxActionListener());
        this.view.addSelectSellerComboBoxAL(this.getSelectSellerComboBoxActionListener());
        this.view.addSelectTruckComboBoxAL(this.getSelectTruckComboBoxActionListener());
        this.view.addSelectDateButtonAL(this.getSelectDateButtonActionListener());
        this.view.addSelectPDFButtonAL(this.getSelectPDFBUttonActionListener());
        this.view.addConfirmChangesButtonAL(this.getConfirmChangesActionListener());
    }

    private ActionListener getConfirmChangesActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newBusinessId = view.getSelectBusinessComboBox().getSelectedItem().toString().split(",")[0];
                newStoreId = view.getSelectStoreComboBox().getSelectedItem().toString().split(",")[0];
                newSellerId = view.getSelectSellerComboBox().getSelectedItem().toString().split(",")[0];
                newTruckId = view.getSelectTruckComboBox().getSelectedItem().toString().split(",")[0];
                if(newDeliveryDate == null){
                    newDeliveryDate = deliveryDate;
                } else if(newPDFRoute == null){
                    newPDFRoute = pdfRoute;
                }
                if (Integer.parseInt(newBusinessId) == 0 || newBusinessId == null) {
                    JOptionPane.showMessageDialog(view, "Please select a business.");
                    return;
                } else if (Integer.parseInt(newStoreId) == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a store.");
                    return;
                } else if (Integer.parseInt(newSellerId) == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a seller.");
                    return;
                } else if (Integer.parseInt(newTruckId) == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a Truck.");
                    return;
                } else if (newDeliveryDate == null) {
                    JOptionPane.showMessageDialog(view, "Please select a Delivery Date.");
                    return;
                } else if (newPDFRoute == null) {
                    JOptionPane.showMessageDialog(view, "Please select a PDF file.");
                    return;
                } else if (view.getAmountTextFieldText().isBlank()) {
                    JOptionPane.showMessageDialog(view, "Please introduce an amount.");
                    return;
                } else if (view.getClientNameTextFieldText().isBlank()) {
                    JOptionPane.showMessageDialog(view, "Please introduce a client name.");
                    return;
                } else if (view.getClientPhoneNumberTextFieldText().isBlank()) {
                    JOptionPane.showMessageDialog(view, "Please introduce a client phone number.");
                    return;
                }
                try {
                    newAmount = Double.parseDouble(view.getAmountTextFieldText().replaceAll(",", "."));
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Please introduce a valid number.");
                    return;
                }
                String clientName = view.getClientNameTextFieldText();
                String clientPhoneNumber = view.getClientPhoneNumberTextFieldText();
                try {
                    DeliveryNoteDAO dndao = new DeliveryNoteDAO();
                    dndao.editDeliveryNote(Integer.parseInt(deliveryId), date, newDeliveryDate, newAmount, Integer.parseInt(clientId), Integer.parseInt(newSellerId), Integer.parseInt(newBusinessId), Integer.parseInt(newStoreId), Integer.parseInt(newTruckId), newPDFRoute);
                    JOptionPane.showMessageDialog(view, "DeliveryNote updated correctly");
                    updateSearchManageDeliveryNotesModel();
                    view.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
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

    private ActionListener getResetEntriesButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetEntries();
            }
        };
        return al;
    }

    private ActionListener getSelectPDFBUttonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int selection = fc.showOpenDialog(view);
                String nPDFRute = null;
                if (selection == JFileChooser.APPROVE_OPTION) {
                    nPDFRute = fc.getSelectedFile().getAbsolutePath();
                    if (!nPDFRute.endsWith(".pdf")) {
                        JOptionPane.showMessageDialog(view, "Please select a PDF file.");
                        return;
                    }
                    JOptionPane.showMessageDialog(view, "Selected PDF: " + nPDFRute);
                    System.out.println("Selected PDF: " + nPDFRute);
                } else {
                    JOptionPane.showMessageDialog(view, "No PDF was selected");
                }
                view.setSelectedPDFPathLabelText(nPDFRute);
                newPDFRoute = nPDFRute;
                
            }
        };
        return al;
    }

    private ActionListener getSelectDateButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditDeliveryNoteController actualController = EditDeliveryNoteController.this;
                SelectDateDialog sdd = new SelectDateDialog(view, true);
                SelectDateController sdc = new SelectDateController(sdd, actualController);
                sdd.setLocationRelativeTo(view);
                sdd.setVisible(true);
            }
        };
        return al;
    }

    public void setDeliveryDate(String str) {
        this.newDeliveryDate = str;
        this.view.setSelectDateButtonText(str);
    }

    private ActionListener getSelectBusinessComboBoxActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ignoreComboChange) {
                    return;
                }
                if (view.getSelectBusinessComboBox().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(view, "Please select a Business.");
                    return;
                }
                newBusinessId = view.getSelectBusinessComboBox().getSelectedItem().toString().split(",")[0];
                setStoresComboBoxByBussinesId(Integer.parseInt(newBusinessId));
                view.getSelectSellerComboBox().setEnabled(false);
            }
        };
        return al;
    }

    private ActionListener getSelectStoreComboBoxActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ignoreComboChange) {
                    return;
                }
                if (view.getSelectStoreComboBox().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(view, "Please select a Store.");
                    return;
                }
                newStoreId = view.getSelectStoreComboBox().getSelectedItem().toString().split(",")[0];
                setSellersComboBoxByStoreId(Integer.parseInt(newStoreId));
                view.getSelectSellerComboBox().setEnabled(true);
            }
        };
        return al;
    }

    private ActionListener getSelectSellerComboBoxActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ignoreComboChange) {
                    return;
                }
                if (view.getSelectSellerComboBox().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(view, "Please select a seller.");
                    return;
                }
                newSellerId = view.getSelectSellerComboBox().getSelectedItem().toString().split(",")[0];
            }
        };
        return al;
    }

    private ActionListener getSelectTruckComboBoxActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ignoreComboChange) {
                    return;
                }
                if (view.getSelectTruckComboBox().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(view, "Please select a truck.");
                    return;
                }
                newTruckId = view.getSelectTruckComboBox().getSelectedItem().toString().split(",")[0];
            }
        };
        return al;
    }
    
    private void setStoresComboBoxByBussinesId(int bussinesId) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            StoresDAO dao = new StoresDAO();
            ResultSet rs = dao.listStoresIdNameByBussinesId(bussinesId);
            while (rs.next()) {
                String storeId = String.valueOf(rs.getInt("store_id"));
                String name = rs.getString("store_name");
                String str = storeId + "," + name;
                model.addElement(str);
            }
            view.getSelectStoreComboBox().setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setSellersComboBoxByStoreId(int storeId) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            SellersDAO dao = new SellersDAO();
            ResultSet rs = dao.listSellersIdNameByStoreId(storeId);
            while (rs.next()) {
                String sellerId = String.valueOf(rs.getInt("seller_id"));
                String name = rs.getString("seller_name");
                String str = sellerId + "," + name;
                model.addElement(str);
            }
            view.getSelectSellerComboBox().setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setTextFields() {
        this.view.setAmountTextFieldText(amount);
        try {
            ClientsDAO dao = new ClientsDAO();
            ResultSet rs = dao.getClientNamePhone(Integer.parseInt(clientId));
            while (rs.next()) {
                this.view.setClientNameTextFieldText(rs.getString("name"));
                this.view.setClientPhoneNumberTextFieldText(rs.getString("phone"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void setComboBoxes() {
        DefaultComboBoxModel<String> businessModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> truckModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> storeModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> sellerModel = new DefaultComboBoxModel<>();
        try {
            BusinessDAO bdao = new BusinessDAO();
            ResultSet brs = bdao.listBusinessesIdName();
            int businessIndex = 0, storeIndex = 0, sellerIndex = 0, truckIndex = 0;
            int cont = 0;
            while (brs.next()) {
                String bId = String.valueOf(brs.getInt("business_id"));
                if (bId.equals(businessId)) {
                    businessIndex = cont;
                }
                cont++;
                String name = brs.getString("name");
                String str = bId + "," + name;
                businessModel.addElement(str);
            }
            StoresDAO sdao = new StoresDAO();
            ResultSet srs = sdao.listStoresIdNameByBussinesId(Integer.parseInt(this.businessId));
            cont = 0;
            while (srs.next()) {
                String sId = String.valueOf(srs.getInt("store_id"));
                if (sId.equals(storeId)) {
                    storeIndex = cont;
                }
                cont++;
                String name = srs.getString("store_name");
                String str = sId + "," + name;
                storeModel.addElement(str);
            }
            SellersDAO sedao = new SellersDAO();
            ResultSet sers = sedao.listSellersIdNameByStoreId(Integer.parseInt(this.storeId));
            cont = 0;
            while (sers.next()) {
                String seId = String.valueOf(sers.getInt("seller_id"));
                if (seId.equals(sellerId)) {
                    sellerIndex = cont;
                }
                cont++;
                String name = sers.getString("seller_name");
                String str = seId + "," + name;
                sellerModel.addElement(str);
            }
            TrucksDAO tdao = new TrucksDAO();
            ResultSet trs = tdao.listTrucksIdName();
            cont = 0;
            while (trs.next()) {
                String tId = String.valueOf(trs.getInt("truck_id"));
                if (tId.equals(truckId)) {
                    truckIndex = cont;
                }
                cont++;
                String name = trs.getString("name");
                String str = tId + "," + name;
                truckModel.addElement(str);
            }
            view.getSelectBusinessComboBox().setModel(businessModel);
            view.getSelectStoreComboBox().setModel(storeModel);
            view.getSelectSellerComboBox().setModel(sellerModel);
            view.getSelectTruckComboBox().setModel(truckModel);
            view.getSelectBusinessComboBox().setSelectedIndex(businessIndex);
            view.getSelectStoreComboBox().setSelectedIndex(storeIndex);
            view.getSelectSellerComboBox().setSelectedIndex(sellerIndex);
            view.getSelectTruckComboBox().setSelectedIndex(truckIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    

    private void resetEntries() {
        ignoreComboChange = true;
        this.setComboBoxes();
        this.setTextFields();
        this.view.setSelectDateButtonText(deliveryDate);
        ignoreComboChange = false;
    }
    
    private void updateSearchManageDeliveryNotesModel() {
        view2.clearDeliveryNotes();
        try {
            DeliveryNoteDAO deliveryNoteDao = new DeliveryNoteDAO();
            ClientsDAO clientsDao = new ClientsDAO();
            BusinessDAO businessDao = new BusinessDAO();
            StoresDAO storesDao = new StoresDAO();
            SellersDAO sellersDao = new SellersDAO();
            TrucksDAO trucksDao = new TrucksDAO();
            ResultSet rs = deliveryNoteDao.listDeliveryNotes();
            while (rs.next()) {
                String cName = null, cPhone = null, bName = null, sName = null, seName = null, tName = null;
                Vector row = new Vector();
                int cId = rs.getInt("client_id");
                ResultSet clientRs = clientsDao.getClientNamePhone(cId);
                while(clientRs.next()){
                    cName = clientRs.getString("name");
                    cPhone = clientRs.getString("phone");
                }
                int bId = rs.getInt("business_id");
                ResultSet businessRs = businessDao.getBusinessName(bId);
                while(businessRs.next()){
                    bName = businessRs.getString("name");
                }
                int sId = rs.getInt("store_id");
                ResultSet storesRs = storesDao.getStoreName(sId);
                while(storesRs.next()){
                    sName = storesRs.getString("name");
                }
                int seId = rs.getInt("seller_id");
                ResultSet sellersRs = sellersDao.getSellerName(seId);
                while(sellersRs.next()){
                    seName = sellersRs.getString("name");
                }
                int tId = rs.getInt("truck_id");
                ResultSet trucksRs = trucksDao.getTruckName(tId);
                while(trucksRs.next()){
                    tName = trucksRs.getString("name");
                }
                row.add(rs.getInt("delivery_note_id"));
                row.add(rs.getString("date"));
                row.add(rs.getString("delivery_date"));
                row.add(cId+","+cName);
                row.add(cPhone);
                row.add(bId+","+bName);
                row.add(sId+","+sName);
                row.add(seId+","+seName);
                row.add(tId+","+tName);
                row.add(rs.getDouble("amount"));
                row.add(rs.getString("pdf_path"));
                view2.addDeliveryNote(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view2.getSearchManageDeliveryNoteTable().getColumnCount(); i++) {
            view2.getSearchManageDeliveryNoteTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void innitComponents() {
        this.setComboBoxes();
        this.setTextFields();
        this.view.setSelectDateButtonText(deliveryDate);
        this.view.setSelectedPDFPathLabelText(pdfRoute);
        this.view.setDefaultCloseOperation();
        this.view.setTitle("Edit Delivery Note");
    }
}
