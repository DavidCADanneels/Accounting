package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Contacts
import be.dafke.Accounting.BusinessModel.Customer
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.Accounting.BusinessModel.Supplier
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
            String customerAccountName = getValue(element, CUSTOMER_ACCOUNT)
            if(customerAccountName){
                Customer customer = new Customer()
                customer.customerAccount = accounting.accounts.getBusinessObject(customerAccountName)
                customer.VATTotal = Utils.parseBigDecimal(getValue(element, VAT_TOTAL))
                customer.turnOver = Utils.parseBigDecimal(getValue(element, TURNOVER))
                contact.customer = customer
            }
            String supplierAccountName = getValue(element, SUPPLIER_ACCOUNT)
            if(supplierAccountName) {
                Supplier supplier = new Supplier()
                supplier.supplierAccount = accounting.accounts.getBusinessObject(supplierAccountName)
                contact.supplier = supplier
            }
            try {
                contacts.addBusinessObject(contact)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
        String companyContactName = getValue(rootElement, COMPANY_CONTACT)
        if(companyContactName) {
            Contact companyContact = contacts.getBusinessObject(companyContactName)
            accounting.companyContact = companyContact
        }
        String noInvoiceContactName = getValue(rootElement, CONTACT_NO_INVOICE)
        if(noInvoiceContactName) {
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
            for(Contact contact: contacts.businessObjects) {
                writer.write """\
  <$CONTACT>
    <$NAME>$contact.name</$NAME>
    <$OFFICIAL_NAME>$contact.officialName</$OFFICIAL_NAME>
    <$STREET_AND_NUMBER>$contact.streetAndNumber</$STREET_AND_NUMBER>
    <$POSTAL_CODE>$contact.postalCode</$POSTAL_CODE>
    <$CITY>$contact.city</$CITY>
    <$COUNTRY_CODE>$contact.countryCode</$COUNTRY_CODE>
    <$EMAIL_ADDRESS>$contact.email</$EMAIL_ADDRESS>
    <$PHONE_NUMBER>$contact.phone</$PHONE_NUMBER>
    <$VAT_NUMBER>$contact.vatNumber</$VAT_NUMBER>"""
                if (contact.customer) {
                    writer.write """
    <$VAT_TOTAL>$contact.customer.VATTotal</$VAT_TOTAL>
    <$TURNOVER>$contact.customer.turnOver</$TURNOVER>
    <$CUSTOMER_ACCOUNT>$contact.customer.customerAccount</$CUSTOMER_ACCOUNT>"""
                    for (SalesOrder salesOrder : contact.customer.salesOrders) {
                        writer.write """
    <$SALES_ORDER>$salesOrder</$SALES_ORDER>"""
                    }
                }
                if (contact.supplier) {
                    writer.write """
    <$SUPPLIER_ACCOUNT>$contact.supplier.supplierAccount</$SUPPLIER_ACCOUNT>"""
                    for (PurchaseOrder purchaseOrder : contact.supplier.purchaseOrders) {
                        writer.write """
    <$PURCHASE_ORDER>$purchaseOrder</$PURCHASE_ORDER>"""
                    }
                }
                writer.write """
  </$CONTACT>
"""
            }
            
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