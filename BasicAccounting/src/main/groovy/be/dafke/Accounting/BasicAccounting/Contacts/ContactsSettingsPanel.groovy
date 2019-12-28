package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Contacts

import javax.swing.*

import java.awt.*

class ContactsSettingsPanel extends JPanel {
    Accounting accounting
    final JComboBox<Contact> companyContactSelection, noInvoiceContactSelection
    final DefaultComboBoxModel<Contact> companyContactModel, noInVoiceContactModel

    ContactsSettingsPanel(Accounting accounting) {
        this.accounting = accounting
        companyContactModel = new DefaultComboBoxModel<>()
        noInVoiceContactModel = new DefaultComboBoxModel<>()
        accounting.contacts.businessObjects.forEach({ contact ->
            companyContactModel.addElement(contact)
            noInVoiceContactModel.addElement(contact)
        })

        companyContactSelection = new JComboBox<>(companyContactModel)
        noInvoiceContactSelection = new JComboBox<>(noInVoiceContactModel)

        companyContactSelection.setSelectedItem(accounting.companyContact)
        companyContactSelection.addActionListener({ e -> updateSelectedCompanyContact() })
        companyContactSelection.enabled = accounting.contactsAccounting

        noInvoiceContactSelection.setSelectedItem(accounting.contactNoInvoice)
        noInvoiceContactSelection.addActionListener({ e -> updateSelectedNoInvoiceContact() })
        noInvoiceContactSelection.enabled = accounting.contactsAccounting

        JPanel panel = new JPanel()
        panel.setLayout(new GridLayout(0, 2))

        panel.add(new JLabel("Company Contact"))
        panel.add(companyContactSelection)
        panel.add(new JLabel("NoInvoice Contact"))
        panel.add(noInvoiceContactSelection)

        add(panel)
    }

    void updateSelectedCompanyContact() {
        Contact contact = (Contact) companyContactSelection.selectedItem
        accounting.companyContact = contact
    }

    void updateSelectedNoInvoiceContact() {
        Contact contact = (Contact) noInvoiceContactSelection.selectedItem
        accounting.contactNoInvoice = contact
    }

    @Override
    void setEnabled(boolean enabled){
        super.setEnabled(enabled)
        companyContactSelection.enabled = enabled
        noInvoiceContactSelection.enabled = enabled
        if(!enabled){
            companyContactSelection.selectedItem = null
            noInvoiceContactSelection.selectedItem = null
            updateSelectedCompanyContact()
            updateSelectedNoInvoiceContact()
        }
    }

    void copyContacts(Accounting copyFrom) {
        companyContactModel.removeAllElements()
        noInVoiceContactModel.removeAllElements()

        if (copyFrom) {
            Contact companyContactFrom = copyFrom.companyContact
            Contact contactNoInvoiceFrom = copyFrom.contactNoInvoice
            Contacts contacts = accounting.contacts


            if (copyFrom.isContactsAccounting() && companyContactFrom != null && contactNoInvoiceFrom != null) {
                Contact companyContact = contacts.getBusinessObject companyContactFrom.name
                Contact contactNoInvoice = contacts.getBusinessObject contactNoInvoiceFrom.name
                accounting.companyContact = companyContact
                accounting.contactNoInvoice = contactNoInvoice
                contacts.businessObjects.each { contact ->
                    companyContactModel.addElement(contact)
                    noInVoiceContactModel.addElement(contact)
                }
                companyContactSelection.selectedItem = companyContact
                noInvoiceContactSelection.selectedItem = contactNoInvoice
            } else {
                accounting.companyContact = null
                accounting.contactNoInvoice = null
                companyContactSelection.selectedItem = null
                noInvoiceContactSelection.selectedItem = null

            }
        } else {
            companyContactSelection.selectedItem = null
            noInvoiceContactSelection.selectedItem = null
        }
    }
}
