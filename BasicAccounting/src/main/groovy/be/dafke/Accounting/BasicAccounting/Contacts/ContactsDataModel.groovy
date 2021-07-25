package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BasicAccounting.Trade.StockUtils
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Contacts
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

/**
 * @author David Danneels
 */

class ContactsDataModel extends SelectableTableModel<Contact> {
    int NAME_COL
    int VAT_NUMBER_COL
    int STREET_COL
    int POSTAL_COL
    int CITY_COL
    int COUNTRY_COL
    int PHONE_COL
    int OFFICIAL_COL
    int EMAIL_COL
    int CUSTOMER_COL
    int SUPPLIER_COL
    int TURNOVER_COL
    int VAT_TOTAL_COL
    int NR_OF_COL
    Contact.ContactType contactType = Contact.ContactType.ALL
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    ArrayList<Integer> nonEditableColumns = new ArrayList<>()

    ContactsDataModel(Contact.ContactType contactType) {
        this.contactType = contactType
        initialize()
    }

    void setContactType(Contact.ContactType contactType) {
        this.contactType = contactType
        initialize()
        fireTableDataChanged()
    }

    void initialize() {
        setColumnNumbers()
        setColumnNames()
        setColumnClasses()
    }

    void setColumnNumbers() {
        if(contactType == Contact.ContactType.CUSTOMERS){
            NAME_COL = 0
            VAT_NUMBER_COL = 1
            STREET_COL = 2
            POSTAL_COL = 3
            CITY_COL = 4
            COUNTRY_COL = 5
            PHONE_COL = 6
            EMAIL_COL = 7
            TURNOVER_COL = 8
            VAT_TOTAL_COL = 9
            NR_OF_COL = 10
            CUSTOMER_COL = 11
            SUPPLIER_COL = 12
        } else if(contactType == Contact.ContactType.SUPPLIERS){
            NAME_COL = 0
            VAT_NUMBER_COL = 1
            STREET_COL = 2
            POSTAL_COL = 3
            CITY_COL = 4
            COUNTRY_COL = 5
            PHONE_COL = 6
            EMAIL_COL = 7
            TURNOVER_COL = 8
            VAT_TOTAL_COL = 9
            NR_OF_COL = 10
            CUSTOMER_COL = 11
            SUPPLIER_COL = 12
        } else if(contactType == Contact.ContactType.ALL){
            NAME_COL = 0
            OFFICIAL_COL = 1
            CUSTOMER_COL = 2
            SUPPLIER_COL = 3
            VAT_NUMBER_COL = 4
            STREET_COL = 5
            POSTAL_COL = 6
            CITY_COL = 7
            COUNTRY_COL = 8
            PHONE_COL = 9
            EMAIL_COL = 10
            TURNOVER_COL = 11
            VAT_TOTAL_COL = 12
            NR_OF_COL = 13
        }
        nonEditableColumns.add(VAT_TOTAL_COL)
        nonEditableColumns.add(TURNOVER_COL)
    }

    void setColumnClasses() {
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(OFFICIAL_COL, String.class)
        columnClasses.put(CUSTOMER_COL, Boolean.class)
        columnClasses.put(SUPPLIER_COL, Boolean.class)
        columnClasses.put(VAT_NUMBER_COL, String.class)
        columnClasses.put(STREET_COL, String.class)
        columnClasses.put(POSTAL_COL, String.class)
        columnClasses.put(CITY_COL, String.class)
        columnClasses.put(COUNTRY_COL, String.class)
        columnClasses.put(PHONE_COL, String.class)
        columnClasses.put(EMAIL_COL, String.class)
        columnClasses.put(TURNOVER_COL, BigDecimal.class)
        columnClasses.put(VAT_TOTAL_COL, BigDecimal.class)
    }

