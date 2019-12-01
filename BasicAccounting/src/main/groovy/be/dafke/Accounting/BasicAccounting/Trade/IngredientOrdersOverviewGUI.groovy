package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

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

    static IngredientOrdersOverviewGUI showIngredientOrdersGUI(Accounting accounting) {
        if (gui == null) {
            gui = new IngredientOrdersOverviewGUI()
            gui.accounting = accounting
            Main.addFrame(gui)
        }
        gui
    }

    void setAccounting(Accounting accounting) {
        overviewPanel.accounting = accounting
    }

    static void fireIngredientAddedOrRemovedForAll(){
        if (gui != null){
            gui.fireIngredientAddedOrRemoved()
        }
    }

    void fireIngredientAddedOrRemoved() {
        overviewPanel.fireIngredientAddedOrRemoved()
    }
}
