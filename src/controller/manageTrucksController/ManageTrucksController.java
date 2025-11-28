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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import view.manageTrucksView.ManageTrucksFrame;
import view.manageTrucksView.editTruckView.EditTruckDialog;

public class ManageTrucksController {
    ManageTrucksFrame view;
    String id, name, description = null;

    public ManageTrucksController(ManageTrucksFrame view) {
        this.view = view;
        this.view.addAddTruckButtonAL(this.getAddTruckButtonActionListener());
        this.view.addClearTextButtonAL(this.getClearTextButtonActionListener());
        this.view.addEditTrucksTableMouseListener(this.getEditTrucksTableMouseListener());
        this.view.addDeleteTruckButtonAL(this.getDeleteTruckButtonActionListener());
        this.view.addEditTruckButtonAL(this.getEditTruckActionListener());
        this.view.addEditTrucksBackButtonAL(this.getBackButtonActionListener());
        this.view.addAddTrucksBackButtonAL(this.getBackButtonActionListener());
        this.innitcomponents();
    }

    private MouseListener getEditTrucksTableMouseListener() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = view.getEditTrucksTable().rowAtPoint(evt.getPoint());
                id = view.getEditTrucksTableIDAt(row, 0);
                name = view.getEditTrucksTableIDAt(row, 1);
                description = view.getEditTrucksTableIDAt(row, 2);
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
                    if(id == null){
                    JOptionPane.showMessageDialog(view, "Please select a Truck to delete.");
                    return;
                }else{
                        int option = JOptionPane.showConfirmDialog(null, "Are you sure to delete "+name+"?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
                    }
                    TrucksDAO dao = new TrucksDAO();
                    dao.deleteTrucks(id);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateEditTrucksModel();
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
                        int option = JOptionPane.showConfirmDialog(null, "The Truck \"" + truckName + "\" already exists.\nCreate it anyway?", "Confirm Duplicate", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.NO_OPTION) {
                            System.out.println("Operation cancelled.");
                            return;
                        }
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

    private ActionListener getEditTruckActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(id == null){
                    JOptionPane.showMessageDialog(view, "Please select a Business to edit.");
                    return;
                }
                EditTruckDialog etd = new EditTruckDialog(view, true);
                EditTruckController etc = new EditTruckController(etd, view, id, name, description);
                etd.setLocationRelativeTo(view);
                etd.setVisible(true);
            }
        };
        return al;
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

    private void innitcomponents() {
        view.setTitle("Manage Trucks");
        this.updateEditTrucksModel();
        view.setSetDefaultCloseOperation();
    }
}
