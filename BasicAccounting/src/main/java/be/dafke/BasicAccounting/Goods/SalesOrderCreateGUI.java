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
public class SalesOrderCreateGUI extends JFrame {
    private final SalesOrderCreatePanel orderPanel;

    private static SalesOrderCreateGUI salesOrderCreateGui = null;

    private SalesOrderCreateGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("CREATE_SO"));
        orderPanel = new SalesOrderCreatePanel(accounting);
        setContentPane(orderPanel);
        pack();
    }

    public static SalesOrderCreateGUI showSalesOrderGUI(Accounting accounting) {
        if (salesOrderCreateGui == null) {
            salesOrderCreateGui = new SalesOrderCreateGUI(accounting);
            Main.addFrame(salesOrderCreateGui);
        }
        return salesOrderCreateGui;
    }

    public static void fireCustomersAddedOrRemovedForAll() {
        if(salesOrderCreateGui!=null){
            salesOrderCreateGui.fireCustomerAddedOrRemoved();
        }
    }

    public void fireCustomerAddedOrRemoved() {
        orderPanel.fireCustomerAddedOrRemoved();
    }
}