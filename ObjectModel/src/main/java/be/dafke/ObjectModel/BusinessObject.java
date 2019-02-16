package be.dafke.ObjectModel;

import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 3:41
 */
public class BusinessObject {
    private String name;
    public final static String NAME = "name";

//    private boolean isSaved;

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

    /**Checks if the BusinessObject is editable:
     * @return if the BusinessObject is editable (default: false)
     */
    public boolean isEditable() {
        return false;
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

    public TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = new TreeMap<>();
        properties.put(NAME,name);
        return properties;
    }
}
