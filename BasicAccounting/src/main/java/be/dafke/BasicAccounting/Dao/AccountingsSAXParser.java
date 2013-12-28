package be.dafke.BasicAccounting.Dao;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Balance;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModelDao.ObjectModelSAXParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
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
//            if(className.equals("Accounting")){
//                writer.write("  <name>"+collection.getName()+"</name>\r\n");
//            }
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


            for(Account account : accounting.getAccounts().getBusinessObjects()){
                AccountsSAXParser.writeAccount(account, accountsFolder, getXmlHeader(account, 2));
            }
            for(Balance balance : accounting.getBalances().getBusinessObjects()){
                BalancesSAXParser.writeBalance(balance, balancesFolder, getXmlHeader(balance, 2));
            }

            for(Journal journal : accounting.getJournals().getBusinessObjects()){
                JournalsSAXParser.writeJournal(journal, journalsFolder, getXmlHeader(journal, 2));
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

    public static void readCollection(Accountings accountings, File xmlFolder) {
        ObjectModelSAXParser.readCollection(accountings, true, xmlFolder);
        for(Accounting accounting : accountings.getBusinessObjects()){
            File rootFolder = new File(xmlFolder, accounting.getName());

            for(Journal journal : accounting.getJournals().getBusinessObjects()){
                File journalsFolder = new File(rootFolder, "Journals");
                JournalsSAXParser.readJournal(journal, accounting.getAccounts(), new File(journalsFolder, journal.getName()+".xml"));
            }
        }
    }
}
