package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.Accounting.Account.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class Project implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String naam;
	private final ProjectAccounts rekeningen;
	private Accounting accounting;

	public Project(String name) {
		naam = name;
		rekeningen = new ProjectAccounts(accounting);
	}

	public void setAccounting(Accounting accounting) {
		this.accounting = accounting;
	}

	public void addAccount(Account account) {
		rekeningen.add(account);
		account.setProject(this);
	}

	public void removeAccount(Account account) {
		account.setProject(null);
		rekeningen.remove(account.toString());
	}

	public Accounts getAccounts() {
		return rekeningen;
	}

	public Account close() {
		Transaction transaction = new Transaction();
        transaction.setDate(Calendar.getInstance());
        transaction.setDescription(getBundle("Accounting").getString("AFSLUITEN_PROJECT"));
		ArrayList<Account> teVerwijderen = new ArrayList<Account>();

		BigDecimal totaalKost = new BigDecimal(0);
		for (Account kost : rekeningen.getAccounts(AccountType.Cost)) {
			BigDecimal amount = kost.saldo();
			transaction.crediteer(kost, amount);
			totaalKost = totaalKost.add(amount);
			teVerwijderen.add(kost);
		}

		BigDecimal totaalOpbrengst = new BigDecimal(0);
		for (Account opbrengst : rekeningen.getAccounts(AccountType.Revenue)) {
			BigDecimal amount = opbrengst.saldo();
			transaction.debiteer(opbrengst, amount);
			totaalOpbrengst = totaalOpbrengst.add(amount);
			teVerwijderen.add(opbrengst);
		}
		Account result = null;
        // TODO ask user to provide a Name instead of autocreating --> catch Exception from creation
//		if (totaalOpbrengst.compareTo(totaalKost) > 0) {
//			BigDecimal winst = totaalOpbrengst.subtract(totaalKost);
//			winst = winst.setScale(2);
//			result = accounting.getAccounts().add(getBundle("Accounting").getString("WINST_PROJECT") + naam, AccountType.Revenue);
//			transaction.crediteer(result, winst);
//		} else {
//			BigDecimal verlies = totaalKost.subtract(totaalOpbrengst);
//			verlies = verlies.setScale(2);
//			result = accounting.getAccounts().add(getBundle("Accounting").getString("VERLIES_PROJECT") + naam, AccountType.Cost);
//			transaction.debiteer(result, verlies);
//		}
		// TODO: transaction.book() !!!
		for(Account account : teVerwijderen) {
			rekeningen.remove(account);
		}
		return result;
	}

	@Override
	public String toString() {
		return naam;
	}

    private class ProjectAccounts extends Accounts{
        public ProjectAccounts(Accounting accounting){
            super(accounting);
        }

        public void add(Account account) {
            put(account.getName(),account);
        }
    }
}
