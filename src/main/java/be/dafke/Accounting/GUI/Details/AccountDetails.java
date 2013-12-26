package be.dafke.Accounting.GUI.Details;

/**
 *
 * @author David Danneels
 */

import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Accounting;
import be.dafke.ComponentModel.RefreshableTable;

import static java.util.ResourceBundle.getBundle;

public class AccountDetails extends RefreshableTable {

	public AccountDetails(Account account, Accounting accounting) {
		super(accounting.toString() + "/" +
                getBundle("Accounting").getString("REKENING_DETAILS") + "/"
                + account.getName(), new AccountDetailsDataModel(account));
		tabel.setAutoCreateRowSorter(true);
	}
}