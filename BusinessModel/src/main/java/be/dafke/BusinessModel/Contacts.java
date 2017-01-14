package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.MustBeRead;

import java.util.Set;
import java.util.TreeMap;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class Contacts extends BusinessCollection<Contact> implements MustBeRead {

    public static final String NAME = "name";
    public static final String ADDRESS_LINE_1 = "addressLine1";
    public static final String ADDRESS_LINE_2 = "addressLine2";
    public static final String TVA_NUMBER = "TVANumber";
    public static final String CONTACTS = "Contacts";

    public Contacts() {
        setName(CONTACTS);
    }

    @Override
    public String getChildType() {
        return "Contact";
    }

    @Override
    public Contact createNewChild(TreeMap<String, String> properties) {
        Contact contact = new Contact();
        contact.setName(properties.get(NAME));
        contact.setAddressLine1(properties.get(ADDRESS_LINE_1));
        contact.setAddressLine2(properties.get(ADDRESS_LINE_2));
        contact.setVatNumber(properties.get(TVA_NUMBER));
        return contact;
    }

    public Set<String> getInitKeySet(){
        Set<String> initKeySet = super.getInitKeySet();
        initKeySet.add(ADDRESS_LINE_1);
        initKeySet.add(ADDRESS_LINE_2);
        initKeySet.add(TVA_NUMBER);
        return initKeySet;
    }
}
