package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Articles;
import be.dafke.BusinessModel.Contacts;
import be.dafke.BusinessModel.Stock;
import be.dafke.BusinessModel.StockItems;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class OrderGUI extends JFrame {
    private final OrderPanel orderPanel;

    private static OrderGUI orderGui = null;

    private OrderGUI(Articles articles, Contacts contacts) {
        super(getBundle("Accounting").getString("STOCK"));
        orderPanel = new OrderPanel(articles, contacts);
        setContentPane(orderPanel);
        pack();
    }

    public static OrderGUI showOrderGUI(Articles articles, Contacts contacts) {
        if (orderGui == null) {
            orderGui = new OrderGUI(articles, contacts);
            Main.addFrame(orderGui);
        }
        return orderGui;
    }
}