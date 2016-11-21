package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessModel.Contacts;
import be.dafke.ComponentModel.RefreshableFrame;

import javax.swing.*;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class ContactsGUI extends RefreshableFrame{

    private final Contacts contacts;

    public ContactsGUI(Contacts contacts) {
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
