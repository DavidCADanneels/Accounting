package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.PromoOrder

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class PromoOrderCreateGUI extends JFrame {
    private final PromoOrderCreatePanel orderPanel

    private static PromoOrderCreateGUI salesOrderCreateGui = null
//    private PromoOrder promoOrder

    private PromoOrderCreateGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("CREATE_PR"))
        orderPanel = new PromoOrderCreatePanel(accounting)
        setContentPane(orderPanel)
        pack()
    }

    static PromoOrderCreateGUI showPromoOrderGUI(Accounting accounting) {
        if (salesOrderCreateGui == null) {
            salesOrderCreateGui = new PromoOrderCreateGUI(accounting)
            Main.addFrame(salesOrderCreateGui)
        }
        salesOrderCreateGui
    }

    void setPromoOrder(PromoOrder promoOrder) {
        orderPanel.setPromoOrder(promoOrder)
//        this.promoOrder = promoOrder
    }

//    PromoOrder getPromoOrder() {
//        promoOrder
//    }
}
