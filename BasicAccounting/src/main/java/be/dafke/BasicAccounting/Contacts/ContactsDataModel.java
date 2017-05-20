package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class ContactsDataModel extends SelectableTableModel<Contact> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int NAME_COL = 0;
	public static final int VAT_NUMBER_COL = 2;
	public static final int STREET_COL = 3;
	public static final int POSTAL_COL = 4;
	public static final int CITY_COL = 5;
	public static final int COUNTRY_COL = 6;
	public static final int PHONE_COL = 7;
	public static final int EMAIL_COL = 8;
	public static final int CUSTOMER_COL = 1;
	public static final int TURNOVER_COL = 9;
	public static final int VAT_TOTAL_COL = 10;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
    private Contacts contacts;
    private ArrayList<Integer> nonEditableColumns = new ArrayList<>();

	public ContactsDataModel(Contacts contacts) {
		this.contacts = contacts;
		nonEditableColumns.add(TURNOVER_COL);
		nonEditableColumns.add(VAT_TOTAL_COL);
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(CUSTOMER_COL, Boolean.class);
		columnClasses.put(VAT_NUMBER_COL, String.class);
		columnClasses.put(STREET_COL, String.class);
		columnClasses.put(POSTAL_COL, String.class);
		columnClasses.put(CITY_COL, String.class);
		columnClasses.put(COUNTRY_COL, String.class);
		columnClasses.put(PHONE_COL, String.class);
		columnClasses.put(EMAIL_COL, String.class);
		columnClasses.put(TURNOVER_COL, BigDecimal.class);
		columnClasses.put(VAT_TOTAL_COL, BigDecimal.class);
	}

	private void setColumnNames() {
		columnNames.put(NAME_COL, getBundle("Contacts").getString("NAME"));
		columnNames.put(CUSTOMER_COL, getBundle("Contacts").getString("CUSTOMER"));
		columnNames.put(VAT_NUMBER_COL, getBundle("Contacts").getString("VAT_NR"));
		columnNames.put(STREET_COL,getBundle("Contacts").getString("STREET_AND_NUMBER"));
		columnNames.put(POSTAL_COL, getBundle("Contacts").getString("POSTAL_CODE"));
		columnNames.put(CITY_COL, getBundle("Contacts").getString("CITY"));
		columnNames.put(COUNTRY_COL, getBundle("Contacts").getString("COUNTRY"));
		columnNames.put(PHONE_COL, getBundle("Contacts").getString("PHONE"));
		columnNames.put(EMAIL_COL, getBundle("Contacts").getString("EMAIL"));
		columnNames.put(TURNOVER_COL, getBundle("Contacts").getString("TURNOVER"));
		columnNames.put(VAT_TOTAL_COL, getBundle("Contacts").getString("VAT_TOTAL"));
	}


	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Contact contact = contacts.getBusinessObjects().get(row);
		if (col == NAME_COL) {
			return contact.getName();
		} else if (col == VAT_NUMBER_COL) {
			return contact.getVatNumber();
		} else if (col == STREET_COL) {
			return contact.getStreetAndNumber();
		} else if (col == POSTAL_COL) {
			return contact.getPostalCode();
		} else if (col == CITY_COL) {
			return contact.getCity();
		} else if (col == COUNTRY_COL) {
			return contact.getCountryCode();
		} else if (col == PHONE_COL) {
			return contact.getPhone();
		} else if (col == EMAIL_COL) {
			return contact.getEmail();
		} else if (col == CUSTOMER_COL) {
			return contact.isCustomer();
		} else if (col == TURNOVER_COL) {
			return contact.getTurnOver();
		} else if (col == VAT_TOTAL_COL) {
			return contact.getVATTotal();
		} else return null;
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
        if(contacts == null){
            return 0;
        }
		return contacts.getBusinessObjects().size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	@Override
	public Class getColumnClass(int col) {
		return columnClasses.get(col);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return !nonEditableColumns.contains(col);
	}

// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		Contact contact = contacts.getBusinessObjects().get(row);
		if(isCellEditable(row, col)){
			if(col== CUSTOMER_COL) {
				Boolean customer = (Boolean) value;
				contact.setCustomer(customer);
			} else {
				String stringValue = (String) value;
				if (col == NAME_COL) {
					contact.setName(stringValue);
				} else if (col == VAT_NUMBER_COL) {
					contact.setVatNumber(stringValue);
				} else if (col == STREET_COL) {
					contact.setStreetAndNumber(stringValue);
				} else if (col == POSTAL_COL) {
					contact.setPostalCode(stringValue);
				} else if (col == CITY_COL) {
					contact.setCity(stringValue);
				} else if (col == COUNTRY_COL) {
					contact.setCountryCode(stringValue);
				} else if (col == PHONE_COL) {
					contact.setPhone(stringValue);
				} else if (col == EMAIL_COL) {
					contact.setEmail(stringValue);
				}
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