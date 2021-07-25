package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class StockGUI extends JFrame {
    final StockPanel stockPanel

    static StockGUI gui = null

    StockGUI() {
        super(getBundle("Accounting").getString("STOCK"))
        stockPanel = new StockPanel()
        setContentPane(stockPanel)
        pack()
    }

    static StockGUI showStock() {
        if (gui == null) {
            gui = new StockGUI()
            Main.addFrame(gui)
        }
        gui
    }
}
