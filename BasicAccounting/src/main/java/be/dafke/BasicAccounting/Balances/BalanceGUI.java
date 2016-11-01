package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.BalancePopupMenu;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.Balance;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.RefreshableTableFrame;

import javax.swing.*;

public class BalanceGUI extends RefreshableTableFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;

	public BalanceGUI(Journals journals, Balance balance) {
		super(balance.getName(),
				new BalanceDataModel(balance));
		//TODO: avoid creating datamodel in constructor via super()
		// tabel.setAutoCreateRowSorter(true);
		popup = new BalancePopupMenu(journals, tabel);
		tabel.addMouseListener(new PopupForTableActivator(popup,tabel));
	}
}
