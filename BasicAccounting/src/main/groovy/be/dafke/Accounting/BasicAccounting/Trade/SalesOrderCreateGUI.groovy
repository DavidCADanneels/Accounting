package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.SalesOrder

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class SalesOrderCreateGUI extends JFrame {
    private final SalesOrderCreatePanel orderPanel

    private static SalesOrderCreateGUI salesOrderCreateGui = null
    private static SalesOrderCreateGUI salesEditCreateGui = null

    private SalesOrderCreateGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("CREATE_SO"))
        orderPanel = new SalesOrderCreatePanel(accounting)
        setContentPane(orderPanel)
        pack()
    }

    static SalesOrderCreateGUI showSalesOrderGUI(Accounting accounting) {
        if (salesOrderCreateGui == null) {
            salesOrderCreateGui = new SalesOrderCreateGUI(accounting)
            Main.addFrame(salesOrderCreateGui)
        }
        salesOrderCreateGui
    }

    static SalesOrderCreateGUI showSalesOrderEditGUI(Accounting accounting) {
        if (salesEditCreateGui == null) {
            salesEditCreateGui = new SalesOrderCreateGUI(accounting)
            Main.addFrame(salesEditCreateGui)
        }
        salesEditCreateGui
    }

    static void fireCustomerAddedOrRemovedForAll() {
        if(salesOrderCreateGui!=null){
            salesOrderCreateGui.fireCustomerAddedOrRemoved()
        }
        if(salesEditCreateGui!=null){
            salesEditCreateGui.fireCustomerAddedOrRemoved()
        }
    }

    void fireCustomerAddedOrRemoved() {
        orderPanel.fireCustomerAddedOrRemoved()
    }

    void setSalesOrder(SalesOrder salesOrder) {
        orderPanel.setSalesOrder(salesOrder)
    }
}
