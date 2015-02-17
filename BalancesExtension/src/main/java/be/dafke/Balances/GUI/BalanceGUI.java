package be.dafke.Balances.GUI;

import be.dafke.Balances.Objects.Balance;
import be.dafke.BasicAccounting.Actions.BalancePopupMenu;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BalanceGUI extends RefreshableBalance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;
	private int selectedRow;
	private int selectedColumn;

	public BalanceGUI(Accountings accountings, Accounting accounting, Balance balance) {
		super(balance.getName() + " (" + accounting.toString() + ")",
				new BalanceDataModel(balance));
		// tabel.setAutoCreateRowSorter(true);
		popup = new BalancePopupMenu(accountings, accounting, this);
		tabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Point cell = me.getPoint();//
				Point location = me.getLocationOnScreen();
				int col = tabel.columnAtPoint(cell);
				boolean clickable = (col == 0 || col == 2 || col == 3 || col == 4);
				if (clickable && me.getClickCount() == 2) {
					selectedRow = tabel.rowAtPoint(cell);
					selectedColumn = tabel.columnAtPoint(cell);
					popup.show(null, location.x, location.y);
				} else popup.setVisible(false);
			}
		});
	}

	@Override
	public void selectObject(Account account) {

	}

	@Override
	public Account getSelectedObject(){
		if(selectedColumn == 0 || selectedColumn == 1) {
			return (Account)(tabel.getModel()).getValueAt(selectedRow, 0);
		}else {
			return (Account)(tabel.getModel()).getValueAt(selectedRow, 3);
		}
	}
}
