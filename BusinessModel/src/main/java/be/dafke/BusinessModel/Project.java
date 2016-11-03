package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.MustBeRead;

import java.util.Properties;
import java.util.TreeMap;

/**
 * @author David Danneels
 */
public class Project extends BusinessCollection<Account> implements MustBeRead {
	/**
	 * 
	 */
	private final Accounts allAccounts;  // needed to lookup existing accounts when adding them to the project
	private final Accounting accounting;

	@Override
	public String getChildType() {
		return "Account";
	}

	public Project(String name, Accounting accounting) {
		setName(name);
		this.accounting = accounting;
		allAccounts = accounting.getAccounts();
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
	public void removeBusinessObject(Account account){
		removeBusinessObject(account.getUniqueProperties());
	}

	public ProjectJournal getJournal() {
		ProjectJournal journal = new ProjectJournal(accounting, getName(), "TMP");
		for(Account account:getBusinessObjects()){
			for(Movement movement :account.getBusinessObjects()){
				Transaction transaction = movement.getBooking().getTransaction();
				journal.addBusinessObject(transaction);
			}
		}
		return journal;
	}

//	public ArrayList<Transaction> getTransactions() {
//		ArrayList<Transaction>  transactions = new ArrayList<> ();
//		for(Account account:getBusinessObjects()){
//			for(Movement movement :account.getBusinessObjects()){
//				Transaction transaction = movement.getBooking().getTransaction();
//				transactions.add(transaction);
//			}
//		}
//		return transactions;
//	}


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
