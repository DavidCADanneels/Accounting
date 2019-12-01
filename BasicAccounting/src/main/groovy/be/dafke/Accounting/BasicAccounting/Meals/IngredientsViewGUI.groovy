package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class IngredientsViewGUI extends JFrame {
    private final IngredientsViewPanel ingredientsViewPanel

    private static final HashMap<Accounting, IngredientsViewGUI> articlesGuis = new HashMap<>()

    private IngredientsViewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("INGREDIENTS"))
        ingredientsViewPanel = new IngredientsViewPanel(accounting, true)
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