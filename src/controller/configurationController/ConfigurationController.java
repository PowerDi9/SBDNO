package controller.configurationController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import view.configurationView.ConfigurationFrame;

public class ConfigurationController {                                          //Controller for the configuration view.

    ConfigurationFrame view;
    String dailyReportFolderPath, listingFolderPath, personalBusinessHeaderPath = null;
    String currencyType = "€";

    public ConfigurationController(ConfigurationFrame view) {
        this.view = view;
        this.view.addBackButtonAL(this.getBackButtonActionListener());
        this.view.addSelectDailyReportFolderButtonAL(this.getSelectDailyReportFolderButtonActionListener());
        this.view.addSelectListingFolderButtonAL(this.getSelectListingFolderButtonActionListener());
        this.view.addSelectPersonalBusinessHeaderButtonAL(this.getSelectPersonalBusinessHeaderButtonActionListener());
        this.view.addConfirmChangesButtonAL(this.getConfirmChangesButtonActionListener());
        innitComponents();
    }

    private ActionListener getConfirmChangesButtonActionListener() {            //Gives an action to the Confirm Changes button
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> array = new ArrayList<>();
                if (view.getEuroRadioButton().isSelected()) {                   //Gets the currency type
                    currencyType = view.getEuroRadioButton().getText();
                } else if (view.getDollarRadioButton().isSelected()) {
                    currencyType = view.getDollarRadioButton().getText();
                } else if (view.getPoundRadioButton().isSelected()) {
                    currencyType = view.getPoundRadioButton().getText();
                }
                array.add(currencyType);                                        //Adds all the config options to an array
                array.add(listingFolderPath);
                array.add(dailyReportFolderPath);
                array.add(personalBusinessHeaderPath);
                saveData(array);                                                //Saves the data
                view.dispose();                                                 //Closes the view
            }
        };
        return al;
    }

    private ActionListener getBackButtonActionListener() {                      //Gives an action to the Back button
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    private ActionListener getSelectDailyReportFolderButtonActionListener() {                   //Gives an action to the Select Daily Report Folder button
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();                                           //Creates a FoleChooser
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);                         //Makes it only accept directories
                fc.setAcceptAllFileFilterUsed(false);                                           //Makes so it doesen't accept all files
                int result = fc.showOpenDialog(view);                                           //Shows it and gets the result
                if (result == JFileChooser.APPROVE_OPTION) {                                    //If the user confirms a path its stored
                    File folder = fc.getSelectedFile();
                    System.out.println("Selected Folder: " + folder.getAbsolutePath());
                    dailyReportFolderPath = folder.getAbsolutePath();
                }
            }
        };
        return al;
    }

    private ActionListener getSelectListingFolderButtonActionListener() {                       //Gives an action to the Select Listing Folder button
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();                                           //Creates a JFileChooser
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);                         //Makes it only accept directories
                fc.setAcceptAllFileFilterUsed(false);                                           //Makes so it doesen't accept all files
                int result = fc.showOpenDialog(view);                                           //Shows it and gets the result
                if (result == JFileChooser.APPROVE_OPTION) {                                    //If the user confirms a path its stored
                    File folder = fc.getSelectedFile();
                    System.out.println("Selected Folder: " + folder.getAbsolutePath());
                    listingFolderPath = folder.getAbsolutePath();
                }
            }
        };
        return al;
    }

    private ActionListener getSelectPersonalBusinessHeaderButtonActionListener() {              //Gives an action to the  Select Personal Business Header Button
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();                                           //Creates a JFileChooser
                int result = fc.showOpenDialog(null);                                           //Shows it and gets the result
                if (result == JFileChooser.APPROVE_OPTION) {                                    //If the user confirms a path its stored
                    File image = fc.getSelectedFile();
                    System.out.println("Selected Image: " + image.getAbsolutePath());
                    personalBusinessHeaderPath = image.getAbsolutePath();
                }
            }
        };
        return al;
    }

    private void saveData(ArrayList<String> data) {                                                                     //Method for saving the data
        try (FileWriter fw = new FileWriter("./data/user_data/config.txt"); PrintWriter pw = new PrintWriter(fw)) {     //Sets the file to write on
            for (String d : data) {                                                                                     //For each string in the data array prints the line on the config file
                pw.println(d);
            }                                                    
        } catch (IOException e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
        }
    }

    private void setVariables() {                                                                                       //Gets the pre-stablished variables and sets it
        try (BufferedReader br = new BufferedReader(new FileReader("./data/user_data/config.txt"))) {                   //Gets the file to read
            ArrayList<String> datos = new ArrayList<>();                                                                //Sets an array with the data
            String linea;
            while ((linea = br.readLine()) != null) {
                datos.add(linea);
            }
            try {                                                                                                       //Gets the data and sets it on the variables
                currencyType = datos.get(0);
                listingFolderPath = datos.get(1);
                dailyReportFolderPath = datos.get(2);
                personalBusinessHeaderPath = datos.get(3);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "There has been a problem getting the configuration, loading default variables.\nTo set a new configuration, select a currency, folders and header.");
                setDefaultConfiguration();                                                                              //If there's a problem with the data sets the default config
            }

        } catch (IOException e) {
            System.err.println("Error al leer datos: " + e.getMessage());
        }
    }

    private void setDefaultConfiguration() {                                                //Sets the default configuration
        currencyType = "€";
        listingFolderPath = "./data/default_configuration/ListingsFolder";
        dailyReportFolderPath = "./data/default_configuration/DailyReportFolder";
        personalBusinessHeaderPath = "./data/default_configuration/SBDNO_header.png";
        ArrayList<String> a = new ArrayList<>();
        a.add(currencyType);
        a.add(listingFolderPath);
        a.add(dailyReportFolderPath);
        a.add(personalBusinessHeaderPath);
        saveData(a);                                                                        //Saves the default configuration
    }

    public void setIcon() {                                                                 //Sets the App Icon
        ImageIcon icon = new ImageIcon("resources/SBDNO_icon.png");
        this.view.setIconImage(icon.getImage());
    }

    private void innitComponents() {                                                        //Initializes the variables, sets the title, default close operation to dispose and sets the App Icon
        setVariables();
        view.setTitle("Configuration");
        view.setDefaultCloseOperation();
        setIcon();
    }
}
