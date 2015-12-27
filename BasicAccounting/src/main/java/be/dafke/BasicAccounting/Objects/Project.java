package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessObject;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author David Danneels
 */
public class Project extends BusinessObject{
	/**
	 * 
	 */
	private final String naam;
	private final HashMap<String, Account> rekeningen;

	public Project(String name) {
		naam = name;
		rekeningen = new HashMap<String, Account>();
	}

	public void addAccount(Account account) {
		rekeningen.put(account.toString(),account);
	}

	public void removeAccount(Account account) {
		rekeningen.remove(account.toString());
	}

	public Collection<Account> getAccounts() {
		return rekeningen.values();
	}

//	public Account close() {
//		Transaction transaction = new Transaction();
//        transaction.setDate(Calendar.getInstance());
//        transaction.setDescription(getBundle("Projects").getString("CLOSE_PROJECT"));
//		ArrayList<Account> teVerwijderen = new ArrayList<Account>();
//
//		BigDecimal totaalKost = new BigDecimal(0);
//		for (Account kost : rekeningen.getAccounts(AccountType.Cost)) {
//			BigDecimal amount = kost.getSaldo();
//			transaction.addBooking(kost, amount,false,false);
//			totaalKost = totaalKost.add(amount);
//			teVerwijderen.add(kost);
//		}
//
//		BigDecimal totaalOpbrengst = new BigDecimal(0);
//		for (Account opbrengst : rekeningen.getAccounts(AccountType.Revenue)) {
//			BigDecimal amount = opbrengst.getSaldo();
//			transaction.addBooking(opbrengst,amount,true,false);
//			totaalOpbrengst = totaalOpbrengst.add(amount);
//			teVerwijderen.add(opbrengst);
//		}
//		Account result = null;
//        // TODO ask user to provide a Name instead of autocreating --> catch Exception from creation
////		if (totaalOpbrengst.compareTo(totaalKost) > 0) {
////			BigDecimal winst = totaalOpbrengst.subtract(totaalKost);
////			winst = winst.setScale(2);
////			result = accounting.getBusinessObjects().add(getBundle("Projects").getString("GAIN_PROJECT") + naam, AccountType.Revenue);
////			transaction.crediteer(result, winst);
////		} else {
////			BigDecimal verlies = totaalKost.subtract(totaalOpbrengst);
////			verlies = verlies.setScale(2);
////			result = accounting.getBusinessObjects().add(getBundle("Projects").getString("LOSS_PROJECT") + naam, AccountType.Cost);
////			transaction.debiteer(result, verlies);
////		}
//		// TODO: transaction.addBusinessObject() !!!
//		for(Account account : teVerwijderen) {
//			rekeningen.remove(account);
//		}
//		return result;
//	}

	@Override
	public String toString() {
		return naam;
	}
}
