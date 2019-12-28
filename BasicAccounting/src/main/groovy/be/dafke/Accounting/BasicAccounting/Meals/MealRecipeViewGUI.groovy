package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class MealRecipeViewGUI extends JFrame {
    final MealRecipeViewPanel mealsEditPanel

    static HashMap<Accounting, MealRecipeViewGUI> mealsGuis = new HashMap<>()

    MealRecipeViewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("MEALS"))
        mealsEditPanel = new MealRecipeViewPanel(accounting)
        setContentPane(mealsEditPanel)
        pack()
    }

    static MealRecipeViewGUI showMeals(Accounting accounting) {
        MealRecipeViewGUI gui = mealsGuis.get(accounting)
        if (gui == null) {
            gui = new MealRecipeViewGUI(accounting)
            mealsGuis.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }

    static void fireTableUpdateForAccounting(Accounting accounting){
        MealRecipeViewGUI gui = mealsGuis.get(accounting)
        if (gui){
            gui.fireTableUpdate()
        }
    }

    void fireTableUpdate(){
        mealsEditPanel.fireTableUpdate()
    }
}