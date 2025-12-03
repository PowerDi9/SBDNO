package controller.manageEmployeesController;

import controller.manageEmployeesController.editEmployeeController.EditEmployeeController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.EmployeesDAO;
import view.manageEmployeesView.ManageEmployeesFrame;
import view.manageEmployeesView.editEmployeeView.EditEmployeeDialog;

public class ManageEmployeesController {                                        //Controller for the manage employees view

    ManageEmployeesFrame view;
    String id, name, status = null;
    ArrayList<String> array = new ArrayList<String>();

    public ManageEmployeesController(ManageEmployeesFrame view) {
        this.view = view;
        this.view.addAddEmployeeButtonAL(this.getAddEmployeeButtonActionListener());
        this.view.addClearTextButtonAL(this.getClearTextButtonActionListener());
        this.view.addEditEmployeeTableMouseListener(this.getEditEmployeesTableMouseListener());
        this.view.addDeleteEmployeeButtonAL(this.getDeleteEmployeesButtonActionListener());
        this.view.addEditEmployeeButtonAL(this.getEditEmployeeActionListener());
        this.view.addEditEmployeeBackButtonAL(this.getBackButtonActionListener());
        this.view.addAddEmployeeBackButtonAL(this.getBackButtonActionListener());
        this.innitcomponents();
    }

    private MouseListener getEditEmployeesTableMouseListener() {                //Gives the the edit employees table a mouse action
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = view.getEditEmployeeTable().rowAtPoint(evt.getPoint());
                id = view.getEditEmployeeTableIDAt(row, 0);
                name = view.getEditEmployeeTableIDAt(row, 1);
                status = view.getEditEmployeeTableIDAt(row, 2);
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

    private ActionListener getClearTextButtonActionListener() {                 //Gives the clear text an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Sets the employee name texfield to blank
                view.setEmployeeNameTextField("");
            }
        };
        return al;
    }

    private ActionListener getDeleteEmployeesButtonActionListener() {           //Gives the delete button employee an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Deletes the employee selected on the table
                try {
                    if (id == null) {
                        JOptionPane.showMessageDialog(view, "Please select a Employee to delete.");
                        return;
                    } else {
                        int option = JOptionPane.showConfirmDialog(null, "Are you sure to delete " + name + "?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
                    }
                    EmployeesDAO dao = new EmployeesDAO();
                    dao.deleteEmployee(id);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditEmployeeModel();
            }
        };
        return al;
    }

    private ActionListener getAddEmployeeButtonActionListener() {               //Gives the add employee button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Adds the employee to the employee table with the information provided
                String employeeName = view.getEmployeeNameTextField();
                String state = view.getEmployeeStatusComboBox().getSelectedItem().toString();
                try {
                    EmployeesDAO dao = new EmployeesDAO();
                    if (dao.employeeExists(employeeName)) {
                        int option = JOptionPane.showConfirmDialog(null, "The employee \"" + employeeName + "\" already exists.\nCreate it anyway?", "Confirm Duplicate", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.NO_OPTION) {
                            System.out.println("Operation cancelled.");
                            return;
                        }
                    }
                    if (dao.insertEmployee(employeeName, state)) {
                        JOptionPane.showMessageDialog(view, "Employee added succesfully");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditEmployeeModel();
            }
        };
        return al;
    }

    private ActionListener getEditEmployeeActionListener() {                    //Gives the edit employee button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Launches the edit employee dialog with the data of the selected employee on the table
                if (id == null) {
                    JOptionPane.showMessageDialog(view, "Please select a employee to edit.");
                    return;
                }
                EditEmployeeDialog eed = new EditEmployeeDialog(view, true);
                EditEmployeeController eec = new EditEmployeeController(eed, view, id, name, status, array);
                eed.setLocationRelativeTo(view);
                eed.setVisible(true);
            }
        };
        return al;
    }

    private void updateEditEmployeeModel() {                                    //Updates the edit employee table
        view.clearEmployees();
        try {
            EmployeesDAO dao = new EmployeesDAO();
            ResultSet rs = dao.listEmployees();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("employee_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("status"));
                view.addEmployee(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view.getEditEmployeeTable().getColumnCount(); i++) {
            view.getEditEmployeeTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void setEmployeeStatusComboBoxModel() {                             //Sets up the employee status combo box
        array.add("Active");
        array.add("Inactive");
        array.add("On leave");
        array.add("On vacation");
        array.add("Retired");
        array.add("In training");
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (int i = 0; i < array.size(); i++) {
            model.addElement(array.get(i));
        }
        view.getEmployeeStatusComboBox().setModel(model);

    }

    public void setIcon() {                                                      //Sets the application icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    private void innitcomponents() {                                            //Initializes the components
        this.setIcon();
        view.setTitle("Manage Employees");
        view.setDefaultCloseOperation();
        this.updateEditEmployeeModel();
        this.setEmployeeStatusComboBoxModel();
    }
}
