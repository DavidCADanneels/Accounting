package be.dafke.Balances.GUI;

import be.dafke.Balances.Objects.Balance;
import be.dafke.BasicAccounting.Actions.BalancePopupMenu;
import be.dafke.BasicAccounting.Actions.PopupForTableActivator;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;

import javax.swing.*;

public class BalanceGUI extends RefreshableBalanceFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;

	public BalanceGUI(Accountings accountings, Accounting accounting, Balance balance) {
		super(balance.getName() + " (" + accounting.toString() + ")",
				new BalanceDataModel(balance));
		// tabel.setAutoCreateRowSorter(true);
		popup = new BalancePopupMenu(accountings, accounting, tabel);
		tabel.addMouseListener(new PopupForTableActivator(popup,tabel));
	}
}
