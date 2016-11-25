package be.dafke.BasicAccounting.Contacts;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Contacts;
import be.dafke.ComponentModel.RefreshableFrame;

import javax.swing.*;
import java.util.HashMap;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class ContactsGUI extends RefreshableFrame{

    private final Contacts contacts;
    public static final String SUPPLIERS = "Suppliers";
    public static final String CUSTOMERS = "Customers";

    private static final HashMap<Contacts, ContactsGUI> contactGuis = new HashMap<>();

    public static ContactsGUI showSuppliers(Contacts contacts) {
        String key = SUPPLIERS + contacts.hashCode();
        ContactsGUI gui = contactGuis.get(contacts);
        if (gui == null) {
            gui = new ContactsGUI(contacts);
            contactGuis.put(contacts,gui);
            Main.addJFrame(key, gui);
        }
        return gui;
    }

    public static ContactsGUI showCustomers(Contacts contacts) {
        String key = CUSTOMERS + contacts.hashCode();
        ContactsGUI gui = contactGuis.get(key);
        if (gui == null) {
            gui = new ContactsGUI(contacts);
            contactGuis.put(contacts,gui);
            Main.addJFrame(key, gui);
        }
        return gui;
    }

    private ContactsGUI(Contacts contacts) {
        super("Contacts");
        this.contacts = contacts;
        setContentPane(createContentPanel());
        pack();
    }

    public JPanel createContentPanel(){
        JPanel contentPanel = new JPanel();
        JButton create = new JButton("new Contact");
        create.addActionListener(e -> new NewContactGUI(contacts).setVisible(true));
        contentPanel.add(create);
        return contentPanel;
    }
}
