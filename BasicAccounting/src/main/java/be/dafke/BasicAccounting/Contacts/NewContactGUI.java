package be.dafke.BasicAccounting.Contacts;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;

import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:34
 */
public class NewContactGUI extends RefreshableDialog {
    public static final String NAME = "NAME_LABEL";
    public static final String STREET_AND_NUMBER = "STREET_AND_NUMBER_LABEL";
    public static final String POSTAL_CODE = "POSTAL_CODE_LABEL";
    public static final String VAT_NR = "VAT_NR_LABEL";
    public static final String CITY = "CITY_LABEL";
    public static final String COUNTRY = "COUNTRY_LABEL";
    public static final String PHONE = "PHONE_LABEL";
    public static final String EMAIL = "EMAIL_LABEL";
    private final JTextField contactName, contactVAT, contactStreet, contactPostalCode, contactCity, contactCountry, contactPhone, contactEmail;
    private final JButton add;
    private final Contacts contacts;
    private Contact contact;
    private boolean newContact;

    public NewContactGUI(Contacts contacts) {
        super(getBundle("Contacts").getString("NEW_CONTACT_GUI_TITLE"));
        this.contacts = contacts;

        JPanel north = new JPanel(new GridLayout(0,2));

        contactName = new JTextField(20);
        contactVAT = new JTextField(20);
        contactStreet = new JTextField(20);
        contactPostalCode = new JTextField(20);
        contactCity = new JTextField(20);
        contactCountry = new JTextField(20);
        contactEmail = new JTextField(20);
        contactPhone = new JTextField(20);

        north.add(new JLabel(getBundle("Contacts").getString(NAME)));
        north.add(contactName);
        north.add(new JLabel(getBundle("Contacts").getString(VAT_NR)));
        north.add(contactVAT);
        north.add(new JLabel(getBundle("Contacts").getString(STREET_AND_NUMBER)));
        north.add(contactStreet);
        north.add(new JLabel(getBundle("Contacts").getString(POSTAL_CODE)));
        north.add(contactPostalCode);
        north.add(new JLabel(getBundle("Contacts").getString(CITY)));
        north.add(contactCity);
        north.add(new JLabel(getBundle("Contacts").getString(COUNTRY)));
        north.add(contactCountry);
        north.add(new JLabel(getBundle("Contacts").getString(PHONE)));
        north.add(contactPhone);
        north.add(new JLabel(getBundle("Contacts").getString(EMAIL)));
        north.add(contactEmail);

		add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_CONTACT"));
		add.addActionListener(e -> saveAccount());

		north.add(add);
        setContentPane(north);
        pack();
    }

    public void setContact(Contact contact){
        this.contact = contact;
        contactName.setText(contact.getName());
        contactVAT.setText(contact.getVatNumber());
        contactStreet.setText(contact.getStreetAndNumber());
        contactPostalCode.setText(contact.getPostalCode());
        contactCity.setText(contact.getCity());
        contactCountry.setText(contact.getCountryCode());
        contactEmail.setText(contact.getEmail());
        contactPhone.setText(contact.getPhone());
    }

    private void saveAccount() {
        if(contact==null) {
            contact = new Contact();
            newContact = true;
        }
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
        if(newContact) {
            try {
                contacts.addBusinessObject(contact);
                Main.fireContactAdded();
            } catch (DuplicateNameException e) {
                ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_DUPLICATE_NAME, name);
            } catch (EmptyNameException e) {
                ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_NAME_EMPTY);
            }
            clearFields();
            contact=null;
        }
    }

    private void clearFields() {
        contactName.setText("");
        contactStreet.setText("");
        contactPostalCode.setText("");
        contactVAT.setText("");
        contactCity.setText("");
        contactCountry.setText("");
        contactEmail.setText("");
        contactPhone.setText("");
    }
}
