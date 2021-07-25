package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class PromoOrdersOverviewGUI extends JFrame {
    final PromoOrdersOverviewPanel ordersOverViewPanel

    static PromoOrdersOverviewGUI gui = null

    PromoOrdersOverviewGUI() {
        super(getBundle("Accounting").getString("PR_OVERVIEW"))
        ordersOverViewPanel = new PromoOrdersOverviewPanel()
        setContentPane(ordersOverViewPanel)
        pack()
    }

    static PromoOrdersOverviewGUI showPromoOrderGUI() {
        if (gui == null) {
            gui = new PromoOrdersOverviewGUI()
            Main.addFrame(gui)
        }
        gui
    }
}
