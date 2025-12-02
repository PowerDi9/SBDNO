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

public class EditEmployeeController {

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

    public ActionListener getAcceptButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    EmployeesDAO dao = new EmployeesDAO();
                    dao.editEmployee(id, view.getEmployeeNameTextFieldText(), view.getEmployeeStateComboBox().getSelectedItem().toString());
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

    public ActionListener getCancelButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    public void updateEditEmployeesModel() {
        view2.clearEmployees();
        try {
            EmployeesDAO dao = new EmployeesDAO();
            ResultSet rs = dao.listEmployees();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("employee_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("state"));
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

    private void setSelectEmployeeComboBoxModel() {
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
        view.getEmployeeStateComboBox().setModel(model);
        view.getEmployeeStateComboBox().setSelectedIndex(index);
    }

    public void setIcon() {
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    public void initComponents() {
        this.setIcon();
        view.setEmployeeNameTextFieldText(name);
        this.setSelectEmployeeComboBoxModel();
        view.setTitle("Edit Employee");
    }
}
