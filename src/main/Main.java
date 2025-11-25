package main;

import view.MainJFrame;
import controller.FrontController;


public class Main {
    
    public static void main(String[] args){
        MainJFrame mainView= new MainJFrame();
        FrontController fc=new FrontController(mainView);
        mainView.setLocationRelativeTo(null);
        mainView.setVisible(true);
    }   
}
