package controller.searchManageDeliveryNoteController.filterDeliveryNotesController.selectAdditionFromDateController;

import controller.searchManageDeliveryNoteController.filterDeliveryNotesController.FilterDeliveryNotesController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import view.addDeliveryNoteView.selectDateDialog.SelectDateDialog;

public class SelectAdditionFromDateController {
    private SelectDateDialog view;
    private Date date;
    private FilterDeliveryNotesController controller;

    public SelectAdditionFromDateController(SelectDateDialog view, FilterDeliveryNotesController controller) {
        this.view = view;
        this.controller = controller;
        this.view.addCancelButtonAl(this.getCancelButtonActionListener());
        this.view.addAcceptButtonAL(this.getAcceptButtonActionListener());
        this.view.setTitle("Select Delivery Date");
    }
    
    private ActionListener getCancelButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }
    
    private ActionListener getAcceptButtonActionListener(){
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                date = view.getDeliveryDateJCalendar().getDate();
                String simpleDate = sdf.format(date);
                controller.setAdditionFromDate(simpleDate);
                view.dispose();
            }
        };
        return al;
    }
    
}
