package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;

import javax.swing.*;

import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class ContactsSettingsPanel extends JPanel {
    public static final String title = getBundle("Contacts").getString("CONTACTS");
    private Accounting accounting;
    private final JComboBox<Contact> companyContactSelection, noInvoiceContactSelection;
    private final DefaultComboBoxModel<Contact> companyContactModel, noInVoiceContactModel;

    public ContactsSettingsPanel(Accounting accounting) {
        this.accounting = accounting;
        companyContactModel = new DefaultComboBoxModel<>();
        noInVoiceContactModel = new DefaultComboBoxModel<>();
        accounting.getContacts().getBusinessObjects().stream().forEach(contact -> companyContactModel.addElement(contact));
        accounting.getContacts().getBusinessObjects().stream().forEach(contact -> noInVoiceContactModel.addElement(contact));

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

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
//        flowLayout.setAlignOnBaseline(true);
        setLayout(flowLayout);

        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Company Contact"));
        panel1.add(companyContactSelection);

        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("NoInvoice Contact"));
        panel2.add(noInvoiceContactSelection);

        add(panel1);
        add(panel2);
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
            updateSelectedCompanyContact();
            noInvoiceContactSelection.setSelectedItem(null);
            updateSelectedNoInvoiceContact();
        }
    }
}
