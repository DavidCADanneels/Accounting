package be.dafke.Accounting.Objects;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 3:41
 */
public class BusinessObject {
    private String name;
    protected String type;
    protected final static String NAME = "name";

//    private boolean isSaved;

    protected BusinessObject(){
        type = this.getClass().getSimpleName();
    }

    public String getType() {
        return type;
    }

    public Map<String,String> getKeyMap(){
        Map<String,String> keyMap = new HashMap<String, String>();
        keyMap.put(NAME, name);
        return keyMap;
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
}
