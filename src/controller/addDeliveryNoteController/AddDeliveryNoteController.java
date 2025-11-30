package controller.addDeliveryNoteController;

import controller.addDeliveryNoteController.selectDateController.SelectDateController;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.dao.BusinessDAO;
import model.dao.ClientsDAO;
import model.dao.DeliveryNoteDAO;
import model.dao.SellersDAO;
import model.dao.StoresDAO;
import model.dao.TrucksDAO;
import view.addDeliveryNoteView.AddDeliveryNoteFrame;
import view.addDeliveryNoteView.selectDateDialog.SelectDateDialog;

public class AddDeliveryNoteController {

    AddDeliveryNoteFrame view;
    boolean ignoreComboChange = false;
    int businessId, storeId, sellerId, truckId, clientId;
    String deliveryDate, pdfRoute, date = null;
    Double amount = null;

    public AddDeliveryNoteController(AddDeliveryNoteFrame view) {
        this.view = view;
        this.innitComponents();
        this.view.addBackButtonAL(this.getBackButtonActionListener());
        this.view.addClearEntriesButtonAL(this.getClearEntriesButtonActionListener());
        this.view.addSelectBusinesComboBoxAL(this.getSelectBusinessComboBoxActionListener());
        this.view.addSelectStoreComboBoxAL(this.getSelectStoreComboBoxActionListener());
        this.view.addSelectSellerComboBoxAL(this.getSelectSellerComboBoxActionListener());
        this.view.addSelectTruckComboBoxAL(this.getSelectTruckComboBoxActionListener());
        this.view.addSelectDateButtonAL(this.getSelectDateButtonActionListener());
        this.view.addSelectPDFButtonAL(this.getSelectPDFBUttonActionListener());
        this.view.addAddDeliveryNoteButtonAL(this.getAddDeliveryNoteActionListener());
    }

