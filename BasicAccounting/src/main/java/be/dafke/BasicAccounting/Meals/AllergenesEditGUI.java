package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.Allergenes;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class AllergenesEditGUI extends JFrame {
    private final AllergenesEditPanel allergenesEditPanel;

    private static final HashMap<Allergenes, AllergenesEditGUI> guis = new HashMap<>();

    private AllergenesEditGUI(Allergenes allergenes) {
        super(getBundle("Accounting").getString("ALLERGENES"));
        allergenesEditPanel = new AllergenesEditPanel(allergenes);
        setContentPane(allergenesEditPanel);
        pack();
    }

    public static AllergenesEditGUI showAllergenes(Allergenes allergenes) {
        AllergenesEditGUI gui = guis.get(allergenes);
        if (gui == null) {
            gui = new AllergenesEditGUI(allergenes);
            guis.put(allergenes, gui);
            Main.addFrame(gui);
        }
        return gui;
    }
}