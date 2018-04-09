package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

import static be.dafke.BusinessModelDao.AccountsIO.readAccounts;
import static be.dafke.BusinessModelDao.ArticlesIO.readArticles;
import static be.dafke.BusinessModelDao.BalancesIO.readBalances;
import static be.dafke.BusinessModelDao.ContactsIO.readContacts;
import static be.dafke.BusinessModelDao.JournalsIO.readJournalTypes;
import static be.dafke.BusinessModelDao.JournalsIO.readJournals;
import static be.dafke.BusinessModelDao.JournalsIO.readJournalsContent;
import static be.dafke.BusinessModelDao.JournalsIO.readTransactions;
import static be.dafke.BusinessModelDao.MortgageIO.readMortgages;
import static be.dafke.BusinessModelDao.ProjectsIO.readProjects;
import static be.dafke.BusinessModelDao.PurchaseOrderIO.readPurchaseOrders;
import static be.dafke.BusinessModelDao.SalesOrderIO.readSalesOrders;
import static be.dafke.BusinessModelDao.StockIO.readStock;
import static be.dafke.BusinessModelDao.VATIO.readVATTransactions;
import static be.dafke.BusinessModelDao.XMLConstants.*;

/**
 * Created by ddanneels on 28/12/2015.
 */
public class XMLReader {

    public static final String VAT_ACCOUNTING = "VatAccounting";
    public static final String CONTACTS_ACCOUNTING = "ContactsAccounting";
    public static final String PROJECTS_ACCOUNTING = "ProjectsAccounting";
    public static final String MORTGAGES_ACCOUNTING = "MortgagesAccounting";

    private static Document getDocument(File xmlFile){
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile.getAbsolutePath());
            doc.getDocumentElement().normalize();

            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Element getRootElement(File xmlFile, String collectionName){
        Document doc = getDocument(xmlFile);
        return (Element) doc.getElementsByTagName(collectionName).item(0);
    }

    public static ArrayList<Element> getChildren(Element rootElement, String elementName){
        ArrayList<Element> children = new ArrayList<>();
        NodeList childrenNodeList = rootElement.getElementsByTagName(elementName);
        for (int i = 0; i < childrenNodeList.getLength(); i++) {
            children.add((Element) childrenNodeList.item(i));
        }
        return children;
    }

    public static void readAccountings(Accountings accountings){
        File xmlFile = new File(ACCOUNTINGS_FILE);
        if(!xmlFile.exists()){
            return;
        }

        Element rootElement = getRootElement(xmlFile, ACCOUNTINGS);
        for (Element element : getChildren(rootElement, ACCOUNTING)) {

            String name = getValue(element, NAME);
            Accounting accounting = new Accounting(name);

            accounting.getAccountTypes().addDefaultTypes();

            String vatAccountingString = getValue(element, VAT_ACCOUNTING);
            boolean vatAccounting = Boolean.valueOf(vatAccountingString);
            accounting.setVatAccounting(vatAccounting);

            String projectsAccountingString = getValue(element, PROJECTS_ACCOUNTING);
            boolean projectsAccounting = Boolean.valueOf(projectsAccountingString);
            accounting.setProjectsAccounting(projectsAccounting);

            String contactsAccountingString = getValue(element, CONTACTS_ACCOUNTING);
            boolean contactsAccounting = Boolean.valueOf(contactsAccountingString);
            accounting.setContactsAccounting(contactsAccounting);

            String mortgagesAccountingString = getValue(element, MORTGAGES_ACCOUNTING);
            boolean mortgagesAccounting = Boolean.valueOf(mortgagesAccountingString);
            accounting.setMortgagesAccounting(mortgagesAccounting);

            try {
                accountings.addBusinessObject(accounting);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readSession(Accountings accountings) {
        File xmlFile = new File( SESSION_FILE);
        if (!xmlFile.exists()) {
            return;
        }
        Element rootElement = getRootElement(xmlFile, SESSION);
        String value = getValue(rootElement, ACTIVE_ACCOUNTING);
        if (value != null) {
            Accountings.setActiveAccounting(accountings.getBusinessObject(value));
        }

        for (Element element : getChildren(rootElement, ACCOUNTING)) {
            String accountingName = getValue(element, NAME);
            String journalName = getValue(element, ACTIVE_JOURNAL);
            Accounting accounting = accountings.getBusinessObject(accountingName);
            Journals journals = accounting.getJournals();
            Journal currentJournal = journalName==null?null:journals.getBusinessObject(journalName);
            accounting.setActiveJournal(currentJournal);
        }

        String nextIdString = getValue(rootElement, NEXT_VAT_ID);
        VATTransaction.setCount(Utils.parseInt(nextIdString));
    }

    public static void readAccountingSkeleton(Accounting accounting) {
        readAccounts(accounting);
        readJournalTypes(accounting);
        if (accounting.isVatAccounting()) {
            accounting.getVatFields().addDefaultFields();
            //            readVATFields(accounting);
            readVATTransactions(accounting);
        }
        readJournals(accounting);
        readBalances(accounting);
        if (accounting.isMortgagesAccounting()) {
            readMortgages(accounting);
        }
        if (accounting.isProjectsAccounting()) {
            readProjects(accounting);
        }
        if (accounting.isContactsAccounting()) {
            readContacts(accounting);
            readArticles(accounting);
            readPurchaseOrders(accounting);
            readSalesOrders(accounting);
            readStock(accounting);
        }
    }

    public static void readAccountingDetails(Accounting accounting) {
        if(!accounting.isRead()) {
            readTransactions(accounting);
            readJournalsContent(accounting.getJournals(), accounting);
        }
        accounting.setRead(true);
    }

    public static String getValue(Element element, String tagName){
        NodeList nodeList = element.getElementsByTagName(tagName);
        if(nodeList.getLength()==0){
//            System.err.println("The tag " + tagName + " is not present.");
            return null;
            // the tag is not present
        } else {
            nodeList = nodeList.item(0).getChildNodes();
            if(nodeList.getLength()==0){
//                System.err.println("The tag " + tagName + " is empty.");
                return null;
                // the tag is empty
            } else {
                if(nodeList.item(0).getNodeValue().equals("null")){
//                    System.err.println("The tag " + tagName + " equals \"null\"");
                    return null;
                }
                return nodeList.item(0).getNodeValue();
            }
        }
    }

    public static boolean getBooleanValue(Element element, String tagName){
        NodeList nodeList = element.getElementsByTagName(tagName);
        if(nodeList.getLength()==0){
//            System.err.println("The tag " + tagName + " is not present.");
            return false;
            // the tag is not present
        } else {
            nodeList = nodeList.item(0).getChildNodes();
            if(nodeList.getLength()==0){
//                System.err.println("The tag " + tagName + " is empty.");
                return false;
                // the tag is empty
            } else {
                if(nodeList.item(0).getNodeValue().equals("false")){
//                    System.err.println("The tag " + tagName + " equals \"null\"");
                    return false;
                }
                String nodeValue = nodeList.item(0).getNodeValue();
                return nodeValue!=null && nodeValue.equals(("true"));
            }
        }
    }

}
