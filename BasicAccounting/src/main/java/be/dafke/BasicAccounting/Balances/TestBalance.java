package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.BalancePopupMenu;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.RefreshableTableFrame;

import javax.swing.JPopupMenu;

import static java.util.ResourceBundle.getBundle;

public class TestBalance extends RefreshableTableFrame<Account> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;

	public TestBalance(Journals journals, Accounts accounts, AccountTypes accountTypes) {
		super(getBundle("Balances").getString("TESTBALANCE"),
				new TestBalanceDataModel(accounts, accountTypes));
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);
		popup = new BalancePopupMenu(journals, tabel);
		tabel.addMouseListener(new PopupForTableActivator(popup,tabel));
	}

	@Override
	public void selectObject(Account account) {

	}

	@Override
	public Account getSelectedObject() {
		return null;
	}
}
