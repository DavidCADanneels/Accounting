package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Meals;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class MealsGUI extends JFrame {
    private final MealsPanel mealsPanel;

    private static HashMap<Accounting, MealsGUI> mealsGuis = new HashMap<>();

    private MealsGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("MEALS"));
        mealsPanel = new MealsPanel(accounting);
        setContentPane(mealsPanel);
        pack();
    }

    public static MealsGUI showMeals(Accounting accounting) {
        MealsGUI gui = mealsGuis.get(accounting);
        if (gui == null) {
            gui = new MealsGUI(accounting);
            mealsGuis.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void fireMealUsageUpdatedForAll(Accounting accounting){
        MealsGUI gui = mealsGuis.get(accounting);
        if (gui !=null){
            gui.fireMealUsageUpdated();
        }
    }

    public void fireMealUsageUpdated(){
        mealsPanel.fireMealUsageUpdated();
    }
}