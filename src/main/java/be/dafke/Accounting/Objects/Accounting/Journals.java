package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * Serialiseerbare map die alle dagboeken bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journals extends BusinessCollection<Journal> {

    private Journal currentJournal;

    public Journals() {
        addKey(Journal.ABBREVIATION);
	}

    public Journal getCurrentJournal() {
        return currentJournal;
    }
    //
    public void setCurrentJournal(Journal journal) {
        currentJournal = journal;
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
}