package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class StockHistoryGUI extends JFrame {
    private final StockHistoryPanel stockPanel

    private static StockHistoryGUI gui = null

    private StockHistoryGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("STOCK_HISTORY"))
        stockPanel = new StockHistoryPanel(accounting)
        setContentPane(stockPanel)
        pack()
    }

    static StockHistoryGUI showStockHistory(Accounting accounting) {
        if (gui == null) {
            gui = new StockHistoryGUI(accounting)
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
