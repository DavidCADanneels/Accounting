package be.dafke.Accounting.GUI.Details;

/**
 *
 * @author David Danneels
 */

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.RefreshableTable;

import static java.util.ResourceBundle.getBundle;

public class AccountDetails extends RefreshableTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountDetails(Account account, Accounting accounting) {
		super(getBundle("Accounting").getString("REKENING_DETAILS")
                + accounting.toString()+"/"+ account.toString(), new AccountDetailsDataModel(account));
		tabel.setAutoCreateRowSorter(true);
	}
}