package be.dafke.Accounting.BasicAccounting.MainApplication

import javax.swing.ButtonGroup
import javax.swing.JPanel
import javax.swing.JToggleButton

class MainViewSelectorPanel extends JPanel {

    JToggleButton journals, orders, meals

    ButtonGroup buttonGroup

    MainViewSelectorPanel() {
        journals = new JToggleButton('Journals', true)
        orders = new JToggleButton('Orders')
        meals = new JToggleButton('Meals')

        buttonGroup = new ButtonGroup()

        buttonGroup.add journals
        buttonGroup.add orders
        buttonGroup.add meals

        add journals
        add orders
        add meals

        journals.addActionListener( {
            Main.switchView(Main.JOURNALS_VIEW)
            Main.switchSubView(Main.EMPTY)
        })
        orders.addActionListener( {
            Main.switchSubView(Main.ORDERS_VIEW)
        })
        meals.addActionListener( {
            Main.switchSubView(Main.MEALS_VIEW)
        })
    }
}
