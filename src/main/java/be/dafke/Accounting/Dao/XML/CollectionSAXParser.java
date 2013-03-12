package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.BusinessCollection;
import be.dafke.Accounting.Objects.BusinessCollectionDependent;
import be.dafke.Accounting.Objects.BusinessCollectionProvider;
import be.dafke.Accounting.Objects.BusinessTypeCollection;
import be.dafke.Accounting.Objects.BusinessTypeProvider;
import be.dafke.Accounting.Objects.BusinessTyped;
import be.dafke.Accounting.Objects.Writeable;
import be.dafke.Accounting.Objects.WriteableBusinessCollection;
import be.dafke.Accounting.Objects.WriteableBusinessObject;
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
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 11/03/13
 * Time: 7:26
 */
public class CollectionSAXParser {
    public static String getXmlHeader(Writeable object, String type) {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
                "<?xml-stylesheet type=\"text/xsl\" href=\"" + object.getXsl2XmlFile() + "\"?>\r\n" +
                "<!DOCTYPE " + type + " SYSTEM \"" + object.getDtdFile() + "\">\r\n";
    }

    public static void writeCollection(WriteableBusinessCollection<WriteableBusinessObject> collection){
        try {
            String collectionName = collection.getBusinessObjectType();
            Writer writer = new FileWriter(collection.getXmlFile());

            // Write the header with correct XSL-File, DTD-File and DTD-Root-Element
            writer.write(getXmlHeader(collection, collectionName));

            // Write the root element e.g. <Accountings>
            writer.write("<"+collectionName+">\r\n");

            // Iterate children and write their data
            for(WriteableBusinessObject object : collection.getBusinessObjects()) {

                String objectType = object.getBusinessObjectType();

                // Write the tag for the child e.g. <Accounting>
                writer.write("  <"+objectType+">\r\n");

                // get the object's properties
                TreeMap<String,String> objectProperties = object.getInitProperties();

                // get the Object's keySet
                Set<String> keySet = object.getInitKeySet();
                
                // iterate the keySet and write the properties
                for(String key : keySet){
                    String objectProperty = objectProperties.get(key);
                    if(objectProperty!=null && !objectProperty.equals("")  && !objectProperty.equals("null")){
                        writer.write("    <" + key + ">" + objectProperty + "</"+ key + ">\r\n");
                    }

//                    String objectProperty = objectProperties.get(key);
//                    if(objectProperty!=null){
//                        String[] parts = objectProperty.split(" | ");
//                        for(int i=0;i<parts.length;i++){
//                            writer.write("    <" + key + ">" + parts[i] + "</"+ key + ">\r\n");
//                        }
//                    }

                }
                // Note: this can be done without using the keySet:
//                for(Map.Entry<String, String> entry : objectProperties.entrySet()){
//                    String key = entry.getKey();
//                    String objectProperty = entry.getValue();
////                    String objectProperty = objectProperties.get(key);
//                    writer.write("    <" + key + ">" + objectProperty + "</"+ key + ">\r\n");
//                }
                // The implementation used is more clear and similar to the read Method

                // write the closing tag e.g. </Accounting>
                writer.write("  </"+objectType+">\r\n");
            }

            // get the Collection's collectionKeySet
            Set<String> keySet = collection.getCollectionKeySet();
            
            // get the Collection's properties
            TreeMap<String,String> collectionProperties = collection.getProperties();

            // iterate the keySet and write the properties
            for(String key : keySet){
                String collectionProperty = collectionProperties.get(key);
                if(collectionProperty!=null && !collectionProperty.equals("")  && !collectionProperty.equals("null")){
                    writer.write("    <" + key + ">" + collectionProperty + "</"+ key + ">\r\n");
                }

//                String collectionProperty = collectionProperties.get(key);
//                if(collectionProperty!=null){
//                   String[] parts = collectionProperty.split(" | ");
//                    for(int i=0;i<parts.length;i++){
//                        writer.write("    <" + key + ">" + parts[i] + "</"+ key + ">\r\n");
//                    }
//                }

            }

            writer.write("</"+collectionName+">\r\n");

            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WriteableBusinessCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WriteableBusinessCollection.class.getName()).log(Level.SEVERE, null, ex);
        }

        // TODO iterate all 'Writeable' childeren and call this function (recursion)
    }

    public static void readCollection(WriteableBusinessCollection collection, String objectName){
        try {
            File file = collection.getXmlFile();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file.getAbsolutePath());
            doc.getDocumentElement().normalize();

            String collectionName = collection.getBusinessObjectType();

            String shortName = objectName;
            int index = shortName.indexOf(".");
            while(index != -1){
                shortName = shortName.substring(index+1);
                index = shortName.indexOf(".");
            }

            // get Root Element e.g. <Accountings>
            Element rootElement = (Element) doc.getElementsByTagName(collectionName).item(0);

            // get Children e.g. <Accounting>
            NodeList childrenNodeList = rootElement.getElementsByTagName(shortName);

            // iterate children and create objects for them
            for (int i = 0; i < childrenNodeList.getLength(); i++) {
                try {
                    // create new instance of object
                    WriteableBusinessObject object = (WriteableBusinessObject)Class.forName(objectName).getConstructors()[0].newInstance();

                    // if object is Typed, fetch its TypeCollection from the collection
                    if(collection instanceof BusinessTypeProvider && object instanceof BusinessTyped){
                        BusinessTypeCollection btc = ((BusinessTypeProvider) collection).getBusinessTypeCollection();
                        ((BusinessTyped)object).setBusinessTypeCollection(btc);
                    }

                    // if object is dependant on another collection, fetch this Collection from the collection
                    if(collection instanceof BusinessCollectionProvider && object instanceof BusinessCollectionDependent){
                        BusinessCollection bc = ((BusinessCollectionProvider) collection).getBusinessCollection();
                        ((BusinessCollectionDependent)object).setBusinessCollection(bc);
                    }

                    // create empty properties TreeMap
                    TreeMap<String,String> properties = new TreeMap<String, String>();

                    // get the Object's keySet
                    Set<String> keySet = object.getInitKeySet();

                    // read all the tags which names are in the keySet
                    // and add their value to the properties
                    Element element = (Element)childrenNodeList.item(i);
                    for(String key : keySet){
                        String value = getValue(element, key);
                        properties.put(key,value);
                    }

                    // provide the properties to the object
                    object.setInitProperties(properties);

                    // add the object to the collection
                    collection.addBusinessObject(object);

                } catch (InstantiationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InvocationTargetException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (EmptyNameException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (DuplicateNameException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }// for

            // create empty properties TreeMap
            TreeMap<String,String> collectionProperties = new TreeMap<String, String>();
            
            // get the Collection's collectionKeySet
            Set<String> keySet = collection.getCollectionKeySet();

            // read all the tags which names are in the keySet
            // and add their value to the properties
            for(String key : keySet){
                NodeList collectionPropertiesNodeList = rootElement.getElementsByTagName(key);
                if(childrenNodeList.getLength()!=0){
                    Element element = (Element)collectionPropertiesNodeList.item(0);
                    if(element!=null){
                        String value = getValue(element, key);
                        collectionProperties.put(key,value);
                    }
                }
            }

            // set the collectionProperties
            collection.setProperties(collectionProperties);

        } catch (IOException io) {
            io.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
