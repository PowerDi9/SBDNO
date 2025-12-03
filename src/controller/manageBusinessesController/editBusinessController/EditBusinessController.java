package controller.manageBusinessesController.editBusinessController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.BusinessDAO;
import view.manageBusinessesView.editBusinessView.EditBusinessDialog;
import view.manageBusinessesView.ManageBusinessesFrame;

public class EditBusinessController {                                           //Controller for editing businesses

    EditBusinessDialog view = null;
    ManageBusinessesFrame view2 = null;
    String id, name, percentage = null;

    public EditBusinessController(EditBusinessDialog view, ManageBusinessesFrame view2, String id, String name, String percentage) {
        this.view = view;
        this.view2 = view2;
        this.id = id;
        this.name = name;
        this.percentage = percentage;
        this.view.addAcceptButtonActionListener(this.getAcceptButtonActionListener());
        this.view.addCancelButtonActionListener(this.getCancelButtonActionListener());
        this.initComponents();
    }

    public ActionListener getAcceptButtonActionListener() {                     //Gives the accept button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Performes an u`date of the selected business with the new data
                try {
                    BusinessDAO dao = new BusinessDAO();
                    dao.editBusiness(id, view.getBusinessNameTextFieldText(), Double.parseDouble(view.getPercentageCommissionTextFieldText().replaceAll(",", ".")));
                    updateEditBusinessesModel();
                    System.out.println("Edited Correctly");
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

    public void updateEditBusinessesModel() {                                   //Updates the edit business table
        view2.clearBusinesses();
        try {
            BusinessDAO dao = new BusinessDAO();
            ResultSet rs = dao.listBusinesses();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("business_id"));
                row.add(rs.getString("name"));
                row.add(rs.getDouble("percentage"));
                view2.addBusiness(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view2.getEditBusinessesTable().getColumnCount(); i++) {
            view2.getEditBusinessesTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void setIcon() {                                                     //Sets the application icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    public void initComponents() {                                              //Initialazes the components
        this.setIcon();
        view.setBusinessNameTextFieldText(name);
        view.setPercentageCommissionTextFieldText(percentage);
        view.setTitle("Edit Business");
    }
}
