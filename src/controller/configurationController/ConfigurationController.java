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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import view.configurationView.ConfigurationFrame;

public class ConfigurationController {

    ConfigurationFrame view;
    String dailyReportFolderPath, listingFolderPath, personalBusinessHeaderPath= null;
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

    private ActionListener getConfirmChangesButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> array = new ArrayList<>();
                if (view.getEuroRadioButton().isSelected()) {
                    currencyType = view.getEuroRadioButton().getText();
                } else if (view.getDollarRadioButton().isSelected()) {
                    currencyType = view.getDollarRadioButton().getText();
                } else if (view.getPoundRadioButton().isSelected()) {
                    currencyType = view.getPoundRadioButton().getText();
                }
                array.add(currencyType);
                array.add(listingFolderPath);
                array.add(dailyReportFolderPath);
                array.add(personalBusinessHeaderPath);
                saveData(array);
                view.dispose();
            }
        };
        return al;
    }

    private ActionListener getBackButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    private ActionListener getSelectDailyReportFolderButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setAcceptAllFileFilterUsed(false);
                int result = fc.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File folder = fc.getSelectedFile();
                    System.out.println("Selected Folder: " + folder.getAbsolutePath());
                    dailyReportFolderPath = folder.getAbsolutePath();
                }
            }
        };
        return al;
    }

    private ActionListener getSelectListingFolderButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setAcceptAllFileFilterUsed(false);
                int result = fc.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File folder = fc.getSelectedFile();
                    System.out.println("Selected Folder: " + folder.getAbsolutePath());
                    listingFolderPath = folder.getAbsolutePath();
                }
            }
        };
        return al;
    }

    private ActionListener getSelectPersonalBusinessHeaderButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File image = fc.getSelectedFile();
                    System.out.println("Selected Image: " + image.getAbsolutePath());
                    personalBusinessHeaderPath = image.getAbsolutePath();
                }
            }
        };
        return al;
    }

    private void saveData(ArrayList<String> data) {
        try (FileWriter fw = new FileWriter("./data/user_data/config.txt"); PrintWriter pw = new PrintWriter(fw)) {
            for (String d : data) {
                pw.println(d);
            }
            System.out.println("Datos guardados correctamente");
        } catch (IOException e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
        }
    }

    private void setVariables() {
        try (BufferedReader br = new BufferedReader(new FileReader("./data/user_data/config.txt"))) {
            ArrayList<String> datos = new ArrayList<>();
            String linea;
            while ((linea = br.readLine()) != null) {
                datos.add(linea);
            }
            try {
                currencyType = datos.get(0);
                listingFolderPath = datos.get(1);
                dailyReportFolderPath = datos.get(2);
                personalBusinessHeaderPath = datos.get(3);
            }catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "There has been a problem getting the configuration, loading default variables.\nTo set a new configuration, select a currency, folders and header.");
                setDefaultConfiguration();
            }

        } catch (IOException e) {
            System.err.println("Error al leer datos: " + e.getMessage());
        }
    }

    private void setDefaultConfiguration() {
        currencyType = "€";
        listingFolderPath = "./data/default_configuration/ListingsFolder";
        dailyReportFolderPath = "./data/default_configuration/DailyReportFolder";
        personalBusinessHeaderPath = "./data/default_configuration/SBDNO_header.png";
        ArrayList<String> a = new ArrayList<>();
        a.add(currencyType);
        a.add(listingFolderPath);
        a.add(dailyReportFolderPath);
        a.add(personalBusinessHeaderPath);
        saveData(a);
    }

    private void innitComponents() {
        setVariables();
        view.setTitle("Configuration");
        view.setDefaultCloseOperation();
    }
}
