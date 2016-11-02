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
	private final Accounts accounts;

	@Override
	public String getChildType() {
		return "Account";
	}

	public Project(Accounts accounts) {
		this.accounts = accounts;
	}

	@Override
	public Account createNewChild(TreeMap<String, String> properties) {
		return accounts.getBusinessObject(properties.get(NAME));
	}

	@Override
	public Properties getOutputProperties() {
		Properties outputMap = new Properties();
		outputMap.put(NAME,getName());
		return outputMap;
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
