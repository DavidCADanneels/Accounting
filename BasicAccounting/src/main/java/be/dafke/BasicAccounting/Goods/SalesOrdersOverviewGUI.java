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
public class SalesOrdersOverviewGUI extends JFrame {
    private final SalesOrdersOverviewPanel ordersOverViewPanel;

    private static SalesOrdersOverviewGUI salesOrderGui = null;

    private SalesOrdersOverviewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("SO_OVERVIEW"));
        ordersOverViewPanel = new SalesOrdersOverviewPanel(accounting);
        setContentPane(ordersOverViewPanel);
        pack();
    }

    public static SalesOrdersOverviewGUI showSalesOrderGUI(Accounting accounting) {
        if (salesOrderGui == null) {
            salesOrderGui = new SalesOrdersOverviewGUI(accounting);
            Main.addFrame(salesOrderGui);
        }
        return salesOrderGui;
    }

    public static void fireSalesOrderAddedOrRemovedForAll(){
        if (salesOrderGui!=null){
            salesOrderGui.fireSalesOrderAddedOrRemoved();
        }
    }

    private void fireSalesOrderAddedOrRemoved() {
        ordersOverViewPanel.fireSalesOrderAddedOrRemoved();
    }
}