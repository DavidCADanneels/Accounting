package be.dafke.BasicAccounting.Contacts;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.util.HashMap;

public class ContactsGUI extends JFrame {


    private static final HashMap<Accounting, ContactsGUI> contactGuis = new HashMap<>();
    private static final HashMap<Accounting, ContactsGUI> suppliersGuis = new HashMap<>();
    private static final HashMap<Accounting, ContactsGUI> customersGuis = new HashMap<>();
    private final ContactsPanel contactsPanel;

    public static ContactsGUI showSuppliers(Accounting accounting) {
        ContactsGUI gui = suppliersGuis.get(accounting);
        if (gui == null) {
            gui = new ContactsGUI(accounting, Contact.ContactType.SUPPLIERS);
            suppliersGuis.put(accounting,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static ContactsGUI showCustomers(Accounting accounting) {
        ContactsGUI gui = customersGuis.get(accounting);
        if (gui == null) {
            gui = new ContactsGUI(accounting, Contact.ContactType.CUSTOMERS);
            customersGuis.put(accounting,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static ContactsGUI showContacts(Accounting accounting) {
        ContactsGUI gui = contactGuis.get(accounting);
        if (gui == null) {
            gui = new ContactsGUI(accounting, Contact.ContactType.ALL);
            contactGuis.put(accounting,gui);
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

    public static void fireTableUpdateForAccounting(Accounting accounting){
        ContactsGUI gui = contactGuis.get(accounting);
        if(gui!=null){
            gui.setContacts();
            gui.fireTableUpdate();
        }
    }

    public static void fireContactDataChangedForAll(){
        contactGuis.values().forEach(ContactsGUI::fireTableUpdate);
        suppliersGuis.values().forEach(ContactsGUI::fireTableUpdate);
        customersGuis.values().forEach(ContactsGUI::fireTableUpdate);
    }

    public static void fireCustomerDataChanged(){
        customersGuis.values().forEach(ContactsGUI::fireTableUpdate);
    }

    public void fireTableUpdate(){
        contactsPanel.fireTableUpdate();
    }

    public static void fireCustomerAddedOrRemovedForAccounting(Accounting accounting){
        ContactsGUI gui = customersGuis.get(accounting);
        if(gui!=null) {
            gui.setContacts();
            gui.fireTableUpdate();
        }
    }

    public static void fireSupplierAddedOrRemovedForAccounting(Accounting accounting){
        ContactsGUI gui = suppliersGuis.get(accounting);
        if (gui!=null) {
            gui.setContacts();
            gui.fireTableUpdate();
        }
    }

    public void setContacts(){
        contactsPanel.setContacts();
    }
}
