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
package controller.manageStoresController.editStoreController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.dao.StoresDAO;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.BusinessDAO;
import view.manageStoresView.ManageStoresFrame;
import view.manageStoresView.editStoreView.EditStoreDialog;

public class EditStoreController {                                              //Controller for the edit store dialog

    EditStoreDialog view = null;
    ManageStoresFrame view2 = null;
    String storeId, businessId, storeName = null;

    public EditStoreController(EditStoreDialog view, ManageStoresFrame view2, String storeId, String businessId, String storeName) {
        this.view = view;
        this.view2 = view2;
        this.storeId = storeId;
        this.businessId = businessId;
        this.storeName = storeName;
        this.view.addAcceptButtonAL(this.getAcceptButtonActionListener());
        this.view.addCancelButtonAL(this.getCancelButtonActionListener());
        this.initComponents();
    }

    private ActionListener getAcceptButtonActionListener() {                     //Gives the accept button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Updates the information of the selected store with the provided information
                try {
                    businessId = view.getSelectBusinessComboBox().getSelectedItem().toString().split(",")[0];
                    StoresDAO dao = new StoresDAO();
                    dao.editStore(storeId, businessId, view.getStoreNameTextFieldText());
                    updateEditStoresModel();
                    System.out.println("Edited Correctly");
                    view.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        };
        return al;
    }

    private ActionListener getCancelButtonActionListener() {                     //Gives the cancel button an action
        ActionListener al = new ActionListener() {  
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    private void updateEditStoresModel() {                                       //Updates the edit stores table
        view2.clearStores();
        try {
            StoresDAO dao = new StoresDAO();
            ResultSet rs = dao.listStores();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("business_id"));
                row.add(rs.getInt("store_id"));
                row.add(rs.getString("business_name"));
                row.add(rs.getString("store_name"));
                view2.addBusiness(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view2.getEditStoresTable().getColumnCount(); i++) {
            view2.getEditStoresTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void setSelectBusinessComboBoxModel() {                             //Sets the select business combo box 
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            BusinessDAO dao = new BusinessDAO();
            ResultSet rs = dao.listBusinessesIdName();
            int index = 0;
            int con = 0;
            while (rs.next()) {
                String businessId = String.valueOf(rs.getInt("business_id"));
                String name = rs.getString("name");
                String str = businessId + "," + name;
                if (Integer.parseInt(this.businessId) == Integer.parseInt(businessId)) {
                    index = con;
                }
                model.addElement(str);
                con++;
            }
            view.getSelectBusinessComboBox().setModel(model);
            view.getSelectBusinessComboBox().setSelectedIndex(index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setIcon() {                                                     //Sets the application Icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    private void initComponents() {                                             //Initializes the components
        this.setIcon();
        view.setStoreNameTextFieldText(storeName);
        view.setTitle("Edit Store");
        this.setSelectBusinessComboBoxModel();
    }
}