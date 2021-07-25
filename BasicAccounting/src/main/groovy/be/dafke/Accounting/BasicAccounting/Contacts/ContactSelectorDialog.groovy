package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.*
import be.dafke.ComponentModel.RefreshableDialog

import javax.swing.*
import java.awt.*

class ContactSelectorDialog extends RefreshableDialog {
    final JButton create, ok
    Contact contact
    Contact.ContactType contactType
    final JComboBox<Contact> combo
    final DefaultComboBoxModel<Contact> model
    static ContactSelectorDialog contactSelectorDialog = null

    ContactSelectorDialog(Contact.ContactType contactType) {
        super("Select Contact")
        this.contactType = contactType
        model = new DefaultComboBoxModel<>()
        combo = new JComboBox<>(model)
        combo.addActionListener({ e -> contact = (Contact) combo.selectedItem })
        create = new JButton("Add contact(s) ...")
        create.addActionListener({ e -> new NewContactDialog().visible = true })
        ok = new JButton("Ok (Close popup)")
        ok.addActionListener({ e -> dispose() })
        JPanel innerPanel = new JPanel(new BorderLayout())
        JPanel panel = new JPanel()
        panel.add(combo)
        panel.add(create)
        innerPanel.add(panel, BorderLayout.CENTER)
        innerPanel.add(ok, BorderLayout.SOUTH)
        setContentPane(innerPanel)
//		setContacts(contacts)
        pack()
    }

    static ContactSelectorDialog getContactSelector(Contact.ContactType contactType){
        if(contactSelectorDialog==null){
            contactSelectorDialog = new ContactSelectorDialog(contactType)
        }
        contactSelectorDialog
    }

    Contact getSelection() {
        contact
    }
}
