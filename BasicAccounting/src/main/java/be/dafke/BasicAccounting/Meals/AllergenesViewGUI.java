package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Allergenes;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class AllergenesViewGUI extends JFrame {
    private final AllergenesViewPanel allergenesEditPanel;

    private static final HashMap<Allergenes, AllergenesViewGUI> guis = new HashMap<>();

    private AllergenesViewGUI(Allergenes allergenes) {
        super(getBundle("Accounting").getString("ALLERGENES"));
        allergenesEditPanel = new AllergenesViewPanel();
        allergenesEditPanel.setAllergenes(allergenes);
        setContentPane(allergenesEditPanel);
        pack();
    }

    public static AllergenesViewGUI showAllergenes(Allergenes allergenes) {
        AllergenesViewGUI gui = guis.get(allergenes);
        if (gui == null) {
            gui = new AllergenesViewGUI(allergenes);
            guis.put(allergenes, gui);
            Main.addFrame(gui);
        }
        return gui;
    }
}