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
package controller.addDeliveryNoteController;

import controller.addDeliveryNoteController.selectDateController.SelectDateController;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.dao.BusinessDAO;
import model.dao.ClientsDAO;
import model.dao.DeliveryNoteDAO;
import model.dao.SellersDAO;
import model.dao.StoresDAO;
import model.dao.TrucksDAO;
import view.addDeliveryNoteView.AddDeliveryNoteFrame;
import view.addDeliveryNoteView.selectDateDialog.SelectDateDialog;

public class AddDeliveryNoteController {                                        //Controller for adding the delivery notes, to the DB.

    AddDeliveryNoteFrame view;
    boolean ignoreComboChange = false;
    int businessId, storeId, sellerId, truckId, clientId;
    String deliveryDate, pdfRoute, date = null;
    Double amount = null;

    public AddDeliveryNoteController(AddDeliveryNoteFrame view) {
        this.view = view;
        this.innitComponents();
        this.view.addBackButtonAL(this.getBackButtonActionListener());
        this.view.addClearEntriesButtonAL(this.getClearEntriesButtonActionListener());
        this.view.addSelectBusinesComboBoxAL(this.getSelectBusinessComboBoxActionListener());
        this.view.addSelectStoreComboBoxAL(this.getSelectStoreComboBoxActionListener());
        this.view.addSelectSellerComboBoxAL(this.getSelectSellerComboBoxActionListener());
        this.view.addSelectTruckComboBoxAL(this.getSelectTruckComboBoxActionListener());
        this.view.addSelectDateButtonAL(this.getSelectDateButtonActionListener());
        this.view.addSelectPDFButtonAL(this.getSelectPDFBUttonActionListener());
        this.view.addAddDeliveryNoteButtonAL(this.getAddDeliveryNoteActionListener());
    }

    private ActionListener getAddDeliveryNoteActionListener() {                 //Gives an action to the Back button
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                date = LocalDate.now().toString().replaceAll("-", "/");                                 //Gets the date and formates it
                if (businessId == 0) {                                                                  //Checks if every variable is registered
                    JOptionPane.showMessageDialog(view, "Please select a business.");
                    return;
                } else if (storeId == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a store.");
                    return;
                } else if (sellerId == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a seller.");
                    return;
                } else if (truckId == 0) {
                    JOptionPane.showMessageDialog(view, "Please select a Truck.");
                    return;
                } else if (deliveryDate == null) {
                    JOptionPane.showMessageDialog(view, "Please select a Delivery Date.");
                    return;
                } else if (pdfRoute == null) {
                    JOptionPane.showMessageDialog(view, "Please select a PDF file.");
                    return;
                } else if (view.getAmountTextFieldText().isBlank()) {
                    JOptionPane.showMessageDialog(view, "Please introduce an amount.");
                    return;
                } else if (view.getClientNameTextFieldText().isBlank()) {
                    JOptionPane.showMessageDialog(view, "Please introduce a client name.");
                    return;
                } else if (view.getClientPhoneNumberTextFieldText().isBlank()) {
                    JOptionPane.showMessageDialog(view, "Please introduce a client phone number.");
                    return;
                }
                try {                                                                                   //Checks for the validity of the amount variable
                    amount = Double.parseDouble(view.getAmountTextFieldText().replaceAll(",", "."));
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Please introduce a valid number.");
                    return;
                }
                String clientName = view.getClientNameTextFieldText();
                String clientPhoneNumber = view.getClientPhoneNumberTextFieldText();
                try {
                    ClientsDAO cdao = new ClientsDAO();                                                 //Gets a connettion to the clients table
                    clientId = cdao.returnGeneratedKeyInsertClient(clientName, clientPhoneNumber);      //Adds the client attached to the delivery note
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    DeliveryNoteDAO dndao = new DeliveryNoteDAO();
                    dndao.insertDeliveryNote(date, deliveryDate, amount, clientId, sellerId, businessId, storeId, truckId, pdfRoute);           //Adds the delivery note 
                } catch (SQLException ex) {
                    System.getLogger(AddDeliveryNoteController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
                JOptionPane.showMessageDialog(view, "DeliveryNote added correctly");
            }
        };
        return al;
    }

