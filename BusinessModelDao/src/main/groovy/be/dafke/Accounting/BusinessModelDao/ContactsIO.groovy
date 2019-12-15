package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Contacts
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils
import org.w3c.dom.Element

import java.util.logging.Level
import java.util.logging.Logger

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*
import static be.dafke.Accounting.BusinessModelDao.XMLReader.*
import static be.dafke.Accounting.BusinessModelDao.XMLWriter.getXmlHeader 

class ContactsIO {

    static void readContacts(Accounting accounting){
        Contacts contacts = accounting.contacts
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$CONTACTS$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, CONTACTS)
        for (Element element : getChildren(rootElement, CONTACT)) {
            Contact contact = new Contact()
            contact.name = getValue(element, NAME)
            contact.officialName = getValue(element, OFFICIAL_NAME)
            contact.streetAndNumber = getValue(element, STREET_AND_NUMBER)
            contact.postalCode = getValue(element, POSTAL_CODE)
            contact.city = getValue(element, CITY)
            contact.countryCode = getValue(element, COUNTRY_CODE)
            contact.email = getValue(element, EMAIL_ADDRESS)
            contact.phone = getValue(element, PHONE_NUMBER)
            contact.vatNumber = getValue(element, VAT_NUMBER)
            contact.VATTotal = Utils.parseBigDecimal(getValue(element, VAT_TOTAL))
            contact.turnOver = Utils.parseBigDecimal(getValue(element, TURNOVER))
            String customerAccountName = getValue(element, CUSTOMER_ACCOUNT)
            if(customerAccountName!=null){
                contact.customerAccount = accounting.accounts.getBusinessObject(customerAccountName)
            }
            String supplierAccountName = getValue(element, SUPPLIER_ACCOUNT)
            if(supplierAccountName!=null) {
                contact.supplierAccount = accounting.accounts.getBusinessObject(supplierAccountName)
            }
            try {
                contacts.addBusinessObject(contact)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
        String companyContactName = getValue(rootElement, COMPANY_CONTACT)
        if(companyContactName!=null) {
            Contact companyContact = contacts.getBusinessObject(companyContactName)
            accounting.companyContact = companyContact
        }
        String noInvoiceContactName = getValue(rootElement, CONTACT_NO_INVOICE)
        if(noInvoiceContactName!=null) {
            Contact noInvoiceContact = contacts.getBusinessObject(noInvoiceContactName)
            accounting.contactNoInvoice = noInvoiceContact
        }
    }

    static void writeContacts(Accounting accounting){
        Contact companyContact = accounting.companyContact
        Contact contactNoInvoice = accounting.contactNoInvoice
        Contacts contacts = accounting.contacts
        File accountsFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$CONTACTS$XML_EXTENSION")
        try{
            Writer writer = new FileWriter(accountsFile)
            writer.write getXmlHeader(CONTACTS, 2)
            for(Contact contact: contacts.businessObjects) writer.write """\
  <$CONTACT>
    <$NAME>$contact.name</$NAME>
    <$OFFICIAL_NAME>$contact.officialName</$OFFICIAL_NAME>
    <$STREET_AND_NUMBER>$contact.streetAndNumber</$STREET_AND_NUMBER>
    <$POSTAL_CODE>$contact.postalCode</$POSTAL_CODE>
    <$CITY>$contact.city</$CITY>
    <$COUNTRY_CODE>$contact.countryCode</$COUNTRY_CODE>
    <$EMAIL_ADDRESS>$contact.email</$EMAIL_ADDRESS>
    <$PHONE_NUMBER>$contact.phone</$PHONE_NUMBER>
    <$VAT_NUMBER>$contact.vatNumber</$VAT_NUMBER>
    <$VAT_TOTAL>$contact.VATTotal</$VAT_TOTAL>
    <$TURNOVER>$contact.turnOver</$TURNOVER>
    <$CUSTOMER_ACCOUNT>$contact.customerAccount</$CUSTOMER_ACCOUNT>
    <$SUPPLIER_ACCOUNT>$contact.supplierAccount</$SUPPLIER_ACCOUNT>
  </$CONTACT>
"""
            
            writer.write """\
  <$COMPANY_CONTACT>${(companyContact==null?"null":companyContact.name)}</$COMPANY_CONTACT>
  <$CONTACT_NO_INVOICE>${(contactNoInvoice==null?"null":contactNoInvoice.name)}</$CONTACT_NO_INVOICE>
</$CONTACTS>"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounting.class.name).log(Level.SEVERE, null, ex)
        }
    }
}