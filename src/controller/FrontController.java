package controller;

import controller.manageBusinessesController.ManageBusinessesController;
import controller.manageStoresController.ManageStoresController;
import view.MainJFrame;
import database.DBConnection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import view.manageBusinessesView.ManageBusinessesFrame;
import view.manageStoresView.ManageStoresFrame;

public class FrontController {
    
    private MainJFrame view;
    private DBConnection dbc;
    

    public FrontController(MainJFrame view) throws SQLException {
        this.view = view;
        this.view.quitMenuItemActionListener(this.getQuitMenuItemActionListener());
        this.view.manageBusinessesMenuItemActionListener(this.getManageBusinessesMenuItemActionListener());
        this.view.manageStoresMenuItemActionListener(this.getManageStoresMenuItemActionListener());
        this.initComponents();
    }
    
    private ActionListener getQuitMenuItemActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
                System.exit(0);
            }
        };
        return al;
    }
    
    private ActionListener getManageBusinessesMenuItemActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageBusinessesFrame mbd = new ManageBusinessesFrame(view, true);
                ManageBusinessesController mbc = new ManageBusinessesController(mbd);
                mbd.setLocationRelativeTo(view);
                mbd.setVisible(true);
            }
        };
        return al;
    }
    
    private ActionListener getManageStoresMenuItemActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageStoresFrame msf = new ManageStoresFrame();
                ManageStoresController msc = new ManageStoresController(msf);
                msf.setLocationRelativeTo(view);
                msf.setVisible(true);
            }
        };
        return al;
    }
    
    public void initComponents() throws SQLException{
        dbc = new DBConnection();
        dbc.initDB();
    }
    
}
