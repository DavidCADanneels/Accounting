package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Dao.Coda.CodaParser;
import be.dafke.Accounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Transaction;
import be.dafke.Accounting.Objects.Coda.BankAccount;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.Accounting.Objects.Coda.Movement;
import be.dafke.Accounting.Objects.Coda.Movements;
import be.dafke.RefreshableComponent;
import be.dafke.RefreshableTable;
import be.dafke.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovementTable extends RefreshableTable implements ActionListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton viewCounterParties, exportToJournal, openMovements, saveToAccounting;
	private final Accountings accountings;

	public MovementTable(Accountings accountings) {
		super("Movements", new MovementDataModel(accountings));
		this.accountings = accountings;
		// tabel.setAutoCreateRowSorter(true);
		tabel.addMouseListener(this);
		viewCounterParties = new JButton("View Counterparties");
		viewCounterParties.addActionListener(this);
		openMovements = new JButton("Read Coda file(s)");
		openMovements.addActionListener(this);
		saveToAccounting = new JButton("Save movements (all/selection)");
		saveToAccounting.addActionListener(this);
		JPanel north = new JPanel();
		north.add(openMovements);
		north.add(viewCounterParties);
		getContentPane().add(north, BorderLayout.NORTH);
		exportToJournal = new JButton("Export selected movements to a Journal");
		// exportToJournal.setEnabled(false);
		exportToJournal.addActionListener(this);
//		JPanel south = new JPanel();
		getContentPane().add(exportToJournal, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == viewCounterParties) {
			RefreshableComponent gui = AccountingMenuBar.getFrame(AccountingMenuBar.OPEN_COUNTERPARTIES);
			gui.setVisible(true);
		} else if (e.getSource() == openMovements) {
			openMovements();
		} else if (e.getSource() == exportToJournal) {
			exportToJournal();
		} else if (e.getSource() == saveToAccounting) {
			saveToAccounting();
		}
	}

	private void saveToAccounting() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh() {
		super.refresh();
//		if (checkCounterParties(null)) {
//
//		}
//		if (checkAccountAndSelection(null)) {
//
//		}
	}

	private void openMovements() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File[] files = chooser.getSelectedFiles();
			CodaParser codaParser = new CodaParser();
			codaParser.parseFile(files, accountings);
		}
		refresh();
	}

	private boolean checkAccountAndSelection(int[] rows) {
		Accounting accounting = accountings.getCurrentAccounting();
		if (accounting == null) {
			JOptionPane.showMessageDialog(this, "Open an accounting first");
			return false;
		}
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(this, "Select some lines first");
			return false;
		}
		return true;
	}

	private boolean checkCounterParties(int[] rows) {
		// Step 1: check if each movement has a counterparty
		// ok, because transactioncode is used
		// TODO: not use transactioncodes as counterparty,
		// but as the user for input.
		JOptionPane.showMessageDialog(this, "TODO: link transactioncode + extra data to counterparties");
		Set<CounterParty> set = new HashSet<CounterParty>();
		List<Movement> list = new ArrayList<Movement>();
		for(int i : rows) {
			CounterParty counterParty = (CounterParty) tabel.getValueAt(i, 5);
			if (counterParty == null) {
				Movements movements = accountings.getCurrentAccounting().getMovements();
				list.add(movements.getMovement(i));
			} else if (counterParty.getAccount() == null) {
				set.add(counterParty);
			}
		}
		if (!list.isEmpty()) {
			System.err.println(list.size() + " movements have no counterparty:");
			StringBuilder builder = new StringBuilder(list.size() + " movements have no counterparty:");
//			for(Movement movement : list) {
//				System.err.println(movement);
//				builder.append("\r\n" + movement);
//			}
			JOptionPane.showMessageDialog(this, builder.toString());
            SearchOptions searchOptions = new SearchOptions();
            searchOptions.searchForCounterParty(null);
			GenericMovementTable gui = new GenericMovementTable(searchOptions, accountings);
			gui.setVisible(true);
			return false;
		}
		if (!set.isEmpty()) {
			System.err.println(set.size() + " counterparties have no account:");
			StringBuilder builder = new StringBuilder(set.size() + " counterparties have no account:");
			for(CounterParty counterParty : set) {
				System.err.println(counterParty);
				builder.append("\r\n" + counterParty);
			}
			JOptionPane.showMessageDialog(this, builder.toString());
            RefreshableComponent gui = AccountingMenuBar.getFrame(AccountingMenuBar.OPEN_COUNTERPARTIES);
			gui.setVisible(true);
			return false;
		}
		return true;
	}

	private void exportToJournal() {
		// TODO save movements (and counterparties) into accounting [or with different button]
		Accounting accounting = accountings.getCurrentAccounting();
		int[] rows = tabel.getSelectedRows();
		if (checkAccountAndSelection(rows)) {
			if (checkCounterParties(rows)) {
				Object[] accounts = accounting.getAccounts().values().toArray();
				Account bankAccount = (Account) JOptionPane.showInputDialog(this, "Select Bank account",
						"Select account", JOptionPane.INFORMATION_MESSAGE, null, accounts, null);
				Journal journal;
				Object[] journals = accounting.getJournals().values().toArray();
				if (accounting.getJournals().size() == 1) {
					journal = (Journal) journals[0];
				} else {
					journal = (Journal) JOptionPane.showInputDialog(this, "Select Journal", "Select journal",
							JOptionPane.INFORMATION_MESSAGE, null, journals, null);
				}
				if (bankAccount != null && journal != null) {
					// TODO null checks
				}
				for(int i : rows) {
					CounterParty counterParty = (CounterParty) tabel.getValueAt(i, 5);
					Account account = counterParty.getAccount();
					boolean debet = tabel.getValueAt(i, 3).equals("D");
					if (account == null) {
						CounterParty counterParty2 = accounting.getCounterParties().getCounterPartyByName(counterParty.getName());
						if (counterParty2 != null) {
							counterParty = counterParty2;
							account = counterParty2.getAccount();
						}
					}
					while (account == null) {
                        account = (Account) JOptionPane.showInputDialog(this, "Select account", "Select account",
                                JOptionPane.INFORMATION_MESSAGE, null, accounts, null);
						counterParty.setAccount(account);
					}
					BigDecimal amount = (BigDecimal) tabel.getValueAt(i, 4);
					Transaction trans = accounting.getCurrentTransaction();
					if (debet) {
						trans.debiteer(account, amount);
						trans.crediteer(bankAccount, amount);
					} else {
						trans.debiteer(bankAccount, amount);
						trans.crediteer(account, amount);
					}
					String cal = (String) tabel.getValueAt(i, 2);
					Calendar date = Utils.toCalendar(cal);
					trans.setDate(date);
					String description = (String) tabel.getValueAt(i, 7);
					trans.setDescription(description);
					trans.book(journal);

                    trans = new Transaction();
                    trans.setDate(date); // take the same date as previous transaction
                    // leave the description empty

                    accounting.setCurrentTransaction(trans);
				}
			}
		}
		super.refresh();
	}

    @Override
	public void mouseClicked(MouseEvent me) {
		Point cell = me.getPoint();//
		// Point location = me.getLocationOnScreen();
		if (me.getClickCount() == 2) {
			int col = tabel.columnAtPoint(cell);
			int row = tabel.rowAtPoint(cell);
			if (col == 5) {
				Movements movements = accountings.getCurrentAccounting().getMovements();
				CounterParty counterParty = (CounterParty) tabel.getValueAt(row, col);
				if (counterParty == null) {
					CounterPartySelector sel = new CounterPartySelector(movements.getMovement(row), accountings);
					sel.setVisible(true);
					counterParty = sel.getSelection();
				}
				if (counterParty != null) {
					Movement movement = movements.getMovement(row);
					movement.setCounterParty(counterParty);
					super.refresh();
					System.out.println(counterParty.getName());
					for(BankAccount account : counterParty.getBankAccounts().values()) {
						System.out.println(account.getAccountNumber());
						System.out.println(account.getBic());
						System.out.println(account.getCurrency());
					}
                    SearchOptions searchOptions = new SearchOptions();
                    searchOptions.searchForCounterParty(counterParty);
					RefreshableTable refreshableTable = new GenericMovementTable(searchOptions, accountings);
                    refreshableTable.setVisible(true);
					// parent.addChildFrame(refreshableTable);
				}
			} else if (col == 6) {
				String transactionCode = (String) tabel.getValueAt(row, 6);
                SearchOptions searchOptions = new SearchOptions();
                searchOptions.searchForTransactionCode(transactionCode);
				RefreshableTable refreshableTable = new GenericMovementTable(searchOptions, accountings);
                refreshableTable.setVisible(true);
				// parent.addChildFrame(refreshableTable);
            } else if (col == 7){
                String communication = (String) tabel.getValueAt(row, 7);
                SearchOptions searchOptions = new SearchOptions();
                searchOptions.searchForCommunication(communication);
                RefreshableTable refreshableTable = new GenericMovementTable(searchOptions, accountings);
                refreshableTable.setVisible(true);
                // parent.addChildFrame(refreshableTable);
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
