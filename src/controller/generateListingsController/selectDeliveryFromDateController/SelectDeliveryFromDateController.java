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
package controller.generateListingsController.selectDeliveryFromDateController;

import controller.generateListingsController.GenerateListingsController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import view.addDeliveryNoteView.selectDateDialog.SelectDateDialog;

public class SelectDeliveryFromDateController {                                     //Controller for getting the delivery from date when generating listings
    private SelectDateDialog view;
    private Date date;
    private GenerateListingsController controller;

    public SelectDeliveryFromDateController(SelectDateDialog view, GenerateListingsController controller) {
        this.view = view;
        this.controller = controller;
        this.view.addCancelButtonAl(this.getCancelButtonActionListener());
        this.view.addAcceptButtonAL(this.getAcceptButtonActionListener());
        this.view.setTitle("Select Delivery Date");
        this.setIcon();
    }
    
    private ActionListener getCancelButtonActionListener() {                        //Gives the cancel buttton an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }
    
    private ActionListener getAcceptButtonActionListener(){                         //Gives the accept button an action
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                            //Gets the selected date, formats it, and gives it to the controller
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                date = view.getDeliveryDateJCalendar().getDate();
                String simpleDate = sdf.format(date);
                controller.setDeliveryFromDate(simpleDate);
                view.dispose();
            }
        };
        return al;
    }
    
    public void setIcon(){                                                          //Sets the application Icon
         ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
            view.setIconImage(icon.getImage());
    }
}