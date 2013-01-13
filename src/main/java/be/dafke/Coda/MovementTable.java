package be.dafke.Coda;

import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting;
import be.dafke.Accounting.Objects.Accountings;
import be.dafke.Accounting.Objects.Journal;
import be.dafke.Accounting.Objects.Transaction;
import be.dafke.Coda.Objects.Movement;
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
import java.util.ResourceBundle;
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
			CounterPartyTable gui = new CounterPartyTable(accountings);
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
		//CounterParties counterParties = accountings.getCurrentAccounting().getCounterParties();
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File[] files = chooser.getSelectedFiles();
			CodaParser codaParser = new CodaParser();
			codaParser.parseFile(files, accountings);
			// Movements movements = accountings.getCurrentAccounting().getMovements();
			// TODO: remove movements from MovementTable constructor
			MovementTable gui = new MovementTable(accountings);
			gui.setVisible(true);
			gui.refresh();
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
			CounterPartyTable gui = new CounterPartyTable(accountings);
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
			CounterPartyTable gui = new CounterPartyTable(accountings);
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
				// Step 3:
				// TODO: ask to make accounts automatically
				// If yes ...
				boolean auto = true;
				JOptionPane.showMessageDialog(this, "Auto-create accounts");
				for(int i : rows) {
					CounterParty counterParty = (CounterParty) tabel.getValueAt(i, 5);
					Account account = counterParty.getAccount();
					boolean debet = tabel.getValueAt(i, 3).equals("D");
					if (account == null) {
						CounterParty counterParty2 = accounting.getCounterParties().get(counterParty.getName());
						if (counterParty2 != null) {
							counterParty = counterParty2;
							account = counterParty2.getAccount();
						}
					}
					if (account == null) {
						if (auto) {
							if (debet) {
								account = new Account(counterParty.getName(), AccountType.Credit);
							} else {
								account = new Account(counterParty.getName(), AccountType.Debit);
							}
							account.setAccounting(accounting);
						} else {
							account = (Account) JOptionPane.showInputDialog(this, "Select account", "Select account",
									JOptionPane.INFORMATION_MESSAGE, null, accounts, null);
						}
						counterParty.setAccount(account);
						accounting.getAccounts().add(account);
						account.setAccounting(accounting);
					}
					BigDecimal amount = (BigDecimal) tabel.getValueAt(i, 4);
					Transaction trans = Transaction.getInstance();
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
					Transaction.newInstance(date, description);
				}
			}
		}
		super.refresh();
	}

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
					CounterPartySelector sel = new CounterPartySelector(this, movements.getMovement(row), accountings);
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
					RefreshableTable refreshableTable = new GenericMovementTable(counterParty, null, true, accountings);
					// parent.addChildFrame(refreshableTable);
				}
			} else if (col == 6) {
				String transactionCode = (String) tabel.getValueAt(row, 6);
				System.out.println(transactionCode);
				System.out.println(ResourceBundle.getBundle("CODA").getString(transactionCode));
				RefreshableTable refreshableTable = new GenericMovementTable(null, transactionCode, true, accountings); // false
																														// ???
				// parent.addChildFrame(refreshableTable);
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}