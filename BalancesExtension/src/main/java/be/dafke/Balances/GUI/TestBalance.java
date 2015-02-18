package be.dafke.Balances.GUI;

import be.dafke.BasicAccounting.Actions.BalancePopupMenu;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.RefreshableTableFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.util.ResourceBundle.getBundle;

public class TestBalance extends RefreshableTableFrame<Account> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;

	public TestBalance(Accountings accountings, Accounting accounting) {
		super(getBundle("Balances").getString("TESTBALANCE") + " (" + accounting.toString() + ")",
				new TestBalanceDataModel(accounting));
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);
		popup = new BalancePopupMenu(accountings, accounting, tabel);
		tabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Point cell = me.getPoint();//
				Point location = me.getLocationOnScreen();
				int col = tabel.columnAtPoint(cell);
				if (me.getButton() == 3) {
					int row = tabel.rowAtPoint(cell);
					tabel.setSelectedRow(row);
					tabel.setSelectedColumn(col);
					popup.show(null, location.x, location.y);
				} else popup.setVisible(false);
			}
		});
	}

	@Override
	public void selectObject(Account account) {

	}

	@Override
	public Account getSelectedObject() {
		return null;
	}
}
