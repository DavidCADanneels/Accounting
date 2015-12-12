package be.dafke.BasicAccounting.GUI.Balances;

import be.dafke.BasicAccounting.Objects.Balance;
import be.dafke.BasicAccounting.Actions.BalancePopupMenu;
import be.dafke.BasicAccounting.Actions.PopupForTableActivator;
import be.dafke.BasicAccounting.Objects.Journals;

import javax.swing.JPopupMenu;

public class BalanceGUI extends RefreshableBalanceFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;

	public BalanceGUI(Journals journals, Balance balance) {
		super(balance.getName(),
				new BalanceDataModel(balance));
		// tabel.setAutoCreateRowSorter(true);
		popup = new BalancePopupMenu(journals, tabel);
		tabel.addMouseListener(new PopupForTableActivator(popup,tabel));
	}
}
