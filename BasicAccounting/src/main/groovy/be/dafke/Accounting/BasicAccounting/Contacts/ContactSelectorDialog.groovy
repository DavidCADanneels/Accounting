package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.*
import be.dafke.ComponentModel.RefreshableDialog

import javax.swing.*
import java.awt.*
import java.util.List

class ContactSelectorDialog extends RefreshableDialog {
    final JButton create, ok
    Contact contact
    Contact.ContactType contactType
    final JComboBox<Contact> combo
    final DefaultComboBoxModel<Contact> model
    Contacts contacts
    static ContactSelectorDialog contactSelectorDialog = null

    ContactSelectorDialog(Accounting accounting, Contact.ContactType contactType) {
        super("Select Contact")
        Contacts contacts = accounting.contacts
        this.contactType = contactType
        model = new DefaultComboBoxModel<>()
        combo = new JComboBox<>(model)
        combo.addActionListener({ e -> contact = (Contact) combo.selectedItem })
        create = new JButton("Add contact(s) ...")
        create.addActionListener({ e -> new NewContactDialog(accounting).visible = true })
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

    static ContactSelectorDialog getContactSelector(Accounting accounting, Contact.ContactType contactType){
        if(contactSelectorDialog==null){
            contactSelectorDialog = new ContactSelectorDialog(accounting, contactType)
        }
        contactSelectorDialog.accounting = accounting
        contactSelectorDialog
    }

    Contact getSelection() {
        contact
    }

    void setAccounting(Accounting accounting){
//		this.accounting = accounting
        setContacts(accounting?accounting.contacts:null)
    }

    void setContacts(Contacts contacts) {
        this.contacts = contacts
        fireContactDataChanged()
    }

    static void fireContactDataChangedForAll() {
        if(contactSelectorDialog ){
            contactSelectorDialog.fireContactDataChanged()
        }
    }

    void fireContactDataChanged() {
        model.removeAllElements()
        List<Contact> list = null
        if(contactType == Contact.ContactType.ALL){
            list = contacts.businessObjects
        } else if (contactType == Contact.ContactType.CUSTOMERS){
            list = contacts.getBusinessObjects{it.customer}
        } else if (contactType == Contact.ContactType.SUPPLIERS){
            list = contacts.getBusinessObjects{it.supplier}
        }
        if (list) {
            for (Contact contact : list) {
                model.addElement(contact)
            }
        }
        invalidate()
        combo.invalidate()
    }
}
