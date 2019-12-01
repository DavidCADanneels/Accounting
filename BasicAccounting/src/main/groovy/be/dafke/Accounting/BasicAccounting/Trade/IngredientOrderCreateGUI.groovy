package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class IngredientOrderCreateGUI extends JFrame {

    static IngredientOrderCreateGUI gui = null
    final IngredientOrderCreatePanel ingredientOrderCreatePanel

    IngredientOrderCreateGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("BUY_INGREDIENTS"))
        ingredientOrderCreatePanel = new IngredientOrderCreatePanel(accounting)
        setContentPane(ingredientOrderCreatePanel)
        pack()
    }

    static IngredientOrderCreateGUI showIngredientsOrderCreateGUI(Accounting accounting) {
        if (gui == null) {
            gui = new IngredientOrderCreateGUI(accounting)
            Main.addFrame(gui)
        }
        gui
    }

    static void fireSupplierAddedOrRemovedForAll() {
        if (gui != null){
            gui.fireArticleAddedOrRemoved()
        }
    }

    void fireArticleAddedOrRemoved(){
        ingredientOrderCreatePanel.fireArticleAddedOrRemoved()
    }
}
