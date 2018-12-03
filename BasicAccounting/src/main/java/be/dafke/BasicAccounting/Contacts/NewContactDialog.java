package be.dafke.BasicAccounting.Contacts;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:34
 */
public class NewContactDialog extends RefreshableDialog {
    private final JButton add;
    private final ContactDetailsPanel contactDetailsPanel;

    public NewContactDialog(Contacts contacts) {
        super(getBundle("Contacts").getString("NEW_CONTACT_GUI_TITLE"));

        contactDetailsPanel = new ContactDetailsPanel(contacts);

		add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_CONTACT"));
		add.addActionListener(e -> contactDetailsPanel.saveAccount());

		contactDetailsPanel.add(add);
        setContentPane(contactDetailsPanel);
        pack();
    }

    public void setContact(Contact contact){
        contactDetailsPanel.setContact(contact);
    }
}
