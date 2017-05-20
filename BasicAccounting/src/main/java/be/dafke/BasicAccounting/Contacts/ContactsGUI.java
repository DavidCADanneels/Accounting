package be.dafke.BasicAccounting.Contacts;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.BusinessModelDao.VATWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.io.File;
import java.util.HashMap;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class ContactsGUI extends JFrame{

    private final Contacts contacts;

    private static final HashMap<Contacts, ContactsGUI> contactGuis = new HashMap<>();

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
        pack();
    }

    public JPanel createContentPanel(){
        JButton create = new JButton("new Contact");
        create.addActionListener(e -> new NewContactGUI(contacts).setVisible(true));

        JButton createList = new JButton("create CustomerListing");
        createList.addActionListener(e -> createCustomerListing());

        JPanel south = new JPanel();
        south.add(create);
        south.add(createList);

        ContactsDataModel contactsDataModel = new ContactsDataModel(contacts);
        JTable center = new JTable(contactsDataModel);
        JScrollPane scroll = new JScrollPane(center);

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
}
