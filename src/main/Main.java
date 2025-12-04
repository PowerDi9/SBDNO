/*
 * SBDNO - Small Business Delivery Note Organizer
 * 
 * Copyright (C) 2025 Adrián González Hermida
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
