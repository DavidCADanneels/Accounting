package be.dafke.ObjectModelDao;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import be.dafke.FOP.Utils;
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

/**
 * User: david
 * Date: 27-12-13
 * Time: 20:55
 */
public class ObjectModelSAXParser {
    public static String getXmlHeader(BusinessObject object, int depth) {
        String className = object.getBusinessObjectType();
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n");
        builder.append("<!DOCTYPE ").append(className).append(" SYSTEM \"");
        for(int i=0 ; i<depth; i++){
            builder.append("../");
        }
        builder.append("../dtd/").append(className).append(".dtd\">\r\n");
        return builder.toString();
    }

    public static void toHtml(BusinessCollection businessCollection, File xmlFolder, File xslFolder, File htmlFolder){
        String businessCollectionName = businessCollection.getName();
        String businessCollectionType = businessCollection.getBusinessObjectType();
        File xmlFile = new File(xmlFolder, businessCollectionName + ".xml");
        File htmlFile = new File(htmlFolder, businessCollectionName + ".html");
        Utils.xmlToHtml(xmlFile, new File(xslFolder, businessCollectionType + ".xsl"), htmlFile, null);
        for(Object object:businessCollection.getBusinessObjects()){
            if(object instanceof BusinessCollection){
                BusinessCollection subCollection = (BusinessCollection)object;
                String subCollectionName = subCollection.getName();
                if(subCollection!=null){
                    // Unnamed collections such as Transaction, Booking, Movement should be written in a separate file
                    // TODO: add abstract method BusinessCollection.shouldBeWritten(): returns false is those cases ?
                    File subXmlFolder = new File(xmlFolder, subCollectionName + ".xml");
                    File subHtmlFolder = new File(htmlFolder, subCollectionName + ".html");
                    toHtml(subCollection, subXmlFolder, xslFolder, subHtmlFolder);
                }
            } else if(object instanceof BusinessObject){
                BusinessObject businessObject = (BusinessObject)object;
                String businessObjectName = businessObject.getName();
                String businessObjectType = businessObject.getBusinessObjectType();
                // TODO: ensure that collections such as Accounts, Journals, Balances, Mortgages, Statements and Counterparties
                // have the same name as their type (= simple class name)
                File xmlCollectionFolder = new File(xmlFolder, businessObjectName);
                File htmlCollectionFolder = new File(htmlFolder, businessObjectName);
                htmlCollectionFolder.mkdirs();
                File objectXmlFile = new File(xmlFolder, businessObjectName+".xml");
                File objectHtmlFile = new File(htmlFolder, businessObjectName+".html");

                Utils.xmlToHtml(objectXmlFile, new File(xslFolder, businessObjectType+".xsl"), objectHtmlFile, null);
            }

        }
    }

