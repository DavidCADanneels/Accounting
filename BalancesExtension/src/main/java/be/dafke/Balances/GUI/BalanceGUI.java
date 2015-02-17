package be.dafke.Balances.GUI;

import be.dafke.Balances.Objects.Balance;
import be.dafke.BasicAccounting.Actions.BalancePopupMenu;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
		tabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Point cell = me.getPoint();//
				Point location = me.getLocationOnScreen();
				int col = tabel.columnAtPoint(cell);
				boolean clickable = (col == 0 || col == 2 || col == 3 || col == 4);
				if (clickable && me.getClickCount() == 2) {
					int row = tabel.rowAtPoint(cell);
					tabel.setSelectedRow(row);
					tabel.setSelectedColumn(col);
					popup.show(null, location.x, location.y);
				} else popup.setVisible(false);
			}
		});
	}
}
