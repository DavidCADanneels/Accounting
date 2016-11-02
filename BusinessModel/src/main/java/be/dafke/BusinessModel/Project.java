package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.MustBeRead;

import java.util.ArrayList;
import java.util.Properties;
import java.util.TreeMap;

/**
 * @author David Danneels
 */
public class Project extends BusinessCollection<Account> implements MustBeRead {
	/**
	 * 
	 */
	private final Accounts allAccounts;
	private final ArrayList<Account> projectAccounts;
	private final ArrayList<Transaction> transactions;

	@Override
	public String getChildType() {
		return "Account";
	}

	public Project(String name, Accounts accounts) {
		setName(name);
		this.allAccounts = accounts;
		projectAccounts = new ArrayList<>();
		transactions = new ArrayList<>();
	}

	@Override
	public Account createNewChild(TreeMap<String, String> properties) {
		return allAccounts.getBusinessObject(properties.get(NAME));
	}

	@Override
	public Properties getOutputProperties() {
		Properties outputMap = new Properties();
		outputMap.put(NAME,getName());
		return outputMap;
	}

	@Override
	public Account addBusinessObject(Account account) throws EmptyNameException, DuplicateNameException {
		super.addBusinessObject(account);
		for(Movement movement :account.getBusinessObjects()){
			Transaction transaction = movement.getBooking().getTransaction();
			if(!transactions.contains(transaction)){
				transactions.add(transaction);
			}
		}
		return account;
	}

	public ArrayList<Transaction> getTransactions() {
		return transactions;
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
}
