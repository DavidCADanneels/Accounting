package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class PromoOrdersOverviewGUI extends JFrame {
    private final PromoOrdersOverviewPanel ordersOverViewPanel

    private static HashMap<Accounting,PromoOrdersOverviewGUI> promoOrdersOverviewGuiMap = new HashMap<>()

    private PromoOrdersOverviewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("PR_OVERVIEW"))
        ordersOverViewPanel = new PromoOrdersOverviewPanel()
        ordersOverViewPanel.setAccounting(accounting)
        setContentPane(ordersOverViewPanel)
        pack()
    }

    static PromoOrdersOverviewGUI showPromoOrderGUI(Accounting accounting) {
        PromoOrdersOverviewGUI gui = promoOrdersOverviewGuiMap.get(accounting)
        if (gui == null) {
            gui = new PromoOrdersOverviewGUI(accounting)
            promoOrdersOverviewGuiMap.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }

    static void firePromoOrderAddedOrRemovedForAccounting(Accounting accounting){
        PromoOrdersOverviewGUI gui = promoOrdersOverviewGuiMap.get(accounting)
        if(gui!=null){
            gui.firePromoOrderAddedOrRemoved()
        }
    }

    private void firePromoOrderAddedOrRemoved() {
        ordersOverViewPanel.firePromoOrderAddedOrRemoved()
    }
}
