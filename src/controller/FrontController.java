package controller;

import controller.manageBusinessesController.ManageBusinessesController;
import controller.manageClientsController.ManageClientsController;
import controller.manageEmployeesController.ManageEmployeesController;
import controller.manageStoresController.ManageStoresController;
import view.MainJFrame;
import database.DBConnection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import view.manageBusinessesView.ManageBusinessesFrame;
import view.manageClientsView.ManageClientsFrame;
import view.manageEmployeesView.ManageEmployeesFrame;
import view.manageStoresView.ManageStoresFrame;

public class FrontController {
    
    private MainJFrame view;
    private DBConnection dbc;
    

    public FrontController(MainJFrame view) throws SQLException {
        this.view = view;
        this.view.quitMenuItemActionListener(this.getQuitMenuItemActionListener());
        this.view.manageBusinessesMenuItemActionListener(this.getManageBusinessesMenuItemActionListener());
        this.view.manageStoresMenuItemActionListener(this.getManageStoresMenuItemActionListener());
        this.view.manageClientsMenuItemActionListener(this.getManageClientsMenuItemActionListener());
        this.view.manageEmployeesMenuItemActionListener(this.getManageEmployeesMenuItemActionListener());
        this.view.manageTrucksMenuItemActionListener(this.getManageTrucksMenuItemActionListener());
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
    
    private ActionListener getManageClientsMenuItemActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageClientsFrame mcf = new ManageClientsFrame();
                ManageClientsController mcc = new ManageClientsController(mcf);
                mcf.setLocationRelativeTo(view);
                mcf.setVisible(true);
            }
        };
        return al;
    }
    
    private ActionListener getManageEmployeesMenuItemActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageEmployeesFrame mef = new ManageEmployeesFrame();
                ManageEmployeesController mec = new ManageEmployeesController(mef);
                mef.setLocationRelativeTo(view);
                mef.setVisible(true);
            }
        };
        return al;
    }
    
    private ActionListener getManageTrucksMenuItemActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        };
        return al;
    }
    
    public void initComponents() throws SQLException{
        dbc = new DBConnection();
        dbc.initDB();
    }
    
}
