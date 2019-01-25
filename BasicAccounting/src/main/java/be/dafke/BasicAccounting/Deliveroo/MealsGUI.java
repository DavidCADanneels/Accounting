package be.dafke.BasicAccounting.Deliveroo;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.DeliverooMeals;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class MealsGUI extends JFrame {
    private final MealsPanel mealsPanel;

    private static HashMap<Accounting, MealsGUI> mealsGuis = new HashMap<>();

    private MealsGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("MEALS"));
        DeliverooMeals deliverooMeals = accounting.getDeliverooMeals();
        mealsPanel = new MealsPanel(deliverooMeals);
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