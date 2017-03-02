package be.dafke.BasicAccounting.Contacts;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Contacts;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class ContactsGUI extends JFrame{

    private final Contacts contacts;

    private static final HashMap<Contacts, ContactsGUI> contactGuis = new HashMap<>();

    public static ContactsGUI showSuppliers(Contacts contacts) {
        ContactsGUI gui = contactGuis.get(contacts);
        if (gui == null) {
            gui = new ContactsGUI(contacts);
            contactGuis.put(contacts,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static ContactsGUI showCustomers(Contacts contacts) {
        ContactsGUI gui = contactGuis.get(contacts);
        if (gui == null) {
            gui = new ContactsGUI(contacts);
            contactGuis.put(contacts,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    private ContactsGUI(Contacts contacts) {
        super(contacts.getAccounting().getName() + " / " + "Contacts");
        this.contacts = contacts;
        setContentPane(createContentPanel());
        pack();
    }

    public JPanel createContentPanel(){
        JButton create = new JButton("new Contact");
        create.addActionListener(e -> new NewContactGUI(contacts).setVisible(true));

        ContactsDataModel contactsDataModel = new ContactsDataModel(contacts);
        JTable center = new JTable(contactsDataModel);
        JScrollPane scroll = new JScrollPane(center);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scroll, CENTER);
        contentPanel.add(create, SOUTH);
        return contentPanel;
    }
}
