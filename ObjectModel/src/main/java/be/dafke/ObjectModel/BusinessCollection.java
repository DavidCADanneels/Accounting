package be.dafke.ObjectModel;

import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 3:48
 */
public class BusinessCollection <V extends BusinessObject> extends BusinessObject {
    protected HashMap<String, TreeMap<String,V>> dataTables;
    protected V currentObject;

    public BusinessCollection(){
        dataTables = new HashMap<>();
        addSearchKey(NAME);
    }

    // -------------------------------------------------------------------------------
    protected void addSearchKey(String key){
        if(dataTables.containsKey(key)){
            System.err.println("This collection already contains this key");
        }
        TreeMap<String, V> newMap = new TreeMap<>();
        dataTables.put(key, newMap);
    }

    // -------------------------------------------------------------------------------------

    // Get

    public ArrayList<V> getBusinessObjects(){
        TreeMap<String,V> map = dataTables.get(NAME);
        return new ArrayList<>(map.values());
    }

    public V getBusinessObject(String name){
        if(name==null) return null;
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(NAME, name);
        return getBusinessObject(entry);
    }

    private V getBusinessObject(Map.Entry<String, String> entry){
        String type = entry.getKey();
        String key = entry.getValue();
        TreeMap<String, V> map = dataTables.get(type);
        return map==null?null:map.get(key);
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
                System.err.println(value);
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

    // Modify

    public V modify(Map.Entry<String,String> oldEntry, Map.Entry<String,String> newEntry) throws EmptyNameException, DuplicateNameException{
        if(!oldEntry.getKey().equals(newEntry.getKey())){
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

    public V modifyName(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<>(NAME, oldName);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<>(NAME, newName);
//        Name is modified in modify Function
//        journal.setName(newName.trim());
        return modify(oldEntry, newEntry);
    }

    // -------------------------------------------------------------------------------------

    // Remove

    /**Removal function for external use: performs a check if the value is deletable
     * @see BusinessObject#isDeletable()
     * @param value the value to delete
     * @throws be.dafke.ObjectModel.Exceptions.NotEmptyException if the value is not deletable
     */
    public void removeBusinessObject(V value) throws NotEmptyException {
        if(value.isDeletable()){
            removeBusinessObject(value.getUniqueProperties());
        } else {
            throw new NotEmptyException();
        }
    }

    protected void removeBusinessObject(Map<String,String> entryMap){
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

    public List<V> getBusinessObjects(Predicate<V> predicate) {
        return getBusinessObjects().stream().filter(predicate).collect(Collectors.toCollection(ArrayList::new));
    }

    public void clear() {
        TreeMap<String, V> stringVTreeMap = dataTables.get(NAME);
        stringVTreeMap.clear();
    }
}
