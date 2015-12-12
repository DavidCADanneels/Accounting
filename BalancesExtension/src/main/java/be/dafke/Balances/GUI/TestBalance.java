package be.dafke.Balances.GUI;

import be.dafke.BasicAccounting.Actions.BalancePopupMenu;
import be.dafke.BasicAccounting.Actions.PopupForTableActivator;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.BasicAccounting.Objects.Journals;
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
