package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class StockHistoryGUI extends JFrame {
    private final StockHistoryPanel stockPanel;

    private static StockHistoryGUI gui = null;

    private StockHistoryGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("STOCK_HISTORY"));
        stockPanel = new StockHistoryPanel(accounting);
        setContentPane(stockPanel);
        pack();
    }

    public static StockHistoryGUI showStockHistory(Accounting accounting) {
        if (gui == null) {
            gui = new StockHistoryGUI(accounting);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void fireStockContentChanged(){
        if(gui!=null){
            gui.updateStockContent();
        }
    }

    private void updateStockContent() {
        stockPanel.fireStockContentChanged();
    }
}