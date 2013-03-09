package be.dafke.Accounting.Objects;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Exceptions.NotEmptyException;
import be.dafke.Accounting.Objects.Accounting.WriteableBusinessObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 3:48
 */
public class BusinessCollection <V extends BusinessObject> extends BusinessObject {
    protected HashMap<String, TreeMap<String,V>> dataTables;

    public BusinessCollection(){
        dataTables = new HashMap<String, TreeMap<String, V>>();
        addKey(NAME);
    }

    protected void addKey(String key){
        if(dataTables.containsKey(key)){
            System.err.println("This collection already contains this key");
        }
        TreeMap<String, V> newMap = new TreeMap<String, V>();
        dataTables.put(key, newMap);
    }

    public List<V> getBusinessObjects(){
        TreeMap<String,V> map = dataTables.get(NAME);
        return new ArrayList<V>(map.values());
    }

    // -------------------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getType()).append(":\r\n");
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
        return addBusinessObject(value, value.getKeyMap());
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
            removeBusinessObject(value.getKeyMap());
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
}
