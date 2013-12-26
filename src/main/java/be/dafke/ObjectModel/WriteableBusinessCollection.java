package be.dafke.ObjectModel;

import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;
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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 4/03/13
 * Time: 16:23
 */
public abstract class WriteableBusinessCollection<V extends WriteableBusinessObject> extends WriteableBusinessObject implements WriteableCollection{
    protected HashMap<String, TreeMap<String,V>> dataTables;
    protected static final String CURRENT = "CurrentObject";
    protected V currentObject;

    //                               CONSTRUCTOR

    public WriteableBusinessCollection(){
        dataTables = new HashMap<String, TreeMap<String, V>>();
        addSearchKey(NAME);
    }

    public abstract V createNewChild(String name);

    protected void addSearchKey(String key){
        if(dataTables.containsKey(key)){
            System.err.println("This collection already contains this key");
        }
        TreeMap<String, V> newMap = new TreeMap<String, V>();
        dataTables.put(key, newMap);
    }

    // -------------------------------------------------------------------------------------

    public V getCurrentObject() {
        return currentObject;
    }

    public void setCurrentObject(V currentObject) {
        this.currentObject = currentObject;
    }

    // -------------------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getBusinessObjectType()).append(":\r\n");
        for(BusinessObject writeableBusinessObject : getBusinessObjects()){
            builder.append(writeableBusinessObject.toString());
        }
        return builder.toString();
    }

    // -------------------------------------------------------------------------------------

    // Get

    public ArrayList<V> getBusinessObjects(){
        TreeMap<String,V> map = dataTables.get(NAME);
        return new ArrayList<V>(map.values());
    }

    public V getBusinessObject(String name){
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<String, String>(NAME, name);
        return getBusinessObject(entry);
    }

    private V getBusinessObject(Map.Entry<String, String> entry){
        String type = entry.getKey();
        String key = entry.getValue();
        TreeMap<String, V> map = dataTables.get(type);
        return map.get(key);
    }

    // -------------------------------------------------------------------------------------

    // Add

    public V addBusinessObject(V value) throws EmptyNameException, DuplicateNameException {
        return addBusinessObject(value, value.getUniqueProperties());
    }

    protected V addBusinessObject(V value, Map<String,String> keyMap) throws EmptyNameException, DuplicateNameException {
        for(Map.Entry<String,String> entry:keyMap.entrySet()){
            String key = entry.getValue();
            if(key==null || "".equals(key.trim())){
                throw new EmptyNameException();
            }
            V found = getBusinessObject(entry);
            if(found!=null){
                throw new DuplicateNameException(key);
            }
        }
        for(Map.Entry<String,String> entry:keyMap.entrySet()){
            // This will not throw any exceptions: we already handled them above.
            addBusinessObject(value, entry);
        }
        return value;
    }

    /**For internal use:
     * modify and merge
     *
     */
    protected V addBusinessObject(V value, Map.Entry<String,String> mapEntry){
        String type = mapEntry.getKey();
        String key = mapEntry.getValue();
        TreeMap<String, V> map = dataTables.get(type);

        key = key.trim();

        if(type.equals(NAME)){
            value.setName(key);
        }
        map.put(key, value);

        value.setXmlFile(new File(xmlFolder, value.getName() + ".xml"));
        if(htmlFolder!=null){
            value.setHtmlFile(new File(htmlFolder, value.getName() + ".html"));
        }
        return value;
    }

    // -------------------------------------------------------------------------------------

    // Modify

    public V modify(Map.Entry<String,String> oldEntry, Map.Entry<String,String> newEntry) throws EmptyNameException, DuplicateNameException{
        if(!oldEntry.getKey().equals(oldEntry.getKey())){
            throw new RuntimeException("Inproper use: keys should have the same value (modify)");
        }
        String key = newEntry.getValue();
        if(key==null || "".equals(key.trim())){
            throw new EmptyNameException();
        }
        V value = getBusinessObject(oldEntry);
        removeBusinessObject(oldEntry);

        V found = getBusinessObject(newEntry);
        if(found!=null){
            addBusinessObject(value, oldEntry);
            throw new DuplicateNameException(key);
        }
        addBusinessObject(value, newEntry);
        return value;
    }

    // -------------------------------------------------------------------------------------

    // Remove

    /**Removal function for external use: performs a check if the value is deletable
     * @see WriteableBusinessObject#isDeletable()
     * @param value the value to delete
     * @throws be.dafke.ObjectModel.Exceptions.NotEmptyException if the value is not deletable
     */
    public void removeBusinessObject(V value) throws NotEmptyException {
        if(value.isDeletable()){
            removeBusinessObject(value.getInitProperties());
        } else {
            throw new NotEmptyException();
        }
    }

    private void removeBusinessObject(Map<String,String> entryMap){
        for(Map.Entry<String,String> entry : entryMap.entrySet()){
            removeBusinessObject(entry);
        }
    }

    //
    /**Remove function for interal use: performs no check
     */
    protected void removeBusinessObject(Map.Entry<String,String> entry){
        String type = entry.getKey();
        String key = entry.getValue();
        dataTables.get(type).remove(key);
    }


    //============================================================================//
    //                                                                            //
    //                              WRITABLE                                      //
    //                                                                            //
    //============================================================================//

    private static final String XMLFOLDER = "xmlFolder";
    private static final String HTMLFOLDER = "htmlFolder";
    protected File htmlFolder;
    protected File xmlFolder;

    @Override
    public void setHtmlFolder(File parentFolder){
        setHtmlFile(new File(parentFolder, getBusinessObjectType() + ".html"));
        htmlFolder = new File(parentFolder, getBusinessObjectType());
        for(V writeableBusinessObject : getBusinessObjects()){
            writeableBusinessObject.setHtmlFile(new File(htmlFolder, writeableBusinessObject.getName() + ".html"));
        }
    }

    @Override
    public void setXmlFolder(File parentFolder) {
        setXmlFile(new File(parentFolder, getBusinessObjectType() + ".xml"));
        xmlFolder = new File(parentFolder, getBusinessObjectType());
        for(WriteableBusinessObject writeableBusinessObject : getBusinessObjects()){
            writeableBusinessObject.setXmlFile(new File(xmlFolder, writeableBusinessObject.getName() + ".xml"));
        }
    }

    //    @Override