    void setColumnNames() {
        columnNames.put(NAME_COL, getBundle("Contacts").getString("NAME"))
        columnNames.put(OFFICIAL_COL, getBundle("Contacts").getString("OFFICIAL_NAME"))
        columnNames.put(CUSTOMER_COL, getBundle("Contacts").getString("CUSTOMER"))
        columnNames.put(SUPPLIER_COL, getBundle("Contacts").getString("SUPPLIER"))
        columnNames.put(VAT_NUMBER_COL, getBundle("Contacts").getString("VAT_NR"))
        columnNames.put(STREET_COL,getBundle("Contacts").getString("STREET_AND_NUMBER"))
        columnNames.put(POSTAL_COL, getBundle("Contacts").getString("POSTAL_CODE"))
        columnNames.put(CITY_COL, getBundle("Contacts").getString("CITY"))
        columnNames.put(COUNTRY_COL, getBundle("Contacts").getString("COUNTRY"))
        columnNames.put(PHONE_COL, getBundle("Contacts").getString("PHONE"))
        columnNames.put(EMAIL_COL, getBundle("Contacts").getString("EMAIL"))
        columnNames.put(TURNOVER_COL, getBundle("Contacts").getString("TURNOVER"))
        columnNames.put(VAT_TOTAL_COL, getBundle("Contacts").getString("VAT_TOTAL"))
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        ArrayList<Contact> list = filter(Session.activeAccounting.contacts)
        Contact contact = list.get(row)
        if (col == NAME_COL) {
            return contact.name
        } else if (col == OFFICIAL_COL) {
            return contact.officialName
        } else if (col == VAT_NUMBER_COL) {
            return contact.vatNumber
        } else if (col == STREET_COL) {
            return contact.streetAndNumber
        } else if (col == POSTAL_COL) {
            return contact.postalCode
        } else if (col == CITY_COL) {
            return contact.city
        } else if (col == COUNTRY_COL) {
            return contact.countryCode
        } else if (col == PHONE_COL) {
            return contact.phone
        } else if (col == EMAIL_COL) {
            return contact.email
        } else if (col == CUSTOMER_COL) {
            return contact.customer
        } else if (col == SUPPLIER_COL) {
            return contact.supplier
        } else if (col == TURNOVER_COL) {
            return contact.turnOver
        } else if (col == VAT_TOTAL_COL) {
            return contact.VATTotal
        }
        null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        ArrayList<Contact> list = filter(Session.activeAccounting.contacts)
        list.size()?:0
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses.get(col)
    }

    @Override
    boolean isCellEditable(int row, int col) {
        !nonEditableColumns.contains(col)
    }

// DE SET METHODEN
// ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        Accounting accounting = Session.activeAccounting
        ArrayList<Contact> list = filter(Session.activeAccounting.contacts)
        Contact contact = list.get(row)
        if(isCellEditable(row, col)){
            if(col== CUSTOMER_COL) {
                Boolean customer = (Boolean) value
                if(customer) {
                    Account customerAccount = StockUtils.getCustomerAccount(contact, accounting)
                    contact.setCustomerAccount(customerAccount)
                } else {
                    contact.setCustomerAccount(null)
                }
            } else if(col== SUPPLIER_COL) {
                Boolean supplier = (Boolean) value
                if(supplier) {
                    Account supplierAccount = StockUtils.getSupplierAccount(contact, accounting)
                    contact.setSupplierAccount(supplierAccount)
                } else {
                    contact.setSupplierAccount(null)
                }
            } else {
                String stringValue = (String) value
                if (col == NAME_COL) {
                    contact.name = stringValue
                } else if (col == OFFICIAL_COL) {
                    contact.officialName = stringValue
                } else if (col == VAT_NUMBER_COL) {
                    contact.vatNumber =stringValue
                } else if (col == STREET_COL) {
                    contact.streetAndNumber = stringValue
                } else if (col == POSTAL_COL) {
                    contact.postalCode = stringValue
                } else if (col == CITY_COL) {
                    contact.city = stringValue
                } else if (col == COUNTRY_COL) {
                    contact.countryCode = stringValue
                } else if (col == PHONE_COL) {
                    contact.phone = stringValue
                } else if (col == EMAIL_COL) {
                    contact.email = stringValue
                }
            }
        }
    }

    ArrayList<Contact> filter(Contacts contacts){
        if(contactType == Contact.ContactType.ALL) {
            contacts.businessObjects
        } else if (contactType == Contact.ContactType.CUSTOMERS){
            contacts.getBusinessObjects{it.customer}
        } else if (contactType == Contact.ContactType.SUPPLIERS){
            contacts.getBusinessObjects{it.supplier}
        }
    }

    @Override
    Contact getObject(int row, int col) {
        ArrayList<Contact> list = filter(Session.activeAccounting.contacts)
        list.get(row)
    }

}