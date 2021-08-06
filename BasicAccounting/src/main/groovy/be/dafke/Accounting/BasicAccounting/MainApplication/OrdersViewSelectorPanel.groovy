package be.dafke.Accounting.BasicAccounting.MainApplication

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.Color

class OrdersViewSelectorPanel extends JPanel {

    JToggleButton salesOrders, purchaseOrders

    ButtonGroup buttonGroup

    String selection = Main.SO_CENTER_VIEW

    OrdersViewSelectorPanel() {
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Orders"))
        salesOrders = new JToggleButton('Sales Orders', true)
        purchaseOrders = new JToggleButton('Purchase Orders')

        buttonGroup = new ButtonGroup()

        buttonGroup.add salesOrders
        buttonGroup.add purchaseOrders

        add salesOrders
        add purchaseOrders

        salesOrders.addActionListener( {
            selection = Main.SO_CENTER_VIEW
            refresh()
        })
        purchaseOrders.addActionListener( {
            selection = Main.PO_CENTER_VIEW
            refresh()
        })
    }

    void refresh(){
        Main.switchView(selection)
    }
}
