package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.MealOrder

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class MealOrdersOverviewGUI extends JFrame {
    private static HashMap<Accounting, MealOrdersOverviewGUI> guis = new HashMap<>()
    private final MealOrdersOverviewPanel mealOrdersOverviewPanel

    private MealOrdersOverviewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("MEAL_ORDER_OVERVIEW"))
        mealOrdersOverviewPanel = new MealOrdersOverviewPanel(accounting)
        setContentPane(mealOrdersOverviewPanel)
        pack()
    }

    static MealOrdersOverviewGUI getInstance(Accounting accounting) {
        MealOrdersOverviewGUI gui = guis.get(accounting)
        if(gui == null){
            gui = new MealOrdersOverviewGUI(accounting)
            guis.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }

    static void fireOrderAddedForAccounting(Accounting accounting, MealOrder mealOrder) {
        MealOrdersOverviewGUI gui = guis.get(accounting)
        if(gui != null) {
            gui.fireOrderAdded(accounting, mealOrder)
        }
    }

    private void fireOrderAdded(Accounting accounting, MealOrder mealOrder) {
        mealOrdersOverviewPanel.fireOrderAdded(accounting, mealOrder)
    }
}