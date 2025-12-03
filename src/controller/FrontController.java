package controller;

import controller.addDeliveryNoteController.AddDeliveryNoteController;
import controller.configurationController.ConfigurationController;
import controller.generateDailyReportController.GenerateDailyReportController;
import controller.generateListingsController.GenerateListingsController;
import controller.manageBusinessesController.ManageBusinessesController;
import controller.manageClientsController.ManageClientsController;
import controller.manageEmployeesController.ManageEmployeesController;
import controller.manageSellersController.ManageSellersController;
import controller.manageStoresController.ManageStoresController;
import controller.manageTrucksController.ManageTrucksController;
import controller.searchManageDeliveryNoteController.SearchManageDeliveryNoteController;
import view.MainJFrame;
import database.DBConnection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import view.addDeliveryNoteView.AddDeliveryNoteFrame;
import view.configurationView.ConfigurationFrame;
import view.generateDailyReportView.GenerateDailyReportFrame;
import view.generateListingsView.GenerateListingsFrame;
import view.manageBusinessesView.ManageBusinessesFrame;
import view.manageClientsView.ManageClientsFrame;
import view.manageEmployeesView.ManageEmployeesFrame;
import view.manageSellersView.ManageSellersFrame;
import view.manageStoresView.ManageStoresFrame;
import view.manageTrucksView.ManageTrucksFrame;
import view.searchManageDeliveryNoteView.SearchManageDeliveryNoteFrame;

public class FrontController {          //Front controller of the application, handles launching the rest of the views
    
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
        this.view.manageSellersMenuItemActionListener(this.getManageSellersMenuItemActionListener());
        this.view.addDeliveryNoteMenuItemActionListener(this.getAddDeliveryNoteMenuItemActionListener());
        this.view.searchManageDeliveryNoteMenuItemActionListener(this.getSearchManageDeliveryNotesMenuItemActionListener());
        this.view.configurationMenuItemActionListener(this.getConfigurationMenuItemActionListener());
        this.view.generateListingMenuItemActionListener(this.getGenerateListingActionListener());
        this.view.generateDailyReportMenuItemActionListener(this.getGenerateDailyReportActionListener());
        this.initComponents();
    }
    
    private ActionListener getQuitMenuItemActionListener() {            //Gives an action to the Quit menu item
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
                System.exit(0);
            }
        };
        return al;
    }
    
    private ActionListener getManageBusinessesMenuItemActionListener() {            //Gives an action to the Manage Businesses menu item, launching the view
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
    
    private ActionListener getManageStoresMenuItemActionListener() {            //Gives an action to the Maaage Stores menu item, launching the view
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
    
    private ActionListener getManageClientsMenuItemActionListener() {            //Gives an action to the Manage Clients menu item, launching the view
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
    
    private ActionListener getManageEmployeesMenuItemActionListener() {            //Gives an action to the Manage Employees menu item, launching the view
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
    
    private ActionListener getManageTrucksMenuItemActionListener() {            //Gives an action to the Manage Trucks menu item, launching the view
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageTrucksFrame mtf = new ManageTrucksFrame();
                ManageTrucksController mtc = new ManageTrucksController(mtf);
                mtf.setLocationRelativeTo(view);
                mtf.setVisible(true);
            }
        };
        return al;
    }
    
    private ActionListener getManageSellersMenuItemActionListener() {            //Gives an action to the Manage Seller menu item, launching the view
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageSellersFrame msf = new ManageSellersFrame();
                ManageSellersController mtc = new ManageSellersController(msf);
                msf.setLocationRelativeTo(view);
                msf.setVisible(true);
            }
        };
        return al;
    }
    
    private ActionListener getAddDeliveryNoteMenuItemActionListener(){            //Gives an action to the Add Delivery Note menu item, launching the view
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDeliveryNoteFrame adnf = new AddDeliveryNoteFrame();
                AddDeliveryNoteController adnc = new AddDeliveryNoteController(adnf);
                adnf.setLocationRelativeTo(view);
                adnf.setVisible(true);
            }
        };
        return al;
    }
    
    private ActionListener getSearchManageDeliveryNotesMenuItemActionListener(){            //Gives an action to the Search / Manage Delivery Notes menu item, launching the view
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchManageDeliveryNoteFrame smdnf = new SearchManageDeliveryNoteFrame();
                SearchManageDeliveryNoteController smdnc = new SearchManageDeliveryNoteController(smdnf);
                smdnf.setLocationRelativeTo(view);
                smdnf.setVisible(true);
            }
        };
        return al;
    }
    
    private ActionListener getConfigurationMenuItemActionListener(){            //Gives an action to the Configuration menu item, launching the view
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigurationFrame cf = new ConfigurationFrame();
                ConfigurationController cc = new ConfigurationController(cf);
                cf.setLocationRelativeTo(view);
                cf.setVisible(true);
            }
        };
        return al;
    }
    
    public ActionListener getGenerateListingActionListener(){            //Gives an action to the Generate Listings menu item, launching the view
        ActionListener al = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                GenerateListingsFrame glf = new GenerateListingsFrame();
                GenerateListingsController glc = new GenerateListingsController(glf);
                glf.setLocationRelativeTo(view);
                glf.setVisible(true);
            }
        };
        return al;
    }
    
    public ActionListener getGenerateDailyReportActionListener(){            //Gives an action to the Generate Daily Report menu item, launching the view
        ActionListener al = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                GenerateDailyReportFrame gdrf = new GenerateDailyReportFrame();
                GenerateDailyReportController gdrc = new GenerateDailyReportController(gdrf);
                gdrf.setLocationRelativeTo(view);
                gdrf.setVisible(true);
            }
        };
        return al;
    }
    
    public void setIcon(){                                                   //Sets the App Icon
         ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
            view.setIconImage(icon.getImage());
    }
    
    public void initComponents() throws SQLException{            //Initializes the icon, db and sets a title
        dbc = new DBConnection();
        dbc.initDB();                                            //This line is really important, initializes the db, enabling the Singleton architecture.
        this.setIcon();
        this.view.setTitle("SBDNO       Small Business Delivery Note Organizer");
    }
    
}
