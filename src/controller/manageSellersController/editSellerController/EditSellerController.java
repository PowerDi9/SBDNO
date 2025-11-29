package controller.manageSellersController.editSellerController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.dao.SellersDAO;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import model.dao.StoresDAO;
import view.manageSellersView.ManageSellersFrame;
import view.manageSellersView.editSellerView.EditSellerDialog;

public class EditSellerController {
    
    EditSellerDialog view = null;
    ManageSellersFrame view2 = null;
    String sellerId, storeId, sellerName = null;

    public EditSellerController(EditSellerDialog view, ManageSellersFrame view2, String sellerId, String storeId, String sellerName) {
        this.view = view;
        this.view2 = view2;
        this.sellerId = sellerId;
        this.storeId = storeId;
        this.sellerName = sellerName;
        this.view.addAcceptButtonAL(this.getAcceptButtonActionListener());
        this.view.addCancelButtonAL(this.getCancelButtonActionListener());
        this.initComponents();
    }

    public ActionListener getAcceptButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    storeId = view.getSelectStoreComboBox().getSelectedItem().toString().split(",")[0];
                    SellersDAO dao = new SellersDAO();
                    dao.editSeller(sellerId, storeId, view.getSellerNameTextFieldText());
                    updateEditSellersModel();
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

    public void updateEditSellersModel() {
        view2.clearSellers();
        try {
            SellersDAO dao = new SellersDAO();
            ResultSet rs = dao.listSellers();
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getInt("store_id"));
                row.add(rs.getInt("seller_id"));
                row.add(rs.getString("store_name"));
                row.add(rs.getString("seller_name"));
                view2.addSeller(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < view2.getEditSellersTable().getColumnCount(); i++) {
            view2.getEditSellersTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    
    private void setSelectStoreComboBoxModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            StoresDAO dao = new StoresDAO();
            ResultSet rs = dao.listStoresIdName();
            int index = 0;
            int con = 0;
            while (rs.next()) {
                String storeId = String.valueOf(rs.getInt("store_id"));
                String name = rs.getString("name");
                String str = storeId + "," + name;
                if(Integer.parseInt(this.storeId) == Integer.parseInt(storeId)){
                    index = con;
                }
                model.addElement(str);
                con ++;
            }
            view.getSelectStoreComboBox().setModel(model);
            view.getSelectStoreComboBox().setSelectedIndex(index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initComponents() {
        view.setSellerNameTextFieldText(sellerName);
        view.setTitle("Edit Sellers");
        this.setSelectStoreComboBoxModel();
    }
}
