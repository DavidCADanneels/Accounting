package be.dafke.BasicAccounting.Contacts;

import be.dafke.BasicAccounting.MainApplication.SaveAllActionListener;
import be.dafke.BusinessModel.Contacts;

import javax.swing.*;
import java.util.HashMap;

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
