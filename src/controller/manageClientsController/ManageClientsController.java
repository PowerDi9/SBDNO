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
package controller.manageClientsController;

import controller.manageClientsController.editClientController.EditClientController;
import controller.manageClientsController.searchClientController.SearchClientsController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.ClientsDAO;

import view.manageClientsView.ManageClientsFrame;
import view.manageClientsView.editClientView.EditClientDialog;
import view.manageClientsView.searchClientsDialog.SearchClientsDialog;

public class ManageClientsController {                                          //Controller for the manage clients view

    ManageClientsFrame view;
    String clientId, clientName, phoneNumber = null;

    public ManageClientsController(ManageClientsFrame view) {
        this.view = view;
        this.view.addAddClientBackButtonAL(this.getBackButtonActionListener());
        this.view.addEditClientBackButtonAL(this.getBackButtonActionListener());
        this.view.addClearTextButtonAL(this.getClearTextButtonActionListener());
        this.view.addEditClientsTableMouseListener(this.getEditClientsTableMouseListener());
        this.view.addAddClientButtonAL(this.getAddClientsButtonActionListener());
        this.view.addDeleteClientButtonAL(this.getDeleteClientButtonActionListener());
        this.view.addEditClientButtonAL(this.getEditClientButtonActionListener());
        this.view.addSearchClientsButtonAL(this.getSearchButtonActionListener());
        this.innitcomponents();
    }

    private MouseListener getEditClientsTableMouseListener() {                  //Gives the edit clients table a mouse action
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {           //Gets the selected client data and sets the variables
                int row = view.getEditClientsJTable().rowAtPoint(evt.getPoint());
                clientId = view.getEditClientsTableIDAt(row, 0);
                clientName = view.getEditClientsTableIDAt(row, 1);
                phoneNumber = view.getEditClientsTableIDAt(row, 2);
            }
        };
        return ma;
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
            public void actionPerformed(ActionEvent e) {                        //Sets the text fields to blank
                view.setClientNameTextFieldText("");
                view.setPhoneNumberTextFieldText("");
            }
        };
        return al;
    }

    private ActionListener getAddClientsButtonActionListener() {                //Gives the add client button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Adds the client to the DB with the provided data
                String name = view.getClientNameTextFieldText();
                String phoneNumber = view.getPhoneNumberTextFieldText();
                try {
                    ClientsDAO dao = new ClientsDAO();
                    if (dao.clientExists(name)) {
                        int option = JOptionPane.showConfirmDialog(null, "The Client \"" + name + "\" already exists.\nCreate it anyway?", "Confirm Duplicate", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                    if (dao.insertClient(name, phoneNumber)) {
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditClientsModel();
            }
        };
        return al;
    }

    private ActionListener getDeleteClientButtonActionListener() {              //Gives the delete client an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Deletes the selected client on the table
                try {
                    if (clientId == null) {
                        JOptionPane.showMessageDialog(view, "Please select a client to delete.");
                        return;
                    } else {
                        int option = JOptionPane.showConfirmDialog(null, "Are you sure to delete " + clientName + "?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
                    }
                    ClientsDAO dao = new ClientsDAO();
                    dao.deleteClient(clientId);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditClientsModel();
            }
        };
        return al;
    }

    private ActionListener getSearchButtonActionListener() {                    //Gives the search button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Launches the search clients dialog
                SearchClientsDialog scd = new SearchClientsDialog(view, true);
                SearchClientsController ecc = new SearchClientsController(scd, view);
                scd.setLocationRelativeTo(view);
                scd.setVisible(true);
            }
        };
        return al;
    }

    private ActionListener getEditClientButtonActionListener() {                //Gives the edit button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Launches the edit client dialog
                if (clientId == null) {
                    JOptionPane.showMessageDialog(view, "Please select a client to edit.");
                    return;
                }
                EditClientDialog ecd = new EditClientDialog(view, true);
                EditClientController ecc = new EditClientController(ecd, view, clientId, clientName, phoneNumber);
                ecd.setLocationRelativeTo(view);
                ecd.setVisible(true);
            }
        };
        return al;
    }

    private void updateEditClientsModel() {                                     //Updates the edit client table
        view.clearClients();
        try {
            ClientsDAO dao = new ClientsDAO();
            ResultSet rs = dao.listClients();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("client_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("phone"));
                view.addClient(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view.getEditClientsJTable().getColumnCount(); i++) {
            view.getEditClientsJTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void setIcon() {                                                     //Sets the application icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    private void innitcomponents() {                                            //Initializes the component
        this.setIcon();
        view.setTitle("Manage Clients");
        view.setSetDefaultCloseOperation();
        this.updateEditClientsModel();
    }
}
