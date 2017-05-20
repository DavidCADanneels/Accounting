package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class ContactsDataModel extends SelectableTableModel<Contact> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] columnNames = {
			getBundle("Contacts").getString("NAME"),
			getBundle("Contacts").getString("VAT_NR"),
			getBundle("Contacts").getString("STREET_AND_NUMBER"),
			getBundle("Contacts").getString("POSTAL_CODE"),
			getBundle("Contacts").getString("CITY"),
			getBundle("Contacts").getString("COUNTRY"),
			getBundle("Contacts").getString("PHONE"),
			getBundle("Contacts").getString("EMAIL"),
			getBundle("Contacts").getString("TURNOVER"),
			getBundle("Contacts").getString("VAT_TOTAL") };
	Class[] columnClasses = {
			String.class,
			String.class,
			String.class,
			String.class,
			String.class,
			String.class,
			String.class,
			String.class,
			BigDecimal.class,
			BigDecimal.class };

    private Contacts contacts;

	public ContactsDataModel(Contacts contacts) {
		this.contacts = contacts;
	}

	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Contact contact = contacts.getBusinessObjects().get(row);
		if (col == 0) {
			return contact.getName();
		} else if (col == 1) {
			return contact.getVatNumber();
		} else if (col == 2) {
			return contact.getStreetAndNumber();
		} else if (col == 3) {
			return contact.getPostalCode();
		} else if (col == 4) {
			return contact.getCity();
		} else if (col == 5) {
			return contact.getCountryCode();
		} else if (col == 6) {
			return contact.getPhone();
		} else if (col == 7) {
			return contact.getEmail();
		} else if (col == 8) {
			return contact.getTurnOver();
		} else if (col == 9) {
			return contact.getVATTotal();
		} else return null;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
        if(contacts == null){
            return 0;
        }
		return contacts.getBusinessObjects().size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Class getColumnClass(int col) {
		return columnClasses[col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col<8;
	}

// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		Contact contact = contacts.getBusinessObjects().get(row);
		String stringValue = (String) value;
		if (col == 0) {
			contact.setName(stringValue);
		} else if (col == 1) {
			contact.setVatNumber(stringValue);
		} else if (col == 2) {
			contact.setStreetAndNumber(stringValue);
		} else if (col == 3) {
			contact.setPostalCode(stringValue);
		} else if (col == 4) {
			contact.setCity(stringValue);
		} else if (col == 5) {
			contact.setCountryCode(stringValue);
		} else if (col == 6) {
			contact.setPhone(stringValue);
		} else if (col == 7) {
			contact.setEmail(stringValue);
		}
	}

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

	@Override
	public Contact getObject(int row, int col) {
		return contacts.getBusinessObjects().get(row);
	}

}