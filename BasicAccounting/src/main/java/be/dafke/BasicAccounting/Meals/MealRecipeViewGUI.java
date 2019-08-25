package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class MealRecipeViewGUI extends JFrame {
    private final MealRecipeViewPanel mealsEditPanel;

    private static HashMap<Accounting, MealRecipeViewGUI> mealsGuis = new HashMap<>();

    private MealRecipeViewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("MEALS"));
        mealsEditPanel = new MealRecipeViewPanel(accounting);
        setContentPane(mealsEditPanel);
        pack();
    }

    public static MealRecipeViewGUI showMeals(Accounting accounting) {
        MealRecipeViewGUI gui = mealsGuis.get(accounting);
        if (gui == null) {
            gui = new MealRecipeViewGUI(accounting);
            mealsGuis.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void fireTableUpdateForAccounting(Accounting accounting){
        MealRecipeViewGUI gui = mealsGuis.get(accounting);
        if (gui !=null){
            gui.fireTableUpdate();
        }
    }

    public void fireTableUpdate(){
        mealsEditPanel.fireTableUpdate();
    }
}