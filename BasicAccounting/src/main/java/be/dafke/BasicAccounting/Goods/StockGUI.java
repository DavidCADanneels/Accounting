package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class StockGUI extends JFrame {
    private final StockPanel stockPanel;

    private static final HashMap<Stock, StockGUI> stockGuis = new HashMap<>();

    private StockGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("STOCK"));
        stockPanel = new StockPanel(accounting);
        setContentPane(stockPanel);
        pack();
    }

    public static StockGUI showStock(Accounting accounting) {
        Stock stock = accounting.getStock();
        StockGUI gui = stockGuis.get(stock);
        if (gui == null) {
            gui = new StockGUI(accounting);
            stockGuis.put(stock, gui);
            Main.addFrame(gui);
        }
        return gui;
    }
}