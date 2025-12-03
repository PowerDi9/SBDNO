package controller.manageClientsController.searchClientController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.ClientsDAO;
import view.manageClientsView.ManageClientsFrame;
import view.manageClientsView.searchClientsDialog.SearchClientsDialog;

public class SearchClientsController {                                          //Controller for the search clients view

    SearchClientsDialog view = null;
    ManageClientsFrame view2 = null;

    public SearchClientsController(SearchClientsDialog view, ManageClientsFrame view2) {
        this.view = view;
        this.view2 = view2;
        this.view.addAcceptButtonActionListener(this.getAcceptButtonActionListener());
        this.view.addCancelButtonActionListener(this.getCancelButtonActionListener());
        this.initComponents();
    }

    public ActionListener getAcceptButtonActionListener() {                     //Gives the accept button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Searches on the clients table for entries with similarities with the data provided
                try {

                    ClientsDAO dao = new ClientsDAO();
                    ResultSet rs = dao.searchClients(view.getClientNameTextFieldText(), view.getPhoneNumberTextFieldText());
                    view2.clearClients();
                    while (rs.next()) {
                        Vector row = new Vector();
                        row.add(rs.getInt("client_id"));
                        row.add(rs.getString("name"));
                        row.add(rs.getString("phone"));
                        view2.addClient(row);

                        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                        for (int i = 0; i < view2.getEditClientsJTable().getColumnCount(); i++) {
                            view2.getEditClientsJTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                        }
                    }
                    view.dispose();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        };
        return al;
    }

    public ActionListener getCancelButtonActionListener() {                     //Gives the cancel button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    public void setIcon() {                                                     //Sets the application icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    public void initComponents() {                                              //Initializes the components
        this.setIcon();
        view.setTitle("Search Clients");
    }
}