    private ActionListener getBackButtonActionListener() {                      //Gives an action to the Back button
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    private ActionListener getClearEntriesButtonActionListener() {              //Gives an action to the Clear Entries button
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearEntries();
            }
        };
        return al;
    }

    private ActionListener getSelectPDFBUttonActionListener() {                 //Gives an action to the Select PDF button
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int selection = fc.showOpenDialog(view);                        //Opens the menu for getting the PDF path
                String newPDFRute = null;
                if (selection == JFileChooser.APPROVE_OPTION) {                 //If the user selects a file
                    newPDFRute = fc.getSelectedFile().getAbsolutePath();        //Gets the file path
                    if (!newPDFRute.endsWith(".pdf")) {                                           //If it doesen't end with .pdf
                        JOptionPane.showMessageDialog(view, "Please select a PDF file.");       //Asks for a pdf.
                        return;                                                 //Exits the action.
                    }
                    JOptionPane.showMessageDialog(view, "Selected PDF: " + newPDFRute);         //Shows the pdf path
                } else {
                    JOptionPane.showMessageDialog(view, "No PDF was selected");                 //If the user doesen't select a file shows the message
                }
                pdfRoute = newPDFRute;                                          //Sets the pdf route
            }
        };
        return al;
    }

    private ActionListener getSelectDateButtonActionListener() {                //Gives an action to the Select Delivery Date button
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDeliveryNoteController controladorActual = AddDeliveryNoteController.this;
                SelectDateDialog sdd = new SelectDateDialog(view, true);
                SelectDateController sdc = new SelectDateController(sdd, controladorActual);
                sdd.setLocationRelativeTo(view);
                sdd.setVisible(true);
            }
        };
        return al;
    }

    public void setDeliveryDate(String str) {                                   //Method the the new Dialog can reach to pass the new delivery date
        this.deliveryDate = str;
        this.view.setSelectDateButtonText(str);
    }

    private ActionListener getSelectBusinessComboBoxActionListener() {          //Gives an action to the Select Business Combo Box
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ignoreComboChange) {                                        //If the clear entries button is pressed does nothing
                    return;
                }
                if (view.getSelectBusinessComboBox().getSelectedItem() == null || view.getSelectBusinessComboBox().getSelectedIndex() == 0) {       //Asks the user to select a business if the selected is null
                    JOptionPane.showMessageDialog(view, "Please select a Business.");
                    return;
                }
                DefaultComboBoxModel<String> emptyModel = new DefaultComboBoxModel<>();                         //gets an empty combo box model for the sellers combo box
                emptyModel.addElement(null);                                                                    //adds a null element to a empty model
                businessId = Integer.parseInt(view.getSelectBusinessComboBox().getSelectedItem().toString().split(",")[0]);  //gets the selected business id
                setStoresComboBoxByBussinesId(businessId);                      //sets the store combo box
                view.getSelectStoreComboBox().setEnabled(true);                 //enables the selected Store Combo Box
                view.getSelectSellerComboBox().setEnabled(false);               //disables the sellers combo box in case its enabled
                view.getSelectSellerComboBox().setModel(emptyModel);            //emptys the seller combo box
            }
        };
        return al;
    }

    private ActionListener getSelectStoreComboBoxActionListener() {             //Gives an action to the Select Business Combo Box
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ignoreComboChange) {                                        //If the clear entries button is pressed does nothing
                    return;
                }
                if (view.getSelectStoreComboBox().getSelectedItem() == null || view.getSelectStoreComboBox().getSelectedIndex() == 0) {       //Asks the user to select a business if the selected is null
                    JOptionPane.showMessageDialog(view, "Please select a Store.");
                    return;
                }
                storeId = Integer.parseInt(view.getSelectStoreComboBox().getSelectedItem().toString().split(",")[0]);   //gets the selected store id
                setSellersComboBoxByStoreId(storeId);                           //sets the sellers combo box
                view.getSelectSellerComboBox().setEnabled(true);                //enables the sellers combo box
            }
        };
        return al;
    }

    private ActionListener getSelectSellerComboBoxActionListener() {            //Gives an action to the Select Sellers Combo Box
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ignoreComboChange) {                                        //If the clear entries button is pressed does nothing
                    return;
                }
                if (view.getSelectSellerComboBox().getSelectedItem() == null || view.getSelectSellerComboBox().getSelectedIndex() == 0) {       //Asks the user to select a seller if the selected is null
                    JOptionPane.showMessageDialog(view, "Please select a seller.");
                    return;
                }
                sellerId = Integer.parseInt(view.getSelectSellerComboBox().getSelectedItem().toString().split(",")[0]);     //gets the selected seller id
            }
        };
        return al;
    }

    private ActionListener getSelectTruckComboBoxActionListener() {             //Gives an action to the Select Truck Combo Box
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ignoreComboChange) {                                        //If the clear entries button is pressed does nothing
                    return;
                }
                if (view.getSelectTruckComboBox().getSelectedItem() == null || view.getSelectTruckComboBox().getSelectedIndex() == 0) {            //Asks the user to select a truck if the selected is null
                    JOptionPane.showMessageDialog(view, "Please select a truck.");
                    return;
                }
                truckId = Integer.parseInt(view.getSelectTruckComboBox().getSelectedItem().toString().split(",")[0]);       //gets the selected truck id
            }
        };
        return al;
    }

    private void setStoresComboBoxByBussinesId(int bussinesId) {                //Sets the stores combo box model by business id 
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();      //Generates a default model
        model.addElement(null);                                                 //Makes the first object of the model null
        try {
            StoresDAO dao = new StoresDAO();                                    //Gets a connettion to the stores table
            ResultSet rs = dao.listStoresIdNameByBussinesId(bussinesId);        //Gets the stores Id and Name by business id
            while (rs.next()) {                                                 //While theres a line continues
                String storeId = String.valueOf(rs.getInt("store_id"));         //Gets the store id
                String name = rs.getString("store_name");                       //Gets the store name
                String str = storeId + "," + name;                              //Sets the string for the model
                model.addElement(str);                                          //Adds the string to the model
            }
            view.getSelectStoreComboBox().setModel(model);                      //Sets the model
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setSellersComboBoxByStoreId(int storeId) {                     //Sets the sellers combo box model by stores id
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();      //Generates a default model
        model.addElement(null);                                                 //Makes the first object of the model null
        try {
            SellersDAO dao = new SellersDAO();                                  //Gets a connettion to the sellers table
            ResultSet rs = dao.listSellersIdNameByStoreId(storeId);             //Gets the seller Id and Name by store id
            while (rs.next()) {                                                 //While theres a line continues
                String sellerId = String.valueOf(rs.getInt("seller_id"));       //Gets the seller id
                String name = rs.getString("seller_name");                      //Gets the store name
                String str = sellerId + "," + name;                             //Sets the string for the model
                model.addElement(str);                                          //Adds the string to the model
            }
            view.getSelectSellerComboBox().setModel(model);                     //Sets the model 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setComboBoxes() {                                                      //Sets the busines and truck combo box
        DefaultComboBoxModel<String> businessModel = new DefaultComboBoxModel<>();      //Generates a default model 
        DefaultComboBoxModel<String> truckModel = new DefaultComboBoxModel<>();         //Generates a default model
        DefaultComboBoxModel<String> emptyModel = new DefaultComboBoxModel<>();         //Generates a default model
        emptyModel.addElement(null);                                                    //Adds the empty model a null
        try {                                                                           
            BusinessDAO bdao = new BusinessDAO();                                       //Gets a connetion to the business table
            ResultSet brs = bdao.listBusinessesIdName();                                //Gets the business Id and Name
            businessModel.addElement(null);                                             //Makes the first object of the model null
            while (brs.next()) {                                                        //While theres a line continues
                String businessId = String.valueOf(brs.getInt("business_id"));          //Gets the business id
                String name = brs.getString("name");                                    //Gets the business name
                String str = businessId + "," + name;                                   //Sets the string for the model
                businessModel.addElement(str);                                          //Adds the string to the model
            }
            TrucksDAO tdao = new TrucksDAO();                                           //Gets a connection to the trucks table
            ResultSet trs = tdao.listTrucksIdName();                                    //Gets the trucks Id and Name
            truckModel.addElement(null);                                                //Makes the first object of the model null
            while (trs.next()) {                                                        //While theres a line continues
                String truckId = String.valueOf(trs.getInt("truck_id"));                //Gets the truck id
                String name = trs.getString("name");                                    //Gets the truck name
                String str = truckId + "," + name;                                      //Sets the string for the model
                truckModel.addElement(str);                                             //Adds the string to the model
            }
            view.getSelectBusinessComboBox().setModel(businessModel);                   //Sets the business model
            view.getSelectTruckComboBox().setModel(truckModel);                         //Sets the truck model
            view.getSelectBusinessComboBox().setSelectedIndex(0);                       //Sets the business combo box index to the null.
            view.getSelectTruckComboBox().setSelectedIndex(0);                          //Sets the truck combo box index to the null.
            view.getSelectStoreComboBox().setModel(emptyModel);                         //Sets the store combo box model
            view.getSelectStoreComboBox().setEnabled(false);                            //Disables the store combo box
            view.getSelectSellerComboBox().setModel(emptyModel);                        //Sets the sellers combo box model
            view.getSelectSellerComboBox().setEnabled(false);                           //Disables the sellers combo box
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearEntries() {                                               //Clears all the entries in the view
        view.setAmountTextFieldText("");                                        //Sets the amount text field to blank
        view.setClientNameTextFieldText("");                                    //Sets the client name text field to blank
        view.setClientPhoneNumberTextFieldText("");                             //Sets the phone number text field to blank
        ignoreComboChange = true;                                               //Makes the combo boxes to ignore this changes
        businessId = storeId = sellerId = truckId = 0;                          //Sets the ids to 0
        deliveryDate = pdfRoute = null;                                         //Sets the delivery date and pdf route to null
        view.setSelectDateButtonText("Select Date");                            //Sets the select date button text to the default
        setComboBoxes();                                                        //Resets the combo boxes
        ignoreComboChange = false;                                              //Makes the combo boxes to stop ignoring changes
    }

    public void setIcon() {                                                     //Sets the App Icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    private void innitComponents() {                                            //Initializes the icon, db, sets a title and sets the frame default close operation to a dispose.
        this.setComboBoxes();
        this.view.setDefaultCloseOperation();
        this.view.setTitle("Add Delivery Note");
        setIcon();
    }
}
