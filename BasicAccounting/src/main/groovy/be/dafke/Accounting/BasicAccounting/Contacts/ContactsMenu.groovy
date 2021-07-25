package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class ContactsMenu extends JMenu {
    JMenuItem suppliers, customers, all

    ContactsMenu() {
        super(getBundle("Contacts").getString("CONTACTS"))
//        setMnemonic(KeyEvent.VK_P)
        suppliers = new JMenuItem(getBundle("Contacts").getString("SUPPLIERS"))
        suppliers.addActionListener({ e ->
            ContactsGUI contactsGUI = ContactsGUI.showSuppliers()
            contactsGUI.setLocation(getLocationOnScreen())
            contactsGUI.visible = true
        })
        suppliers.enabled = false

        customers = new JMenuItem(getBundle("Contacts").getString("CUSTOMERS"))
        customers.addActionListener({ e ->
            ContactsGUI contactsGUI = ContactsGUI.showCustomers()
            contactsGUI.setLocation(getLocationOnScreen())
            contactsGUI.visible = true
        })
        customers.enabled = false

        all = new JMenuItem(getBundle("Contacts").getString("ALL"))
        all.addActionListener({ e ->
            ContactsGUI contactsGUI = ContactsGUI.showContacts()
            contactsGUI.setLocation(getLocationOnScreen())
            contactsGUI.visible = true
        })
        all.enabled = false

        add(customers)
        add(suppliers)
        add(all)
    }

    void refresh(){
        boolean enableButtons = Session.activeAccounting.contacts!=null
        suppliers.enabled = enableButtons
        customers.enabled = enableButtons
        all.enabled = enableButtons
    }
}
