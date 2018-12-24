package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;

import javax.swing.*;

import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class ContactsSettingsPanel extends JPanel {
    private Accounting accounting;
    private final JComboBox<Contact> companyContactSelection, noInvoiceContactSelection;
    private final DefaultComboBoxModel<Contact> companyContactModel, noInVoiceContactModel;

    public ContactsSettingsPanel(Accounting accounting) {
        this.accounting = accounting;
        companyContactModel = new DefaultComboBoxModel<>();
        noInVoiceContactModel = new DefaultComboBoxModel<>();
        accounting.getContacts().getBusinessObjects().forEach(contact -> {
            companyContactModel.addElement(contact);
            noInVoiceContactModel.addElement(contact);
        });

        companyContactSelection = new JComboBox<>(companyContactModel);
        noInvoiceContactSelection = new JComboBox<>(noInVoiceContactModel);

        Contact companyContact = accounting.getCompanyContact();
        companyContactSelection.setSelectedItem(companyContact);
        companyContactSelection.addActionListener(e -> updateSelectedCompanyContact());
        companyContactSelection.setEnabled(accounting.isContactsAccounting());

        Contact contactNoInvoice = accounting.getContactNoInvoice();
        noInvoiceContactSelection.setSelectedItem(contactNoInvoice);
        noInvoiceContactSelection.addActionListener(e -> updateSelectedNoInvoiceContact());
        noInvoiceContactSelection.setEnabled(accounting.isContactsAccounting());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        panel.add(new JLabel("Company Contact"));
        panel.add(companyContactSelection);
        panel.add(new JLabel("NoInvoice Contact"));
        panel.add(noInvoiceContactSelection);

        add(panel);
    }

    public void updateSelectedCompanyContact() {
        Contact contact = (Contact) companyContactSelection.getSelectedItem();
        accounting.setCompanyContact(contact);
    }

    public void updateSelectedNoInvoiceContact() {
        Contact contact = (Contact) noInvoiceContactSelection.getSelectedItem();
        accounting.setContactNoInvoice(contact);
    }

    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        companyContactSelection.setEnabled(enabled);
        noInvoiceContactSelection.setEnabled(enabled);
        if(!enabled){
            companyContactSelection.setSelectedItem(null);
            noInvoiceContactSelection.setSelectedItem(null);
            updateSelectedCompanyContact();
            updateSelectedNoInvoiceContact();
        }
    }

    public void copyContacts(Accounting copyFrom) {
        Contact companyContactFrom = copyFrom.getCompanyContact();
        Contact contactNoInvoiceFrom = copyFrom.getContactNoInvoice();
        Contacts contacts = accounting.getContacts();

        companyContactModel.removeAllElements();
        noInVoiceContactModel.removeAllElements();

        if(copyFrom.isContactsAccounting() && companyContactFrom!=null && contactNoInvoiceFrom!=null) {
            Contact companyContact = contacts.getBusinessObject(companyContactFrom.getName());
            Contact contactNoInvoice = contacts.getBusinessObject(contactNoInvoiceFrom.getName());
            accounting.setCompanyContact(companyContact);
            accounting.setContactNoInvoice(contactNoInvoice);
            contacts.getBusinessObjects().forEach(contact -> {
                companyContactModel.addElement(contact);
                noInVoiceContactModel.addElement(contact);
                companyContactSelection.setSelectedItem(companyContact);
                noInvoiceContactSelection.setSelectedItem(contactNoInvoice);
            });
        } else {
            accounting.setCompanyContact(null);
            accounting.setContactNoInvoice(null);
            companyContactSelection.setSelectedItem(null);
            noInvoiceContactSelection.setSelectedItem(null);

        }
    }
}
