package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.Accounting;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class MealIngredientsEditGUI extends JFrame {
    private final MealIngredientsEditPanel mealsEditPanel;

    private static HashMap<Accounting, MealIngredientsEditGUI> mealsGuis = new HashMap<>();

    private MealIngredientsEditGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("MEALS"));
        mealsEditPanel = new MealIngredientsEditPanel(accounting);
        setContentPane(mealsEditPanel);
        pack();
    }

    public static MealIngredientsEditGUI showMeals(Accounting accounting) {
        MealIngredientsEditGUI gui = mealsGuis.get(accounting);
        if (gui == null) {
            gui = new MealIngredientsEditGUI(accounting);
            mealsGuis.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void fireTableUpdateForAccounting(Accounting accounting){
        MealIngredientsEditGUI gui = mealsGuis.get(accounting);
        if (gui !=null){
            gui.fireTableUpdate();
        }
    }

    public void fireTableUpdate(){
        mealsEditPanel.fireTableUpdate();
    }
}