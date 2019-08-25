package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class MealIngredientsViewGUI extends JFrame {
    private final MealIngredientsViewPanel mealsEditPanel;

    private static HashMap<Accounting, MealIngredientsViewGUI> mealsGuis = new HashMap<>();

    private MealIngredientsViewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("MEALS"));
        mealsEditPanel = new MealIngredientsViewPanel(accounting);
        setContentPane(mealsEditPanel);
        pack();
    }

    public static MealIngredientsViewGUI showMeals(Accounting accounting) {
        MealIngredientsViewGUI gui = mealsGuis.get(accounting);
        if (gui == null) {
            gui = new MealIngredientsViewGUI(accounting);
            mealsGuis.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void fireTableUpdateForAccounting(Accounting accounting){
        MealIngredientsViewGUI gui = mealsGuis.get(accounting);
        if (gui !=null){
            gui.fireTableUpdate();
        }
    }

    public void fireTableUpdate(){
        mealsEditPanel.fireTableUpdate();
    }
}