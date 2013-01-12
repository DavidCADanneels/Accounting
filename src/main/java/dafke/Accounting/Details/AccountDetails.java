package be.dafke.Accounting.Details;

/**
 *
 * @author David Danneels
 */

import be.dafke.ParentFrame;
import be.dafke.RefreshableTable;
import be.dafke.Accounting.Objects.Account;

public class AccountDetails extends RefreshableTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountDetails(Account account, ParentFrame parent) {
		super(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("REKENING_DETAILS")
				+ account.toString(), new AccountDetailsDataModel(account, parent), parent);
		tabel.setAutoCreateRowSorter(true);
	}
}