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
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.EmployeesDAO;
import model.dao.TruckEmployeeIntermediateDAO;
import view.manageTrucksView.ManageTrucksFrame;
import view.manageTrucksView.editTruckView.EditTruckDialog;

public class ManageTrucksController {                                           //Controller for the manage trucks view

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

    private MouseListener getEditTrucksTableMouseListener() {                   //Gives the edit trucks table a mouse action
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {           //Gets the information of the selected truck and stores it on the variables
                int row = view.getEditTrucksTable().rowAtPoint(evt.getPoint());
                truckId = view.getEditTrucksTableIDAt(row, 0);
                truckName = view.getEditTrucksTableIDAt(row, 1);
                description = view.getEditTrucksTableIDAt(row, 2);
            }
        };
        return ma;
    }

    private MouseListener getManageAssignedEmployeesTableMouseListener() {      //Gives the manage ssigned emplees table a mouse action
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {           //Gets the information of the selected employee and stores it on the variables
                int row = view.getManageAssignedEmployeesTableTable().rowAtPoint(evt.getPoint());
                employeeId = view.getManageAssignedEmployeesTableIDAt(row, 0);
                description = view.getManageAssignedEmployeesTableIDAt(row, 2);
            }
        };
        return ma;
    }

    private ActionListener getBackButtonActionListener() {                      //Gives the back buttons an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    private ActionListener getClearTextButtonActionListener() {                 //Gives the clear text button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setTruckNameTextFieldText("");
                view.setTruckDescriptionTextAreaText("");
            }
        };
        return al;
    }

    private ActionListener getDeleteTruckButtonActionListener() {               //Gives the delete truck button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Deletes the truck selected on the edit truck table
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

    private ActionListener getUnassignEmployeeButtonActionListener() {          //Gives the unassign employee button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Unnasigns the selected employee to the selected truck
                try {
                    TruckEmployeeIntermediateDAO dao = new TruckEmployeeIntermediateDAO();
                    if (employeeId == null) {
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

    private ActionListener getAddTruckButtonActionListener() {                  //Gives the add truck button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Adds the truck to the truck table with the provided information
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

    private ActionListener getAssignEmployeeButtonActionListener() {            //Gives the assign employee button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Assigns the selected employee to the selected truck
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

    private ActionListener getShowAssignedEmployeesActionListener() {           //Gives the show assigned employees button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Shows the assigned employees to the selected truck in the assigned employees table
                int truckid = Integer.parseInt(view.getMAESelectTruckComboBox().getSelectedItem().toString().split(",")[0]);
                maeTruckId = truckid;
                updateAssignedEmployeesModel(truckid);
            }
        };
        return al;
    }

    private ActionListener getEditTruckActionListener() {                       //Gives the edit truck button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Lauches the edit truck dialog with the provided information
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

    private void updateAssignedEmployeesModel(int truckid) {                    //updates the assigned employees table
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

    private void updateEditTrucksModel() {                                      //Updates the edit truck table
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

    private void setSelectTruckComboBoxModel() {                                //Sets the select Truck combo box
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

    private void setSelectEmployeeComboBoxModel() {                             //Sets the selct employee combo box
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

    public void setIcon() {                                                     //Sets the application icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    private void innitcomponents() {                                            //Initializes the components
        this.setIcon();
        view.setTitle("Manage Trucks");
        this.updateEditTrucksModel();
        view.setSetDefaultCloseOperation();
        this.setSelectEmployeeComboBoxModel();
        this.setSelectTruckComboBoxModel();
    }
}
