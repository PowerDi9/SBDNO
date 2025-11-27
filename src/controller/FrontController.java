package controller;

import controller.manageBusinessesController.ManageBusinessesController;
import view.MainJFrame;
import database.DBConnection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import view.manageBusinessesView.ManageBusinessesFrame;

public class FrontController {
    
    private MainJFrame view;
    private DBConnection dbc;
    

    public FrontController(MainJFrame view) throws SQLException {
        this.view = view;
        this.view.quitMenuItemActionListener(this.getQuitMenuItemActionListener());
        this.view.manageBusinessesMenuItemActionListener(this.getManageBusinessesMenuItemActionListener());
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
                ManageBusinessesFrame mbf = new ManageBusinessesFrame(view, true);
                ManageBusinessesController mbc = new ManageBusinessesController(mbf);
                mbf.setLocationRelativeTo(view);
                mbf.setVisible(true);
            }
        };
        return al;
    }
    
    public void initComponents() throws SQLException{
        dbc = new DBConnection();
        dbc.initDB();
    }
    
}
