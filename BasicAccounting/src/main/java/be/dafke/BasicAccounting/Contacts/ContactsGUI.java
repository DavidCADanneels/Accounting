package be.dafke.BasicAccounting.Contacts;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.util.HashMap;

public class ContactsGUI extends JFrame {


    private static final HashMap<Contacts, ContactsGUI> contactGuis = new HashMap<>();
    private static final HashMap<Contacts, ContactsGUI> suppliersGuis = new HashMap<>();
    private static final HashMap<Contacts, ContactsGUI> customersGuis = new HashMap<>();
    private final ContactsPanel contactsPanel;

    public static ContactsGUI showSuppliers(Accounting accounting) {
        Contacts contacts = accounting.getContacts();
        ContactsGUI gui = suppliersGuis.get(contacts);
        if (gui == null) {
            gui = new ContactsGUI(accounting, Contact.ContactType.SUPPLIERS);
            suppliersGuis.put(contacts,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static ContactsGUI showCustomers(Accounting accounting) {
        Contacts contacts = accounting.getContacts();
        ContactsGUI gui = customersGuis.get(contacts);
        if (gui == null) {
            gui = new ContactsGUI(accounting, Contact.ContactType.CUSTOMERS);
            customersGuis.put(contacts,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static ContactsGUI showContacts(Accounting accounting) {
        Contacts contacts = accounting.getContacts();
        ContactsGUI gui = contactGuis.get(contacts);
        if (gui == null) {
            gui = new ContactsGUI(accounting, Contact.ContactType.ALL);
            contactGuis.put(contacts,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    private ContactsGUI(Accounting accounting, Contact.ContactType contactType) {
        super("Contacts");
        contactsPanel = new ContactsPanel(contactType, accounting);
        setContentPane(contactsPanel);
        setPreferredSize(new Dimension(1000,400));
        pack();
    }


    public static void fireContactAddedForAll(){
        contactGuis.values().forEach(contactsGUI -> {
            contactsGUI.setContacts();
            contactsGUI.fireContactDataChanged();
        });
        // 'customer' and 'supplier' are false at creation time, no need to refresh the other frames
    }

    public static void fireContactDataChangedForAll(){
        contactGuis.values().forEach(ContactsGUI::fireContactDataChanged);
        suppliersGuis.values().forEach(ContactsGUI::fireContactDataChanged);
        customersGuis.values().forEach(ContactsGUI::fireContactDataChanged);
    }

    public static void fireCustomerDataChanged(){
        customersGuis.values().forEach(ContactsGUI::fireContactDataChanged);
    }

    public void fireContactDataChanged(){
        contactsPanel.fireContactDataChanged();
    }

    public static void fireCustomerAddedOrRemovedForAll(){
        customersGuis.values().forEach(contactsGUI -> {
            contactsGUI.setContacts();
            contactsGUI.fireContactDataChanged();
        });
    }

    public static void fireSupplierAddedOrRemovedForAll(){
        suppliersGuis.values().forEach(contactsGUI -> {
            contactsGUI.setContacts();
            contactsGUI.fireContactDataChanged();
        });
    }

    public void setContacts(){
        contactsPanel.setContacts();
    }
}
