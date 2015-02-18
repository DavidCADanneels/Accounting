package be.dafke.Coda.GUI;

import be.dafke.BasicAccounting.GUI.InputWindows.AccountSelector;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.Coda.Objects.BankAccount;
import be.dafke.Coda.Objects.CounterParties;
import be.dafke.Coda.Objects.CounterParty;
import be.dafke.Coda.Objects.Statements;
import be.dafke.ComponentModel.RefreshableTableFrame;
import be.dafke.ComponentModel.RefreshableTableModel;
import be.dafke.ObjectModel.BusinessObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.regex.Pattern;

public class CounterPartyTableFrame extends RefreshableTableFrame<CounterParty> implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Accounting accounting;
    private final Statements statements;

    public CounterPartyTableFrame(Accounting accounting, CounterParties counterParties, Statements statements) {
		super("Counterparties (" + accounting.toString() + ")", new CounterPartyDataModel(counterParties));
		this.accounting = accounting;
        this.statements = statements;
		// tabel.setAutoCreateRowSorter(true);
		tabel.addMouseListener(this);
        tabel.setRowSorter(null);
	}

    @Override
	public void mouseClicked(MouseEvent me) {
		Point cell = me.getPoint();
//		Point location = me.getLocationOnScreen();
		if (me.getButton() == 3) {
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
				RefreshableTableFrame refreshTable = new GenericStatementTableFrame(searchOptions, statements);
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
                        ((RefreshableTableModel<BusinessObject>)tabel.getModel()).fireTableDataChanged();
                    }
                }
			} else if (col == 5) {
                AccountSelector sel = new AccountSelector(accounting);
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

    @Override
    public void selectObject(CounterParty counterParty) {

    }

    @Override
    public CounterParty getSelectedObject() {
        return null;
    }
}
