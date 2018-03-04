package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Order;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class SalesOrderGUI extends JFrame {
    private final SalesOrderPanel orderPanel;

    private static SalesOrderGUI salesOrderGui = null;

    private SalesOrderGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("CREATE_PO"));
        orderPanel = new SalesOrderPanel(accounting);
        setContentPane(orderPanel);
        pack();
    }

    public static SalesOrderGUI showSalesOrderGUI(Accounting accounting) {
        if (salesOrderGui == null) {
            salesOrderGui = new SalesOrderGUI(accounting);
            Main.addFrame(salesOrderGui);
        }
        return salesOrderGui;
    }
}