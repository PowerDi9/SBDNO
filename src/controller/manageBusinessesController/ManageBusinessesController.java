package controller.manageBusinessesController;

import controller.manageBusinessesController.editBusinessController.EditBusinessController;
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
import model.dao.BusinessDAO;
import view.manageBusinessesView.editBusinessView.EditBusinessDialog;
import view.manageBusinessesView.ManageBusinessesFrame;

public class ManageBusinessesController {                                                           //Controller for the manage businesses view

    ManageBusinessesFrame view;
    String id, name, percentage = null;

    public ManageBusinessesController(ManageBusinessesFrame view) {
        this.view = view;
        this.view.addAddBusinessButtonAL(this.getAddBusinessButtonActionListener());
        this.view.addClearTextButtonAL(this.getClearTextButtonActionListener());
        this.view.addEditBusinessesTableMouseListener(this.getEditBusinessTableMouseListener());
        this.view.addDeleteBusinessButtonAL(this.getDeleteBusinessButtonActionListener());
        this.view.addEditBusinessButtonAL(this.getEditBusinessActionListener());
        this.view.addEditBusinessesBackButtonAL(this.getBackButtonActionListener());
        this.view.addAddBusinessBackButtonAL(this.getBackButtonActionListener());
        this.innitcomponents();
    }

    private MouseListener getEditBusinessTableMouseListener() {                                     //Gives the edit business table a mouse action
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = view.getEditBusinessesTable().rowAtPoint(evt.getPoint());
                id = view.getEditBusinessTableIDAt(row, 0);
                name = view.getEditBusinessTableIDAt(row, 1);
                percentage = view.getEditBusinessTableIDAt(row, 2);
            }
        };
        return ma;
    }

    private ActionListener getBackButtonActionListener() {                                          //Gives the back button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    private ActionListener getClearTextButtonActionListener() {                                     //Gives the clear text button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                                            //Sets the text fields text to blank
                view.setBusinessNameTextField("");
                view.setPercentageCommissionTextField("");
            }
        };
        return al;
    }

    private ActionListener getDeleteBusinessButtonActionListener() {                                //Gives the delete button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                                            //Deletes the selected busines on the table
                try {
                    if (id == null) {
                        JOptionPane.showMessageDialog(view, "Please select a Business to delete.");
                        return;
                    } else {
                        int option = JOptionPane.showConfirmDialog(null, "Are you sure to delete " + name + "?\nDoing this will make all it's stores to be deleted as well.", "Confirm deletion", JOptionPane.YES_NO_OPTION);
                    }
                    BusinessDAO dao = new BusinessDAO();
                    dao.deleteBusiness(id);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditBusinessesModel();
            }
        };
        return al;
    }

    private ActionListener getAddBusinessButtonActionListener() {                                   //Gives the add business button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                                            //Adds the business to the db with the given data
                String bname = view.getBusinessNameTextField();
                double percentage = Double.parseDouble(view.getPercentageCommissionTextField().replaceAll(",", "."));
                try {
                    BusinessDAO dao = new BusinessDAO();
                    if (dao.businessExists(bname)) {
                        int option = JOptionPane.showConfirmDialog(null, "The Bussines \"" + bname + "\" already exists.\nCreate it anyway?", "Confirm Duplicate", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.NO_OPTION) {
                            System.out.println("Operation cancelled.");
                            return;
                        }
                    }
                    if (dao.insertBusiness(bname, percentage)) {
                        System.out.println("Business added successfully");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditBusinessesModel();
            }
        };
        return al;
    }

    private ActionListener getEditBusinessActionListener() {                                        //Gives the edit business button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (id == null) {
                    JOptionPane.showMessageDialog(view, "Please select a Business to edit.");
                    return;
                }
                EditBusinessDialog ebd = new EditBusinessDialog(view, true);
                EditBusinessController ebc = new EditBusinessController(ebd, view, id, name, percentage);
                ebd.setLocationRelativeTo(view);
                ebd.setVisible(true);
            }
        };
        return al;
    }

    private void updateEditBusinessesModel() {                                                      //Updates the edit business table
        view.clearBusinesses();
        try {
            BusinessDAO dao = new BusinessDAO();
            ResultSet rs = dao.listBusinesses();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("business_id"));
                row.add(rs.getString("name"));
                row.add(rs.getDouble("percentage"));
                view.addBusiness(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view.getEditBusinessesTable().getColumnCount(); i++) {
            view.getEditBusinessesTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void setIcon() {                                                                         //Sets the application icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    private void innitcomponents() {                                                                //initializes the components
        this.setIcon();
        view.setTitle("Manage Businesses");
        this.updateEditBusinessesModel();
    }
}
