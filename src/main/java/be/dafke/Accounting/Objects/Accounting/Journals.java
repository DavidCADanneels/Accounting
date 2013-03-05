package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Exceptions.NotEmptyException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Serialiseerbare map die alle dagboeken bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journals extends BusinessCollection<Journal> {

	private final HashMap<String, Journal> abbreviations;
    private final HashMap<String, Journal> journals;

    private Journal currentJournal;

    public Journals() {
        super("Journals");
        abbreviations = new HashMap<String, Journal>();
        journals = new HashMap<String, Journal>();
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
		ArrayList<Journal> result = new ArrayList<Journal>(journals.values());
		result.remove(j);
		return result;
	}

	public ArrayList<Journal> getBusinessObjects() {
		return new ArrayList<Journal>(journals.values());
	}

//	public void setSaved(boolean save) {
//		accounting.setSavedXML(save);
//		accounting.setSavedHTML(save);
//		this.save = save;
//	}

    public Journal addJournal(String name, String abbreviation, JournalType type) throws EmptyNameException, DuplicateNameException {
        if(name==null || "".equals(name.trim())){
            throw new EmptyNameException();
        }
        if(abbreviation==null || "".equals(abbreviation.trim())){
            throw new EmptyNameException();
        }
        if(journals.containsKey(name.trim()) || abbreviations.containsKey(abbreviation.trim())){
            throw new DuplicateNameException();
        }
        Journal journal = new Journal(name.trim(), abbreviation.trim(), type);

        journals.put(journal.getName(), journal);
        abbreviations.put(journal.getAbbreviation(), journal);
        return journal;
    }

    public void removeJournal(Journal journal) throws NotEmptyException {
        if(journal.getTransactions().isEmpty()){
            journals.remove(journal.getName());
            abbreviations.remove(journal.getAbbreviation());
        } else {
            throw new NotEmptyException();
        }
    }

    public Journal modifyJournalName(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        if(newName==null || "".equals(newName.trim())){
            throw new EmptyNameException();
        }
        Journal journal = journals.get(oldName);
        journals.remove(oldName);
        if(journals.containsKey(newName.trim())){
            journals.put(oldName, journal);
            throw new DuplicateNameException();
        }
        journals.put(newName, journal);
        journal.setName(newName.trim());
        return journal;
    }

    public Journal modifyJournalAbbreviation(String oldAbbreviation, String newAbbreviation) throws EmptyNameException, DuplicateNameException {
        if(newAbbreviation==null || "".equals(newAbbreviation.trim())){
            throw new EmptyNameException();
        }
        Journal journal = abbreviations.get(oldAbbreviation);
        abbreviations.remove(oldAbbreviation);
        if(abbreviations.containsKey(newAbbreviation.trim())){
            abbreviations.put(oldAbbreviation, journal);
            throw new DuplicateNameException();
        }
        abbreviations.put(newAbbreviation,journal);
        journal.setAbbreviation(newAbbreviation.trim());
        return journal;
    }

    @Override
    public Journal getBusinessObject(String current) {
        return journals.get(current);
    }
}