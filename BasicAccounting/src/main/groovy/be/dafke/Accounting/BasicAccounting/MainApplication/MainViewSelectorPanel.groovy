package be.dafke.Accounting.BasicAccounting.MainApplication

import javax.swing.JButton
import javax.swing.JPanel

class MainViewSelectorPanel extends JPanel {

    JButton journals, salesOrders, purchaseOrders

    MainViewSelectorPanel() {
        journals = new JButton('Journals')
        salesOrders = new JButton('Sales Orders')
        purchaseOrders = new JButton('Purchase Orders')

        add journals
        add salesOrders
        add purchaseOrders

        journals.enabled = false
        salesOrders.enabled = true
        purchaseOrders.enabled = true

        journals.addActionListener( {
            Main.switchView(Main.JOURNALS_VIEW)
            journals.enabled = false
            salesOrders.enabled = true
            purchaseOrders.enabled = true
        })
        salesOrders.addActionListener( {
            Main.switchView(Main.SO_VIEW)
            journals.enabled = true
            salesOrders.enabled = false
            purchaseOrders.enabled = true
        })
        purchaseOrders.addActionListener( {
            Main.switchView(Main.PO_VIEW)
            journals.enabled = true
            salesOrders.enabled = true
            purchaseOrders.enabled = false
        })
    }
}
