package be.dafke.Accounting.BasicAccounting.MainApplication

import javax.swing.ButtonGroup
import javax.swing.JPanel
import javax.swing.JToggleButton

class MainViewSelectorPanel extends JPanel {

    JToggleButton journals, orders, meals, contacts

    ButtonGroup buttonGroup

    MainViewSelectorPanel() {
        journals = new JToggleButton('Journals', true)
        orders = new JToggleButton('Orders')
        meals = new JToggleButton('Meals')
        contacts = new JToggleButton('Contacts')

        buttonGroup = new ButtonGroup()

        buttonGroup.add journals
        buttonGroup.add orders
        buttonGroup.add meals
        buttonGroup.add contacts

        add journals
        add orders
        add meals
        add contacts

        journals.addActionListener( {
            Main.switchView(Main.JOURNALS_VIEW)
            Main.switchSubView(Main.JOURNALS_VIEW)
        })
        orders.addActionListener( {
            Main.switchSubView(Main.ORDERS_VIEW)
        })
        contacts.addActionListener( {
            Main.switchView(Main.CONTACTS_VIEW)
            Main.switchSubView(Main.EMPTY)
        })
        meals.addActionListener( {
            Main.switchSubView(Main.MEALS_VIEW)
        })
    }
}
