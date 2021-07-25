package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class SalesOrdersOverviewGUI extends JFrame {
    final SalesOrdersOverviewPanel ordersOverViewPanel

    static SalesOrdersOverviewGUI gui = null

    SalesOrdersOverviewGUI(){
        super(getBundle("Accounting").getString("SO_OVERVIEW"))
        ordersOverViewPanel = new SalesOrdersOverviewPanel()
        setContentPane(ordersOverViewPanel)
        pack()
    }

    static SalesOrdersOverviewGUI showSalesOrderGUI() {
        if (gui == null) {
            gui = new SalesOrdersOverviewGUI()
            Main.addFrame(gui)
        }
        gui
    }
}