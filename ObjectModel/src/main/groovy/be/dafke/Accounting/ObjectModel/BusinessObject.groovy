package be.dafke.Accounting.ObjectModel 
class BusinessObject {
    String name
    final static String NAME = "name"

//    boolean isSaved

    @Override
    String toString() {
        name
    }

    /**Checks if the BusinessObject is editable:
     * @if the BusinessObject is editable (default: false)
     */
    boolean isEditable() {
        false
    }

    /**Checks if the BusinessObject is deletable:
     * @if the BusinessObject is deletable (default: false)
     */
    boolean isDeletable() {
        false
    }
    // TODO: make interfaces: Mergeable etc
    boolean isMergeable(){
        false
    }

    TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = new TreeMap()
        properties.put(NAME,name)
        properties
    }
}
