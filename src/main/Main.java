package main;

import view.MainJFrame;
import controller.FrontController;
import java.sql.SQLException;


public class Main {
    
    public static void main(String[] args) throws SQLException{
        MainJFrame mainView= new MainJFrame();
        FrontController fc=new FrontController(mainView);
        mainView.setLocationRelativeTo(null);
        mainView.setTitle("Small Business Delivery Note Organizer");
        mainView.setVisible(true);
    }   
}
