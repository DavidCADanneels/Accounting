package be.dafke.ObjectModelDao;

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
import java.io.IOException;
import java.util.Set;
import java.util.TreeMap;

/**
 * User: david
 * Date: 27-12-13
 * Time: 20:55
 */
public class ObjectModelSAXParser {
    public static void readCollection(BusinessCollection businessCollection, boolean recursive, File parentFolder){
        String className = businessCollection.getBusinessObjectType();
        String shortName = businessCollection.getChildType();
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

//            for(String shortName:shortNames){

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
