package be.dafke.Accounting.BasicAccounting.MainApplication

import javax.swing.ButtonGroup
import javax.swing.JToggleButton
import javax.swing.JPanel
import javax.swing.JToggleButton

class MainViewSelectorPanel extends JPanel {

    JToggleButton journals, salesOrders, purchaseOrders, ingredients, allergenes

    ButtonGroup buttonGroup

    MainViewSelectorPanel() {
        journals = new JToggleButton('Journals', true)
        salesOrders = new JToggleButton('Sales Orders')
        purchaseOrders = new JToggleButton('Purchase Orders')
        ingredients = new JToggleButton('Ingredients')
        allergenes = new JToggleButton('Allergenes')

        buttonGroup = new ButtonGroup()

        buttonGroup.add journals
        buttonGroup.add salesOrders
        buttonGroup.add purchaseOrders
        buttonGroup.add ingredients
        buttonGroup.add allergenes

        add journals
        add salesOrders
        add purchaseOrders
        add ingredients
        add allergenes

        journals.addActionListener( {
            Main.switchView(Main.JOURNALS_VIEW)
        })
        salesOrders.addActionListener( {
            Main.switchView(Main.SO_VIEW)
        })
        purchaseOrders.addActionListener( {
            Main.switchView(Main.PO_VIEW)
        })
        ingredients.addActionListener( {
            Main.switchView(Main.IN_VIEW)
        })
        allergenes.addActionListener( {
            Main.switchView(Main.AL_VIEW)
        })
    }
}
