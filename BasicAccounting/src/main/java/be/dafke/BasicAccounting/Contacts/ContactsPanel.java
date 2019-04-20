package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.BusinessModelDao.VATWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static java.util.ResourceBundle.getBundle;

public class ContactsPanel extends JPanel implements ListSelectionListener {
    private final Contacts contacts;
    private final Accounting accounting;

    private JTable table;
    private ContactsDataModel contactsDataModel;
    private JButton details;

    public ContactsPanel(Contact.ContactType contactType, Accounting accounting) {
        this.accounting = accounting;
        contacts = accounting.getContacts();

        JButton create = new JButton(getBundle("Contacts").getString("NEW_CONTACT"));
        create.addActionListener(e -> {
            NewContactDialog newContactDialog = new NewContactDialog(accounting);
            newContactDialog.setLocation(getLocationOnScreen());
            newContactDialog.setVisible(true);
        });

        JButton createList = new JButton(getBundle("Contacts").getString("CUSTUMER_LISTING"));
        createList.addActionListener(e -> createCustomerListing());

        details = new JButton(getBundle("Contacts").getString("EDIT_CONTACT"));
        details.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if(selectedRow!=-1) {
                Contact contact = contactsDataModel.getObject(selectedRow, 0);
                NewContactDialog newContactDialog = new NewContactDialog(accounting);
                newContactDialog.setContact(contact);
                newContactDialog.setVisible(true);
            }
        });
        details.setEnabled(false);

        JPanel south = new JPanel();
        south.add(create);
        south.add(details);
        if(contactType == Contact.ContactType.CUSTOMERS) {
            south.add(createList);
        }

        contactsDataModel = new ContactsDataModel(accounting, contacts, contactType);
        table = new JTable(contactsDataModel);
        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(this);
        table.setSelectionModel(selection);
        JScrollPane scroll = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(scroll, CENTER);
        add(south, SOUTH);
    }

    private void createCustomerListing() {
        Accounting accounting = contacts.getAccounting();
        Contact companyContact = accounting.getCompanyContact();
        if (companyContact == null) {
            setCompanyContact(accounting);
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML_EXTENSION files", "xml"));
        String year = JOptionPane.showInputDialog(this, "Year:");
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            VATWriter.writeCustomerListing(selectedFile, year, companyContact, contacts);
        }
    }

    public static void setCompanyContact(Accounting accounting){
        // TODO: replace companyContact by Contact of type 'OWN'
        ContactSelectorDialog contactSelectorDialog = ContactSelectorDialog.getContactSelector(accounting, Contact.ContactType.ALL);
        contactSelectorDialog.setVisible(true);
        Contact companyContact = contactSelectorDialog.getSelection();
        accounting.setCompanyContact(companyContact);
    }

    public void fireContactDataChanged(){
        contactsDataModel.fireTableDataChanged();
    }

    public void setContacts(){
        contactsDataModel.setContacts(contacts);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int[] rows = table.getSelectedRows();
            if (rows.length != 0) {
                details.setEnabled(true);
            } else {
                details.setEnabled(false);
            }
        }
    }
}
