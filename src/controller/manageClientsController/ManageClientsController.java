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
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.ClientsDAO;

import view.manageClientsView.ManageClientsFrame;
import view.manageClientsView.editClientView.EditClientDialog;
import view.manageClientsView.searchClientsDialog.SearchClientsDialog;

public class ManageClientsController {

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

    private MouseListener getEditClientsTableMouseListener() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = view.getEditClientsJTable().rowAtPoint(evt.getPoint());
                clientId = view.getEditClientsTableIDAt(row, 0);
                clientName = view.getEditClientsTableIDAt(row, 1);
                phoneNumber = view.getEditClientsTableIDAt(row, 2);
            }
        };
        return ma;
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
                view.setClientNameTextFieldText("");
                view.setPhoneNumberTextFieldText("");
            }
        };
        return al;
    }

    private ActionListener getAddClientsButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = view.getClientNameTextFieldText();
                String phoneNumber = view.getPhoneNumberTextFieldText();
                try {
                    ClientsDAO dao = new ClientsDAO();
                    if (dao.clientExists(name)) {
                        int option = JOptionPane.showConfirmDialog(null, "The Client \"" + name + "\" already exists.\nCreate it anyway?", "Confirm Duplicate", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.NO_OPTION) {
                            System.out.println("Operation cancelled.");
                            return;
                        }
                    }
                    if (dao.insertClient(name, phoneNumber)) {
                        System.out.println("Client added successfully");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditClientsModel();
            }
        };
        return al;
    }

    private ActionListener getDeleteClientButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

    private ActionListener getSearchButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchClientsDialog scd = new SearchClientsDialog(view, true);
                SearchClientsController ecc = new SearchClientsController(scd, view);
                scd.setLocationRelativeTo(view);
                scd.setVisible(true);
            }
        };
        return al;
    }

    private ActionListener getEditClientButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

    private void updateEditClientsModel() {
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

    private void innitcomponents() {
        view.setTitle("Manage Businesses");
        view.setSetDefaultCloseOperation();
        this.updateEditClientsModel();
    }
}
