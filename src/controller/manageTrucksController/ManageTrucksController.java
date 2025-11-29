package controller.manageTrucksController;

import controller.manageTrucksController.editTruckController.EditTruckController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import model.dao.TrucksDAO;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.EmployeesDAO;
import model.dao.TruckEmployeeIntermediateDAO;
import view.manageTrucksView.ManageTrucksFrame;
import view.manageTrucksView.editTruckView.EditTruckDialog;

public class ManageTrucksController {

    ManageTrucksFrame view;
    String truckId, employeeId, truckName, description = null;
    int maeTruckId;

    public ManageTrucksController(ManageTrucksFrame view) {
        this.view = view;
        this.view.addAddTruckButtonAL(this.getAddTruckButtonActionListener());
        this.view.addAssignEmployeeButtonAL(this.getAssignEmployeeButtonActionListener());
        this.view.addClearTextButtonAL(this.getClearTextButtonActionListener());
        this.view.addEditTrucksTableMouseListener(this.getEditTrucksTableMouseListener());
        this.view.addManageAssignedEmployeesTableMouseListener(this.getManageAssignedEmployeesTableMouseListener());
        this.view.addDeleteTruckButtonAL(this.getDeleteTruckButtonActionListener());
        this.view.addUnassignEmployeeButtonAL(this.getUnassignEmployeeButtonActionListener());
        this.view.addEditTruckButtonAL(this.getEditTruckActionListener());
        this.view.addShowAssignedEmployeesButtonAL(this.getShowAssignedEmployeesActionListener());
        this.view.addEditTrucksBackButtonAL(this.getBackButtonActionListener());
        this.view.addAddTrucksBackButtonAL(this.getBackButtonActionListener());
        this.view.addAssignEmployeesBackButtonAL(this.getBackButtonActionListener());
        this.view.addManageAssignedEmployeesBackButtonAL(this.getBackButtonActionListener());
        this.innitcomponents();
    }

    private MouseListener getEditTrucksTableMouseListener() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = view.getEditTrucksTable().rowAtPoint(evt.getPoint());
                truckId = view.getEditTrucksTableIDAt(row, 0);
                truckName = view.getEditTrucksTableIDAt(row, 1);
                description = view.getEditTrucksTableIDAt(row, 2);
            }
        };
        return ma;
    }

    private MouseListener getManageAssignedEmployeesTableMouseListener() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = view.getManageAssignedEmployeesTableTable().rowAtPoint(evt.getPoint());
                employeeId = view.getManageAssignedEmployeesTableIDAt(row, 0);
                description = view.getManageAssignedEmployeesTableIDAt(row, 2);
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
                view.setTruckNameTextFieldText("");
                view.setTruckDescriptionTextAreaText("");
            }
        };
        return al;
    }

    private ActionListener getDeleteTruckButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (truckId == null) {
                        JOptionPane.showMessageDialog(view, "Please select a Truck to delete.");
                        return;
                    } else {
                        int option = JOptionPane.showConfirmDialog(null, "Are you sure to delete " + truckName + "?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
                    }
                    TrucksDAO dao = new TrucksDAO();
                    dao.deleteTrucks(truckId);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditTrucksModel();
            }
        };
        return al;
    }

    private ActionListener getUnassignEmployeeButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TruckEmployeeIntermediateDAO dao = new TruckEmployeeIntermediateDAO();
                    if(employeeId == null){
                        JOptionPane.showMessageDialog(view, "Please select an employee to unassign.");
                        return;
                    }
                    int id = Integer.parseInt(employeeId);
                    if (dao.removeEmployeeFromTruck(maeTruckId, id)) {
                        System.out.println("Employee removed correctly");
                    }
                    updateAssignedEmployeesModel(maeTruckId);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        };
        return al;
    }

    private ActionListener getAddTruckButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String truckName = view.getTruckNameTextFieldText();
                String truckDescription = view.getTruckDescriptionTextAreaText();
                try {
                    TrucksDAO dao = new TrucksDAO();
                    if (dao.truckExists(truckName)) {
                        JOptionPane.showMessageDialog(view, "The Truck " + truckName + " already exists.\n Please use other name");
                        return;
                    }
                    if (dao.insertTruck(truckName, truckDescription)) {
                        System.out.println("Truck added successfully");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditTrucksModel();
            }
        };
        return al;
    }

    private ActionListener getAssignEmployeeButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int truckid = Integer.parseInt(view.getSelectTruckComboBox().getSelectedItem().toString().split(",")[0]);
                int employeid = Integer.parseInt(view.getSelectEmployeeComboBox().getSelectedItem().toString().split(",")[0]);
                try {
                    TruckEmployeeIntermediateDAO dao = new TruckEmployeeIntermediateDAO();
                    if (dao.assignEmployeeToTruck(truckid, employeid)) {
                        System.out.println("Employee assigned correctly");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        };
        return al;
    }

    private ActionListener getShowAssignedEmployeesActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int truckid = Integer.parseInt(view.getMAESelectTruckComboBox().getSelectedItem().toString().split(",")[0]);
                maeTruckId = truckid;
                updateAssignedEmployeesModel(truckid);
            }
        };
        return al;
    }

    private ActionListener getEditTruckActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (truckId == null) {
                    JOptionPane.showMessageDialog(view, "Please select a Business to edit.");
                    return;
                }
                EditTruckDialog etd = new EditTruckDialog(view, true);
                EditTruckController etc = new EditTruckController(etd, view, truckId, truckName, description);
                etd.setLocationRelativeTo(view);
                etd.setVisible(true);
            }
        };
        return al;
    }
    
    private void updateAssignedEmployeesModel(int truckid){
        view.clearAssignedEmployees();
                try {
                    TruckEmployeeIntermediateDAO dao = new TruckEmployeeIntermediateDAO();
                    ResultSet rs = dao.getEmployeesByTruck(truckid);
                    while (rs.next()) {
                        Vector row = new Vector();
                        row.add(rs.getInt("employee_id"));
                        row.add(rs.getString("name"));
                        row.add(rs.getString("state"));
                        view.addAssignedEmployee(row);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                for (int i = 0; i < view.getManageAssignedEmployeesTableTable().getColumnCount(); i++) {
                    view.getManageAssignedEmployeesTableTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }
    }

    private void updateEditTrucksModel() {
        view.clearTrucks();
        try {
            TrucksDAO dao = new TrucksDAO();
            ResultSet rs = dao.listTrucks();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("truck_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("description"));
                view.addTruck(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view.getEditTrucksTable().getColumnCount(); i++) {
            view.getEditTrucksTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void setSelectTruckComboBoxModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            TrucksDAO dao = new TrucksDAO();
            ResultSet rs = dao.listTrucks();
            while (rs.next()) {
                String truckId = String.valueOf(rs.getInt("truck_id"));
                String name = rs.getString("name");
                String str = truckId + "," + name;
                model.addElement(str);
            }
            view.getSelectTruckComboBox().setModel(model);
            view.getMAESelectTruckComboBox().setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setSelectEmployeeComboBoxModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            EmployeesDAO dao = new EmployeesDAO();
            ResultSet rs = dao.listEmployees();
            while (rs.next()) {
                String employeeId = String.valueOf(rs.getInt("employee_id"));
                String name = rs.getString("name");
                String str = employeeId + "," + name;
                model.addElement(str);
            }
            view.getSelectEmployeeComboBox().setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void innitcomponents() {
        view.setTitle("Manage Trucks");
        this.updateEditTrucksModel();
        view.setSetDefaultCloseOperation();
        this.setSelectEmployeeComboBoxModel();
        this.setSelectTruckComboBoxModel();
    }
}