//    protected File getXmlFolder(){
//        return xmlFolder;
//    }
//
    @Override
    public File getHtmlFolder() {
        return htmlFolder;
    }

    @Override
    public void createXmlFolder(){
        if(xmlFolder.mkdirs()){
            System.out.println(xmlFolder + " has been created");
        }
    }

    @Override
    public void createHtmlFolder(){
        if(htmlFolder.mkdirs()){
            System.out.println(htmlFolder + " has been created");
        }
    }

    //============================================================================//
    //                                                                            //
    //                              BUSINESS OBJECT                               //
    //                                                                            //
    //============================================================================//

    // KeySets and Properties ==============================================

    @Override
    public Set<String> getInitKeySet() {
        Set<String> keySet = super.getInitKeySet();
        keySet.add(XMLFOLDER);
        keySet.add(HTMLFOLDER);
        return keySet;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String, String> properties = super.getInitProperties();
        if(xmlFolder!=null){
            properties.put(XMLFOLDER, xmlFolder.getPath());
        }
        if(htmlFolder!=null){
            properties.put(HTMLFOLDER, htmlFolder.getPath());
        }
        return properties;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
        String xmlFolderPath = properties.get(XMLFOLDER);
        String htmlFolderPath = properties.get(HTMLFOLDER);
        if(xmlFolderPath!=null){
            xmlFolder = new File(xmlFolderPath);
        }
        if(htmlFolderPath!=null){
            htmlFolder = new File(htmlFolderPath);
        }
    }

    // WRITE

    public String getXmlHeader() {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
                "<?xml-stylesheet type=\"text/xsl\" href=\"" + xsl2XmlFile + "\"?>\r\n" +
                "<!DOCTYPE " + getBusinessObjectType() + " SYSTEM \"" + dtdFile + "\">\r\n";
    }

    public void writeCollection(){
        try {
            String collectionName = getBusinessObjectType();
            Writer writer = new FileWriter(getXmlFile());

            // Write the header with correct XSL-File, DTD-File and DTD-Root-Element
            writer.write(getXmlHeader());

            // Write the root element e.g. <Accountings>
            writer.write("<" + collectionName + ">\r\n");
            if(collectionName.equals("Accounting")){
                writer.write("  <name>"+getName()+"</name>\r\n");
            }
            // Iterate children and write their data
            for(WriteableBusinessObject object : getBusinessObjects()) {

                String objectType = object.getBusinessObjectType();

                // Write the tag for the child e.g. <Accounting>
                writer.write("  <"+objectType+">\r\n");

                // get the object's properties
                TreeMap<String,String> objectProperties = object.getInitProperties();

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

            if(getCurrentObject()!=null){
                writer.write("    <CurrentObject>" + getCurrentObject().getName() + "</CurrentObject>\r\n");
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

        for(V businessObject : getBusinessObjects()) {
            if(businessObject instanceof WriteableBusinessCollection){
                WriteableBusinessCollection<WriteableBusinessObject> businessCollection = ((WriteableBusinessCollection<WriteableBusinessObject>)businessObject);
                businessCollection.writeCollection();
            }
        }
    }

    // READ

    public abstract void readCollection();
//    {
//        readCollection(getBusinessObjectNames());
//    }

    public void readCollection(String shortName, boolean recursive){
        ArrayList<String> list = new ArrayList<String>();
        list.add(shortName);
        readCollection(list, recursive);
    }
    public void readCollection(ArrayList<String> shortNames, boolean recursive){
        try {
            File file = getXmlFile();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file.getAbsolutePath());
            doc.getDocumentElement().normalize();

            String collectionName = getBusinessObjectType();

            // get Root Element e.g. <Accountings>
            Element rootElement = (Element) doc.getElementsByTagName(collectionName).item(0);

            for(String shortName:shortNames){

                // get Children e.g. <Accounting>
                NodeList childrenNodeList = rootElement.getElementsByTagName(shortName);

                // iterate children and create objects for them
                for (int i = 0; i < childrenNodeList.getLength(); i++) {
                    V object = getBusinessObject(shortName);
                    if(object==null){
                        try {
                            // create new instance of object
                            object = createNewChild(shortName);

                            // if object is Typed, fetch its TypeCollection from the collection
                            if(this instanceof BusinessTypeProvider && object instanceof BusinessTypeCollectionDependent){
                                BusinessTypeCollection btc = ((BusinessTypeProvider) this).getBusinessTypeCollection();
                                ((BusinessTypeCollectionDependent)object).setBusinessTypeCollection(btc);
                            }

                            // if object is dependant on another collection, fetch this Collection from the collection
                            if(this instanceof BusinessCollectionProvider && object instanceof BusinessCollectionDependent){
                                WriteableBusinessCollection bc = ((BusinessCollectionProvider) this).getBusinessCollection();
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
                            addBusinessObject(object);

                        } catch (EmptyNameException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (DuplicateNameException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    } // if null
                }// for each ChildNode
            }// for each name in ArrayList
            String value = getValue(rootElement, CURRENT);
            if(value!=null){
                currentObject = getBusinessObject(value);
                System.err.println("current Object: "+currentObject.getName());
            }
        } catch (IOException io) {
            io.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(recursive){
            for(V businessObject : getBusinessObjects()) {
                if(businessObject instanceof WriteableBusinessCollection){
                    WriteableBusinessCollection<WriteableBusinessObject> businessCollection = ((WriteableBusinessCollection<WriteableBusinessObject>)businessObject);
                    businessCollection.readCollection();
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