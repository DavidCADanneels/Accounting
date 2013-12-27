package be.dafke.BasicAccounting.Dao;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Balance;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.Mortgage.Dao.MortgagesSAXParser;
import be.dafke.Mortgage.Objects.Mortgage;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessCollectionDependent;
import be.dafke.ObjectModel.BusinessCollectionProvider;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.BusinessTypeCollectionDependent;
import be.dafke.ObjectModel.BusinessTypeProvider;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 20/01/13
 * Time: 10:22
 */
public class AccountingsSAXParser {

    public static String getXmlHeader(BusinessObject object, int depth) {
        String className = object.getBusinessObjectType();
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n");
        builder.append("<?xml-stylesheet type=\"text/xsl\" href=\"");
        for(int i=0 ; i<depth; i++){
            builder.append("../");
        }
        builder.append("../xsl/").append(className).append("2xml.xsl\"?>\r\n").append("<!DOCTYPE ").append(className).append(" SYSTEM \"");
        for(int i=0 ; i<depth; i++){
            builder.append("../");
        }
        builder.append("../dtd/").append(className).append(".dtd\">\r\n");
        return builder.toString();
    }

    public static void writeCollection(BusinessCollection collection, File parentFolder, int depth){
        String className = collection.getBusinessObjectType();
        String name = collection.getName();
        File childFolder = new File(parentFolder, name);
        File xmlFile = new File(childFolder, className+".xml");
        try {
            Writer writer = new FileWriter(xmlFile);

            // Write the header with correct XSL-File, DTD-File and DTD-Root-Element
            writer.write(getXmlHeader(collection, depth));

            // Write the root element e.g. <Accountings>
            writer.write("<" + className + ">\r\n");
            if(className.equals("Accounting")){
                writer.write("  <name>"+collection.getName()+"</name>\r\n");
            }
            // Iterate children and write their data
            for(Object object : collection.getBusinessObjects()) {
                BusinessObject businessObject = (BusinessObject) object;
                String objectType = businessObject.getBusinessObjectType();

                // Write the tag for the child e.g. <Accounting>
                writer.write("  <"+objectType+">\r\n");

                // get the object's properties
                TreeMap<String,String> objectProperties = businessObject.getInitProperties();

//                iterate the properties and write them out (if not null)
                for(Map.Entry<String, String> entry : objectProperties.entrySet()){
                    String key = entry.getKey();
                    String objectProperty = entry.getValue();
                    if(objectProperty!=null && !objectProperty.equals("")){
                        writer.write("    <" + key + ">" + objectProperty + "</"+ key + ">\r\n");
                    }
                }
                // The implementation used is more clear and similar to the read Method

                // write the closing tag e.g. </Accounting>
                writer.write("  </" + objectType + ">\r\n");
            }

            if(collection.getCurrentObject()!=null){
                writer.write("    <CurrentObject>" + collection.getCurrentObject().getName() + "</CurrentObject>\r\n");
            }

            writer.write("</"+className+">\r\n");

            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BusinessCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BusinessCollection.class.getName()).log(Level.SEVERE, null, ex);
        }

        for(Object businessObject : collection.getBusinessObjects()) {
            if(businessObject instanceof BusinessCollection){
                BusinessCollection<BusinessObject> businessCollection = ((BusinessCollection<BusinessObject>)businessObject);
                writeCollection(businessCollection, childFolder, depth+1);
            }
        }
    }

    public static void writeAccountings(Accountings accountings) {
//        accountings.createDefaultValuesIfNull();
        File xmlFolder = accountings.getXmlFolder();
        xmlFolder.mkdirs();
        writeCollection(accountings, xmlFolder, 0);


        for(Accounting accounting : accountings.getBusinessObjects()) {
//            accounting.writeCollection();
            File rootFolder = new File(xmlFolder, accounting.getName());

            File accountsFolder = new File(rootFolder, "Accounts");
            File balancesFolder = new File(rootFolder, "Balances");
            File journalsFolder = new File(rootFolder, "Journals");
            File mortgagesFolder = new File(rootFolder, "Mortgages");


            for(Account account : accounting.getAccounts().getBusinessObjects()){
                AccountsSAXParser.writeAccount(account, accountsFolder, getXmlHeader(account, 1));
            }
            for(Balance balance : accounting.getBalances().getBusinessObjects()){
                BalancesSAXParser.writeBalance(balance, balancesFolder, getXmlHeader(balance, 1));
            }

            for(Journal journal : accounting.getJournals().getBusinessObjects()){
                JournalsSAXParser.writeJournal(journal, journalsFolder, getXmlHeader(journal, 1));
            }
            for(Mortgage mortgage:accounting.getMortgages().getBusinessObjects()){
                MortgagesSAXParser.writeMortgage(mortgage, mortgagesFolder, getXmlHeader(mortgage, 1));
            }
        }

//        toHtml(accountings);
    }

