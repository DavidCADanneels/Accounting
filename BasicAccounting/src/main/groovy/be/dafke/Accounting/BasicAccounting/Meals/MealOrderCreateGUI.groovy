package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class MealOrderCreateGUI extends JFrame {
    private static HashMap<Accounting, MealOrderCreateGUI> guis = new HashMap<>()
    private final MealOrderCreatePanel mealOrderCreatePanel

    private MealOrderCreateGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("MEAL_ORDER_INPUT"))
        mealOrderCreatePanel = new MealOrderCreatePanel(accounting)
        setContentPane(mealOrderCreatePanel)
        pack()
    }

    static MealOrderCreateGUI getInstance(Accounting accounting) {
        MealOrderCreateGUI gui = guis.get(accounting)
        if(gui == null){
            gui = new MealOrderCreateGUI(accounting)
            guis.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }

    static void calculateTotalsForAll(Accounting accounting){
        MealOrderCreateGUI gui = guis.get(accounting)
        if(gui!=null){
            gui.calculateTotals()
        }
    }

    void calculateTotals(){
        mealOrderCreatePanel.calculateTotals()
    }

}