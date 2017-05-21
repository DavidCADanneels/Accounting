package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
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
public class Journals extends BusinessCollection<Journal> {
    public static final String ABBREVIATION = "abbr";// TODO: 'abbr' or 'abbreviation'
    private Accounting accounting;

    public Journals(Journals journals){
        this(journals.accounting);
        for(Journal journal:journals.getBusinessObjects()) {
            try {
                addBusinessObject(new Journal(journal));
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
        currentObject = journals.getBusinessObject(journals.currentObject.getName());
    }

    @Override
    public Journal addBusinessObject(Journal journal) throws EmptyNameException, DuplicateNameException {
        super.addBusinessObject(journal);
        journal.setAccounting(accounting);
        return journal;
    }

    public Journals(Accounting accounting) {
        this.accounting = accounting;
        addSearchKey(ABBREVIATION);
	}

    public Accounting getAccounting() {
        return accounting;
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
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

    public Journal modifyJournalAbbreviation(String oldAbbreviation, String newAbbreviation) throws EmptyNameException, DuplicateNameException {
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<>(ABBREVIATION, oldAbbreviation);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<>(ABBREVIATION, newAbbreviation);
        Journal journal = modify(oldEntry, newEntry);
        journal.setAbbreviation(newAbbreviation.trim());
        return journal;
    }
}