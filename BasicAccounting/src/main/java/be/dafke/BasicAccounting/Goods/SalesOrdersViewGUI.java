package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class SalesOrdersViewGUI extends JFrame {
    private final SalesOrdersViewPanel orderPanel;

    private static SalesOrdersViewGUI salesOrdersViewGUI = null;

    private SalesOrdersViewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("VIEW_SO"));
        orderPanel = new SalesOrdersViewPanel(accounting);
        setContentPane(orderPanel);
        pack();
    }

    public static SalesOrdersViewGUI showSalesOrderGUI(Accounting accounting) {
        if (salesOrdersViewGUI == null) {
            salesOrdersViewGUI = new SalesOrdersViewGUI(accounting);
            Main.addFrame(salesOrdersViewGUI);
        }
        return salesOrdersViewGUI;
    }

    public static void fireSalesOrderAddedOrRemovedForAll(){
        if (salesOrdersViewGUI!=null){
            salesOrdersViewGUI.fireSalesOrderAddedOrRemoved();
        }
    }

    private void fireSalesOrderAddedOrRemoved() {
        orderPanel.fireSalesOrderAddedOrRemoved();
    }
}