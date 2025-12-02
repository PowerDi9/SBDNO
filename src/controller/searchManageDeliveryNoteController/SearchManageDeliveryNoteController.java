package controller.searchManageDeliveryNoteController;

import controller.searchManageDeliveryNoteController.editDeliveryNoteController.EditDeliveryNoteController;
import controller.searchManageDeliveryNoteController.filterDeliveryNotesController.FilterDeliveryNotesController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import model.dao.DeliveryNoteDAO;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.BusinessDAO;
import model.dao.ClientsDAO;
import model.dao.SellersDAO;
import model.dao.StoresDAO;
import model.dao.TrucksDAO;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import view.searchManageDeliveryNoteView.SearchManageDeliveryNoteFrame;
import view.searchManageDeliveryNoteView.editDeliveryNoteView.EditDeliveryNoteFrame;
import view.searchManageDeliveryNoteView.filterDeliveryNotesView.FilterDeliveryNotesDialog;

public class SearchManageDeliveryNoteController {

    SearchManageDeliveryNoteFrame view;
    String deliveryId, introductionDate, deliveryDate, clientId, clientPhone, clientName, businessId, businessName, storeId, storeName, sellerId, sellerName, truckId, truckName, amount, pdfPath = null;

    public SearchManageDeliveryNoteController(SearchManageDeliveryNoteFrame view) {
        this.view = view;
        this.view.addBackButtonAL(this.getBackButtonActionListener());
        this.view.addSearchManageDeliveryNoteTableMouseListener(this.getSearchManageDeliveryNotesTableMouseListener());
        this.view.addDeleteDeliveryNoteButtonAL(this.getDeleteDeliveryNoteButtonActionListener());
        this.view.addEditDeliveryNoteButtonAL(this.getEditDeliveryNoteActionListener());
        this.view.addViewPDFButtonAL(this.getViewPDFButtonActionListener());
        this.view.addFilterDeliveryNotesButtonAL(this.getFilterDeliveryNotesButtonActionListener());
        this.innitcomponents();
    }

