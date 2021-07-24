package be.dafke.Accounting.BasicAccounting.MainApplication

import javax.swing.ButtonGroup
import javax.swing.JToggleButton
import javax.swing.JPanel
import javax.swing.JToggleButton

class MainViewSelectorPanel extends JPanel {

    JToggleButton journals, salesOrders, purchaseOrders, meals

    ButtonGroup buttonGroup

    MainViewSelectorPanel() {
        journals = new JToggleButton('Journals', true)
        salesOrders = new JToggleButton('Sales Orders')
        purchaseOrders = new JToggleButton('Purchase Orders')
        meals = new JToggleButton('Meals')

        buttonGroup = new ButtonGroup()

        buttonGroup.add journals
        buttonGroup.add salesOrders
        buttonGroup.add purchaseOrders
        buttonGroup.add meals

        add journals
        add salesOrders
        add purchaseOrders
        add meals

        journals.addActionListener( {
            Main.switchView(Main.JOURNALS_VIEW)
            Main.switchSubView(Main.EMPTY)
        })
        salesOrders.addActionListener( {
            Main.switchView(Main.SO_VIEW)
            Main.switchSubView(Main.EMPTY)
        })
        purchaseOrders.addActionListener( {
            Main.switchView(Main.PO_VIEW)
            Main.switchSubView(Main.EMPTY)
        })
        meals.addActionListener( {
            Main.switchSubView(Main.MEALS_VIEW)
        })

    }
}
