package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.Accounting.Account.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * @author David Danneels
 */
public class Project implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String naam;
	private final Accounts rekeningen;
	private Accounting accounting;

	public Project(String name) {
		naam = name;
		rekeningen = new Accounts();
	}

	public void setAccounting(Accounting accounting) {
		this.accounting = accounting;
	}

	public Accounting getAccounting() {
		return accounting;
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
        transaction.setDescription(java.util.ResourceBundle.getBundle("Accounting").getString("AFSLUITEN_PROJECT"));
		ArrayList<Account> teVerwijderen = new ArrayList<Account>();

		BigDecimal totaalKost = new BigDecimal(0);
		Iterator<Account> it1 = rekeningen.getAccounts(AccountType.Cost).iterator();
		while (it1.hasNext()) {
			Account kost = it1.next();
			BigDecimal amount = kost.saldo();
			transaction.crediteer(kost, amount);
			totaalKost.add(amount);
			teVerwijderen.add(kost);
		}

		BigDecimal totaalOpbrengst = new BigDecimal(0);
		Iterator<Account> it2 = rekeningen.getAccounts(AccountType.Revenue).iterator();
		while (it2.hasNext()) {
			Account opbrengst = it2.next();
			BigDecimal amount = opbrengst.saldo();
			transaction.debiteer(opbrengst, amount);
			totaalOpbrengst.add(amount);
			teVerwijderen.add(opbrengst);
		}
		Account result;
		if (totaalOpbrengst.compareTo(totaalKost) > 0) {
			BigDecimal winst = totaalOpbrengst.subtract(totaalKost);
			winst = winst.setScale(2);
			result = new Account(java.util.ResourceBundle.getBundle("Accounting").getString(
					"WINST_PROJECT")
					+ naam, AccountType.Revenue);
			transaction.crediteer(result, winst);
		} else {
			BigDecimal verlies = totaalKost.subtract(totaalOpbrengst);
			verlies = verlies.setScale(2);
			result = new Account(java.util.ResourceBundle.getBundle("Accounting").getString(
					"VERLIES_PROJECT")
					+ naam, AccountType.Cost);
			transaction.debiteer(result, verlies);
		}
		result.setAccounting(accounting);
		accounting.getAccounts().add(result);
		// transaction.book() !!!
		for(int i = 0; i < teVerwijderen.size(); i++) {
			rekeningen.remove(teVerwijderen.get(i));
		}
		return result;
	}

	@Override
	public String toString() {
		return naam;
	}
}
