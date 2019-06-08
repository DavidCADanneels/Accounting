package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Allergenes;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class AllergenesGUI extends JFrame {
    private final AllergenesPanel allergenesPanel;

    private static final HashMap<Allergenes, AllergenesGUI> guis = new HashMap<>();

    private AllergenesGUI(Allergenes allergenes) {
        super(getBundle("Accounting").getString("ALLERGENES"));
        allergenesPanel = new AllergenesPanel(allergenes);
        setContentPane(allergenesPanel);
        pack();
    }

    public static AllergenesGUI showAllergenes(Allergenes allergenes) {
        AllergenesGUI gui = guis.get(allergenes);
        if (gui == null) {
            gui = new AllergenesGUI(allergenes);
            guis.put(allergenes, gui);
            Main.addFrame(gui);
        }
        return gui;
    }
}