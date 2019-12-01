package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BasicAccounting.Trade.StockUtils
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Contacts
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class ContactDetailsPanel extends JPanel {
    static final String NAME = "NAME_LABEL"
    static final String OFFICIAL_NAME = "OFFICIAL_NAME_LABEL"
    static final String STREET_AND_NUMBER = "STREET_AND_NUMBER_LABEL"
    static final String POSTAL_CODE = "POSTAL_CODE_LABEL"
    static final String VAT_NR = "VAT_NR_LABEL"
    static final String CITY = "CITY_LABEL"
    static final String COUNTRY = "COUNTRY_LABEL"
    static final String PHONE = "PHONE_LABEL"
    static final String EMAIL = "EMAIL_LABEL"
    static final String CUSTOMER = "CUSTOMER"
    static final String SUPPLIER = "SUPPLIER"
    static final String CUSTOMER_LABEL = "CUSTOMER_LABEL"
    static final String SUPPLIER_LABEL = "SUPPLIER_LABEL"
    final JCheckBox customer, supplier
    final JTextField customerAccountName, supplierAccountName
    final JTextField contactName, contactVAT, contactStreet, contactPostalCode, contactCity, contactCountry, contactPhone, contactEmail, officialName
    Accounting accounting
    Contacts contacts
    Contact contact
    boolean newContact

    ContactDetailsPanel() {
        setLayout(new GridLayout(0,2))

        contactName = new JTextField(20)
        contactVAT = new JTextField(20)
        contactStreet = new JTextField(20)
        contactPostalCode = new JTextField(20)
        contactCity = new JTextField(20)
        contactCountry = new JTextField(20)
        contactEmail = new JTextField(20)
        contactPhone = new JTextField(20)
        officialName = new JTextField(20)
        customerAccountName = new JTextField(20)
        supplierAccountName = new JTextField(20)
        customer = new JCheckBox(getBundle("Contacts").getString(CUSTOMER))
        supplier = new JCheckBox(getBundle("Contacts").getString(SUPPLIER))

        customerAccountName.enabled = false
        supplierAccountName.enabled = false

        customer.addActionListener({ e ->
            if (customer.selected) {
                StockUtils.getCustomerAccount(contact, accounting)
            }

        })
        supplier.addActionListener({ e ->
            StockUtils.getSupplierAccount(contact, accounting)
        })

        add(new JLabel(getBundle("Contacts").getString(NAME)))
        add(contactName)
        add(new JLabel(getBundle("Contacts").getString(OFFICIAL_NAME)))
        add(officialName)
        add(new JLabel(getBundle("Contacts").getString(VAT_NR)))
        add(contactVAT)
        add(new JLabel(getBundle("Contacts").getString(STREET_AND_NUMBER)))
        add(contactStreet)
        add(new JLabel(getBundle("Contacts").getString(POSTAL_CODE)))
        add(contactPostalCode)
        add(new JLabel(getBundle("Contacts").getString(CITY)))
        add(contactCity)
        add(new JLabel(getBundle("Contacts").getString(COUNTRY)))
        add(contactCountry)
        add(new JLabel(getBundle("Contacts").getString(PHONE)))
        add(contactPhone)
        add(new JLabel(getBundle("Contacts").getString(EMAIL)))
        add(contactEmail)
        add(customer)
        add(supplier)
        add(new JLabel(getBundle("Contacts").getString(CUSTOMER_LABEL)))
        add(customerAccountName)
        add(new JLabel(getBundle("Contacts").getString(SUPPLIER_LABEL)))
        add(supplierAccountName)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        setContacts(accounting?accounting.contacts:null)
    }

    void setContacts(Contacts contacts) {
        this.contacts = contacts
    }

    void setEnabled(boolean enabled){
        contactName.enabled = enabled
        contactVAT.enabled = enabled
        contactStreet.enabled = enabled
        contactPostalCode.enabled = enabled
        contactCity.enabled = enabled
        contactCountry.enabled = enabled
        contactEmail.enabled = enabled
        contactPhone.enabled = enabled
        officialName.enabled = enabled
        customer.enabled = enabled
        supplier.enabled = enabled
    }

    void setContact(Contact contact) {
        this.contact = contact
        contactName.setText(contact.name)
        contactVAT.setText(contact.vatNumber)
        contactStreet.setText(contact.streetAndNumber)
        contactPostalCode.setText(contact.postalCode)
        contactCity.setText(contact.city)
        contactCountry.setText(contact.countryCode)
        contactEmail.setText(contact.email)
        contactPhone.setText(contact.phone)
        officialName.setText(contact.officialName)
        customer.setSelected(contact.customer)
        supplier.setSelected(contact.supplier)
        Account customerAccount = contact.customerAccount
        if (customerAccount != null){
            customerAccountName.setText(customerAccount.toString())
        } else {
            customerAccountName.setText("")
        }
        Account supplierAccount = contact.supplierAccount
        if(supplierAccount!=null) {
            supplierAccountName.setText(supplierAccount.toString())
        } else {
            supplierAccountName.setText("")
        }
    }

    void saveAccount() {
        if(contact==null) {
            contact = new Contact()
            newContact = true
        }
        contact.name = contactName.getText().trim()
        contact.officialName = officialName.getText().trim()
        contact.vatNumber = contactVAT.getText().trim()
        contact.streetAndNumber = contactStreet.getText().trim()
        contact.postalCode = contactPostalCode.getText().trim()
        contact.city = contactCity.getText().trim()
        contact.countryCode = contactCountry.getText().trim()
        contact.email = contactEmail.getText().trim()
        contact.phone = contactPhone.getText().trim()
//        contact.setCustomer(customer.selected)
//        contact.setSupplier(supplier.selected)
        if(newContact) {
            try {
                contacts.addBusinessObject(contact)
                Main.fireContactAdded(accounting, contact)
            } catch (DuplicateNameException e) {
                ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNT_DUPLICATE_NAME, name)
            } catch (EmptyNameException e) {
                ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNT_NAME_EMPTY)
            }
            clearFields()
            contact=null
        }
//        Main.fireContactAdded(contact)
    }

    void clearFields() {
        contactName.setText("")
        contactStreet.setText("")
        contactPostalCode.setText("")
        contactVAT.setText("")
        contactCity.setText("")
        contactCountry.setText("")
        contactEmail.setText("")
        contactPhone.setText("")
        officialName.setText("")
        customerAccountName.setText("")
        supplierAccountName.setText("")
        customer.selected = false
        supplier.selected = false
    }
}
