/*
 * SBDNO - Small Business Delivery Note Organizer
 * 
 * Copyright (C) 2025 Adrián González Hermida
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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

public class EditTruckController {                                              //Controller for the edit truck dialog

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

    private ActionListener getAcceptButtonActionListener() {                     //Gives the accept button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        //Updates the information of the selected truck in the trucks table with the provided information
                try {
                    TrucksDAO dao = new TrucksDAO();
                    dao.editTruck(truckId, view.getTruckNameTextFieldText(), view.getTruckDescriptionTextAreaText());
                    System.out.println("Edited Correctly");
                    updateEditTrucksModel();
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

    private void updateEditTrucksModel() {                                  //Updates the edit trucks model 
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

    private void setIcon() {                                                    //Sets the application icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    private void initComponents() {                                             //initializes the components
        this.setIcon();
        view.setTruckNameTextFieldText(truckName);
        view.setTruckDescriptionTextAreaText(truckDescription);
        view.setTitle("Edit Truck");
    }
}
