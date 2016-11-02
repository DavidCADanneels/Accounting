package be.dafke.ObjectModel;

import java.util.Properties;
import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 3:41
 */
public class BusinessObject {
    private String name;
    protected String businessObjectType;
    public final static String NAME = "name";

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

    //
    public Properties getOutputProperties(){
        Properties properties = new Properties();
        properties.put(NAME,name);
        return properties;
    }
    //
    public TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = new TreeMap<>();
        properties.put(NAME,name);
        return properties;
    }
}
