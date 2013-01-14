package be.dafke.Accounting.GUI.Details;

/**
 *
 * @author David Danneels
 */

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.RefreshableTable;

public class AccountDetails extends RefreshableTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountDetails(Account account) {
		super(java.util.ResourceBundle.getBundle("Accounting").getString("REKENING_DETAILS")
				+ account.toString(), new AccountDetailsDataModel(account));
		tabel.setAutoCreateRowSorter(true);
	}
}