package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class IngredientsEditGUI extends JFrame {
    final IngredientsEditPanel ingredientsViewPanel

    static final HashMap<Accounting, IngredientsEditGUI> articlesGuis = new HashMap<>()

    IngredientsEditGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("INGREDIENTS"))
        ingredientsViewPanel = new IngredientsEditPanel(accounting)
        setContentPane(ingredientsViewPanel)
        pack()
    }

    static IngredientsEditGUI showIngredients(Accounting accounting) {
        IngredientsEditGUI gui = articlesGuis.get(accounting)
        if (gui == null) {
            gui = new IngredientsEditGUI(accounting)
            articlesGuis.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }
}