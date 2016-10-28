package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.*;

/**
 * Serialiseerbare map die alle dagboeken bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journals extends BusinessCollection<Journal> {
    public static final String TYPE = "type";
    public static final String ABBREVIATION = "abbr";// TODO: 'abbr' or 'abbreviation'

    private final Accounting accounting;

    @Override
    public String getChildType(){
        return "Journal";
    }

    public Journals(Accounting accounting) {
        this.accounting = accounting;
        addSearchKey(ABBREVIATION);
        setName("Journals");
	}

    @Override
    public Journal createNewChild(TreeMap<String, String> properties) {
        String name = properties.get(NAME);
        String abbreviation = properties.get(Journals.ABBREVIATION);
        String typeName = properties.get(Journals.TYPE);
        Journal journal = new Journal(accounting, name, abbreviation);
        if(typeName!=null){
            journal.setType(accounting.getJournalTypes().getBusinessObject(typeName));
        }
        return journal;
    }

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<>();
        keySet.add(NAME);
        keySet.add(ABBREVIATION);
        keySet.add(TYPE);
        return keySet;
    }

   /* @Override
    public void readCollection() {
        readCollection("Journal", true);
    }*/

    /**
	 * Geeft alle dagboeken terug behalve het gegeven dagboek
	 * @param j het dagboek dat we willen uitsluiten
	 * @return alle dagboeken behalve het gegeven dagboek
	 */
	public ArrayList<Journal> getAllJournalsExcept(Journal j) {
		ArrayList<Journal> result = new ArrayList<>(getBusinessObjects());
		result.remove(j);
		return result;
	}

    public Journal modifyJournalName(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<>(NAME, oldName);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<>(NAME, newName);
//        Name is modified in modify Function
//        journal.setName(newName.trim());
        return modify(oldEntry, newEntry);
    }

    public Journal modifyJournalAbbreviation(String oldAbbreviation, String newAbbreviation) throws EmptyNameException, DuplicateNameException {
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<>(ABBREVIATION, oldAbbreviation);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<>(ABBREVIATION, newAbbreviation);
        Journal journal = modify(oldEntry, newEntry);
        journal.setAbbreviation(newAbbreviation.trim());
        return journal;
    }
}