    private ActionListener getAddDeliveryNoteActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                date = LocalDate.now().toString().replaceAll("-", "/");
                if (businessId == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a business.");
                    return;
                } else if (storeId == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a store.");
                    return;
                } else if (sellerId == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a seller.");
                    return;
                } else if (truckId == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a Truck.");
                    return;
                } else if (deliveryDate == null) {
                    JOptionPane.showMessageDialog(view, "Please select a Delivery Date.");
                    return;
                } else if (pdfRoute == null) {
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
                    amount = Double.parseDouble(view.getAmountTextFieldText().replaceAll(",", "."));
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Please introduce a valid number.");
                    return;
                }
                String clientName = view.getClientNameTextFieldText();
                String clientPhoneNumber = view.getClientPhoneNumberTextFieldText();
                try {
                    ClientsDAO cdao = new ClientsDAO();
                    clientId = cdao.returnGeneratedKeyInsertClient(clientName, clientPhoneNumber);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    DeliveryNoteDAO dndao = new DeliveryNoteDAO();
                    dndao.insertDeliveryNote(date, deliveryDate, amount, clientId, sellerId, businessId, storeId, truckId, pdfRoute);
                } catch (SQLException ex) {
                    System.getLogger(AddDeliveryNoteController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
                JOptionPane.showMessageDialog(view, "DeliveryNote added correctly");

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

    private ActionListener getClearEntriesButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearEntries();
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
                String newPDFRute = null;
                if (selection == JFileChooser.APPROVE_OPTION) {
                    newPDFRute = fc.getSelectedFile().getAbsolutePath();
                    if(!newPDFRute.endsWith(".pdf")){
                        JOptionPane.showMessageDialog(view, "Please select a PDF file.");
                        return;
                    }
                    JOptionPane.showMessageDialog(view, "Selected PDF: " + newPDFRute);
                    System.out.println("Selected PDF: " + newPDFRute);
                } else {
                    JOptionPane.showMessageDialog(view, "No PDF was selected");
                }
                pdfRoute = newPDFRute;
            }
        };
        return al;
    }

    private ActionListener getSelectDateButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDeliveryNoteController controladorActual = AddDeliveryNoteController.this;
                SelectDateDialog sdd = new SelectDateDialog(view, true);
                SelectDateController sdc = new SelectDateController(sdd, controladorActual);
                sdd.setLocationRelativeTo(view);
                sdd.setVisible(true);
            }
        };
        return al;
    }

    public void setDeliveryDate(String str) {
        this.deliveryDate = str;
        this.view.setSelectDateButtonText(str);
    }

    private ActionListener getSelectBusinessComboBoxActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ignoreComboChange) {
                    return;
                }
                if (view.getSelectBusinessComboBox().getSelectedItem() == null || view.getSelectBusinessComboBox().getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a Business.");
                    return;
                }
                businessId = Integer.parseInt(view.getSelectBusinessComboBox().getSelectedItem().toString().split(",")[0]);
                setStoresComboBoxByBussinesId(businessId);
                view.getSelectStoreComboBox().setEnabled(true);
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
                if (view.getSelectStoreComboBox().getSelectedItem() == null || view.getSelectStoreComboBox().getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a Store.");
                    return;
                }
                storeId = Integer.parseInt(view.getSelectStoreComboBox().getSelectedItem().toString().split(",")[0]);
                setSellersComboBoxByStoreId(storeId);
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
                if (view.getSelectSellerComboBox().getSelectedItem() == null || view.getSelectSellerComboBox().getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a seller.");
                    return;
                }
                sellerId = Integer.parseInt(view.getSelectSellerComboBox().getSelectedItem().toString().split(",")[0]);
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
                if (view.getSelectTruckComboBox().getSelectedItem() == null || view.getSelectTruckComboBox().getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a truck.");
                    return;
                }
                truckId = Integer.parseInt(view.getSelectTruckComboBox().getSelectedItem().toString().split(",")[0]);
            }
        };
        return al;
    }

    private void setStoresComboBoxByBussinesId(int bussinesId) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement(null);
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
        model.addElement(null);
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

    private void setComboBoxes() {
        DefaultComboBoxModel<String> businessModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> truckModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> emptyModel = new DefaultComboBoxModel<>();
        emptyModel.addElement(null);
        try {
            BusinessDAO bdao = new BusinessDAO();
            ResultSet brs = bdao.listBusinessesIdName();
            businessModel.addElement(null);
            while (brs.next()) {
                String businessId = String.valueOf(brs.getInt("business_id"));
                String name = brs.getString("name");
                String str = businessId + "," + name;
                businessModel.addElement(str);
            }
            TrucksDAO tdao = new TrucksDAO();
            ResultSet trs = tdao.listTrucksIdName();
            truckModel.addElement(null);
            while (trs.next()) {
                String truckId = String.valueOf(trs.getInt("truck_id"));
                String name = trs.getString("name");
                String str = truckId + "," + name;
                truckModel.addElement(str);
            }
            view.getSelectBusinessComboBox().setModel(businessModel);
            view.getSelectTruckComboBox().setModel(truckModel);
            view.getSelectBusinessComboBox().setSelectedIndex(0);
            view.getSelectTruckComboBox().setSelectedIndex(0);
            view.getSelectStoreComboBox().setModel(emptyModel);
            view.getSelectStoreComboBox().setEnabled(false);
            view.getSelectSellerComboBox().setModel(emptyModel);
            view.getSelectSellerComboBox().setEnabled(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearEntries() {
        view.setAmountTextFieldText("");
        view.setClientNameTextFieldText("");
        view.setClientPhoneNumberTextFieldText("");
        ignoreComboChange = true;
        businessId = storeId = sellerId = truckId = 0;
        deliveryDate = pdfRoute = null;
        view.setSelectDateButtonText("Select Date");
        setComboBoxes();
        ignoreComboChange = false;
    }

    private void innitComponents() {
        this.setComboBoxes();
        this.view.setDefaultCloseOperation();
        this.view.setTitle("Add Delivery Note");
    }
}
