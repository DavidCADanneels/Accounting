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

	public AccountDetails(String title, Account account) {
		super(title, new AccountDetailsDataModel(account));
		tabel.setAutoCreateRowSorter(true);
	}
}