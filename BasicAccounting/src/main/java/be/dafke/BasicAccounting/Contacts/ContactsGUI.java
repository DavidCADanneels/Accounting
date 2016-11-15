package be.dafke.BasicAccounting.Contacts;

import be.dafke.ComponentModel.RefreshableFrame;

import javax.swing.*;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class ContactsGUI extends RefreshableFrame{

    public ContactsGUI() {
        super("Contacts");
        setContentPane(createContentPanel());
        pack();
    }

    public JPanel createContentPanel(){
        JPanel contentPanel = new JPanel();

        return contentPanel;
    }
}
