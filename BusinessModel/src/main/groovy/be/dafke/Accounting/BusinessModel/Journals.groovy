package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

import java.util.function.Predicate

/**
 * Serialiseerbare map die alle dagboeken bevat
 * @author David Danneels
 * @since 01/10/2010
 */
class Journals extends BusinessCollection<Journal> {
    static final String ABBREVIATION = "abbr"// TODO: 'abbr' or 'abbreviation'
    Accounting accounting

    Journals(Journals journals){
        this(journals.accounting)
        for(Journal journal:journals.businessObjects) {
            try {
                addBusinessObject(new Journal(journal))
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    @Override
    Journal addBusinessObject(Journal journal) throws EmptyNameException, DuplicateNameException {
        super.addBusinessObject(journal)
        journal.accounting = accounting
        journal
    }

    Journals(Accounting accounting) {
        this.accounting = accounting
        addSearchKey(ABBREVIATION)
    }

    Accounting getAccounting() {
        accounting
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
    }

    /* @Override
    void readCollection() {
        readCollection("Journal", true)
    }*/

    /**
     * Geeft alle dagboeken terug behalve het gegeven dagboek
     * @param j het dagboek dat we willen uitsluiten
     * @alle dagboeken behalve het gegeven dagboek
     */
    ArrayList<Journal> getAllJournalsExcept(Journal j) {
        ArrayList<Journal> result = new ArrayList(getBusinessObjects())
        result.remove(j)
        result
    }

    Journal getJournal(String abbreviation){
        Predicate<Journal> predicate = Journal.withAbbr(abbreviation)
        List<Journal> journals = getBusinessObjects(predicate)
        (journals==null||journals.isEmpty())?null:journals.get(0)
    }

    Journal modifyJournalAbbreviation(String oldAbbreviation, String newAbbreviation) throws EmptyNameException, DuplicateNameException {
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry(ABBREVIATION, oldAbbreviation)
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry(ABBREVIATION, newAbbreviation)
        Journal journal = modify(oldEntry, newEntry)
        journal.setAbbreviation(newAbbreviation.trim())
        journal
    }
}