package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Contact

import javax.swing.*
import java.awt.*

class ContactsGUI extends JFrame {


    static ContactsGUI contactGui = null
    static ContactsGUI suppliersGui = null
    static ContactsGUI customersGui = null
    final ContactsPanel contactsPanel

    static ContactsGUI showSuppliers() {
        if (suppliersGui == null) {
            suppliersGui = new ContactsGUI(Contact.ContactType.SUPPLIERS)
            Main.addFrame(suppliersGui)
        }
        suppliersGui
    }

    static ContactsGUI showCustomers() {
        if (customersGui == null) {
            customersGui = new ContactsGUI(Contact.ContactType.CUSTOMERS)
            Main.addFrame(customersGui)
        }
        customersGui
    }

    static ContactsGUI showContacts() {
        if (contactGui == null) {
            contactGui = new ContactsGUI(Contact.ContactType.ALL)
            Main.addFrame(contactGui)
        }
        contactGui
    }

    ContactsGUI(Contact.ContactType contactType) {
        super("Contacts")
        ContactsDataModel contactsDataModel = new ContactsDataModel(contactType)
        contactsPanel = new ContactsPanel(contactsDataModel)
        setContentPane(contactsPanel)
        setPreferredSize(new Dimension(1000,400))
        pack()
    }
}