    private MouseListener getSearchManageDeliveryNotesTableMouseListener() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = view.getSearchManageDeliveryNoteTable().rowAtPoint(evt.getPoint());
                deliveryId = view.getSearchManageDeliveryNoteTableIDAt(row, 0);
                introductionDate = view.getSearchManageDeliveryNoteTableIDAt(row, 1);
                deliveryDate = view.getSearchManageDeliveryNoteTableIDAt(row, 2);
                clientId = view.getSearchManageDeliveryNoteTableIDAt(row, 3).split(",")[0];
                clientName = view.getSearchManageDeliveryNoteTableIDAt(row, 3).split(",")[1];
                clientPhone = view.getSearchManageDeliveryNoteTableIDAt(row, 4);
                businessId = view.getSearchManageDeliveryNoteTableIDAt(row, 5).split(",")[0];
                businessName = view.getSearchManageDeliveryNoteTableIDAt(row, 5).split(",")[1];
                storeId = view.getSearchManageDeliveryNoteTableIDAt(row, 6).split(",")[0];
                storeName = view.getSearchManageDeliveryNoteTableIDAt(row, 6).split(",")[1];
                sellerId = view.getSearchManageDeliveryNoteTableIDAt(row, 7).split(",")[0];
                sellerName = view.getSearchManageDeliveryNoteTableIDAt(row, 7).split(",")[1];
                truckId = view.getSearchManageDeliveryNoteTableIDAt(row, 8).split(",")[0];
                truckName = view.getSearchManageDeliveryNoteTableIDAt(row, 8).split(",")[1];
                amount = view.getSearchManageDeliveryNoteTableIDAt(row, 9);
                pdfPath = view.getSearchManageDeliveryNoteTableIDAt(row, 10);
            }
        };
        return ma;
    }

    private ActionListener getBackButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    private ActionListener getViewPDFButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pdfPath == null) {
                    JOptionPane.showMessageDialog(view, "Please select a delivery note to view its PDF");
                }
                try {
                    SwingController controller = new SwingController();
                    SwingViewBuilder factory = new SwingViewBuilder(controller);
                    JPanel viewerComponentPanel = factory.buildViewerPanel();

                    JFrame frame = new JFrame("PDF Viewer");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.getContentPane().add(viewerComponentPanel);
                    frame.setSize(800, 600);
                    frame.setVisible(true);
                    controller.openDocument(pdfPath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "An error ocurred trying to open the PDF");
                }
            }
        };
        return al;
    }

    private ActionListener getDeleteDeliveryNoteButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (deliveryId == null) {
                        JOptionPane.showMessageDialog(view, "Please select a delivery note to delete.");
                        return;
                    } else {
                        int option = JOptionPane.showConfirmDialog(null, "Are you sure to delete this delivery note?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
                    }
                    DeliveryNoteDAO dao = new DeliveryNoteDAO();
                    dao.deleteDeliveryNote(Integer.parseInt(deliveryId));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                updateSearchManageDeliveryNotesModel();
            }
        };
        return al;
    }

    private ActionListener getEditDeliveryNoteActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (deliveryId == null) {
                    JOptionPane.showMessageDialog(view, "Please select a delivery note to edit.");
                    return;
                }
                EditDeliveryNoteFrame ednf = new EditDeliveryNoteFrame();
                EditDeliveryNoteController ednc = new EditDeliveryNoteController(ednf, view, deliveryId, introductionDate, deliveryDate, clientId, businessId, storeId, sellerId, truckId, amount, pdfPath);
                ednf.setLocationRelativeTo(view);
                ednf.setVisible(true);
            }
        };
        return al;
    }

    private ActionListener getFilterDeliveryNotesButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FilterDeliveryNotesDialog fdnd = new FilterDeliveryNotesDialog(view, true);
                FilterDeliveryNotesController fdnc = new FilterDeliveryNotesController(fdnd, view);
                fdnd.setLocationRelativeTo(view);
                fdnd.setVisible(true);
            }
        };
        return al;
    }

    private void updateSearchManageDeliveryNotesModel() {
        view.clearDeliveryNotes();
        try {
            DeliveryNoteDAO deliveryNoteDao = new DeliveryNoteDAO();
            ClientsDAO clientsDao = new ClientsDAO();
            BusinessDAO businessDao = new BusinessDAO();
            StoresDAO storesDao = new StoresDAO();
            SellersDAO sellersDao = new SellersDAO();
            TrucksDAO trucksDao = new TrucksDAO();
            ResultSet rs = deliveryNoteDao.listDeliveryNotes();
            while (rs.next()) {
                String cName = null, cPhone = null, bName = null, sName = null, seName = null, tName = null;
                Vector row = new Vector();
                int cId = rs.getInt("client_id");
                ResultSet clientRs = clientsDao.getClientNamePhone(cId);
                while (clientRs.next()) {
                    cName = clientRs.getString("name");
                    cPhone = clientRs.getString("phone");
                }
                int bId = rs.getInt("business_id");
                ResultSet businessRs = businessDao.getBusinessName(bId);
                while (businessRs.next()) {
                    bName = businessRs.getString("name");
                }
                int sId = rs.getInt("store_id");
                ResultSet storesRs = storesDao.getStoreName(sId);
                while (storesRs.next()) {
                    sName = storesRs.getString("name");
                }
                int seId = rs.getInt("seller_id");
                ResultSet sellersRs = sellersDao.getSellerName(seId);
                while (sellersRs.next()) {
                    seName = sellersRs.getString("name");
                }
                int tId = rs.getInt("truck_id");
                ResultSet trucksRs = trucksDao.getTruckName(tId);
                while (trucksRs.next()) {
                    tName = trucksRs.getString("name");
                }
                row.add(rs.getInt("delivery_note_id"));
                row.add(rs.getString("date"));
                row.add(rs.getString("delivery_date"));
                row.add(cId + "," + cName);
                row.add(cPhone);
                row.add(bId + "," + bName);
                row.add(sId + "," + sName);
                row.add(seId + "," + seName);
                row.add(tId + "," + tName);
                row.add(rs.getDouble("amount"));
                row.add(rs.getString("pdf_path"));
                view.addDeliveryNote(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view.getSearchManageDeliveryNoteTable().getColumnCount(); i++) {
            view.getSearchManageDeliveryNoteTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void setIcon() {
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        view.setIconImage(icon.getImage());
    }

    private void innitcomponents() {
        this.setIcon();
        view.setTitle("Search / Manage Delivery Notes");
        this.updateSearchManageDeliveryNotesModel();
        view.setDefaultCloseOperation();
    }
}
