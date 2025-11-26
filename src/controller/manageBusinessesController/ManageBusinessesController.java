package controller.manageBusinessesController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.dao.BusinessDAO;
import view.manageBusinessesView.ManageBusinessesDialog;

public class ManageBusinessesController {

    ManageBusinessesDialog view;
    BusinessDAO dao = new BusinessDAO();

    public ManageBusinessesController(ManageBusinessesDialog view) {
        this.view = view;
        this.view.addAddBusinessButtonAL(this.getAddBusinessButtonActionListener());
        this.view.addClearTextButtonAL(this.getClearTextButtonActionListener());
        this.innitcomponents();
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

    private ActionListener getAddBusinessButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bname = view.getBusinessNameTextField();
                try {
                    if (dao.businessExists(bname)) {
                        int option = JOptionPane.showConfirmDialog(null, "The Bussines \""+bname+"\" already exists.\n¿Create it anyway?","Confirm Duplicate",JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.NO_OPTION) {
                            System.out.println("Operación cancelada.");
                            return;
                        }
                    }
                    if (dao.insertBusiness(bname)) {
                        System.out.println("Business added successfully");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        };
        return al;
    }

    public void innitcomponents() {
        view.setTitle("Manage Businesses");
    }
}
