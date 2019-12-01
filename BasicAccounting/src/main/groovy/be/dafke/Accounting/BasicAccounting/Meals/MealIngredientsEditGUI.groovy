package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class MealIngredientsEditGUI extends JFrame {
    final MealIngredientsEditPanel mealsEditPanel

    static HashMap<Accounting, MealIngredientsEditGUI> mealsGuis = new HashMap<>()

    MealIngredientsEditGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("MEALS"))
        mealsEditPanel = new MealIngredientsEditPanel(accounting)
        setContentPane(mealsEditPanel)
        pack()
    }

    static MealIngredientsEditGUI showMeals(Accounting accounting) {
        MealIngredientsEditGUI gui = mealsGuis.get(accounting)
        if (gui == null) {
            gui = new MealIngredientsEditGUI(accounting)
            mealsGuis.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }

    static void fireTableUpdateForAccounting(Accounting accounting){
        MealIngredientsEditGUI gui = mealsGuis.get(accounting)
        if (gui !=null){
            gui.fireTableUpdate()
        }
    }

    void fireTableUpdate(){
        mealsEditPanel.fireTableUpdate()
    }
}