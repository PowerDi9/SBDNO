package controller;

import view.MainJFrame;
import database.CrearBD;

public class FrontController {
    
    private MainJFrame view;
    private CrearBD cbs;

    public FrontController(MainJFrame view) {
        this.view = view;
        this.initComponents();
    }
    
    public void initComponents(){
        cbs = new CrearBD();
        cbs.CrearBD();
    }
    
}
