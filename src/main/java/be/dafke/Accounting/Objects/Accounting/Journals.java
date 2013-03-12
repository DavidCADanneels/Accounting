package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.BusinessTypeCollection;
import be.dafke.Accounting.Objects.BusinessTypeProvider;
import be.dafke.Accounting.Objects.WriteableBusinessCollection;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Serialiseerbare map die alle dagboeken bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journals extends WriteableBusinessCollection<Journal> implements BusinessTypeProvider<JournalType> {

    private BusinessTypeCollection<JournalType> businessTypeCollection;

    public Journals() {
        addSearchKey(Journal.ABBREVIATION);
	}

    /**
	 * Geeft alle dagboeken terug behalve het gegeven dagboek
	 * @param j het dagboek dat we willen uitsluiten
	 * @return alle dagboeken behalve het gegeven dagboek
	 */
	public ArrayList<Journal> getAllJournalsExcept(Journal j) {
		ArrayList<Journal> result = new ArrayList<Journal>(getBusinessObjects());
		result.remove(j);
		return result;
	}

    public Journal modifyJournalName(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<String,String>(Journal.NAME, oldName);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<String,String>(Journal.NAME, newName);
//        Name is modified in modify Function
//        journal.setName(newName.trim());
        return modify(oldEntry, newEntry);
    }

    public Journal modifyJournalAbbreviation(String oldAbbreviation, String newAbbreviation) throws EmptyNameException, DuplicateNameException {
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<String,String>(Journal.ABBREVIATION, oldAbbreviation);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<String,String>(Journal.ABBREVIATION, newAbbreviation);
        Journal journal = modify(oldEntry, newEntry);
        journal.setAbbreviation(newAbbreviation.trim());
        return journal;
    }

    @Override
    public void setBusinessTypeCollection(BusinessTypeCollection<JournalType> businessTypeCollection) {
        this.businessTypeCollection = businessTypeCollection;
    }

    @Override
    public BusinessTypeCollection<JournalType> getBusinessTypeCollection() {
        return businessTypeCollection;
    }

    // KeySets and Properties

    @Override
    public Set<String> getInitKeySet() {
        Set<String> keySet = super.getInitKeySet();
        return keySet;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String, String> properties = super.getUniqueProperties();
        return properties;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
    }

    @Override
    public TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> properties = super.getUniqueProperties();
        return properties;
    }

    @Override
    public Set<String> getCollectionKeySet(){
        Set<String> collectionKeySet = super.getCollectionKeySet();
        return collectionKeySet;
    }

    @Override
    public TreeMap<String,String> getProperties() {
        TreeMap<String, String> outputMap = super.getProperties();
        return outputMap;
    }

    @Override
    public void setProperties(TreeMap<String, String> properties){
        super.setProperties(properties);
    }
}