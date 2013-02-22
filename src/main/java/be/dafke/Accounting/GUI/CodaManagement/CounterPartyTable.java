package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Coda.BankAccount;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.RefreshableTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CounterPartyTable extends RefreshableTable implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Accountings accountings;

	public CounterPartyTable(Accountings accountings) {
		super("Counterparties", new CounterPartyDataModel(accountings));
		this.accountings = accountings;
		// tabel.setAutoCreateRowSorter(true);
		tabel.addMouseListener(this);
	}

    @Override
	public void mouseClicked(MouseEvent me) {
		Point cell = me.getPoint();
//		Point location = me.getLocationOnScreen();
		if (me.getClickCount() == 2) {
			int col = tabel.columnAtPoint(cell);
			int row = tabel.rowAtPoint(cell);
			if (col == 0) {
				CounterParty counterParty = (CounterParty) tabel.getValueAt(row, col);
				System.out.println(counterParty.getName());
				for(BankAccount account : counterParty.getBankAccounts().values()) {
					System.out.println(account.getAccountNumber());
					System.out.println(account.getBic());
					System.out.println(account.getCurrency());
				}
				RefreshableTable refreshTable = new GenericMovementTable(counterParty, null, true, accountings);
				// parent.addChildFrame(refreshTable);
			} else if (col == 4) {
				Account account = (Account) tabel.getValueAt(row, col);
				boolean active = accountings.isActive();
				if (!active) {
					JOptionPane.showMessageDialog(this, "Open an accounting first");
				} else {
					Accounting accounting = accountings.getCurrentAccounting();
					AccountSelector sel = new AccountSelector(accountings);
					sel.setVisible(true);
					account = sel.getSelection();

					if (account != null) {
						CounterParty counterParty = (CounterParty) tabel.getValueAt(row, 0);
						counterParty.setAccount(account);
//						CounterParties counterParties = accounting.getCounterParties();
//						counterParties.remove(counterParty.getName());
//						counterParties.put(counterParty.getName(), counterParty);
						super.refresh();
					}
				}
			}

		}
	}

    @Override
    public void mouseEntered(MouseEvent e) {
	}

    @Override
	public void mouseExited(MouseEvent e) {
	}

    @Override
	public void mousePressed(MouseEvent e) {
	}

    @Override
	public void mouseReleased(MouseEvent e) {
	}
}
