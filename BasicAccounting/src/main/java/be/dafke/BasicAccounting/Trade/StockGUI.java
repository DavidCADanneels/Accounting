package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class StockGUI extends JFrame {
    private final StockPanel stockPanel;

    private static StockGUI gui = null;

    private StockGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("STOCK"));
        stockPanel = new StockPanel(accounting);
        setContentPane(stockPanel);
        pack();
    }

    public static StockGUI showStock(Accounting accounting) {
        if (gui == null) {
            gui = new StockGUI(accounting);
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