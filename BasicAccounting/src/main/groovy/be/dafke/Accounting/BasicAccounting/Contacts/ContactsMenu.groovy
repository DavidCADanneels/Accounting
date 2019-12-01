package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contacts

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class ContactsMenu extends JMenu {
    JMenuItem suppliers, customers, all

    Accounting accounting
//    Contacts contacts

    ContactsMenu() {
        super(getBundle("Contacts").getString("CONTACTS"))
//        setMnemonic(KeyEvent.VK_P)
        suppliers = new JMenuItem(getBundle("Contacts").getString("SUPPLIERS"))
        suppliers.addActionListener({ e ->
            ContactsGUI contactsGUI = ContactsGUI.showSuppliers(accounting)
            contactsGUI.setLocation(getLocationOnScreen())
            contactsGUI.visible = true
        })
        suppliers.enabled = false

        customers = new JMenuItem(getBundle("Contacts").getString("CUSTOMERS"))
        customers.addActionListener({ e ->
            ContactsGUI contactsGUI = ContactsGUI.showCustomers(accounting)
            contactsGUI.setLocation(getLocationOnScreen())
            contactsGUI.visible = true
        })
        customers.enabled = false

        all = new JMenuItem(getBundle("Contacts").getString("ALL"))
        all.addActionListener({ e ->
            ContactsGUI contactsGUI = ContactsGUI.showContacts(accounting)
            contactsGUI.setLocation(getLocationOnScreen())
            contactsGUI.visible = true
        })
        all.enabled = false

        add(customers)
        add(suppliers)
        add(all)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        setContacts(accounting?accounting.contacts:null)
    }

    void setContacts(Contacts contacts){
//        this.contacts = contacts
        suppliers.enabled = contacts!=null
        customers.enabled = contacts!=null
        all.enabled = contacts!=null
    }
}
