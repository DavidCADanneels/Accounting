package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class ContactsIO {

    public static void readContacts(Accounting accounting, File accountingFolder){
        Contacts contacts = accounting.getContacts();
        File xmlFile = new File(accountingFolder, "Contacts.xml");
        Element rootElement = getRootElement(xmlFile, CONTACTS);
        for (Element element : getChildren(rootElement, CONTACT)) {
            Contact contact = new Contact();
            contact.setName(getValue(element, NAME));
            contact.setStreetAndNumber(getValue(element, STREET_AND_NUMBER));
            contact.setPostalCode(getValue(element, POSTAL_CODE));
            contact.setCity(getValue(element, CITY));
            contact.setCountryCode(getValue(element, COUNTRY_CODE));
            contact.setEmail(getValue(element, EMAIL_ADDRESS));
            contact.setPhone(getValue(element, PHONE_NUMBER));
            contact.setVatNumber(getValue(element, VAT_NUMBER));
            contact.setCustomer(Boolean.valueOf(getValue(element, CUSTOMER)));
            contact.setVATTotal(Utils.parseBigDecimal(getValue(element, VAT_TOTAL)));
            contact.setTurnOver(Utils.parseBigDecimal(getValue(element, TURNOVER)));
            try {
                contacts.addBusinessObject(contact);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
        String companyContactName = getValue(rootElement, COMPANY_CONTACT);
        if(companyContactName!=null) {
            Contact companyContact = contacts.getBusinessObject(companyContactName);
            accounting.setCompanyContact(companyContact);
        }
    }

    public static void writeContacts(Contacts contacts, Contact companyContact, File accountingFolder){
        File accountsFile = new File(accountingFolder, "Contacts.xml");
        try{
            Writer writer = new FileWriter(accountsFile);
            writer.write(getXmlHeader(CONTACTS, 2));
            for(Contact contact: contacts.getBusinessObjects()) {
                writer.write(
                        "  <"+CONTACT+">\n" +
                        "    <"+NAME+">" + contact.getName() + "</"+NAME+">\n" +
                        "    <"+STREET_AND_NUMBER+">" + contact.getStreetAndNumber() + "</"+STREET_AND_NUMBER+">\n" +
                        "    <"+POSTAL_CODE+">" + contact.getPostalCode() + "</"+POSTAL_CODE+">\n" +
                        "    <"+CITY+">" + contact.getCity() + "</"+CITY+">\n" +
                        "    <"+COUNTRY_CODE+">" + contact.getCountryCode() + "</"+COUNTRY_CODE+">\n" +
                        "    <"+EMAIL_ADDRESS+">" + contact.getEmail() + "</"+EMAIL_ADDRESS+">\n" +
                        "    <"+PHONE_NUMBER+">" + contact.getPhone() + "</"+PHONE_NUMBER+">\n" +
                        "    <"+VAT_NUMBER+">" + contact.getVatNumber() + "</"+VAT_NUMBER+">\n" +
                        "    <"+VAT_TOTAL+">" + contact.getVATTotal() + "</"+VAT_TOTAL+">\n" +
                        "    <"+TURNOVER+">" + contact.getTurnOver() + "</"+TURNOVER+">\n" +
                        "    <"+CUSTOMER+">" + contact.isCustomer() + "</"+CUSTOMER+">\n" +
                        "  </"+CONTACT+">\n"
                );
            }
            writer.write("  <"+COMPANY_CONTACT+">"+(companyContact==null?"null":companyContact.getName())+"</"+COMPANY_CONTACT+">\n");
            writer.write("</Contacts>");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
