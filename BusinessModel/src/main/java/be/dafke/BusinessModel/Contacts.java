package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.util.TreeMap;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class Contacts extends BusinessCollection<Contact> {

    public static final String NAME = "name";
    public static final String ADDRESS_LINE_1 = "addressLine1";
    public static final String ADDRESS_LINE_2 = "addressLine2";

    @Override
    public String getChildType() {
        return "Contact";
    }

    @Override
    public Contact createNewChild(TreeMap<String, String> properties) {
        Contact contact = new Contact();
        contact.setName(properties.get(NAME));
        contact.setName(properties.get(ADDRESS_LINE_1));
        contact.setName(properties.get(ADDRESS_LINE_2));
        return contact;
    }
}
