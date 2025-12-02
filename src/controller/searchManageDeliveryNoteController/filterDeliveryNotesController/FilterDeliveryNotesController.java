package controller.searchManageDeliveryNoteController.filterDeliveryNotesController;

import controller.searchManageDeliveryNoteController.filterDeliveryNotesController.selectAdditionFromDateController.SelectAdditionFromDateController;
import controller.searchManageDeliveryNoteController.filterDeliveryNotesController.selectAdditionUntilDateController.SelectAdditionUntilDateController;
import controller.searchManageDeliveryNoteController.filterDeliveryNotesController.selectDeliveryFromDateController.SelectDeliveryFromDateController;
import controller.searchManageDeliveryNoteController.filterDeliveryNotesController.selectDeliveryUntilDateController.SelectDeliveryUntilDateController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import model.dao.BusinessDAO;
import model.dao.SellersDAO;
import model.dao.StoresDAO;
import model.dao.TrucksDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.ImageIcon;
import model.dao.ClientsDAO;
import model.dao.DeliveryNoteDAO;
import view.addDeliveryNoteView.selectDateDialog.SelectDateDialog;
import view.searchManageDeliveryNoteView.SearchManageDeliveryNoteFrame;
import view.searchManageDeliveryNoteView.filterDeliveryNotesView.FilterDeliveryNotesDialog;

public class FilterDeliveryNotesController {

    FilterDeliveryNotesDialog view;
    SearchManageDeliveryNoteFrame view2;
    Integer businessId, storeId, sellerId, truckId = null;
    String additionFromDate, additionUntilDate, deliveryFromDate, deliveryUntilDate, clientName, clientPhone = null;
    Double minAmount, maxAmount = null;

    public FilterDeliveryNotesController(FilterDeliveryNotesDialog view, SearchManageDeliveryNoteFrame view2) {
        this.view = view;
        this.view2 = view2;
        this.innitComponents();
        this.view.addBackButtonAL(this.getBackButtonActionListener());
        this.view.addClearEntriesButtonAL(this.getClearEntriesButtonActionListener());
        this.view.addSelectBusinesComboBoxAL(this.getSelectBusinessComboBoxActionListener());
        this.view.addSelectStoreComboBoxAL(this.getSelectStoreComboBoxActionListener());
        this.view.addSelectAdditionFromDateButtonAL(this.getSelectAdditionFromDateButtonActionListener());
        this.view.addSelectAdditionUntilDateButtonAL(this.getSelectAdditionUntilDateButtonActionListener());
        this.view.addSelectDeliveryFromDateButtonAL(this.getSelectDeliveryFromDateButtonActionListener());
        this.view.addSelectDeliveryUntilDateButtonAL(this.getSelectDeliveryUntilDateButtonActionListener());
        this.view.addFilterDeliveryNotesButtonAL(this.getFilterDeliveryNotesButtonActionListener());
    }

    private ActionListener getFilterDeliveryNotesButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.getSelectBusinessComboBox().getSelectedItem() != null && view.getSelectBusinessComboBox().getSelectedIndex() > 0) {
                    businessId = Integer.parseInt(view.getSelectBusinessComboBox().getSelectedItem().toString().split(",")[0]);
                }
                if (view.getSelectStoreComboBox().getSelectedItem() != null && view.getSelectStoreComboBox().getSelectedIndex() > 0) {
                    storeId = Integer.parseInt(view.getSelectStoreComboBox().getSelectedItem().toString().split(",")[0]);
                }
                if (view.getSelectSellerComboBox().getSelectedItem() != null && view.getSelectSellerComboBox().getSelectedIndex() > 0) {
                    sellerId = Integer.parseInt(view.getSelectSellerComboBox().getSelectedItem().toString().split(",")[0]);
                }
                if (view.getSelectTruckComboBox().getSelectedItem() != null && view.getSelectTruckComboBox().getSelectedIndex() > 0) {
                    truckId = Integer.parseInt(view.getSelectTruckComboBox().getSelectedItem().toString().split(",")[0]);
                }
                if (!view.getMinAmountTextFieldText().isEmpty()) {
                    minAmount = Double.parseDouble(view.getMinAmountTextFieldText());
                }
                if (!view.getMaxAmountTextFieldText().isEmpty()) {
                    maxAmount = Double.parseDouble(view.getMaxAmountTextFieldText());
                }
                clientName = view.getClientNameTextFieldText();
                clientPhone = view.getClientPhoneNumberTextFieldText();
                try {
                    DeliveryNoteDAO dao = new DeliveryNoteDAO();
                    ClientsDAO clientsDao = new ClientsDAO();
                    BusinessDAO businessDao = new BusinessDAO();
                    StoresDAO storesDao = new StoresDAO();
                    SellersDAO sellersDao = new SellersDAO();
                    TrucksDAO trucksDao = new TrucksDAO();
                    view2.clearDeliveryNotes();
                    ResultSet rs = dao.filterDeliveryNotes(additionFromDate, additionUntilDate, deliveryFromDate, deliveryUntilDate, clientName, clientPhone, sellerId, businessId, storeId, truckId, minAmount, maxAmount);
                    while (rs.next()) {
                        String cName = null, cPhone = null, bName = null, sName = null, seName = null, tName = null;
                        Vector row = new Vector();
                        int cId = rs.getInt("client_id");
                        ResultSet clientRs = clientsDao.getClientNamePhone(cId);
                        while (clientRs.next()) {
                            cName = clientRs.getString("name");
                            cPhone = clientRs.getString("phone");
                        }
                        int bId = rs.getInt("business_id");
                        ResultSet businessRs = businessDao.getBusinessName(bId);
                        while (businessRs.next()) {
                            bName = businessRs.getString("name");
                        }
                        int sId = rs.getInt("store_id");
                        ResultSet storesRs = storesDao.getStoreName(sId);
                        while (storesRs.next()) {
                            sName = storesRs.getString("name");
                        }
                        int seId = rs.getInt("seller_id");
                        ResultSet sellersRs = sellersDao.getSellerName(seId);
                        while (sellersRs.next()) {
                            seName = sellersRs.getString("name");
                        }
                        int tId = rs.getInt("truck_id");
                        ResultSet trucksRs = trucksDao.getTruckName(tId);
                        while (trucksRs.next()) {
                            tName = trucksRs.getString("name");
                        }
                        row.add(rs.getInt("delivery_note_id"));
                        row.add(rs.getString("date"));
                        row.add(rs.getString("delivery_date"));
                        row.add(cId + "," + cName);
                        row.add(cPhone);
                        row.add(bId + "," + bName);
                        row.add(sId + "," + sName);
                        row.add(seId + "," + seName);
                        row.add(tId + "," + tName);
                        row.add(rs.getDouble("amount"));
                        row.add(rs.getString("pdf_path"));
                        view2.addDeliveryNote(row);
                    }
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

    private ActionListener getClearEntriesButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearEntries();
            }
        };
        return al;
    }

    private ActionListener getSelectAdditionFromDateButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FilterDeliveryNotesController ctrl = FilterDeliveryNotesController.this;
                SelectDateDialog sdd = new SelectDateDialog(view, true);
                SelectAdditionFromDateController safdc = new SelectAdditionFromDateController(sdd, ctrl);
                sdd.setLocationRelativeTo(view);
                sdd.setVisible(true);
            }
        };
        return al;
    }

    private ActionListener getSelectAdditionUntilDateButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FilterDeliveryNotesController ctrl = FilterDeliveryNotesController.this;
                SelectDateDialog sdd = new SelectDateDialog(view, true);
                SelectAdditionUntilDateController saudc = new SelectAdditionUntilDateController(sdd, ctrl);
                sdd.setLocationRelativeTo(view);
                sdd.setVisible(true);
            }
        };
        return al;
    }

    private ActionListener getSelectDeliveryFromDateButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FilterDeliveryNotesController ctrl = FilterDeliveryNotesController.this;
                SelectDateDialog sdd = new SelectDateDialog(view, true);
                SelectDeliveryFromDateController saudc = new SelectDeliveryFromDateController(sdd, ctrl);
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
                FilterDeliveryNotesController ctrl = FilterDeliveryNotesController.this;
                SelectDateDialog sdd = new SelectDateDialog(view, true);
                SelectDeliveryUntilDateController saudc = new SelectDeliveryUntilDateController(sdd, ctrl);
                sdd.setLocationRelativeTo(view);
                sdd.setVisible(true);
            }
        };
        return al;
    }

    private ActionListener getSelectBusinessComboBoxActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultComboBoxModel<String> emptyModel = new DefaultComboBoxModel<>();
                emptyModel.addElement(null);
                if (view.getSelectBusinessComboBox().getSelectedItem() == null) {
                    view.getSelectStoreComboBox().setModel(emptyModel);
                    view.getSelectStoreComboBox().setEnabled(false);
                    view.getSelectSellerComboBox().setModel(emptyModel);
                    view.getSelectSellerComboBox().setEnabled(false);
                } else {
                    setStoresComboBoxByBussinesId(Integer.parseInt(view.getSelectBusinessComboBox().getSelectedItem().toString().split(",")[0]));
                    view.getSelectStoreComboBox().setEnabled(true);
                    view.getSelectSellerComboBox().setModel(emptyModel);
                    view.getSelectSellerComboBox().setEnabled(false);
                }
            }
        };
        return al;
    }

    private ActionListener getSelectStoreComboBoxActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultComboBoxModel<String> emptyModel = new DefaultComboBoxModel<>();
                emptyModel.addElement(null);
                if (view.getSelectStoreComboBox().getSelectedItem() == null) {
                    view.getSelectSellerComboBox().setModel(emptyModel);
                    view.getSelectSellerComboBox().setEnabled(false);
                } else {
                    setSellersComboBoxByStoreId(Integer.parseInt(view.getSelectStoreComboBox().getSelectedItem().toString().split(",")[0]));
                    view.getSelectSellerComboBox().setEnabled(true);
                }
            }
        };
        return al;
    }

    public void setAdditionFromDate(String str) {
        this.additionFromDate = str;
        this.view.setSelectAdditionFromDateText(str);
    }

    public void setAdditionUntilDate(String str) {
        this.additionUntilDate = str;
        this.view.setSelectAdditionUntilDateText(str);
    }

    public void setDeliveryFromDate(String str) {
        this.deliveryFromDate = str;
        this.view.setSelectDeliveryFromDateText(str);
    }

    public void setDeliveryUntilDate(String str) {
        this.deliveryUntilDate = str;
        this.view.setSelectDeliveryUntilDateText(str);
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
        view.setMaxAmountTextFieldText("");
        view.setMinAmountTextFieldText("");
        view.setClientNameTextFieldText("");
        view.setClientPhoneNumberTextFieldText("");
        businessId = storeId = sellerId = truckId = null;
        additionFromDate = additionUntilDate = deliveryFromDate = deliveryUntilDate = null;
        view.setSelectAdditionFromDateText("Select Date");
        view.setSelectAdditionUntilDateText("Select Date");
        view.setSelectDeliveryFromDateText("Select Date");
        view.setSelectDeliveryUntilDateText("Select Date");
        setComboBoxes();
    }

    public void setIcon() {
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    private void innitComponents() {
        this.setIcon();
        this.setComboBoxes();
        this.view.setTitle("Filter Delivery Notes");
    }
}
