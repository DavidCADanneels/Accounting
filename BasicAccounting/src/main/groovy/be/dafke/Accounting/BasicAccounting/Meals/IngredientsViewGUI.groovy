package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class IngredientsViewGUI extends JFrame {
    final IngredientsViewPanel ingredientsViewPanel

    static final HashMap<Accounting, IngredientsViewGUI> articlesGuis = new HashMap<>()

    IngredientsViewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("INGREDIENTS"))
        ingredientsViewPanel = new IngredientsViewPanel(accounting)
        setContentPane(ingredientsViewPanel)
        pack()
    }

    static IngredientsViewGUI showIngredients(Accounting accounting) {
        IngredientsViewGUI gui = articlesGuis.get(accounting)
        if (gui == null) {
            gui = new IngredientsViewGUI(accounting)
            articlesGuis.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }
}