package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contacts

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class ContactsMenu extends JMenu {
    private JMenuItem suppliers, customers, all

    private Accounting accounting
//    private Contacts contacts

    ContactsMenu() {
        super(getBundle("Contacts").getString("CONTACTS"))
//        setMnemonic(KeyEvent.VK_P)
        suppliers = new JMenuItem(getBundle("Contacts").getString("SUPPLIERS"))
        suppliers.addActionListener({ e ->
            ContactsGUI contactsGUI = ContactsGUI.showSuppliers(accounting)
            contactsGUI.setLocation(getLocationOnScreen())
            contactsGUI.setVisible(true)
        })
        suppliers.setEnabled(false)

        customers = new JMenuItem(getBundle("Contacts").getString("CUSTOMERS"))
        customers.addActionListener({ e ->
            ContactsGUI contactsGUI = ContactsGUI.showCustomers(accounting)
            contactsGUI.setLocation(getLocationOnScreen())
            contactsGUI.setVisible(true)
        })
        customers.setEnabled(false)

        all = new JMenuItem(getBundle("Contacts").getString("ALL"))
        all.addActionListener({ e ->
            ContactsGUI contactsGUI = ContactsGUI.showContacts(accounting)
            contactsGUI.setLocation(getLocationOnScreen())
            contactsGUI.setVisible(true)
        })
        all.setEnabled(false)

        add(customers)
        add(suppliers)
        add(all)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        setContacts(accounting==null?null:accounting.getContacts())
    }

    void setContacts(Contacts contacts){
//        this.contacts = contacts
        suppliers.setEnabled(contacts!=null)
        customers.setEnabled(contacts!=null)
        all.setEnabled(contacts!=null)
    }
}
