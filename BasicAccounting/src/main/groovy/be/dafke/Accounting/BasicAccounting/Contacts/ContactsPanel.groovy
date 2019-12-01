package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Contacts
import be.dafke.Accounting.BusinessModelDao.VATWriter

import javax.swing.*
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.filechooser.FileNameExtensionFilter
import java.awt.*

import static java.awt.BorderLayout.CENTER
import static java.awt.BorderLayout.SOUTH
import static java.util.ResourceBundle.getBundle

class ContactsPanel extends JPanel implements ListSelectionListener {
    final Contacts contacts
    final Accounting accounting

    JTable table
    ContactsDataModel contactsDataModel
    JButton details

    ContactsPanel(Contact.ContactType contactType, Accounting accounting) {
        this.accounting = accounting
        contacts = accounting.contacts

        JButton create = new JButton(getBundle("Contacts").getString("NEW_CONTACT"))
        create.addActionListener({ e ->
            NewContactDialog newContactDialog = new NewContactDialog(accounting)
            newContactDialog.setLocation(getLocationOnScreen())
            newContactDialog.visible = true
        })

        JButton createList = new JButton(getBundle("Contacts").getString("CUSTUMER_LISTING"))
        createList.addActionListener({ e -> createCustomerListing() })

        details = new JButton(getBundle("Contacts").getString("EDIT_CONTACT"))
        details.addActionListener({ e ->
            int selectedRow = table.getSelectedRow()
            if (selectedRow != -1) {
                Contact contact = contactsDataModel.getObject(selectedRow, 0)
                NewContactDialog newContactDialog = new NewContactDialog(accounting)
                newContactDialog.setContact(contact)
                newContactDialog.visible = true
            }
        })
        details.enabled = false

        JPanel south = new JPanel()
        south.add(create)
        south.add(details)
        if(contactType == Contact.ContactType.CUSTOMERS) {
            south.add(createList)
        }

        contactsDataModel = new ContactsDataModel(accounting, contacts, contactType)
        table = new JTable(contactsDataModel)
        DefaultListSelectionModel selection = new DefaultListSelectionModel()
        selection.addListSelectionListener(this)
        table.setSelectionModel(selection)
        JScrollPane scroll = new JScrollPane(table)

        setLayout(new BorderLayout())
        add(scroll, CENTER)
        add(south, SOUTH)
    }

    void createCustomerListing() {
        Accounting accounting = contacts.accounting
        Contact companyContact = accounting.getCompanyContact()
        if (companyContact == null) {
            setCompanyContact(accounting)
        }

        JFileChooser fileChooser = new JFileChooser()
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML_EXTENSION files", "xml"))
        String year = JOptionPane.showInputDialog(this, "Year:")
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile()

            VATWriter.writeCustomerListing(selectedFile, year, companyContact, contacts)
        }
    }

    static void setCompanyContact(Accounting accounting){
        // TODO: replace companyContact by Contact of type 'OWN'
        ContactSelectorDialog contactSelectorDialog = ContactSelectorDialog.getContactSelector(accounting, Contact.ContactType.ALL)
        contactSelectorDialog.visible = true
        Contact companyContact = contactSelectorDialog.selection
        accounting.companyContact = companyContact
    }

    void fireTableUpdate(){
        contactsDataModel.fireTableDataChanged()
    }

    void setContacts(){
        contactsDataModel.contacts = contacts
    }

    @Override
    void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int[] rows = table.selectedRows
            if (rows.length != 0) {
                details.enabled = true
            } else {
                details.enabled = false
            }
        }
    }
}
