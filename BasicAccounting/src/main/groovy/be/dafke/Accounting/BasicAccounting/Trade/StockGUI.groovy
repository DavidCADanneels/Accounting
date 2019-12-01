package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class StockGUI extends JFrame {
    private final StockPanel stockPanel

    private static StockGUI gui = null

    private StockGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("STOCK"))
        stockPanel = new StockPanel(accounting)
        setContentPane(stockPanel)
        pack()
    }

    static StockGUI showStock(Accounting accounting) {
        if (gui == null) {
            gui = new StockGUI(accounting)
            Main.addFrame(gui)
        }
        gui
    }

    static void fireStockContentChanged(){
        if(gui!=null){
            gui.updateStockContent()
        }
    }

    private void updateStockContent() {
        stockPanel.fireStockContentChanged()
    }
}
