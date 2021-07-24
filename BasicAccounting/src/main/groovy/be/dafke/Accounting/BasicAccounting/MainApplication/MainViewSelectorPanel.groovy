package be.dafke.Accounting.BasicAccounting.MainApplication

import javax.swing.JButton
import javax.swing.JPanel

class MainViewSelectorPanel extends JPanel {

    JButton journals, salesOrders, purchaseOrders, ingredients

    MainViewSelectorPanel() {
        journals = new JButton('Journals')
        salesOrders = new JButton('Sales Orders')
        purchaseOrders = new JButton('Purchase Orders')
        ingredients = new JButton('Ingredients and Allergenes')

        add journals
        add salesOrders
        add purchaseOrders
        add ingredients

        journals.enabled = false
        salesOrders.enabled = true
        purchaseOrders.enabled = true
        ingredients.enabled = true

        journals.addActionListener( {
            Main.switchView(Main.JOURNALS_VIEW)
            journals.enabled = false
            salesOrders.enabled = true
            purchaseOrders.enabled = true
            ingredients.enabled = true
        })
        salesOrders.addActionListener( {
            Main.switchView(Main.SO_VIEW)
            journals.enabled = true
            salesOrders.enabled = false
            purchaseOrders.enabled = true
            ingredients.enabled = true
        })
        purchaseOrders.addActionListener( {
            Main.switchView(Main.PO_VIEW)
            journals.enabled = true
            salesOrders.enabled = true
            purchaseOrders.enabled = false
            ingredients.enabled = true
        })
        ingredients.addActionListener( {
            Main.switchView(Main.IA_VIEW)
            journals.enabled = true
            salesOrders.enabled = true
            purchaseOrders.enabled = true
            ingredients.enabled = false
        })
    }
}
