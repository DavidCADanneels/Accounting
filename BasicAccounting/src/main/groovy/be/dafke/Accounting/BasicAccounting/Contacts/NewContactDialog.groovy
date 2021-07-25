package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.ComponentModel.RefreshableDialog

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class NewContactDialog extends RefreshableDialog {
    final JButton addButton
    final ContactDetailsPanel contactDetailsPanel

    NewContactDialog() {
        super(getBundle("Contacts").getString("NEW_CONTACT_GUI_TITLE"))

        contactDetailsPanel = new ContactDetailsPanel()

        addButton = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_CONTACT"))
        addButton.addActionListener({ e -> contactDetailsPanel.saveAccount() })

        contactDetailsPanel.add(addButton)
        setContentPane(contactDetailsPanel)
        pack()
    }

    void setContact(Contact contact){
        contactDetailsPanel.setContact(contact)
    }
}