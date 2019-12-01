package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class ArticlesGUI extends JFrame {
    final ArticlesPanel articlesPanel

    static final HashMap<Accounting, ArticlesGUI> articlesGuis = new HashMap<>()

    ArticlesGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("ARTICLES"))
        articlesPanel = new ArticlesPanel(accounting)
        setContentPane(articlesPanel)
        pack()
    }

    static ArticlesGUI showArticles(Accounting accounting) {
        ArticlesGUI gui = articlesGuis.get(accounting)
        if (gui == null) {
            gui = new ArticlesGUI(accounting)
            articlesGuis.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }

    static void fireTableUpdateForAccounting(Accounting accounting){
        ArticlesGUI gui = articlesGuis.get(accounting)
        if(gui!=null) {
            gui.fireTableUpdate()
        }
    }

    void fireTableUpdate(){
        articlesPanel.fireTableUpdate()
    }

    static void fireSupplierAddedOrRemovedForAccounting(Accounting accounting){
        ArticlesGUI gui = articlesGuis.get(accounting)
        if(gui!=null){
            gui.fireSupplierAddedOrRemoved()
        }
    }

    void fireSupplierAddedOrRemoved(){
        articlesPanel.fireSupplierAddedOrRemoved()
        articlesPanel.fireTableUpdate()
    }

    static void fireIngredientAddedOrRemovedForAccounting(Accounting accounting){
        ArticlesGUI gui = articlesGuis.get(accounting)
        if(gui!=null){
            gui.fireIngredientAddedOrRemoved()
        }
    }

    void fireIngredientAddedOrRemoved(){
        articlesPanel.fireIngredientsAddedOrRemoved()
    }
}
