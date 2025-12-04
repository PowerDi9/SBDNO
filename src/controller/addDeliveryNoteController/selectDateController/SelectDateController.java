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
package controller.addDeliveryNoteController.selectDateController;

import controller.addDeliveryNoteController.AddDeliveryNoteController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import view.addDeliveryNoteView.selectDateDialog.SelectDateDialog;

public class SelectDateController {                         //Generic controller for getting the delivery date of the add delivery note view

    private SelectDateDialog view;
    private Date date;
    private AddDeliveryNoteController controller;

    public SelectDateController(SelectDateDialog view, AddDeliveryNoteController controller) {
        this.view = view;
        this.controller = controller;
        this.view.addCancelButtonAl(this.getCancelButtonActionListener());
        this.view.addAcceptButtonAL(this.getAcceptButtonActionListener());
        this.view.setTitle("Select Delivery Date");
        this.setIcon();
    }

    private ActionListener getCancelButtonActionListener() {                    //Gives an action to the Cancel button
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    private ActionListener getAcceptButtonActionListener() {                    //Gives an action to the Accept button
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");      //Sets a simple format
                date = view.getDeliveryDateJCalendar().getDate();               //Gets the selected date
                String simpleDate = sdf.format(date);                           //Changes the JCalendarDate to the simple format
                controller.setDeliveryDate(simpleDate);                         //Gives the date to the controller that oppened this Dialog
                view.dispose();
            }
        };
        return al;
    }

    public void setIcon() {                                                     //Sets the App Icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        this.view.setIconImage(icon.getImage());
    }
}
