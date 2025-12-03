package controller.manageEmployeesController.editEmployeeController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.dao.EmployeesDAO;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import view.manageEmployeesView.ManageEmployeesFrame;
import view.manageEmployeesView.editEmployeeView.EditEmployeeDialog;

public class EditEmployeeController {                                           //Controller for the edit employee dialog

    EditEmployeeDialog view;
    ManageEmployeesFrame view2 = null;
    String id, name, state = null;
    ArrayList<String> al = null;

    public EditEmployeeController(EditEmployeeDialog view, ManageEmployeesFrame view2, String id, String name, String state, ArrayList<String> al) {
        this.view = view;
        this.view2 = view2;
        this.id = id;
        this.name = name;
        this.state = state;
        this.al = al;
        this.view.addAcceptButtonActionListener(this.getAcceptButtonActionListener());
        this.view.addCancelButtonActionListener(this.getCancelButtonActionListener());
        this.initComponents();
    }

    private ActionListener getAcceptButtonActionListener() {                     //Gives the accept button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Updates the selected employee information in the DB
                try {
                    EmployeesDAO dao = new EmployeesDAO();
                    dao.editEmployee(id, view.getEmployeeNameTextFieldText(), view.getEmployeeStatusComboBox().getSelectedItem().toString());
                    updateEditEmployeesModel();
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

    private void updateEditEmployeesModel() {                                    //Updates the edit employees table
        view2.clearEmployees();
        try {
            EmployeesDAO dao = new EmployeesDAO();
            ResultSet rs = dao.listEmployees();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("employee_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("status"));
                view2.addEmployee(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view2.getEditEmployeeTable().getColumnCount(); i++) {
            view2.getEditEmployeeTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void setSelectEmployeeStatusComboBoxModel() {                       //Sets the select employee status combo box
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        int index = 0;
        int con = 0;
        for (int i = 0; i < al.size(); i++) {
            model.addElement(al.get(i));
            if (this.state.equals(al.get(i))) {
                index = con;
            }
            con++;
        }
        view.getEmployeeStatusComboBox().setModel(model);
        view.getEmployeeStatusComboBox().setSelectedIndex(index);
    }

    private void setIcon() {                                                    //Sets the application Icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    private void initComponents() {                                             //Initializes the components
        this.setIcon();
        view.setEmployeeNameTextFieldText(name);
        this.setSelectEmployeeStatusComboBoxModel();
        view.setTitle("Edit Employee");
    }
}
