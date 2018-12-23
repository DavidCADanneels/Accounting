package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class ContactsSettingsPanel extends JPanel {
    public static final String title = getBundle("Contacts").getString("CONTACTS");
    private Accounting accounting;
    private JComboBox<Contact> allContacts;
    private DefaultComboBoxModel<Contact> model;

    public ContactsSettingsPanel(Accounting accounting) {
        this.accounting = accounting;
        model = new DefaultComboBoxModel<>();
        accounting.getContacts().getBusinessObjects().stream().forEach(contact -> model.addElement(contact));

        allContacts = new JComboBox<>(model);
        Contact companyContact = accounting.getCompanyContact();
        allContacts.setSelectedItem(companyContact);
        allContacts.addActionListener(e -> updateSelectedContact());
        allContacts.setEnabled(accounting.isContactsAccounting());

        add(new JLabel("Company Contact"));
        add(allContacts);
    }

    public void updateSelectedContact() {
        Contact contact = (Contact)allContacts.getSelectedItem();
        accounting.setCompanyContact(contact);
    }

    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        allContacts.setEnabled(enabled);
        if(!enabled){
            allContacts.setSelectedItem(null);
            updateSelectedContact();
        }
    }
}
