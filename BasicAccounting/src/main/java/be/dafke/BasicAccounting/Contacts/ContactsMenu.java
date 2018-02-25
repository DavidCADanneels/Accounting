package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contacts;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class ContactsMenu extends JMenu {
    private JMenuItem suppliers, customers;

    private Contacts contacts;

    public ContactsMenu() {
        super(getBundle("Contacts").getString("CONTACTS"));
//        setMnemonic(KeyEvent.VK_P);
        suppliers = new JMenuItem(getBundle("Contacts").getString(
                "SUPPLIERS"));
        suppliers.addActionListener(e -> ContactsGUI.showSuppliers(contacts).setVisible(true));
        suppliers.setEnabled(false);

        customers = new JMenuItem(getBundle("Contacts").getString(
                "CUSTOMERS"));
        customers.addActionListener(e -> ContactsGUI.showCustomers(contacts).setVisible(true));
        customers.setEnabled(false);

        add(customers);
        add(suppliers);
    }

    public void setAccounting(Accounting accounting) {
        setContacts(accounting==null?null:accounting.getContacts());
    }

    public void setContacts(Contacts contacts){
        this.contacts = contacts;
        suppliers.setEnabled(contacts!=null);
        customers.setEnabled(contacts!=null);
    }
}
