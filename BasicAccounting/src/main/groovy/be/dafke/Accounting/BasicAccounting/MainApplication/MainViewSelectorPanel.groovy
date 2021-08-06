package be.dafke.Accounting.BasicAccounting.MainApplication

import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.ButtonGroup
import javax.swing.JPanel
import javax.swing.JToggleButton
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.Color
import java.awt.GridLayout

class MainViewSelectorPanel extends JPanel {

    JToggleButton journals, orders, meals, contacts

    ButtonGroup buttonGroup

    MainViewSelectorPanel() {
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Main"))
        journals = new JToggleButton('Journals', true)
        orders = new JToggleButton('Orders')
        meals = new JToggleButton('Meals')
        contacts = new JToggleButton('Contacts')

        buttonGroup = new ButtonGroup()

        buttonGroup.add journals
        buttonGroup.add orders
        buttonGroup.add meals
        buttonGroup.add contacts

        setLayout(new GridLayout(2,0))
        add journals
        add orders
        add meals
        add contacts

        journals.addActionListener( {
            Main.switchView(Main.JOURNALS_CENTER_VIEW)
            Main.switchSubView(Main.JOURNALS_CENTER_VIEW)
        })
        orders.addActionListener( {
//            Main.switchView(Main.SO_CENTER_VIEW)
            Main.switchSubView(Main.ORDERS_MENU_VIEW)
        })
        contacts.addActionListener( {
            Main.switchView(Main.CONTACTS_CENTER_VIEW)
            Main.switchSubView(Main.CONTACTS_MENU_VIEW)
        })
        meals.addActionListener( {
//            Main.switchView(Main.INGREDIENTS_CENTER_VIEW)
            Main.switchSubView(Main.MEALS_MENU_VIEW)
        })
    }

    void enableButtons(){
        contacts.enabled = Session.activeAccounting.contactsAccounting
        meals.enabled = Session.activeAccounting.mealsAccounting
        orders.enabled = Session.activeAccounting.tradeAccounting
    }
}
