package controller.manageSellersController;

import controller.manageSellersController.editSellerController.EditSellerController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import javax.swing.DefaultComboBoxModel;
import model.dao.SellersDAO;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.StoresDAO;
import view.manageSellersView.ManageSellersFrame;
import view.manageSellersView.editSellerView.EditSellerDialog;

public class ManageSellersController {
    
    ManageSellersFrame view;
    String sellerId, storeId, storeName, name = null;

    public ManageSellersController(ManageSellersFrame view) {
        this.view = view;
        this.view.addAddSellerBackButtonAL(this.getBackButtonActionListener());
        this.view.addEditSellersBackButtonAL(this.getBackButtonActionListener());
        this.view.addClearTextButtonAL(this.getClearTextButtonActionListener());
        this.view.addEditSellersTableMouseListener(this.getEditSellersTableMouseListener());
        this.view.addAddSellerButtonAL(this.getAddSellerButtonActionListener());
        this.view.addDeleteSellerButtonAL(this.getDeleteSellerButtonActionListener());
        this.view.addEditSellerButtonAL(this.getEditSellerActionListener());
        this.innitcomponents();
    }
    
    private MouseListener getEditSellersTableMouseListener() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = view.getEditSellersTable().rowAtPoint(evt.getPoint());
                storeId = view.getEditSellersTableIDAt(row, 0);
                sellerId = view.getEditSellersTableIDAt(row, 1);
                storeName = view.getEditSellersTableIDAt(row, 2);
                name = view.getEditSellersTableIDAt(row, 3);
            }
        };
        return ma;
    }

    private void setSelectStoreComboBoxModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            StoresDAO dao = new StoresDAO();
            ResultSet rs = dao.listStoresIdName();
            while (rs.next()) {
                String storeId = String.valueOf(rs.getInt("store_id"));
                String name = rs.getString("name");
                String str = storeId + "," + name;
                model.addElement(str);
            }
            view.getSelectStoreComboBox().setModel(model);
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
                view.setSellerNameTextFieldText("");
            }
        };
        return al;
    }
    
    private ActionListener getAddSellerButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = view.getSellerNameTextFieldText();
                int storeId = Integer.parseInt(view.getSelectStoreComboBox().getSelectedItem().toString().split(",")[0]);
                try {
                    SellersDAO dao = new SellersDAO();
                    if (dao.sellerExists(name)) {
                        int option = JOptionPane.showConfirmDialog(null, "The seller \"" + name + "\" already exists.\nCreate it anyway?", "Confirm Duplicate", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.NO_OPTION) {
                            System.out.println("Operation cancelled.");
                            return;
                        }
                    }
                    if (dao.insertSeller(storeId, name)) {
                        System.out.println("Seller added successfully");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditSellersModel();
            }
        };
        return al;
    }
    
    private ActionListener getDeleteSellerButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(sellerId == null){
                    JOptionPane.showMessageDialog(view, "Please select a seller to delete.");
                    return;
                }else{
                        int option = JOptionPane.showConfirmDialog(null, "Are you sure to delete "+name+"?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
                    }
                    SellersDAO dao = new SellersDAO();
                    dao.deleteSeller(sellerId);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditSellersModel();
            }
        };
        return al;
    }
    
    private ActionListener getEditSellerActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(storeId == null){
                    JOptionPane.showMessageDialog(view, "Please select a seller to edit.");
                    return;
                }
                EditSellerDialog esd = new EditSellerDialog(view, true);
                EditSellerController esc = new EditSellerController(esd, view, sellerId, storeId, name);
                esd.setLocationRelativeTo(view);
                esd.setVisible(true);
            }
        };
        return al;
    }

    private void updateEditSellersModel() {
        view.clearSellers();
        try {
            SellersDAO dao = new SellersDAO();
            ResultSet rs = dao.listSellers();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("store_id"));
                row.add(rs.getInt("seller_id"));
                row.add(rs.getString("store_name"));
                row.add(rs.getString("seller_name"));
                view.addSeller(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view.getEditSellersTable().getColumnCount(); i++) {
            view.getEditSellersTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void innitcomponents() {
        view.setTitle("Manage Businesses");
        view.setSetDefaultCloseOperation();
        this.updateEditSellersModel();
        this.setSelectStoreComboBoxModel();
    }
}
