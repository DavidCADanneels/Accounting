package be.dafke.BasicAccounting.GUI.Details;

/**
 *
 * @author David Danneels
 */

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.RefreshableTable;

import static java.util.ResourceBundle.getBundle;

public class AccountDetails extends RefreshableTable {

	public AccountDetails(Account account, Accounting accounting) {
		super(accounting.toString() + "/" +
                getBundle("Accounting").getString("ACCOUNT_DETAILS") + "/"
                + account.getName(), new AccountDetailsDataModel(account));
		tabel.setAutoCreateRowSorter(true);
	}
}