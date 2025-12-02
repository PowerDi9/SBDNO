package controller.searchManageDeliveryNoteController.editDeliveryNoteController.selectDateController;

import controller.searchManageDeliveryNoteController.editDeliveryNoteController.EditDeliveryNoteController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import view.addDeliveryNoteView.selectDateDialog.SelectDateDialog;

public class SelectDateController {

    private SelectDateDialog view;
    private Date date;
    private EditDeliveryNoteController controller;

    public SelectDateController(SelectDateDialog view, EditDeliveryNoteController controller) {
        this.view = view;
        this.controller = controller;
        this.view.addCancelButtonAl(this.getCancelButtonActionListener());
        this.view.addAcceptButtonAL(this.getAcceptButtonActionListener());
        this.view.setTitle("Select Delivery Date");
        this.setIcon();
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

    private ActionListener getAcceptButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                date = view.getDeliveryDateJCalendar().getDate();
                String simpleDate = sdf.format(date);
                controller.setDeliveryDate(simpleDate);
                view.dispose();
            }
        };
        return al;
    }

    public void setIcon() {
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }
}
