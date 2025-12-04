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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.BusinessDAO;
import model.dao.StoresDAO;
import view.manageStoresView.ManageStoresFrame;
import view.manageStoresView.editStoreView.EditStoreDialog;

public class ManageStoresController {                                           //Controller for the manage stores view

    ManageStoresFrame view;
    String storeId, businessId, businessName, name = null;

    public ManageStoresController(ManageStoresFrame view) {
        this.view = view;
        this.view.addAddStoreBackButtonAL(this.getBackButtonActionListener());
        this.view.addEditStoresBackButtonAL(this.getBackButtonActionListener());
        this.view.addClearTextButtonAL(this.getClearTextButtonActionListener());
        this.view.addEditStoresTableMouseListener(this.getEditStoresTableMouseListener());
        this.view.addAddStoreButtonAL(this.getAddStoreButtonActionListener());
        this.view.addDeleteStoreButtonAL(this.getDeleteStoreButtonActionListener());
        this.view.addEditStoreButtonAL(this.getEditStoreActionListener());
        this.innitcomponents();
    }
    
    private MouseListener getEditStoresTableMouseListener() {                   //Gives the edit stores table a mouse action
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

    private void setSelectBusinessComboBoxModel() {                             //Sets the select business combo box model
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

    private ActionListener getBackButtonActionListener() {                      //Gives the back button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }
    
    private ActionListener getClearTextButtonActionListener() {                 //Gives the clear text button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Sets the store name text field to blank
                view.setStoreNameTextFieldText("");
            }
        };
        return al;
    }
    
    private ActionListener getAddStoreButtonActionListener() {                  //Gives the add store button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Adds the store to the store table with the provided information
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
    
    private ActionListener getDeleteStoreButtonActionListener() {               //Gives the delete store button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Deletes the selected store in the edit store table
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
    
    private ActionListener getEditStoreActionListener() {                       //Gives the edit store button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Launches the edit store dialog with the information of the selected store in the edit store table
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

    private void updateEditStoresModel() {                                      //Updates the edit store table
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
    
    public void setIcon(){                                                      //Sets the application Icon
         ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
            view.setIconImage(icon.getImage());
    }

    private void innitcomponents() {                                            //Initializes the components
        this.setIcon();
        view.setTitle("Manage Stores");
        view.setSetDefaultCloseOperation();
        this.updateEditStoresModel();
        this.setSelectBusinessComboBoxModel();
    }
}
