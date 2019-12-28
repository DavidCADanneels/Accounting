package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class MealRecipeEditGUI extends JFrame {
    final MealRecipeEditPanel mealRecipeEditPanel

    static HashMap<Accounting, MealRecipeEditGUI> mealsGuis = new HashMap<>()

    MealRecipeEditGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("MEALS"))
        mealRecipeEditPanel = new MealRecipeEditPanel(accounting)
        setContentPane(mealRecipeEditPanel)
        pack()
    }

    static MealRecipeEditGUI showMeals(Accounting accounting) {
        MealRecipeEditGUI gui = mealsGuis.get(accounting)
        if (gui == null) {
            gui = new MealRecipeEditGUI(accounting)
            mealsGuis.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }

    static void fireTableUpdateForAccounting(Accounting accounting){
        MealRecipeEditGUI gui = mealsGuis.get(accounting)
        if (gui){
            gui.fireTableUpdate()
        }
    }

    void fireTableUpdate(){
        mealRecipeEditPanel.fireTableUpdate()
    }
}