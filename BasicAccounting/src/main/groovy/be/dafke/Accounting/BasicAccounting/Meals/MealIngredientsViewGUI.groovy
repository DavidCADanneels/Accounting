package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class MealIngredientsViewGUI extends JFrame {
    final MealIngredientsViewPanel mealsEditPanel

    static HashMap<Accounting, MealIngredientsViewGUI> mealsGuis = new HashMap<>()

    MealIngredientsViewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("MEALS"))
        mealsEditPanel = new MealIngredientsViewPanel(accounting)
        setContentPane(mealsEditPanel)
        pack()
    }

    static MealIngredientsViewGUI showMeals(Accounting accounting) {
        MealIngredientsViewGUI gui = mealsGuis.get(accounting)
        if (gui == null) {
            gui = new MealIngredientsViewGUI(accounting)
            mealsGuis.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }

    static void fireTableUpdateForAccounting(Accounting accounting){
        MealIngredientsViewGUI gui = mealsGuis.get(accounting)
        if (gui){
            gui.fireTableUpdate()
        }
    }

    void fireTableUpdate(){
        mealsEditPanel.fireTableUpdate()
    }
}