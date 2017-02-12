package be.dafke.BasicAccounting.Contacts;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
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
    public static final String NAME = "NAME";
    public static final String STREET_AND_NUMBER = "STREET_AND_NUMBER";
    public static final String POSTAL_CODE = "POSTAL_CODE";
    public static final String VAT_NR = "VAT_NR";
    public static final String CITY = "CITY";
    public static final String COUNTRY = "COUNTRY";
    public static final String PHONE = "PHONE";
    public static final String EMAIL = "EMAIL";
    private final JTextField contactName, contactVAT, contactStreet, contactPostalCode, contactCity, contactCountry, contactPhone, contactEmail;
    private final JButton add;
    private final Contacts contacts;

    public NewContactGUI(Contacts contacts) {
        super(getBundle("Contacts").getString("NEW_CONTACT_GUI_TITLE"));
        this.contacts = contacts;

        JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));

        contactName = new JTextField(20);
        contactVAT = new JTextField(20);
        contactStreet = new JTextField(20);
        contactPostalCode = new JTextField(20);
        contactCity = new JTextField(20);
        contactCountry = new JTextField(20);
        contactEmail = new JTextField(20);
        contactPhone = new JTextField(20);

        north.add(createPanel(contactName, NAME));
        north.add(createPanel(contactVAT, VAT_NR));
        north.add(createPanel(contactStreet, STREET_AND_NUMBER));
        north.add(createPanel(contactPostalCode, POSTAL_CODE));
        north.add(createPanel(contactCity, CITY));
        north.add(createPanel(contactCountry, COUNTRY));
        north.add(createPanel(contactPhone, PHONE));
        north.add(createPanel(contactEmail, EMAIL));

//        JPanel line4 = new JPanel();
//		north.add(line4);

		add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_CONTACT"));
		add.addActionListener(e -> createContact());

		north.add(add);
        setContentPane(north);
        pack();
    }

    private JPanel createPanel(JComponent component, String label){
        JPanel panel = new JPanel();
        panel.add(new JLabel(getBundle("Contacts").getString(label)));
        panel.add(component);
        return panel;
    }

    private void createContact() {
        Contact contact = new Contact();
        String name = contactName.getText().trim();
        contact.setName(name);
        String vat = contactVAT.getText().trim();
        contact.setVatNumber(vat);
        String street = contactStreet.getText().trim();
        contact.setStreetAndNumber(street);
        String postalCode = contactPostalCode.getText().trim();
        contact.setPostalCode(postalCode);
        String city = contactCity.getText().trim();
        contact.setCity(city);
        String country = contactCountry.getText().trim();
        contact.setCountryCode(country);
        String email = contactEmail.getText().trim();
        contact.setEmail(email);
        String phone = contactPhone.getText().trim();
        contact.setPhone(phone);
        try {
            contacts.addBusinessObject(contact);
            Main.fireContactDataChanged(contact);
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_DUPLICATE_NAME, name);
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_NAME_EMPTY);
        }
        contactName.setText("");
        contactStreet.setText("");
        contactPostalCode.setText("");
    }
}
