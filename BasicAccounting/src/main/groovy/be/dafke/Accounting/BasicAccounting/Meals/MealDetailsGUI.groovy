package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Meal

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class MealDetailsGUI extends JFrame {
    final MealDetailsPanel newMealPanel

    static HashMap<Meal, MealDetailsGUI> mealsGuis = new HashMap<>()

    MealDetailsGUI(Meal meal) {
        super(getBundle("Accounting").getString("MEALS_DETAILS"))
        newMealPanel = new MealDetailsPanel(meal)
        setContentPane(newMealPanel)
        pack()
    }

    static MealDetailsGUI showDetails(Meal meal) {
        MealDetailsGUI gui = mealsGuis.get(meal)
        if (gui == null) {
            gui = new MealDetailsGUI(meal)
            mealsGuis.put(meal, gui)
            Main.addFrame(gui)
        }
        gui
    }
}