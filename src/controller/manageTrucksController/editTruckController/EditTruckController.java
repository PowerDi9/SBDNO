package controller.manageTrucksController.editTruckController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.dao.TrucksDAO;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import view.manageTrucksView.ManageTrucksFrame;
import view.manageTrucksView.editTruckView.EditTruckDialog;

public class EditTruckController {

    EditTruckDialog view = null;
    ManageTrucksFrame view2 = null;
    String truckId, truckName, truckDescription = null;

    public EditTruckController(EditTruckDialog view, ManageTrucksFrame view2, String truckId, String truckName, String truckDescription) {
        this.view = view;
        this.view2 = view2;
        this.truckId = truckId;
        this.truckName = truckName;
        this.truckDescription = truckDescription;
        this.view.addAcceptButtonAL(this.getAcceptButtonActionListener());
        this.view.addCancelButtonAL(this.getCancelButtonActionListener());
        this.initComponents();
    }

    public ActionListener getAcceptButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TrucksDAO dao = new TrucksDAO();
                    dao.editTruck(truckId, view.getTruckNameTextFieldText(), view.getTruckDescriptionTextAreaText());
                    System.out.println("Edited Correctly");
                    updateEditBusinessesModel();
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

    public void updateEditBusinessesModel() {
        view2.clearTrucks();
        try {
            TrucksDAO dao = new TrucksDAO();
            ResultSet rs = dao.listTrucks();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("truck_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("description"));
                view2.addTruck(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view2.getEditTrucksTable().getColumnCount(); i++) {
            view2.getEditTrucksTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void setIcon() {
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    public void initComponents() {
        this.setIcon();
        view.setTruckNameTextFieldText(truckName);
        view.setTruckDescriptionTextAreaText(truckDescription);
        view.setTitle("Edit Truck");
    }
}