//    private static void toHtml(Accountings accountings){
//        accountings.xmlToHtml();
//        for(Accounting accounting:accountings.getBusinessObjects()){
//            if(accounting.getHtmlFolder()!=null && !accounting.getHtmlFolder().getPath().equals("null")){
//                accounting.xmlToHtml();
//
//                for(BusinessCollection<BusinessObject> collection : accounting.getBusinessObjects()){
//                    collection.xmlToHtml();
//                    if(collection.getHtmlFolder()!=null){
//                        for(BusinessObject writeableBusinessObject : collection.getBusinessObjects()){
////                        TODO: add isSavedHTML
////                        if(writeableBusinessObject.isSavedHTML()){
//                            writeableBusinessObject.xmlToHtml();
////                        }
//                        }
//                    }
//                }
//            }
//        }
//    }


    // READ

    public static Accountings readCollection(File xmlFolder) {
        Accountings accountings = new Accountings(xmlFolder);
        readCollection(accountings, true, xmlFolder);
        for(Accounting accounting : accountings.getBusinessObjects()){
            File rootFolder = new File(xmlFolder, accounting.getName());
            for(Mortgage mortgage : accounting.getMortgages().getBusinessObjects()){
                File mortgagesFolder = new File(rootFolder, "Mortgages");
                MortgagesSAXParser.readMortgage(mortgage, new File(mortgagesFolder, mortgage.getName()+".xml"));
            }

            for(Journal journal : accounting.getJournals().getBusinessObjects()){
                File journalsFolder = new File(rootFolder, "Journals");
                JournalsSAXParser.readJournal(journal, accounting.getAccounts(), new File(journalsFolder, journal.getName()+".xml"));
            }
        }
        return accountings;
    }

    public static void readCollection(BusinessCollection businessCollection, boolean recursive, File parentFolder){
        String className = businessCollection.getBusinessObjectType();
        String shortName = businessCollection.getChildType();
        String name = businessCollection.getName();
//        File xmlFile = parentFolder+".xml";
        File childFolder = new File(parentFolder, name);
        File xmlFile = new File(childFolder, className+".xml");

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile.getAbsolutePath());
            doc.getDocumentElement().normalize();

            String collectionName = businessCollection.getBusinessObjectType();

            // get Root Element e.g. <Accountings>
            Element rootElement = (Element) doc.getElementsByTagName(collectionName).item(0);

//            for(String shortName:shortNames){

                // get Children e.g. <Accounting>
                NodeList childrenNodeList = rootElement.getElementsByTagName(shortName);

                // iterate children and create objects for them
                for (int i = 0; i < childrenNodeList.getLength(); i++) {
                    BusinessObject object = businessCollection.getBusinessObject(shortName);
                    if(object==null) try {
                        // create new instance of object
                        object = businessCollection.createNewChild(shortName);

                        // if object is Typed, fetch its TypeCollection from the collection
                        if (businessCollection instanceof BusinessTypeProvider && object instanceof BusinessTypeCollectionDependent) {
                            BusinessTypeCollection btc = ((BusinessTypeProvider) businessCollection).getBusinessTypeCollection();
                            ((BusinessTypeCollectionDependent) object).setBusinessTypeCollection(btc);
                        }

                        // if object is dependant on another collection, fetch this Collection from the collection
                        if (businessCollection instanceof BusinessCollectionProvider && object instanceof BusinessCollectionDependent) {
                            BusinessCollection bc = ((BusinessCollectionProvider) businessCollection).getBusinessCollection();
                            ((BusinessCollectionDependent) object).setBusinessCollection(bc);
                        }

                        // create empty properties TreeMap
                        TreeMap<String, String> properties = new TreeMap<String, String>();

                        // get the Object's keySet
                        Set<String> keySet = object.getInitKeySet();

                        // read all the tags which names are in the keySet
                        // and add their value to the properties
                        Element element = (Element) childrenNodeList.item(i);
                        for (String key : keySet) {
                            String value = getValue(element, key);
                            properties.put(key, value);
                        }

                        // provide the properties to the object
                        object.setInitProperties(properties);

                        // add the object to the collection
                        businessCollection.addBusinessObject(object);

                    } catch (EmptyNameException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (DuplicateNameException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    // if null
                }// for each ChildNode
//            }// for each name in ArrayList
            String value = getValue(rootElement, businessCollection.CURRENT);
            if(value!=null){
                businessCollection.setCurrentObject(businessCollection.getBusinessObject(value));
//                System.err.println("current Object: "+businessCollection.getCurrentObject().getName());
            }
        } catch (IOException io) {
            io.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(recursive){
            for(Object businessObject : businessCollection.getBusinessObjects()) {
                if(businessObject instanceof BusinessCollection){
                    BusinessCollection<BusinessObject> subCollection = ((BusinessCollection<BusinessObject>)businessObject);
                    readCollection(subCollection, true, childFolder);
                }
            }
        }

        // TODO iterate all 'Writeable' childeren and call this function (recursion)
    }

    private static String getValue(Element element, String tagName){
        NodeList nodeList = element.getElementsByTagName(tagName);
        if(nodeList.getLength()==0){
//            System.err.println("The tag " + tagName + " is not present.");
            return null;
            // the tag is not present
        } else {
            nodeList = nodeList.item(0).getChildNodes();
            if(nodeList.getLength()==0){
                System.err.println("The tag " + tagName + " is empty.");
                return null;
                // the tag is empty
            } else {
                if(nodeList.item(0).getNodeValue().equals("null")){
                    System.err.println("The tag " + tagName + " equals \"null\"");
                    return null;
                }
                return nodeList.item(0).getNodeValue();
            }
        }
    }


}
