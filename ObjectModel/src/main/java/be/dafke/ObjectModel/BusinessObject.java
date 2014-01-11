package be.dafke.ObjectModel;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 3:41
 */
public abstract class BusinessObject {
    private String name;
    protected String businessObjectType;
    protected final static String NAME = "name";

//    private boolean isSaved;

    public BusinessObject(){
        businessObjectType = this.getClass().getSimpleName();
    }

    public String getBusinessObjectType() {
        return businessObjectType;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
//		setSaved(false);
    }

    /**Checks if the BusinessObject is deletable:
     * @return if the BusinessObject is deletable (default: false)
     */
    public boolean isDeletable() {
        return false;
    }
    // TODO: make interfaces: Mergeable etc
    public boolean isMergeable(){
        return false;
    }

    // KeySet and Properties
    //
    // Keys found in the CollectionFile e.g. Account.NAME in Accounts.xml file
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(NAME);
        return keySet;
    }
    //
    public void setInitProperties(TreeMap<String, String> properties){
        name = properties.get(NAME);
    }
    //
    public TreeMap<String, String> getInitProperties(BusinessCollection collection){
        TreeMap<String,String> properties = new TreeMap<String, String>();
        properties.put(NAME,name);
        return properties;
    }
    //
    public TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = new TreeMap<String, String>();
        properties.put(NAME,name);
        return properties;
    }

    public abstract boolean separateFile();
}
