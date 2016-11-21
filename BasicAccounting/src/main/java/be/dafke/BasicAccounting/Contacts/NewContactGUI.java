package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessActions.ActionUtils;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:34
 */
public class NewContactGUI extends RefreshableDialog {
    private final JTextField nameField, address1, address2;
    private final JButton add;
    private final Contacts contacts;

    public NewContactGUI(Contacts contacts) {
        super(getBundle("Contacts").getString("NEW_CONTACT_GUI_TITLE"));
        this.contacts = contacts;

        JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));

		JPanel line1 = new JPanel();
		line1.add(new JLabel(getBundle("Contacts").getString("NAME_LABEL")));
		nameField = new JTextField(20);
        line1.add(nameField);
        //
        north.add(line1);

        JPanel line2 = new JPanel();
        line2.add(new JLabel(getBundle("Contacts").getString("ADDR_LINE1_LABEL")));
        address1 = new JTextField(20);
        line2.add(address1);
        //
        north.add(line2);

        JPanel line3 = new JPanel();
        line3.add(new JLabel(getBundle("Contacts").getString("ADDR_LINE2_LABEL")));
        address2 = new JTextField(20);
        line3.add(address2);
        //
        north.add(line3);

//        JPanel line4 = new JPanel();
//		north.add(line4);

		add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_ACCOUNT"));
		add.addActionListener(e -> createContact());

		north.add(add);
        setContentPane(north);
        pack();
    }

    private void createContact() {
        Contact contact = new Contact();
        String name = nameField.getText().trim();
        contact.setName(name);
        String addr1 = address1.getText().trim();
        contact.setAddressLine1(addr1);
        String addr2 = address2.getText().trim();
        contact.setAddressLine2(addr2);
        try {
            contacts.addBusinessObject(contact);
//            Main.fireAccountDataChanged(account);
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_DUPLICATE_NAME, name);
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_NAME_EMPTY);
        }
        nameField.setText("");
        address1.setText("");
        address2.setText("");
    }

    public void refresh() {
        // nothing to do here
    }
}
