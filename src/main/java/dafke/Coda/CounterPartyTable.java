package be.dafke.Coda;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import be.dafke.ParentFrame;
import be.dafke.RefreshableTable;
import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Accountings;

public class CounterPartyTable extends RefreshableTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static CounterPartyTable table;

	public static CounterPartyTable getInstance(ParentFrame parent) {
		if (table == null) {
			table = new CounterPartyTable(parent);
		}
		parent.addChildFrame(table);
		return table;
	}

	private CounterPartyTable(final ParentFrame parent) {
		super("Counterparties", new CounterPartyDataModel(), parent);
		// tabel.setAutoCreateRowSorter(true);
		tabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Point cell = me.getPoint();
//				Point location = me.getLocationOnScreen();
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
						RefreshableTable refreshTable = new GenericMovementTable(parent, counterParty, null);
						parent.addChildFrame(refreshTable);
					} else if (col == 4) {
						Account account = (Account) tabel.getValueAt(row, col);
						if (!Accountings.isActive()) {
							JOptionPane.showMessageDialog(parent, "Open an accounting first");
						} else {
							AccountSelector sel = new AccountSelector(parent);
							sel.setVisible(true);
							account = sel.getSelection();

							if (account != null) {
								CounterParty counterParty = (CounterParty) tabel.getValueAt(row, 0);
								counterParty.setAccount(account);
								CounterParties.getInstance().remove(counterParty.getName());
								CounterParties.getInstance().put(counterParty.getName(), counterParty);
								parent.repaintAllFrames();
							}
						}
					}

				}
			}
		});
	}
}
