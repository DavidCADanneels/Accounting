package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contact

import javax.swing.*
import java.awt.*

class ContactsGUI extends JFrame {


    static final HashMap<Accounting, ContactsGUI> contactGuis = new HashMap<>()
    static final HashMap<Accounting, ContactsGUI> suppliersGuis = new HashMap<>()
    static final HashMap<Accounting, ContactsGUI> customersGuis = new HashMap<>()
    final ContactsPanel contactsPanel

    static ContactsGUI showSuppliers(Accounting accounting) {
        ContactsGUI gui = suppliersGuis.get(accounting)
        if (gui == null) {
            gui = new ContactsGUI(accounting, Contact.ContactType.SUPPLIERS)
            suppliersGuis.put(accounting,gui)
            Main.addFrame(gui)
        }
        gui
    }

    static ContactsGUI showCustomers(Accounting accounting) {
        ContactsGUI gui = customersGuis.get(accounting)
        if (gui == null) {
            gui = new ContactsGUI(accounting, Contact.ContactType.CUSTOMERS)
            customersGuis.put(accounting,gui)
            Main.addFrame(gui)
        }
        gui
    }

    static ContactsGUI showContacts(Accounting accounting) {
        ContactsGUI gui = contactGuis.get(accounting)
        if (gui == null) {
            gui = new ContactsGUI(accounting, Contact.ContactType.ALL)
            contactGuis.put(accounting,gui)
            Main.addFrame(gui)
        }
        gui
    }

    ContactsGUI(Accounting accounting, Contact.ContactType contactType) {
        super("Contacts")
        contactsPanel = new ContactsPanel(contactType, accounting)
        setContentPane(contactsPanel)
        setPreferredSize(new Dimension(1000,400))
        pack()
    }

    static void fireTableUpdateForAccounting(Accounting accounting){
        ContactsGUI gui = contactGuis.get(accounting)
        if(gui!=null){
            gui.setContacts()
            gui.fireTableUpdate()
        }
    }

    static void fireContactDataChangedForAll(){
        contactGuis.values().each{it.fireTableUpdate()}
        suppliersGuis.values().each{it.fireTableUpdate()}
        customersGuis.values().each{it.fireTableUpdate()}
    }

    static void fireCustomerDataChanged(){
        customersGuis.values().each{it.fireTableUpdate()}
    }

    void fireTableUpdate(){
        contactsPanel.fireTableUpdate()
    }

    static void fireCustomerAddedOrRemovedForAccounting(Accounting accounting){
        ContactsGUI gui = customersGuis.get(accounting)
        if(gui!=null) {
            gui.setContacts()
            gui.fireTableUpdate()
        }
    }

    static void fireSupplierAddedOrRemovedForAccounting(Accounting accounting){
        ContactsGUI gui = suppliersGuis.get(accounting)
        if (gui!=null) {
            gui.setContacts()
            gui.fireTableUpdate()
        }
    }

    void setContacts(){
        contactsPanel.setContacts()
    }
}
