package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.PromoOrder

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class PromoOrderCreateGUI extends JFrame {
    final PromoOrderCreatePanel orderPanel

    static PromoOrderCreateGUI salesOrderCreateGui = null

    PromoOrderCreateGUI() {
        super(getBundle("Accounting").getString("CREATE_PR"))
        orderPanel = new PromoOrderCreatePanel()
        setContentPane(orderPanel)
        pack()
    }

    static PromoOrderCreateGUI showPromoOrderGUI() {
        if (salesOrderCreateGui == null) {
            salesOrderCreateGui = new PromoOrderCreateGUI()
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
