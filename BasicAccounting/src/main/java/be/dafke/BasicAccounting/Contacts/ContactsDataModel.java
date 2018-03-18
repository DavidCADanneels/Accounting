package be.dafke.BasicAccounting.Contacts;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class ContactsDataModel extends SelectableTableModel<Contact> {
	public int NAME_COL;
	public int VAT_NUMBER_COL;
	public int STREET_COL;
	public int POSTAL_COL;
	public int CITY_COL;
	public int COUNTRY_COL;
	public int PHONE_COL;
	public int OFFICIAL_COL;
	public int EMAIL_COL;
	public int CUSTOMER_COL;
	public int SUPPLIER_COL;
	public int TURNOVER_COL;
	public int VAT_TOTAL_COL;
	public int NR_OF_COL;
	private Contact.ContactType contactType;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
    private List<Contact> contacts;
    private ArrayList<Integer> nonEditableColumns = new ArrayList<>();

	public ContactsDataModel(Contacts contacts, Contact.ContactType contactType) {
		this.contactType = contactType;
		if(contactType == Contact.ContactType.ALL) {
			this.contacts = contacts.getBusinessObjects();
		} else if (contactType == Contact.ContactType.CUSTOMERS){
			this.contacts = contacts.getBusinessObjects(Contact::isCustomer);
		} else if (contactType == Contact.ContactType.SUPPLIERS) {
			this.contacts = contacts.getBusinessObjects(Contact::isSupplier);
		}
		initialize();
	}

	private void initialize() {
		setColumnNumbers();
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnNumbers() {
		if(contactType == Contact.ContactType.CUSTOMERS){
			NAME_COL = 0;
			VAT_NUMBER_COL = 1;
			STREET_COL = 2;
			POSTAL_COL = 3;
			CITY_COL = 4;
			COUNTRY_COL = 5;
			PHONE_COL = 6;
			EMAIL_COL = 7;
			TURNOVER_COL = 8;
			VAT_TOTAL_COL = 9;
			NR_OF_COL = 10;
			CUSTOMER_COL = 11;
			SUPPLIER_COL = 12;
		} else if(contactType == Contact.ContactType.SUPPLIERS){
			NAME_COL = 0;
			VAT_NUMBER_COL = 1;
			STREET_COL = 2;
			POSTAL_COL = 3;
			CITY_COL = 4;
			COUNTRY_COL = 5;
			PHONE_COL = 6;
			EMAIL_COL = 7;
			TURNOVER_COL = 8;
			VAT_TOTAL_COL = 9;
			NR_OF_COL = 10;
			CUSTOMER_COL = 11;
			SUPPLIER_COL = 12;
		} else if(contactType == Contact.ContactType.ALL){
			NAME_COL = 0;
			OFFICIAL_COL = 1;
			CUSTOMER_COL = 2;
			SUPPLIER_COL = 3;
			VAT_NUMBER_COL = 4;
			STREET_COL = 5;
			POSTAL_COL = 6;
			CITY_COL = 7;
			COUNTRY_COL = 8;
			PHONE_COL = 9;
			EMAIL_COL = 10;
			TURNOVER_COL = 11;
			VAT_TOTAL_COL = 12;
			NR_OF_COL = 13;
		}
		nonEditableColumns.add(VAT_TOTAL_COL);
		nonEditableColumns.add(TURNOVER_COL);
	}


	private void setColumnClasses() {
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(OFFICIAL_COL, String.class);
		columnClasses.put(CUSTOMER_COL, Boolean.class);
		columnClasses.put(SUPPLIER_COL, Boolean.class);
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
		columnNames.put(OFFICIAL_COL, getBundle("Contacts").getString("OFFICIAL_NAME"));
		columnNames.put(CUSTOMER_COL, getBundle("Contacts").getString("CUSTOMER"));
		columnNames.put(SUPPLIER_COL, getBundle("Contacts").getString("SUPPLIER"));
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
		Contact contact = contacts.get(row);
		if (col == NAME_COL) {
			return contact.getName();
		} else if (col == OFFICIAL_COL) {
			return contact.getOfficialName();
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
		} else if (col == SUPPLIER_COL) {
			return contact.isSupplier();
		} else if (col == TURNOVER_COL) {
			return contact.getTurnOver();
		} else if (col == VAT_TOTAL_COL) {
			return contact.getVATTotal();
		} else return null;
	}

	public int getColumnCount() {
		return NR_OF_COL;
	}

	public int getRowCount() {
        if(contacts == null){
            return 0;
        }
		return contacts.size();
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
		Contact contact = contacts.get(row);
		if(isCellEditable(row, col)){
			if(col== CUSTOMER_COL) {
				Boolean customer = (Boolean) value;
				contact.setCustomer(customer);
				Main.fireCustomerAddedOrRemoved();
			} else if(col== SUPPLIER_COL) {
				Boolean supplier = (Boolean) value;
				contact.setSupplier(supplier);
				Main.fireSupplierAddedOrRemoved();
			} else {
				String stringValue = (String) value;
				if (col == NAME_COL) {
					contact.setName(stringValue);
				} else if (col == OFFICIAL_COL) {
					contact.setOfficialName(stringValue);
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
				Main.fireContactDataChanged();
			}
		}
	}

    public void setContacts(Contacts contacts) {
		if(contactType == Contact.ContactType.ALL) {
			this.contacts = contacts.getBusinessObjects();
		} else if (contactType == Contact.ContactType.CUSTOMERS){
			setCustomers(contacts);
		} else if (contactType == Contact.ContactType.SUPPLIERS){
			setSuppliers(contacts);
		}
		fireTableDataChanged();
    }
    public void setCustomers(Contacts contacts) {
        this.contacts = contacts.getBusinessObjects(Contact::isCustomer);
    }
    public void setSuppliers(Contacts contacts) {
        this.contacts = contacts.getBusinessObjects(Contact::isSupplier);
    }

	@Override
	public Contact getObject(int row, int col) {
		return contacts.get(row);
	}

}