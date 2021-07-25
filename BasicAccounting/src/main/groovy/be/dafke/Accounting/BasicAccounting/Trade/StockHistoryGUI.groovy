package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class StockHistoryGUI extends JFrame {
    final StockHistoryPanel stockPanel

    static StockHistoryGUI gui = null

    StockHistoryGUI() {
        super(getBundle("Accounting").getString("STOCK_HISTORY"))
        stockPanel = new StockHistoryPanel()
        setContentPane(stockPanel)
        pack()
    }

    static StockHistoryGUI showStockHistory() {
        if (gui == null) {
            gui = new StockHistoryGUI()
            Main.addFrame(gui)
        }
        gui
    }
}
