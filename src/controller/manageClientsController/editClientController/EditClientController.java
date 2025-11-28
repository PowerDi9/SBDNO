package controller.manageClientsController.editClientController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.dao.ClientsDAO;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import view.manageClientsView.ManageClientsFrame;
import view.manageClientsView.editClientView.EditClientDialog;

public class EditClientController {
    EditClientDialog view = null;
    ManageClientsFrame view2 = null;
    String id, clientName, phoneNumber = null;

    public EditClientController(EditClientDialog view, ManageClientsFrame view2, String id, String clientName, String phoneNumber) {
        this.view = view;
        this.view2 = view2;
        this.id = id;
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
        this.view.addAcceptButtonActionListener(this.getAcceptButtonActionListener());
        this.view.addCancelButtonActionListener(this.getCancelButtonActionListener());
        this.initComponents();
    }

    public ActionListener getAcceptButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ClientsDAO dao = new ClientsDAO();
                    dao.editClient(id, view.getClientNameTextFieldText(), view.getPhoneNumberTextFieldText());
                    updateEditClientsModel();
                    System.out.println("Edited Correctly");
                    view.dispose();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        };
        return al;
    }

    public ActionListener getCancelButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    public void updateEditClientsModel() {
        view2.clearClients();
        try {
            ClientsDAO dao = new ClientsDAO();
            ResultSet rs = dao.listClients();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("client_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("phone"));
                view2.addClient(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view2.getEditClientsJTable().getColumnCount(); i++) {
            view2.getEditClientsJTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initComponents() {
        view.setClientNameTextFieldText(clientName);
        view.setPhoneNumberTextFieldText(phoneNumber);
        view.setTitle("Edit Client");
    }
}
