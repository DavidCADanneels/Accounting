package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessCollectionProvider;
import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.BusinessTypeProvider;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * Serialiseerbare map die alle dagboeken bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journals extends BusinessCollection<Journal> implements BusinessTypeProvider<JournalType>, BusinessCollectionProvider<Account> {

    private BusinessCollection<Account> businessCollection;

    @Override
    public String getChildType(){
        return "Journal";
    }

    @Override
    public void setBusinessCollection(BusinessCollection<Account> businessCollection){
        this.businessCollection = businessCollection;
    }

    @Override
    public BusinessCollection<Account> getBusinessCollection() {
        return businessCollection;
    }

    private BusinessTypeCollection<JournalType> businessTypeCollection;

    public Journals() {
        addSearchKey(Journal.ABBREVIATION);
        setName("Journals");
	}

    @Override
    public Journal createNewChild(String name) {
        return new Journal();
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
		ArrayList<Journal> result = new ArrayList<Journal>(getBusinessObjects());
		result.remove(j);
		return result;
	}

    public Journal modifyJournalName(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<String,String>(NAME, oldName);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<String,String>(NAME, newName);
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
}