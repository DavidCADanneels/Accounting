package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
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
    public static final String CONTACTS = "Contacts";
    public static final String CONTACT = "Contact";

    public static final String ADDRESS_LINE_1 = "addressLine1";
    public static final String ADDRESS_LINE_2 = "addressLine2";
    public static final String VAT_NUMBER = "TVANumber";

    public static void readContacts(Contacts contacts, File accountingFolder){
        File xmlFile = new File(accountingFolder, "Contacts.xml");
        Element rootElement = getRootElement(xmlFile, CONTACTS);
        for (Element element : getChildren(rootElement, CONTACT)) {
            Contact contact = new Contact();
            contact.setName(getValue(element, NAME));
            contact.setAddressLine1(getValue(element, ADDRESS_LINE_1));
            contact.setAddressLine2(getValue(element, ADDRESS_LINE_2));
            contact.setVatNumber(getValue(element, VAT_NUMBER));
            try {
                contacts.addBusinessObject(contact);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }

    }

    public static void writeContacts(Contacts contacts, File accountingFolder){
        File accountsFile = new File(accountingFolder, "Contacts.xml");
        try{
            Writer writer = new FileWriter(accountsFile);
            writer.write(getXmlHeader(CONTACTS, 2));
            for(Contact contact: contacts.getBusinessObjects()) {
                writer.write(
                        "  <"+CONTACT+">\n" +
                        "    <"+NAME+">" + contact.getName() + "</"+NAME+">\n" +
                        "    <"+ADDRESS_LINE_1+">" + contact.getAddressLine1() + "</"+ADDRESS_LINE_1+">\n" +
                        "    <"+ADDRESS_LINE_2+">" + contact.getAddressLine2() + "</"+ADDRESS_LINE_2+">\n" +
                        "    <"+ VAT_NUMBER +">" + contact.getVatNumber() + "</"+ VAT_NUMBER +">\n" +
                        "  </"+CONTACT+">\n"
                );
            }
            writer.write("</Contacts>");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