    public static void writeCollection(BusinessCollection collection, File parentFolder, int depth){
        String className = collection.getBusinessObjectType();
        String name = collection.getName();
        parentFolder.mkdirs();
        File childFolder = null;
        try{
            childFolder = new File(parentFolder, name);
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
        File xmlFile = new File(parentFolder, name+".xml");
        try {
            Writer writer = new FileWriter(xmlFile);

            // Write the header with correct XSL-File, DTD-File and DTD-Root-Element
            writer.write(getXmlHeader(collection, depth));

            // Write the root element e.g. <Accountings>
            writer.write("<" + className + ">\r\n");
//            writer.write("  <name>"+name+"</name>\r\n");
            // TODO: write collection.getInitProperties not only name
            // get the object's properties
            TreeMap<String,String> collectionProperties = collection.getInitProperties();

//                iterate the properties and write them out (if not null)
            for(Map.Entry<String, String> entry : collectionProperties.entrySet()){
                String key = entry.getKey();
                String objectProperty = entry.getValue();
                if(objectProperty!=null && !objectProperty.equals("")){
                    writer.write("  <" + key + ">" + objectProperty + "</"+ key + ">\r\n");
                }
            }
//            if(className.equals("Accounting")){
//                writer.write("  <name>"+collection.getName()+"</name>\r\n");
//            }

            writeChildren(writer, collection);

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
            if(businessObject instanceof BusinessCollection && ((BusinessCollection) businessObject).getName()!=null){
                BusinessCollection<BusinessObject> businessCollection = ((BusinessCollection<BusinessObject>)businessObject);
                writeCollection(businessCollection, childFolder, depth+1);
            }
        }
    }

    private static void writeChildren(Writer writer, BusinessCollection collection) {
        try{
            // Iterate children and write their data
            for(Object object : collection.getBusinessObjects()) {
                BusinessObject businessObject = (BusinessObject) object;
                String objectName = businessObject.getName();
                // TODO: remove this if to get more details in the parent file
                if(objectName!=null){
                    String objectType = businessObject.getBusinessObjectType();

                    // Write the tag for the child e.g. <Accounting>
                    writer.write("  <"+objectType+">\r\n");

                    // get the object's properties
                    TreeMap<String,String> objectProperties = businessObject.getInitProperties();

                    // iterate the properties and write them out (if not null)
                    for(Map.Entry<String, String> entry : objectProperties.entrySet()){
                        String key = entry.getKey();
                        String objectProperty = entry.getValue();
                        if(objectProperty!=null && !objectProperty.equals("")){
                            writer.write("    <" + key + ">" + objectProperty + "</"+ key + ">\r\n");
                        }
                    }
                    // The implementation used is more clear and similar to the read Method
                    if(object instanceof BusinessCollection){
                        BusinessCollection subCollection = (BusinessCollection) object;
                        writeChildren(writer, subCollection);
                    }

                    // write the closing tag e.g. </Accounting>
                    writer.write("  </" + objectType + ">\r\n");
                }
            }
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    public static void readCollection(BusinessCollection businessCollection, boolean recursive, File parentFolder){
//        String className = businessCollection.getBusinessObjectType();
        String name = businessCollection.getName();
        File childFolder = new File(parentFolder, name);
        File xmlFile = new File(parentFolder, name+".xml");

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile.getAbsolutePath());
            doc.getDocumentElement().normalize();

            String collectionName = businessCollection.getBusinessObjectType();

            // get Root Element e.g. <Accountings>
            Element rootElement = (Element) doc.getElementsByTagName(collectionName).item(0);

            readChildren(rootElement, businessCollection);
        } catch (IOException io) {
            io.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(recursive){
            for(Object businessObject : businessCollection.getBusinessObjects()) {
                if(businessObject instanceof BusinessCollection){
                    BusinessCollection<BusinessObject> subCollection = ((BusinessCollection<BusinessObject>)businessObject);
                    if(subCollection.mustBeRead()){
                        readCollection(subCollection, true, childFolder);
                    }
                }
            }
        }
    }

    private static void readChildren(Element rootElement,BusinessCollection businessCollection){
        String shortName = businessCollection.getChildType();

        // get Children e.g. <Accounting>
        NodeList childrenNodeList = rootElement.getElementsByTagName(shortName);

        // iterate children and create objects for them
        for (int i = 0; i < childrenNodeList.getLength(); i++) {
            BusinessObject object = businessCollection.getBusinessObject(shortName);
            if(object==null) {
                try {
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

                    if(object instanceof BusinessCollection){
                        readChildren((Element)childrenNodeList.item(i),(BusinessCollection)object);
//                            BusinessCollection collection = (BusinessCollection)object;
//                            String childType = collection.getChildType();
//                            NodeList elementsByTagName = ((Element) childrenNodeList.item(i)).getElementsByTagName(childType);
//                            for (int j = 0; j < elementsByTagName.getLength(); j++) {
//
//                            }
                    }

                    // add the object to the collection
                    businessCollection.addBusinessObject(object);

                } catch (EmptyNameException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (DuplicateNameException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            // if null
        }// for each ChildNode
//            }// for each name in ArrayList
        String value = getValue(rootElement, businessCollection.CURRENT);
        if(value!=null){
            businessCollection.setCurrentObject(businessCollection.getBusinessObject(value));
//                System.err.println("current Object: "+businessCollection.getCurrentObject().getName());
        }
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
