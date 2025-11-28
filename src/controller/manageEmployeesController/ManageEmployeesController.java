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
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.EmployeesDAO;
import view.manageEmployeesView.ManageEmployeesFrame;
import view.manageEmployeesView.editEmployeeView.EditEmployeeDialog;

public class ManageEmployeesController {

    ManageEmployeesFrame view;
    String id, name, state = null;
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

    private MouseListener getEditEmployeesTableMouseListener() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = view.getEditEmployeeTable().rowAtPoint(evt.getPoint());
                id = view.getEditEmployeeTableIDAt(row, 0);
                name = view.getEditEmployeeTableIDAt(row, 1);
                state = view.getEditEmployeeTableIDAt(row, 2);
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
                view.setEmployeeNameTextField("");
            }
        };
        return al;
    }

    private ActionListener getDeleteEmployeesButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

    private ActionListener getAddEmployeeButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeName = view.getEmployeeNameTextField();
                String state = view.getEmployeeStateComboBox().getSelectedItem().toString();
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
                        System.out.println("Employee added successfully");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditEmployeeModel();
            }
        };
        return al;
    }

    private ActionListener getEditEmployeeActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (id == null) {
                    JOptionPane.showMessageDialog(view, "Please select a employee to edit.");
                    return;
                }
                EditEmployeeDialog eed = new EditEmployeeDialog(view, true);
                EditEmployeeController eec = new EditEmployeeController(eed, view, id, name, state, array);
                eed.setLocationRelativeTo(view);
                eed.setVisible(true);
            }
        };
        return al;
    }

    private void updateEditEmployeeModel() {
        view.clearEmployees();
        try {
            EmployeesDAO dao = new EmployeesDAO();
            ResultSet rs = dao.listEmployees();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("employee_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("state"));
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

    private void setEmployeeComboBoxModel() {
        array.add("Active");
        array.add("Inactive");
        array.add("On leave");
        array.add("On vacation");
        array.add("Retired");
        array.add("In training");
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (int i = 0; i < array.size(); i++){
            model.addElement(array.get(i));
        }
        view.getEmployeeStateComboBox().setModel(model);

    }

    private void innitcomponents() {
        view.setTitle("Manage Employees");
        view.setDefaultCloseOperation();
        this.updateEditEmployeeModel();
        this.setEmployeeComboBoxModel();
    }
}
