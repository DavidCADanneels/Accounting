package be.dafke.Accounting.BasicAccounting.MainApplication

import javax.swing.*

class OrdersViewSelectorPanel extends JPanel {

    JToggleButton salesOrders, purchaseOrders

    ButtonGroup buttonGroup

    OrdersViewSelectorPanel() {
        salesOrders = new JToggleButton('Sales Orders', true)
        purchaseOrders = new JToggleButton('Purchase Orders')

        buttonGroup = new ButtonGroup()

        buttonGroup.add salesOrders
        buttonGroup.add purchaseOrders

        add salesOrders
        add purchaseOrders

        salesOrders.addActionListener( {
            Main.switchView(Main.SO_VIEW)
        })
        purchaseOrders.addActionListener( {
            Main.switchView(Main.PO_VIEW)
        })
    }
}
