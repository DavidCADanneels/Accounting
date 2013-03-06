package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Exceptions.NotEmptyException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 4/03/13
 * Time: 16:23
 */
public abstract class BusinessCollection<V extends BusinessObject> extends BusinessObject{

    protected static final String NAME = "name";

    protected HashMap<String, TreeMap<String,V>> dataTables;

    protected File htmlFolder;
    protected File xmlFolder;

    public BusinessCollection(){
        dataTables = new HashMap<String, TreeMap<String, V>>();
        addKey(NAME);
    }

    public void addKey(String key){
        if(dataTables.containsKey(key)){
            System.err.println("This collection already contains this key");
        }
        TreeMap<String, V> newMap = new TreeMap<String, V>();
        dataTables.put(key, newMap);
    }

    public V getBusinessObject(int nr) {
        return getBusinessObjects().get(nr);
    }

    public int getSize() {
        return size();
    }

    public V getBusinessObject(String name){
        return getBusinessObject(name, NAME);
    }

    public V getBusinessObject(String key, String type){
        TreeMap<String, V> map = dataTables.get(type);
        return map.get(key);
    }

    public V addBusinessObject(V value) throws EmptyNameException, DuplicateNameException{
        return addBusinessObject(value, NAME, value.getName());
    }

    public V addBusinessObject(V value, List<String> types, List<String> keys) throws EmptyNameException, DuplicateNameException {
        if(types.size() != keys.size()){
            System.err.println("Inproper use: typeList and keyList have different lengths !!!");
            return null;
        }

        for(int i=0;i<types.size();i++){
            String type = types.get(i);
            String key = keys.get(i);

            TreeMap<String, V> map = dataTables.get(type);
            V foundValue = map.get(key);

            if(foundValue!=null){
                value = merge(foundValue, value);
            }
            map.put(key,value);
        }
        return value;

    }

    protected V merge(V valueToKeep, V valueToRemove) throws EmptyNameException, DuplicateNameException {
        return null;
    }

    public V addBusinessObject(V value, String type, String key) throws EmptyNameException, DuplicateNameException{
        if(key == null || key.equals("")){
            throw new EmptyNameException();
        }
        TreeMap<String, V> map = dataTables.get(type);
        if(map.containsKey(key)){
            throw new DuplicateNameException();
        }

        value.setXmlFile(new File(xmlFolder, value.getName() + ".xml"));
        if(htmlFolder!=null){
            value.setHtmlFile(new File(htmlFolder, value.getName() + ".html"));
        }
        map.put(key, value);
        return value;
    }

    public List<V> getBusinessObjects(){
        TreeMap<String,V> map = dataTables.get(NAME);
        return new ArrayList<V>(map.values());
    }

    public V get(String name){
        return getBusinessObject(name);
    }

    public V get(int index){
        return getBusinessObjects().get(index);
    }

    public int size(){
        return getBusinessObjects().size();
    }

    public void removeBusinessObject(V value) throws NotEmptyException {
//        if(!value.isDeletable()){
            removeBusinessObject(NAME, value.getName());
//        }
    }

    public void removeBusinessObject(String type, String key){
        dataTables.get(type).remove(key);
    }


    public void setHtmlFolder(File parentFolder){
        setHtmlFile(new File(parentFolder, getType() + ".html"));
        htmlFolder = new File(parentFolder, getType());
        for(BusinessObject businessObject: getBusinessObjects()){
            businessObject.setHtmlFile(new File(htmlFolder, businessObject.getName() + ".html"));
        }
    }

    protected void setXmlFolder(File parentFolder) {
        setXmlFile(new File(parentFolder, getType() + ".xml"));
        xmlFolder = new File(parentFolder, getType());
        for(BusinessObject businessObject: getBusinessObjects()){
            businessObject.setXmlFile(new File(xmlFolder, businessObject.getName() + ".xml"));
        }
    }

//    protected File getXmlFolder(){
//        return xmlFolder;
//    }
//
//    public File getHtmlFolder() {
//        return htmlFolder;
//    }

    protected void createXmlFolder(){
        if(xmlFolder.mkdirs()){
            System.out.println(xmlFolder + " has been created");
        }
    }

    protected void createHtmlFolder(){
        if(htmlFolder.mkdirs()){
            System.out.println(htmlFolder + " has been created");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getType()).append(":\r\n");
        for(BusinessObject businessObject : getBusinessObjects()){
            builder.append(businessObject.toString());
        }
        return builder.toString();
    }

}
