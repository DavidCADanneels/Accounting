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
public class OrderGUI extends JFrame {
    private final OrderPanel orderPanel;

    private static OrderGUI purchaseOrderGui = null;
    private static OrderGUI salesOrderGui = null;

    private OrderGUI(Accounting accounting, Order.OrderType orderType) {
        super(getBundle("Accounting").getString("STOCK"));
        orderPanel = new OrderPanel(accounting, orderType);
        setContentPane(orderPanel);
        pack();
    }

    public static OrderGUI showPurchaseOrderGUI(Accounting accounting) {
        if (purchaseOrderGui == null) {
            purchaseOrderGui = new OrderGUI(accounting, Order.OrderType.PURCHASE);
            Main.addFrame(purchaseOrderGui);
        }
        return purchaseOrderGui;
    }

    public static OrderGUI showSalesOrderGUI(Accounting accounting) {
        if (salesOrderGui == null) {
            salesOrderGui = new OrderGUI(accounting, Order.OrderType.SALE);
            Main.addFrame(salesOrderGui);
        }
        return salesOrderGui;
    }
}