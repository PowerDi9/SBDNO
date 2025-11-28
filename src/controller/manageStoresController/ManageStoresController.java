package controller.manageStoresController;

import controller.manageStoresController.editStoreController.EditStoreController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.BusinessDAO;
import model.dao.StoresDAO;
import view.manageStoresView.ManageStoresFrame;
import view.manageStoresView.editStoreView.EditStoreDialog;

public class ManageStoresController {

    ManageStoresFrame view;
    String storeId, businessId, businessName, name = null;

    public ManageStoresController(ManageStoresFrame view) {
        this.view = view;
        this.view.addAddStoreBackButtonAL(this.getBackButtonActionListener());
        this.view.addEditStoresBackButtonAL(this.getBackButtonActionListener());
        this.view.addClearTextButtonAL(this.getClearTextButtonActionListener());
        this.view.addEditStoresTableMouseListener(this.getEditStoresTableMouseListener());
        this.view.addAddStoreButtonAL(this.getAddBusinessButtonActionListener());
        this.view.addDeleteStoreButtonAL(this.getDeleteBusinessButtonActionListener());
        this.view.addEditStoreButtonAL(this.getEditStoreActionListener());
        this.innitcomponents();
    }
    
    private MouseListener getEditStoresTableMouseListener() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = view.getEditStoresTable().rowAtPoint(evt.getPoint());
                businessId = view.getEditStoresTableIDAt(row, 0);
                storeId = view.getEditStoresTableIDAt(row, 1);
                businessName = view.getEditStoresTableIDAt(row, 2);
                name = view.getEditStoresTableIDAt(row, 3);
            }
        };
        return ma;
    }

    private void setSelectBusinessComboBoxModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            BusinessDAO dao = new BusinessDAO();
            ResultSet rs = dao.listBusinessesIdName();
            while (rs.next()) {
                String businessId = String.valueOf(rs.getInt("business_id"));
                String name = rs.getString("name");
                String str = businessId + "," + name;
                model.addElement(str);
            }
            view.getSelectBusinessComboBox().setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    
    private ActionListener getClearTextButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setStoreNameTextFieldText("");
            }
        };
        return al;
    }
    
    private ActionListener getAddBusinessButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = view.getStoreNameTextFieldText();
                int businessId = Integer.parseInt(view.getSelectBusinessComboBox().getSelectedItem().toString().split(",")[0]);
                try {
                    StoresDAO dao = new StoresDAO();
                    if (dao.storeExists(name)) {
                        int option = JOptionPane.showConfirmDialog(null, "The Store \"" + name + "\" already exists.\nCreate it anyway?", "Confirm Duplicate", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.NO_OPTION) {
                            System.out.println("Operation cancelled.");
                            return;
                        }
                    }
                    if (dao.insertStore(businessId, name)) {
                        System.out.println("Store added successfully");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditStoresModel();
            }
        };
        return al;
    }
    
    private ActionListener getDeleteBusinessButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(storeId == null){
                    JOptionPane.showMessageDialog(view, "Please select a Store to delete.");
                    return;
                }else{
                        int option = JOptionPane.showConfirmDialog(null, "Are you sure to delete "+name+"?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
                    }
                    StoresDAO dao = new StoresDAO();
                    dao.deleteStore(storeId);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditStoresModel();
            }
        };
        return al;
    }
    
    private ActionListener getEditStoreActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(storeId == null){
                    JOptionPane.showMessageDialog(view, "Please select a Store to edit.");
                    return;
                }
                EditStoreDialog esd = new EditStoreDialog(view, true);
                EditStoreController esc = new EditStoreController(esd, view, storeId, businessId, name);
                esd.setLocationRelativeTo(view);
                esd.setVisible(true);
            }
        };
        return al;
    }

    private void updateEditStoresModel() {
        view.clearStores();
        try {
            StoresDAO dao = new StoresDAO();
            ResultSet rs = dao.listStores();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("business_id"));
                row.add(rs.getInt("store_id"));
                row.add(rs.getString("business_name"));
                row.add(rs.getString("store_name"));
                view.addBusiness(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view.getEditStoresTable().getColumnCount(); i++) {
            view.getEditStoresTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void innitcomponents() {
        view.setTitle("Manage Businesses");
        view.setSetDefaultCloseOperation();
        this.updateEditStoresModel();
        this.setSelectBusinessComboBoxModel();
    }
}
