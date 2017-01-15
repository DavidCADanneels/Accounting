package be.dafke.BusinessModel;

import java.util.Calendar;

/**
 * Boekhoudkundig dagboek
 * @author David Danneels
 * @since 01/10/2010
 */
public class ProjectJournal extends Journal {

    public ProjectJournal(String name, String abbreviation) {
        super(name, abbreviation, null);
	}

	public void removeBusinessObject(Transaction transaction) {
        Calendar date = transaction.getDate();
        transactions.removeValue(date, transaction);
	}

	public Transaction addBusinessObject(Transaction transaction) {
        Calendar date = transaction.getDate();
//        transaction.setJournal(this); // keep link with original journal
        if(!transactions.values().contains(transaction)){
            transactions.addValue(date, transaction);
        }
        return transaction;
	}
}