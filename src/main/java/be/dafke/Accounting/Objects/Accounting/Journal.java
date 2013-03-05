package be.dafke.Accounting.Objects.Accounting;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Boekhoudkundig dagboek
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journal extends BusinessObject{
	private String abbreviation;
	private int id;
	private final ArrayList<Transaction> transacties;
//	private boolean save;
	private JournalType journalType;
    private Transaction currentTransaction = new Transaction();
    private Account currentAccount;

	protected Journal() {
//		save = true;
		transacties = new ArrayList<Transaction>();
		id = 1;
	}

    public Booking getBooking(int row){
        ArrayList<Booking> boekingen = new ArrayList<Booking>();
        for(Transaction transaction : transacties){
            boekingen.addAll(transaction.getBookings());
        }
        return boekingen.get(row);
    }

    public Transaction getTransaction(int row){
        return getBooking(row).getTransaction();
    }

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public JournalType getJournalType() {
        return journalType;
    }

    public void setJournalType(JournalType journalType) {
        this.journalType = journalType;
    }

    /**
	 * Geeft de naam van het dagboek en de bijhorende afkorting terug
	 * @return de naam van het dagboek en de bijhorende afkorting <b><i>naam dagboek(afkorting)</i></b>
	 */

	@Override
	public String toString() {
		return getName() + " (" + abbreviation + ")";
	}

	/**
	 * Geeft de transacties terug die bij dit dagboek horen
	 * @return de transacties die bij dit dagboek horen
	 */
	public ArrayList<Transaction> getTransactions() {
		return transacties;
	}

	/**
	 * Geeft het id van de volgende transactie terug
	 * @return het id van de volgende transactie
	 */
	public int getId() {
		return id;
	}

	/**
	 * Geeft de afkorting van het dagboek terug
	 * @return de afkorting van het dagboek
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Verwijdert de gegeven transactie
	 * @param transaction de te verwijderen transactie
	 */
	private void deleteTransaction(Transaction transaction) {
		boolean found = false;
		for(Transaction trans : transacties) {
			if (found) {
				trans.lowerID();
			} else if (trans == transaction) {
				found = true;
			}
		}
		transacties.remove(transaction);
	}

	/**
	 * Voegt een transactie toe
	 * @param transaction de toe te voegen transactie
	 */
	private void addTransaction(Transaction transaction) {
		Calendar datum = transaction.getDate();
		int plaats = 0;
		if (transacties.size() == 0 || datum.compareTo(transacties.get(transacties.size() - 1).getDate()) >= 0) {
			transaction.setId(id);
			transacties.add(transaction);
		} else {
			boolean found = false;
			for(int i = 0; i < transacties.size(); i++) {
				Transaction transactie = transacties.get(i);
				Calendar date = transactie.getDate();
				if (found) {
					transactie.raiseID();
				} else if (date.compareTo(datum) > 0) {
					transactie.raiseID();
					plaats = i;
					found = true;
				}
			}
			if (!found) {
				// hier kom je nooit
				transaction.setId(id);
				transacties.add(transaction);
			} else {
				transaction.setId(plaats + 1);
				transacties.add(plaats, transaction);
			}
		}
	}

	/**
	 * Verwijdert de gegeven transactie
	 * @param transaction de te verwijderen transactie
	 */
	protected void unbook(Transaction transaction) {
		deleteTransaction(transaction);
		ArrayList<Booking> boekingen = transaction.getBookings();
		for(Booking boeking : boekingen) {
			Account account = boeking.getAccount();
			account.unbook(boeking);
		}
		id--;
//		accounting.setSavedXML(false);
//		accounting.setSavedHTML(false);
//		save = false;
	}

	/**
	 * Boek de gegeven transactie
	 * @param transaction de te boeken transactie
	 */
	protected void book(Transaction transaction) {
        transaction.setAbbreviation(abbreviation);
		addTransaction(transaction);
		ArrayList<Booking> boekingen = transaction.getBookings();
		for(Booking boeking : boekingen) {
			Account rek = boeking.getAccount();
			rek.book(boeking);
		}
		id++;
//		accounting.setSavedXML(false);
//		accounting.setSavedHTML(false);
//		save = false;
	}

	public void setAbbreviation(String newAbbreviation) {
		abbreviation = newAbbreviation;
	}
}