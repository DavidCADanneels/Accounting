package be.dafke.Accounting.Objects;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Exceptions.NotEmptyException;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 3:48
 */
public class BusinessCollection <V extends BusinessObject> extends BusinessObject {
    protected HashMap<String, TreeMap<String,V>> dataTables;
    protected static final String CURRENT = "CurrentObject";
    protected V currentObject;

    public BusinessCollection(){
        dataTables = new HashMap<String, TreeMap<String, V>>();
        addSearchKey(NAME);
    }

    protected void addSearchKey(String key){
        if(dataTables.containsKey(key)){
            System.err.println("This collection already contains this key");
        }
        TreeMap<String, V> newMap = new TreeMap<String, V>();
        dataTables.put(key, newMap);
    }

    public ArrayList<V> getBusinessObjects(){
        TreeMap<String,V> map = dataTables.get(NAME);
        return new ArrayList<V>(map.values());
    }

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
     * @throws be.dafke.Accounting.Exceptions.NotEmptyException if the value is not deletable
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

    public Set<String> getCollectionKeySet(){
        Set<String> collectionKeySet = new TreeSet<String>();
        collectionKeySet.add(CURRENT);
        return collectionKeySet;
    }

    public TreeMap<String,String> getProperties() {
        TreeMap<String, String> properties = new TreeMap<String, String>();
        if(currentObject!=null){
            properties.put(CURRENT, currentObject.getName());
        }
        return properties;
    }

    public void setProperties(TreeMap<String, String> properties){
        String currentName = properties.get(CURRENT);
        if(currentName!=null){
            currentObject = getBusinessObject(currentName);
        }
    }

    // Redundant functions: call super ===================
    public Set<String> getInitKeySet(){
        Set<String> keySet = super.getInitKeySet();
        return keySet;
    }
    //
    public void setInitProperties(TreeMap<String, String> properties){
        super.setInitProperties(properties);
    }
    //
    public TreeMap<String, String> getInitProperties(){
        TreeMap<String,String> properties = super.getInitProperties();
        return properties;
    }
    //
    public TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = super.getUniqueProperties();
        return properties;
    }
    //====================================================

}
