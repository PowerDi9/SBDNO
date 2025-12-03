package main;

import view.MainJFrame;
import controller.FrontController;
import java.sql.SQLException;


public class Main {
    
    public static void main(String[] args) throws SQLException{                 //Main of the application
        MainJFrame mainView= new MainJFrame();                                  //Launches the main view
        FrontController fc=new FrontController(mainView);                       //Sets the controller
        mainView.setLocationRelativeTo(null);                                   //Sets its location to the center of the screen
        mainView.setTitle("Small Business Delivery Note Organizer");            //Sets the title
        mainView.setVisible(true);                                              //Makes it visible
    }   
}
