package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class IngredientOrdersOverviewGUI extends JFrame {

    static IngredientOrdersOverviewGUI gui = null
    final IngredientOrdersOverviewPanel overviewPanel

    IngredientOrdersOverviewGUI() {
        super(getBundle("Accounting").getString("INGREDIENT_ORDERS"))
        overviewPanel = new IngredientOrdersOverviewPanel()

        setContentPane(overviewPanel)
        pack()
    }

    static IngredientOrdersOverviewGUI showIngredientOrdersGUI() {
        if (gui == null) {
            gui = new IngredientOrdersOverviewGUI()
            Main.addFrame(gui)
        }
        gui
    }
}
