package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;

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
			getBundle("Contacts").getString("CUSTOMER"),
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
			Boolean.class,
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
    private ArrayList<Integer> nonEditableColumns = new ArrayList<>();

	public ContactsDataModel(Contacts contacts) {
		this.contacts = contacts;
		nonEditableColumns.add(9);
		nonEditableColumns.add(10);
	}

	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Contact contact = contacts.getBusinessObjects().get(row);
		if (col == 0) {
			return contact.getName();
		} else if (col == 2) {
			return contact.getVatNumber();
		} else if (col == 3) {
			return contact.getStreetAndNumber();
		} else if (col == 4) {
			return contact.getPostalCode();
		} else if (col == 5) {
			return contact.getCity();
		} else if (col == 6) {
			return contact.getCountryCode();
		} else if (col == 7) {
			return contact.getPhone();
		} else if (col == 8) {
			return contact.getEmail();
		} else if (col == 1) {
			return contact.isCustomer();
		} else if (col == 9) {
			return contact.getTurnOver();
		} else if (col == 10) {
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
		return col<9;
	}

// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		Contact contact = contacts.getBusinessObjects().get(row);
		if(nonEditableColumns.contains(col)){
			// do nothing, not editable
		} else if(col==1) {
			Boolean customer = (Boolean) value;
			contact.setCustomer(customer);
		} else{
			String stringValue = (String) value;
			if (col == 0) {
				contact.setName(stringValue);
			} else if (col == 2) {
				contact.setVatNumber(stringValue);
			} else if (col == 3) {
				contact.setStreetAndNumber(stringValue);
			} else if (col == 4) {
				contact.setPostalCode(stringValue);
			} else if (col == 5) {
				contact.setCity(stringValue);
			} else if (col == 6) {
				contact.setCountryCode(stringValue);
			} else if (col == 7) {
				contact.setPhone(stringValue);
			} else if (col == 8) {
				contact.setEmail(stringValue);
			}
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