package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Coda.BankAccount;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.RefreshableTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.regex.Pattern;

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
                SearchOptions searchOptions = new SearchOptions();
                searchOptions.setCounterParty(counterParty);
                searchOptions.setSearchOnCounterParty(true);
				RefreshableTable refreshTable = new GenericMovementTable(searchOptions, accountings);
                refreshTable.setVisible(true);
				// parent.addChildFrame(refreshTable);
            } else if (col == 1){
                String alias = (String) tabel.getValueAt(row, col);
                if(alias != null && !alias.equals("")){
                    String aliases[] = alias.split(Pattern.quote(" | "));
                    int result = JOptionPane.showOptionDialog(this,"Select new name", "Select new name",
                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE, null,aliases,aliases[0]);
                    if(result != JOptionPane.CLOSED_OPTION && result != JOptionPane.CANCEL_OPTION){
                        CounterParty counterParty = (CounterParty) tabel.getValueAt(row, 0);
                        String name = counterParty.getName();
                        counterParty.setName(aliases[result]);
                        counterParty.removeAlias(aliases[result]);
                        // TODO: ask user if old name should be saved as alias
                        counterParty.addAlias(name);
                        model.fireTableDataChanged();
                    }
                }
			} else if (col == 5) {
				boolean active = accountings.isActive();
				if (!active) {
					JOptionPane.showMessageDialog(this, "Open an accounting first");
				} else {
					AccountSelector sel = new AccountSelector(accountings.getCurrentAccounting());
					sel.setVisible(true);
					Account account = sel.getSelection();

					if (account != null) {
						CounterParty counterParty = (CounterParty) tabel.getValueAt(row, 0);
						counterParty.setAccount(account);
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
