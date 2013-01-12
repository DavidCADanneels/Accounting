package be.dafke.Coda;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import be.dafke.ParentFrame;
import be.dafke.RefreshableTable;
import be.dafke.Utils;
import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accountings;
import be.dafke.Accounting.Objects.Journal;
import be.dafke.Accounting.Objects.Transaction;
import be.dafke.Coda.Objects.Movement;

public class MovementTable extends RefreshableTable implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton viewCounterParties, exportToJournal, openMovements;
	// private final RefreshableFrame counterPartyTable = null;
//	private final AccountingGUIFrame parent;

	private static MovementTable table;

	public static MovementTable getInstance(ParentFrame parent) {
		if (table == null) {
			table = new MovementTable(parent);
		}
		parent.addChildFrame(table);
		return table;
	}

	private MovementTable(final ParentFrame parent) {
		super("Movements", new MovementDataModel(), parent);
		// tabel.setAutoCreateRowSorter(true);
		this.parent = parent;
		tabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Point cell = me.getPoint();//
				// Point location = me.getLocationOnScreen();
				if (me.getClickCount() == 2) {
					int col = tabel.columnAtPoint(cell);
					int row = tabel.rowAtPoint(cell);
					if (col == 5) {
						CounterParty counterParty = (CounterParty) tabel.getValueAt(row, col);
						if (counterParty == null) {
							CounterPartySelector sel = new CounterPartySelector(parent, Movements.getMovement(row));
							sel.setVisible(true);
							counterParty = sel.getSelection();
						}
						if (counterParty != null) {
							Movement movement = Movements.getMovement(row);
							movement.setCounterParty(counterParty);
							parent.repaintAllFrames();
							System.out.println(counterParty.getName());
							for(BankAccount account : counterParty.getBankAccounts().values()) {
								System.out.println(account.getAccountNumber());
								System.out.println(account.getBic());
								System.out.println(account.getCurrency());
							}
							RefreshableTable refreshableTable = new GenericMovementTable(parent, counterParty, null);
							parent.addChildFrame(refreshableTable);
						}
					} else if (col == 6) {
						String transactionCode = (String) tabel.getValueAt(row, 6);
						System.out.println(transactionCode);
						System.out.println(ResourceBundle.getBundle("be/dafke/Coda/Bundle").getString(transactionCode));
						RefreshableTable refreshableTable = new GenericMovementTable(parent, null, transactionCode);
						parent.addChildFrame(refreshableTable);
					}
				}
			}
		});
		viewCounterParties = new JButton("View Counterparties");
		viewCounterParties.addActionListener(this);
		openMovements = new JButton("Read Coda file(s)");
		openMovements.addActionListener(this);
		JPanel north = new JPanel();
		north.add(viewCounterParties);
		north.add(openMovements);
		getContentPane().add(north, BorderLayout.NORTH);
		exportToJournal = new JButton("Export selected movements to a Journal");
		// exportToJournal.setEnabled(false);
		exportToJournal.addActionListener(this);
		getContentPane().add(exportToJournal, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == viewCounterParties) {
			CounterPartyTable.getInstance(parent).setVisible(true);
		} else if (e.getSource() == openMovements) {
			openMovements();
		} else if (e.getSource() == exportToJournal) {
			exportToJournal();
		}
	}

	private void openMovements() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File[] files = chooser.getSelectedFiles();
			CodaParser codaParser = new CodaParser();
			codaParser.parseFile(files);
			MovementTable.getInstance(parent).setVisible(true);
			MovementTable.getInstance(parent).refresh();
		}
		parent.repaintAllFrames();
	}

	private boolean checkAccountAndSelection(int[] rows) {
		if (Accountings.getCurrentAccounting() == null) {
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
				list.add(Movements.getMovement(i));
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
			CounterPartyTable.getInstance(parent).setVisible(true);
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
			CounterPartyTable.getInstance(parent).setVisible(true);
			return false;
		}
		return true;
	}

	private void exportToJournal() {
		int[] rows = tabel.getSelectedRows();
		if (checkAccountAndSelection(rows)) {
			if (checkCounterParties(rows)) {
				Object[] accounts = Accountings.getCurrentAccounting().getAccounts().values().toArray();
				Account bankAccount = (Account) JOptionPane.showInputDialog(parent, "Select Bank account",
						"Select account", JOptionPane.INFORMATION_MESSAGE, null, accounts, null);
				Journal journal;
				Object[] journals = Accountings.getCurrentAccounting().getJournals().values().toArray();
				if (Accountings.getCurrentAccounting().getJournals().size() == 1) {
					journal = (Journal) journals[0];
				} else {
					journal = (Journal) JOptionPane.showInputDialog(parent, "Select Journal", "Select journal",
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
						CounterParty counterParty2 = CounterParties.getInstance().get(counterParty.getName());
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
						} else {
							account = (Account) JOptionPane.showInputDialog(parent, "Select account", "Select account",
									JOptionPane.INFORMATION_MESSAGE, null, accounts, null);
						}
						counterParty.setAccount(account);
						Accountings.getCurrentAccounting().getAccounts().add(account);
						account.setAccounting(Accountings.getCurrentAccounting());
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
		parent.repaintAllFrames();
	}
}
