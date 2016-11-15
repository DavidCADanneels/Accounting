package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessActions.AccountingListener;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contacts;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class ContactsMenu extends JMenu implements AccountingListener {
    private JMenuItem suppliers, customers;
    public static final String SUPPLIERS = "Suppliers";
    public static final String CUSTOMERS = "Customers";
    private Contacts contacts;

    public ContactsMenu() {
        super(getBundle("Contacts").getString("CONTACTS"));
        setMnemonic(KeyEvent.VK_P);
        suppliers = new JMenuItem(getBundle("Contacts").getString(
                "SUPPLIERS"));
        suppliers.addActionListener(e -> showSuppliers());
        suppliers.setEnabled(false);

        customers = new JMenuItem(getBundle("Contacts").getString(
                "CUSTOMERS"));
        customers.addActionListener(e -> showCustomers());
        customers.setEnabled(false);

        add(customers);
        add(suppliers);
    }

    private void showSuppliers() {
        String key = SUPPLIERS + contacts.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if (gui == null) {
            gui = new ContactsGUI();
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    private void showCustomers() {
        String key = CUSTOMERS + contacts.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if (gui == null) {
            gui = new ContactsGUI();
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    @Override
    public void setAccounting(Accounting accounting) {
        contacts=accounting==null?null:accounting.getContacts();
        suppliers.setEnabled(contacts!=null);
        customers.setEnabled(contacts!=null);
    }
}
