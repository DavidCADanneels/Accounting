package be.dafke.Accounting.BasicAccounting.MainApplication

import javax.swing.BoxLayout
import javax.swing.ButtonGroup
import javax.swing.JPanel
import javax.swing.JToggleButton
import java.awt.GridLayout

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

        setLayout(new GridLayout(0,1))
        add journals
        add orders
        add meals
        add contacts

        journals.addActionListener( {
            Main.switchView(Main.JOURNALS_CENTER_VIEW)
            Main.switchSubView(Main.JOURNALS_CENTER_VIEW)
        })
        orders.addActionListener( {
            Main.switchSubView(Main.ORDERS_MENU_VIEW)
        })
        contacts.addActionListener( {
            Main.switchView(Main.CONTACTS_CENTER_VIEW)
            Main.switchSubView(Main.EMPTY_MENU_VIEW)
        })
        meals.addActionListener( {
            Main.switchSubView(Main.MEALS_MENU_VIEW)
        })
    }
}
