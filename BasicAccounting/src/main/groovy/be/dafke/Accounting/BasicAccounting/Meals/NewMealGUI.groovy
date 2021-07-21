package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Meals

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class NewMealGUI extends JFrame {
    final NewMealPanel newMealPanel

    static HashMap<Meals, NewMealGUI> mealsGuis = new HashMap<>()

    NewMealGUI(Meals meals) {
        super(getBundle("Accounting").getString("MEALS_DETAILS"))
        newMealPanel = new NewMealPanel(meals)
        setContentPane(newMealPanel)
        pack()
    }

    static NewMealGUI showMeals(Meals meals) {
        NewMealGUI gui = mealsGuis.get(meals)
        if (gui == null) {
            gui = new NewMealGUI(meals)
            mealsGuis.put(meals, gui)
            Main.addFrame(gui)
        }
        gui
    }
}