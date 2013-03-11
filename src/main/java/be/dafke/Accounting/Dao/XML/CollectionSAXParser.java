package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.BusinessTypeCollection;
import be.dafke.Accounting.Objects.BusinessTypeProvider;
import be.dafke.Accounting.Objects.BusinessTyped;
import be.dafke.Accounting.Objects.Writeable;
import be.dafke.Accounting.Objects.WriteableBusinessCollection;
import be.dafke.Accounting.Objects.WriteableBusinessObject;
import be.dafke.Utils;
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
import java.util.Map;
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
            String collectionType = collection.getBusinessObjectType();
            Writer writer = new FileWriter(collection.getXmlFile());

            writer.write(getXmlHeader(collection, collectionType));

            writer.write("<"+collectionType+">\r\n");

            for(WriteableBusinessObject object : collection.getBusinessObjects()) {

                String objectType = object.getBusinessObjectType();

                writer.write("  <"+objectType+">\r\n");

                for(Map.Entry<String,String> entry : object.getOutputMap().entrySet()){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    writer.write("    <" + key + ">" + value + "</"+ key + ">\r\n");
                }

                writer.write("  </"+objectType+">\r\n");
            }
            for(Map.Entry<String,String> entry : collection.getOutputMap().entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                writer.write("  <" + key + ">" + value + "</"+ key + ">\r\n");
            }

            writer.write("</"+collectionType+">\r\n");

            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WriteableBusinessCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WriteableBusinessCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    public static WriteableBusinessCollection<WriteableBusinessObject> readCollection(String collectionname, String objectName){
//        WriteableBusinessCollection<WriteableBusinessObject> result = null;
//        try {
//            result = (WriteableBusinessCollection<WriteableBusinessObject>)Class.forName(collectionname).getConstructors()[0].newInstance();
//            readCollection(result, objectName);
//        } catch (InstantiationException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//        return result;
//    }

    public static void readCollection(WriteableBusinessCollection<WriteableBusinessObject> collection, String objectName){
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

            Element rootElement = (Element) doc.getElementsByTagName(collectionName).item(0);
            NodeList nodeList = rootElement.getElementsByTagName(shortName);

            for (int i = 0; i < nodeList.getLength(); i++) {
                try {
                    WriteableBusinessObject object = (WriteableBusinessObject)Class.forName(objectName).getConstructors()[0].newInstance();

                    if(collection instanceof BusinessTypeProvider && object instanceof BusinessTyped){
                        BusinessTypeCollection btc = ((BusinessTypeProvider) collection).getBusinessTypeCollection();
                        ((BusinessTyped)object).setBusinessTypeCollection(btc);
                    }

                    TreeMap<String,String> properties = new TreeMap<String, String>();

                    Element element = (Element)nodeList.item(i);
                    for(String key:object.getKeySet()){
                        String value = Utils.getValue(element, key);
                        properties.put(key,value);
                    }
                    object.setProperties(properties);

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
        } catch (IOException io) {
            io.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
