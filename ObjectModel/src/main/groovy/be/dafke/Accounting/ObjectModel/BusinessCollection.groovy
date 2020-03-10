package be.dafke.Accounting.ObjectModel

import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException

import java.util.function.Predicate

class BusinessCollection <V extends BusinessObject> extends BusinessObject {
    protected HashMap<String, TreeMap<String,V>> dataTables

    BusinessCollection(){
        dataTables = [:]
        addSearchKey(NAME)
    }

    // -------------------------------------------------------------------------------
    protected void addSearchKey(String key){
        if(dataTables.containsKey(key)){
            System.err.println("This collection already contains this key")
        }
        TreeMap<String, V> newMap = new TreeMap()
        dataTables.put(key, newMap)
    }

    // -------------------------------------------------------------------------------------

    // Get

    ArrayList<V> getBusinessObjects(){
        TreeMap<String,V> map = dataTables.get(NAME)
        new ArrayList(map.values())
    }

    V getBusinessObject(String name){
        if(name==null) null
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry(NAME, name)
        getBusinessObject(entry)
    }

    V getBusinessObject(Map.Entry<String, String> entry){
        String type = entry.getKey()
        String key = entry.getValue()
        TreeMap<String, V> map = dataTables.get(type)
        map?.get(key)
    }

    // -------------------------------------------------------------------------------------

    // Add

    V addBusinessObject(V value) throws EmptyNameException, DuplicateNameException {
        addBusinessObject(value, value.getUniqueProperties())
    }

    protected V addBusinessObject(V value, Map<String,String> keyMap) throws EmptyNameException, DuplicateNameException {
        for(Map.Entry<String,String> entry:keyMap.entrySet()){
            String key = entry.getValue()
            if(key==null || "".equals(key.trim())){
                System.err.println(value)
                throw new EmptyNameException()
            }
            V found = getBusinessObject(entry)
            if(found){
                throw new DuplicateNameException(key)
            }
        }
        for(Map.Entry<String,String> entry:keyMap.entrySet()){
            // This will not throw any exceptions: we already handled them above.
            addBusinessObject(value, entry)
        }
        value
    }

    /**For internal use:
     * modify and merge
     *
     */
    protected V addBusinessObject(V value, Map.Entry<String,String> mapEntry){
        String type = mapEntry.getKey()
        String key = mapEntry.getValue()
        TreeMap<String, V> map = dataTables.get(type)

        key = key.trim()

        if(type.equals(NAME)){
            value.setName(key)
        }

        map.put(key, value)
        value
    }

    // Modify

    V modify(Map.Entry<String,String> oldEntry, Map.Entry<String,String> newEntry) throws EmptyNameException, DuplicateNameException{
        if(!oldEntry.getKey().equals(newEntry.getKey())){
            throw new RuntimeException("Inproper use: keys should have the same value (modify)")
        }
        String key = newEntry.getValue()
        if(key==null || "".equals(key.trim())){
            throw new EmptyNameException()
        }
        V value = getBusinessObject(oldEntry)
        removeBusinessObject(oldEntry)

        V found = getBusinessObject(newEntry)
        if(found){
            addBusinessObject(value, oldEntry)
            throw new DuplicateNameException(key)
        }
        addBusinessObject(value, newEntry)
        value
    }

    V modifyName(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry(NAME, oldName)
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry(NAME, newName)
//        Name is modified in modify Function
//        journal.setName(newName.trim())
        modify(oldEntry, newEntry)
    }

    // -------------------------------------------------------------------------------------

    // Remove

    /**Removal function for external use: performs a check if the value is deletable
     * @see BusinessObject#isDeletable()
     * @param value the value to delete
     * @throws be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException if the value is not deletable
     */
    void removeBusinessObject(V value) throws NotEmptyException {
        if(value.deletable){
            removeBusinessObject(value.getUniqueProperties())
        } else {
            throw new NotEmptyException()
        }
    }

    protected void removeBusinessObject(Map<String,String> entryMap){
        for(Map.Entry<String,String> entry : entryMap.entrySet()){
            removeBusinessObject(entry)
        }
    }

    //
    /**Remove function for interal use: performs no check
     */
    protected void removeBusinessObject(Map.Entry<String,String> entry){
        String type = entry.getKey()
        String key = entry.getValue()
        dataTables.get(type).remove(key)
    }

    List<V> getBusinessObjects(Predicate<V> predicate) {
        getBusinessObjects().stream().filter(predicate).collect().toList()
    }

    void clear() {
        TreeMap<String, V> stringVTreeMap = dataTables.get(NAME)
        stringVTreeMap.clear()
    }
}
