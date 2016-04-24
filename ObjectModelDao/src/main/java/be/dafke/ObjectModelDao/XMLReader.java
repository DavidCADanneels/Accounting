package be.dafke.ObjectModelDao;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.MustBeRead;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by ddanneels on 28/12/2015.
 */
public class XMLReader {
    public static void readCollection(BusinessCollection businessCollection, File parentFolder){
        String businessCollectionName = businessCollection.getName();
        File childFolder = new File(parentFolder, businessCollectionName);
        File xmlFile = new File(parentFolder, businessCollectionName+".xml");

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//            documentBuilderFactory.setValidating(true);
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
        for(Object businessObject : businessCollection.getBusinessObjects()) {
            if(businessObject instanceof BusinessCollection){
                BusinessCollection<BusinessObject> subCollection = ((BusinessCollection<BusinessObject>)businessObject);
                String type = subCollection.getBusinessObjectType();
                String name = subCollection.getName();
                if(type.equals(name) || (subCollection instanceof MustBeRead)){
                    readCollection(subCollection, childFolder);
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
                // create empty properties TreeMap
                TreeMap<String, String> properties = new TreeMap<String, String>();

                // get the Object's keySet
                Set<String> keySet = businessCollection.getInitKeySet();

                // read all the tags which names are in the keySet
                // and add their value to the properties
                Element element = (Element) childrenNodeList.item(i);
                for (String key : keySet) {
                    String value = getValue(element, key);
                    properties.put(key, value);
                }

                // create new instance of object
                BusinessObject object = businessCollection.createNewChild(properties);

                // provide the properties to the object
//                object.setInitProperties(properties);

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
        String value = getValue(rootElement, BusinessCollection.CURRENT);
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
