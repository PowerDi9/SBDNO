package controller.manageBusinessesController;

import controller.manageBusinessesController.editBusinessController.EditBusinessController;
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
import model.dao.BusinessDAO;
import view.manageBusinessView.editBusinessView.EditBusinessDialog;
import view.manageBusinessesView.ManageBusinessesDialog;

public class ManageBusinessesController {

    ManageBusinessesDialog view;
    String id, name, percentage = null;

    public ManageBusinessesController(ManageBusinessesDialog view) {
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

    private MouseListener getEditBusinessTableMouseListener() {
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
    
    private ActionListener getBackButtonActionListener(){
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
                view.setBusinessNameTextField("");
                view.setPercentageCommissionTextField("");
            }
        };
        return al;
    }

    private ActionListener getDeleteBusinessButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(id == null){
                    JOptionPane.showMessageDialog(view, "Please select a Business to delete.");
                    return;
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

    private ActionListener getAddBusinessButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bname = view.getBusinessNameTextField();
                double percentage = Double.parseDouble(view.getPercentageCommissionTextField().replaceAll(",", "."));
                try {
                    BusinessDAO dao = new BusinessDAO();
                    if (dao.businessExists(bname)) {
                        int option = JOptionPane.showConfirmDialog(null, "The Bussines \"" + bname + "\" already exists.\n¿Create it anyway?", "Confirm Duplicate", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.NO_OPTION) {
                            System.out.println("Operación cancelada.");
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

    private ActionListener getEditBusinessActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(id == null){
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

    private void updateEditBusinessesModel() {
        view.clearBusinesses();
        try {
            BusinessDAO dao = new BusinessDAO();
            ResultSet rs = dao.listBusinesses();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("id_empresa"));
                row.add(rs.getString("nombre"));
                row.add(rs.getDouble("porcentaje"));
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

    private void innitcomponents() {
        view.setTitle("Manage Businesses");
        this.updateEditBusinessesModel();
    }
}
