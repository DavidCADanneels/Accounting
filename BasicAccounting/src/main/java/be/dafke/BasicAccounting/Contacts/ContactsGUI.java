package be.dafke.BasicAccounting.Contacts;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.BusinessModelDao.VATWriter;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class ContactsGUI extends JFrame {


    private static final HashMap<Contacts, ContactsGUI> contactGuis = new HashMap<>();
    private static final HashMap<Contacts, ContactsGUI> suppliersGuis = new HashMap<>();
    private static final HashMap<Contacts, ContactsGUI> customersGuis = new HashMap<>();
    private final ContactsPanel contactsPanel;

    public static ContactsGUI showSuppliers(Contacts contacts) {
        ContactsGUI gui = suppliersGuis.get(contacts);
        if (gui == null) {
            gui = new ContactsGUI(contacts, Contact.ContactType.SUPPLIERS);
            suppliersGuis.put(contacts,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static ContactsGUI showCustomers(Contacts contacts) {
        ContactsGUI gui = customersGuis.get(contacts);
        if (gui == null) {
            gui = new ContactsGUI(contacts, Contact.ContactType.CUSTOMERS);
            customersGuis.put(contacts,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static ContactsGUI showContacts(Contacts contacts) {
        ContactsGUI gui = contactGuis.get(contacts);
        if (gui == null) {
            gui = new ContactsGUI(contacts, Contact.ContactType.ALL);
            contactGuis.put(contacts,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    private ContactsGUI(Contacts contacts, Contact.ContactType contactType) {
        super("Contacts");
        contactsPanel = new ContactsPanel(contactType, contacts);
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
        contactGuis.values().forEach(contactsGUI -> contactsGUI.fireContactDataChanged());
        suppliersGuis.values().forEach(contactsGUI -> contactsGUI.fireContactDataChanged());
        customersGuis.values().forEach(contactsGUI -> contactsGUI.fireContactDataChanged());
    }

    public static void fireCustomerDataChanged(){
        customersGuis.values().forEach(contactsGUI -> contactsGUI.fireContactDataChanged());
    }

    public void fireContactDataChanged(){
        contactsPanel.fireContactDataChanged();
    }

    public static void fireCustomersAddedOrRemovedForAll(){
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
