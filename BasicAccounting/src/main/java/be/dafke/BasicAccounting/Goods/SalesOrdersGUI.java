package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class SalesOrdersGUI extends JFrame {
    private final SalesOrdersViewPanel orderViewPanel;
    private final SalesOrdersCreatePanel orderCreatePanel;

    private static SalesOrdersGUI salesOrdersGui = null;

    private SalesOrdersGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("SO"));
        orderViewPanel = new SalesOrdersViewPanel(accounting);
        orderCreatePanel = new SalesOrdersCreatePanel(accounting);

        JSplitPane splitPane = Main.createSplitPane(orderViewPanel, orderCreatePanel, VERTICAL_SPLIT);

        setContentPane(splitPane);
        pack();
    }

    public static SalesOrdersGUI showSalesOrderGUI(Accounting accounting) {
        if (salesOrdersGui == null) {
            salesOrdersGui = new SalesOrdersGUI(accounting);
            Main.addFrame(salesOrdersGui);
        }
        return salesOrdersGui;
    }

    public static void fireSalesOrderAddedOrRemovedForAll(){
        if (salesOrdersGui !=null){
            salesOrdersGui.fireSalesOrderAddedOrRemoved();
        }
    }

    private void fireSalesOrderAddedOrRemoved() {
        orderViewPanel.firePurchaseOrderAddedOrRemoved();
    }

    public static void fireCustomerAddedOrRemovedForAll() {
        if(salesOrdersGui !=null){
            salesOrdersGui.fireCustomerAddedOrRemoved();
        }
    }

    public void fireCustomerAddedOrRemoved() {
        orderCreatePanel.fireCustomerAddedOrRemoved();
    }
}