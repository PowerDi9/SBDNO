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

        mainJFrameMenuBar = new javax.swing.JMenuBar();
        sbdnoMenu = new javax.swing.JMenu();
        configurationMenuItem = new javax.swing.JMenuItem();
        quitMenuItem = new javax.swing.JMenuItem();
        businessesMenu = new javax.swing.JMenu();
        manageBusinessesMenuItem = new javax.swing.JMenuItem();
        manageStoresMenuItem = new javax.swing.JMenuItem();
        clientsMenu = new javax.swing.JMenu();
        manageClientsMenuItem = new javax.swing.JMenuItem();
        employeesMenu = new javax.swing.JMenu();
        manageEmployeesMenuItem = new javax.swing.JMenuItem();
        trucksMenu = new javax.swing.JMenu();
        manageTrucksMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        businessesMenu.setText("Businesses");
        businessesMenu.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        manageBusinessesMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        manageBusinessesMenuItem.setText("Manage Businesses...");
        businessesMenu.add(manageBusinessesMenuItem);

        manageStoresMenuItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        manageStoresMenuItem.setText("Manage Stores...");
        businessesMenu.add(manageStoresMenuItem);

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
        manageTrucksMenuItem.setText("Manage Trucks");
        trucksMenu.add(manageTrucksMenuItem);

        mainJFrameMenuBar.add(trucksMenu);

        setJMenuBar(mainJFrameMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 662, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
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
    
    public void quitMenuItemActionListener(ActionListener al){
        this.quitMenuItem.addActionListener(al);
    }
    public void manageClientsMenuItemActionListener(ActionListener al){
        this.manageClientsMenuItem.addActionListener(al);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu businessesMenu;
    private javax.swing.JMenu clientsMenu;
    private javax.swing.JMenuItem configurationMenuItem;
    private javax.swing.JMenu employeesMenu;
    private javax.swing.JMenuBar mainJFrameMenuBar;
    private javax.swing.JMenuItem manageBusinessesMenuItem;
    private javax.swing.JMenuItem manageClientsMenuItem;
    private javax.swing.JMenuItem manageEmployeesMenuItem;
    private javax.swing.JMenuItem manageStoresMenuItem;
    private javax.swing.JMenuItem manageTrucksMenuItem;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JMenu sbdnoMenu;
    private javax.swing.JMenu trucksMenu;
    // End of variables declaration//GEN-END:variables
}
