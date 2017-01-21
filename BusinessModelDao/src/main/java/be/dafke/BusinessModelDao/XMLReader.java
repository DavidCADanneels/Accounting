package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

import static be.dafke.BusinessModelDao.AccountsIO.readAccounts;
import static be.dafke.BusinessModelDao.BalancesIO.readBalances;
import static be.dafke.BusinessModelDao.ContactsIO.readContacts;
import static be.dafke.BusinessModelDao.JournalsIO.readJournalTypes;
import static be.dafke.BusinessModelDao.JournalsIO.readJournals;
import static be.dafke.BusinessModelDao.MortgageIO.readMortgages;
import static be.dafke.BusinessModelDao.ProjectsIO.readProjects;
import static be.dafke.BusinessModelDao.VATIO.readVATFields;
import static be.dafke.BusinessModelDao.VATIO.readVATTransactions;
import static be.dafke.BusinessModelDao.XMLConstants.*;

/**
 * Created by ddanneels on 28/12/2015.
 */
public class XMLReader {
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

    public static void readAccountings(Accountings accountings, File xmlFolder){
        File subFolder = new File(xmlFolder, ACCOUNTINGS);
        subFolder.mkdirs();
        File xmlFile = new File(xmlFolder, "Accountings.xml");
        if(!xmlFile.exists()){
            return;
        }

        Element rootElement = getRootElement(xmlFile, ACCOUNTINGS);
        for (Element element : getChildren(rootElement, ACCOUNTING)) {

            String name = getValue(element, NAME);
            Accounting accounting = new Accounting(name);

            try {
                accountings.addBusinessObject(accounting);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
        String value = getValue(rootElement, CURRENT);
        if (value != null) {
            accountings.setCurrentObject(accountings.getBusinessObject(value));
        }
    }

    public static void readAccounting(Accounting accounting, File xmlFolder) {
        File accountingsFolder = new File(xmlFolder, "Accountings");
        File accountingFolder = new File(accountingsFolder, accounting.getName());
        readAccounts(accounting.getAccounts(), accounting.getAccountTypes(), accountingFolder);
        readMortgages(accounting.getMortgages(), accounting.getAccounts(),accountingFolder);
        readJournalTypes(accounting.getJournalTypes(), accounting.getAccountTypes(), accountingFolder);
        readVATFields(accounting.getVatFields(),accountingFolder);
        readVATTransactions(accounting.getVatTransactions(), accounting.getVatFields(), accounting.getAccounts(), accountingFolder);
        readProjects(accounting.getProjects(), accounting.getAccounts(), accounting.getAccountTypes(), accountingFolder);
        readJournals(accounting.getJournals(), accounting.getAccounts(), accounting.getJournalTypes(), accounting.getVatTransactions(), accountingFolder);
        readBalances(accounting.getBalances(),accounting.getAccounts(),accounting.getAccountTypes(),accountingFolder);
        readContacts(accounting.getContacts(),accountingFolder);
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

}
