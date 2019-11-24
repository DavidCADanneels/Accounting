package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.Accounting;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class MealRecipeEditGUI extends JFrame {
    private final MealRecipeEditPanel mealRecipeEditPanel;

    private static HashMap<Accounting, MealRecipeEditGUI> mealsGuis = new HashMap<>();

    private MealRecipeEditGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("MEALS"));
        mealRecipeEditPanel = new MealRecipeEditPanel(accounting);
        setContentPane(mealRecipeEditPanel);
        pack();
    }

    public static MealRecipeEditGUI showMeals(Accounting accounting) {
        MealRecipeEditGUI gui = mealsGuis.get(accounting);
        if (gui == null) {
            gui = new MealRecipeEditGUI(accounting);
            mealsGuis.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void fireTableUpdateForAccounting(Accounting accounting){
        MealRecipeEditGUI gui = mealsGuis.get(accounting);
        if (gui !=null){
            gui.fireTableUpdate();
        }
    }

    public void fireTableUpdate(){
        mealRecipeEditPanel.fireTableUpdate();
    }
}