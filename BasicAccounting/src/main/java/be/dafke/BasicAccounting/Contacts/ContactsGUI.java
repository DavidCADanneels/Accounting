package be.dafke.BasicAccounting.Contacts;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.BusinessModelDao.VATWriter;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class ContactsGUI extends JFrame implements ListSelectionListener {

    private final Contacts contacts;

    private static final HashMap<Contacts, ContactsGUI> contactGuis = new HashMap<>();
    private JTable table;
    private ContactsDataModel contactsDataModel;
    private JButton details;

    public static ContactsGUI showSuppliers(Contacts contacts) {
        ContactsGUI gui = contactGuis.get(contacts);
        if (gui == null) {
            gui = new ContactsGUI(contacts);
            contactGuis.put(contacts,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static ContactsGUI showCustomers(Contacts contacts) {
        ContactsGUI gui = contactGuis.get(contacts);
        if (gui == null) {
            gui = new ContactsGUI(contacts);
            contactGuis.put(contacts,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    private ContactsGUI(Contacts contacts) {
        super(contacts.getAccounting().getName() + " / " + "Contacts");
        this.contacts = contacts;
        setContentPane(createContentPanel());
        setPreferredSize(new Dimension(1000,400));
        pack();
    }

    public JPanel createContentPanel(){
        JButton create = new JButton(getBundle("Contacts").getString("NEW_CONTACT"));
        create.addActionListener(e -> new NewContactGUI(contacts).setVisible(true));

        JButton createList = new JButton(getBundle("Contacts").getString("CUSTUMER_LISTING"));
        createList.addActionListener(e -> createCustomerListing());

        details = new JButton(getBundle("Contacts").getString("EDIT_CONTACT"));
        details.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if(selectedRow!=-1) {
                Contact contact = contactsDataModel.getObject(selectedRow, 0);
                NewContactGUI newContactGUI = new NewContactGUI(contacts);
                newContactGUI.setContact(contact);
                newContactGUI.setVisible(true);
            }
        });
        details.setEnabled(false);

        JPanel south = new JPanel();
        south.add(create);
        south.add(createList);
        south.add(details);

        contactsDataModel = new ContactsDataModel(contacts);
        table = new JTable(contactsDataModel);
        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(this);
        table.setSelectionModel(selection);
        JScrollPane scroll = new JScrollPane(table);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scroll, CENTER);
        contentPanel.add(south, SOUTH);
        return contentPanel;
    }

    private void createCustomerListing() {
        Accounting accounting = contacts.getAccounting();
        Contact companyContact = accounting.getCompanyContact();
        if (companyContact == null) {
            ContactSelector contactSelector = ContactSelector.getContactSelector(accounting.getContacts());
            contactSelector.setVisible(true);
            companyContact = contactSelector.getSelection();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));
        String year = JOptionPane.showInputDialog(this, "Year:");
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            VATWriter.writeCustomerListing(selectedFile, year, companyContact, contacts);
        }
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
