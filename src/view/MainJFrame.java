package view;

import java.awt.event.ActionListener;

public class MainJFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainJFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imageLabel = new javax.swing.JLabel();
        mainJFrameMenuBar = new javax.swing.JMenuBar();
        sbdnoMenu = new javax.swing.JMenu();
        configurationMenuItem = new javax.swing.JMenuItem();
        quitMenuItem = new javax.swing.JMenuItem();
        deliveryNoteMenu = new javax.swing.JMenu();
        addDeliveryNoteMenuItem = new javax.swing.JMenuItem();
        searchManageDeliveryNotesMenuItem = new javax.swing.JMenuItem();
        businessesMenu = new javax.swing.JMenu();
        manageBusinessesMenuItem = new javax.swing.JMenuItem();
        manageStoresMenuItem = new javax.swing.JMenuItem();
        manageSellersMenuItem = new javax.swing.JMenuItem();
        clientsMenu = new javax.swing.JMenu();
        manageClientsMenuItem = new javax.swing.JMenuItem();
        employeesMenu = new javax.swing.JMenu();
        manageEmployeesMenuItem = new javax.swing.JMenuItem();
        trucksMenu = new javax.swing.JMenu();
        manageTrucksMenuItem = new javax.swing.JMenuItem();
        reportsMenu = new javax.swing.JMenu();
        generateDailyReportMenuItem = new javax.swing.JMenuItem();
        generateListingMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/SBDNO.png"))); // NOI18N

        sbdnoMenu.setText("SBDNO");
        sbdnoMenu.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        configurationMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        configurationMenuItem.setText("Configuration...");
        sbdnoMenu.add(configurationMenuItem);

        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        quitMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        quitMenuItem.setText("Quit");
        sbdnoMenu.add(quitMenuItem);

        mainJFrameMenuBar.add(sbdnoMenu);

        deliveryNoteMenu.setText("Delivery Notes");
        deliveryNoteMenu.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        addDeliveryNoteMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addDeliveryNoteMenuItem.setText("Add Delivery Note...");
        deliveryNoteMenu.add(addDeliveryNoteMenuItem);

        searchManageDeliveryNotesMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        searchManageDeliveryNotesMenuItem.setText("Search/Manage Delivery Notes...");
        deliveryNoteMenu.add(searchManageDeliveryNotesMenuItem);

        mainJFrameMenuBar.add(deliveryNoteMenu);

        businessesMenu.setText("Businesses");
        businessesMenu.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        manageBusinessesMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        manageBusinessesMenuItem.setText("Manage Businesses...");
        businessesMenu.add(manageBusinessesMenuItem);

        manageStoresMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        manageStoresMenuItem.setText("Manage Stores...");
        businessesMenu.add(manageStoresMenuItem);

        manageSellersMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        manageSellersMenuItem.setText("Manage Sellers...");
        businessesMenu.add(manageSellersMenuItem);

        mainJFrameMenuBar.add(businessesMenu);

        clientsMenu.setText("Clients");
        clientsMenu.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        manageClientsMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        manageClientsMenuItem.setText("Manage Clients...");
        clientsMenu.add(manageClientsMenuItem);

        mainJFrameMenuBar.add(clientsMenu);

        employeesMenu.setText("Employees");
        employeesMenu.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        manageEmployeesMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        manageEmployeesMenuItem.setText("Manage Employees...");
        employeesMenu.add(manageEmployeesMenuItem);

        mainJFrameMenuBar.add(employeesMenu);

        trucksMenu.setText("Trucks");
        trucksMenu.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        manageTrucksMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        manageTrucksMenuItem.setText("Manage Trucks...");
        trucksMenu.add(manageTrucksMenuItem);

        mainJFrameMenuBar.add(trucksMenu);

        reportsMenu.setText("Reports");
        reportsMenu.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        generateDailyReportMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        generateDailyReportMenuItem.setText("Generate Daily Report...");
        reportsMenu.add(generateDailyReportMenuItem);

        generateListingMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        generateListingMenuItem.setText("Generate Listing...");
        reportsMenu.add(generateListingMenuItem);

        mainJFrameMenuBar.add(reportsMenu);

        setJMenuBar(mainJFrameMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imageLabel, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imageLabel)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void manageBusinessesMenuItemActionListener(ActionListener al){
        this.manageBusinessesMenuItem.addActionListener(al);
    }
    
    public void manageStoresMenuItemActionListener(ActionListener al){
        this.manageStoresMenuItem.addActionListener(al);
    }
    
    public void configurationMenuItemActionListener(ActionListener al){
        this.configurationMenuItem.addActionListener(al);
    }
    
    public void manageEmployeesMenuItemActionListener(ActionListener al){
        this.manageEmployeesMenuItem.addActionListener(al);
    }
    
    public void manageTrucksMenuItemActionListener(ActionListener al){
        this.manageTrucksMenuItem.addActionListener(al);
    }
    
    public void manageSellersMenuItemActionListener(ActionListener al){
        this.manageSellersMenuItem.addActionListener(al);
    }
    
    public void quitMenuItemActionListener(ActionListener al){
        this.quitMenuItem.addActionListener(al);
    }
    
    public void manageClientsMenuItemActionListener(ActionListener al){
        this.manageClientsMenuItem.addActionListener(al);
    }
    
    public void addDeliveryNoteMenuItemActionListener(ActionListener al){
        this.addDeliveryNoteMenuItem.addActionListener(al);
    }
    
    public void searchManageDeliveryNoteMenuItemActionListener(ActionListener al){
        this.searchManageDeliveryNotesMenuItem.addActionListener(al);
    }
    
    public void generateListingMenuItemActionListener(ActionListener al){
        this.generateListingMenuItem.addActionListener(al);
    }
    
    public void generateDailyReportMenuItemActionListener(ActionListener al){
        this.generateDailyReportMenuItem.addActionListener(al);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addDeliveryNoteMenuItem;
    private javax.swing.JMenu businessesMenu;
    private javax.swing.JMenu clientsMenu;
    private javax.swing.JMenuItem configurationMenuItem;
    private javax.swing.JMenu deliveryNoteMenu;
    private javax.swing.JMenu employeesMenu;
    private javax.swing.JMenuItem generateDailyReportMenuItem;
    private javax.swing.JMenuItem generateListingMenuItem;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JMenuBar mainJFrameMenuBar;
    private javax.swing.JMenuItem manageBusinessesMenuItem;
    private javax.swing.JMenuItem manageClientsMenuItem;
    private javax.swing.JMenuItem manageEmployeesMenuItem;
    private javax.swing.JMenuItem manageSellersMenuItem;
    private javax.swing.JMenuItem manageStoresMenuItem;
    private javax.swing.JMenuItem manageTrucksMenuItem;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JMenu reportsMenu;
    private javax.swing.JMenu sbdnoMenu;
    private javax.swing.JMenuItem searchManageDeliveryNotesMenuItem;
    private javax.swing.JMenu trucksMenu;
    // End of variables declaration//GEN-END:variables
}
