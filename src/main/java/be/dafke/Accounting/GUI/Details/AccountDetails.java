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

	public AccountDetails(Account account, Accounting accounting) {
		super(accounting.toString() + "/" +
                getBundle("Accounting").getString("REKENING_DETAILS") + "/"
                + account.getName(), new AccountDetailsDataModel(account));
		tabel.setAutoCreateRowSorter(true);
	}
}