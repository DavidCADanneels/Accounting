package be.dafke.BasicAccounting.Balances;

import be.dafke.BusinessModel.Balance;
import be.dafke.BasicAccounting.BalancePopupMenu;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.Journals;

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
