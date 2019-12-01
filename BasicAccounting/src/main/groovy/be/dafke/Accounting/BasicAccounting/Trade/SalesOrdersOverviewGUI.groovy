package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class SalesOrdersOverviewGUI extends JFrame {
    final SalesOrdersOverviewPanel ordersOverViewPanel

    static HashMap<Accounting,SalesOrdersOverviewGUI> map = new HashMap<>()

    SalesOrdersOverviewGUI(){
        super(getBundle("Accounting").getString("SO_OVERVIEW"))
        ordersOverViewPanel = new SalesOrdersOverviewPanel()
        setContentPane(ordersOverViewPanel)
        pack()
    }

    static SalesOrdersOverviewGUI showSalesOrderGUI(Accounting accounting) {
        SalesOrdersOverviewGUI gui = map.get(accounting)
        if (gui == null) {
            gui = new SalesOrdersOverviewGUI()
            gui.accounting = accounting
            map.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }

    void setAccounting(Accounting accounting) {
        ordersOverViewPanel.accounting = accounting
    }

    static void fireSalesOrderAddedOrRemovedForAccounting(Accounting accounting){
        SalesOrdersOverviewGUI gui = map.get(accounting)
        if (gui!=null){
            gui.fireSalesOrderAddedOrRemoved()
        }
    }

    void fireSalesOrderAddedOrRemoved() {
        ordersOverViewPanel.fireSalesOrderAddedOrRemoved()
    }
}