package be.dafke.BasicAccounting.Contacts;

import be.dafke.Accounting.BusinessModel.Accounting;
import be.dafke.Accounting.BusinessModel.Contact;
import be.dafke.ComponentModel.RefreshableDialog;

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

    public NewContactDialog(Accounting accounting) {
        super(getBundle("Contacts").getString("NEW_CONTACT_GUI_TITLE"));

        contactDetailsPanel = new ContactDetailsPanel();
        contactDetailsPanel.setAccounting(accounting);

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
