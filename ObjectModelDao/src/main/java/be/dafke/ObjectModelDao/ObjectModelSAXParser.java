package be.dafke.ObjectModelDao;

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
import be.dafke.ObjectModel.MustBeRead;
import org.owasp.encoder.Encode;
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

        File xmlCollectionFolder = new File(xmlFolder, businessCollectionName);
        File htmlCollectionFolder = new File(htmlFolder, businessCollectionName);
        htmlCollectionFolder.mkdirs();

        for(Object object:businessCollection.getBusinessObjects()){
            BusinessObject businessObject = (BusinessObject)object;
            String businessObjectName = businessObject.getName();

            String businessObjectType = businessObject.getBusinessObjectType();

            File objectXmlFile = new File(xmlCollectionFolder, businessObjectName+".xml");
            File objectHtmlFile = new File(htmlCollectionFolder, businessObjectName+".html");

            Utils.xmlToHtml(objectXmlFile, new File(xslFolder, businessObjectType+".xsl"), objectHtmlFile, null);

            if(object instanceof BusinessCollection && !((BusinessCollection)object).writeGrandChildren()){
                BusinessCollection subCollection = (BusinessCollection)object;
                String subCollectionName = subCollection.getName();
                if(subCollectionName!=null){
                    toHtml(subCollection, xmlCollectionFolder, xslFolder, htmlCollectionFolder);
                }
            }
        }
    }

    public static void writeCollection(BusinessObject businessObject, File parentFolder, int depth){
        System.out.println(Encode.forXmlContent("Test & deploy"));
        String businessObjectName = businessObject.getName();
        String businessObjectType = businessObject.getBusinessObjectType();
        parentFolder.mkdirs();
        File childFolder = null;
        try{
            childFolder = new File(parentFolder, businessObjectName);
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
        File xmlFile = new File(parentFolder, businessObjectName+".xml");
        try {
            Writer writer = new FileWriter(xmlFile);

            // Write the header with correct XSL-File, DTD-File and DTD-Root-Element
            writer.write(getXmlHeader(businessObject, depth));

            // Write the root element e.g. <Accountings>
            writer.write("<" + businessObjectType + ">\r\n");

            // get the object's properties
            TreeMap<String,String> collectionProperties = businessObject.getInitProperties(null);

//                iterate the properties and write them out (if not null)
            for(Map.Entry<String, String> entry : collectionProperties.entrySet()){
                String key = entry.getKey();
                String objectProperty = entry.getValue();
                if(objectProperty!=null && !objectProperty.equals("")){
                    writer.write("  <" + key + ">" + objectProperty + "</"+ key + ">\r\n");
                }
            }

            if(businessObject instanceof BusinessCollection){
                BusinessCollection businessCollection = (BusinessCollection)businessObject;
                writeChildren(writer, businessCollection);

                if(businessCollection.getCurrentObject()!=null){
                    writer.write("    <CurrentObject>" + businessCollection.getCurrentObject().getName() + "</CurrentObject>\r\n");
                }
            }

            writer.write("</"+businessObjectType+">\r\n");

            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BusinessCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BusinessCollection.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(businessObject instanceof BusinessCollection){
            BusinessCollection businessCollection = (BusinessCollection)businessObject;
            for(Object object : businessCollection.getBusinessObjects()) {
                if(object instanceof BusinessObject){
                    BusinessObject childObject = (BusinessObject) object;
//                    String type = childObject.getBusinessObjectType();
                    if(childObject.getName()!=null){
                        writeCollection(childObject, childFolder, depth + 1);
                    }
                }
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
                String objectType = businessObject.getBusinessObjectType();

                // Write the tag for the child e.g. <Accounting>
                writer.write("  <"+objectType+">\r\n");

                // get the object's properties
                TreeMap<String,String> objectProperties = businessObject.getInitProperties(collection);

                // iterate the properties and write them out (if not null)
                for(Map.Entry<String, String> entry : objectProperties.entrySet()){
                    String key = entry.getKey();
                    String objectProperty = entry.getValue();
                    if(objectProperty!=null && !objectProperty.equals("")){
                        writer.write("    <" + key + ">" + objectProperty + "</"+ key + ">\r\n");
                    }
                }
                // The implementation used is more clear and similar to the read Method
                if(object instanceof BusinessCollection && collection.writeGrandChildren()){
                    BusinessCollection subCollection = (BusinessCollection) object;
                    writeChildren(writer, subCollection);
                }
                // write the closing tag e.g. </Accounting>
                writer.write("  </" + objectType + ">\r\n");
            }
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    public static void readCollection(BusinessCollection businessCollection, boolean recursive, File parentFolder){
        String businessCollectionName = businessCollection.getName();
        File childFolder = new File(parentFolder, businessCollectionName);
        File xmlFile = new File(parentFolder, businessCollectionName+".xml");

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
                    String type = subCollection.getBusinessObjectType();
                    String name = subCollection.getName();
                    if(type.equals(name) || (subCollection instanceof MustBeRead)){
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
            try {
                // create new instance of object
                BusinessObject object = businessCollection.createNewChild();

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
                }

                // add the object to the collection
                businessCollection.addBusinessObject(object);

            } catch (EmptyNameException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (DuplicateNameException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
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
