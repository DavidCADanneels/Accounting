package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Dao.Coda.CodaParser;
import be.dafke.Accounting.Dao.Coda.CsvParser;
import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.BankAccount;
import be.dafke.Accounting.Objects.Accounting.Booking;
import be.dafke.Accounting.Objects.Accounting.CounterParty;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Statement;
import be.dafke.Accounting.Objects.Accounting.Statements;
import be.dafke.Accounting.Objects.Accounting.Transaction;
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

public class StatementTable extends RefreshableTable implements ActionListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton viewCounterParties, exportToJournal, readCoda, readCsv, saveToAccounting;
	private final Accounting accounting;

	public StatementTable(Accounting accounting, ActionListener actionListener) {
		super("Statements (" + accounting.toString() + ")", new StatementDataModel(accounting));
		this.accounting = accounting;
		// tabel.setAutoCreateRowSorter(true);
		tabel.addMouseListener(this);
		viewCounterParties = new JButton("View Counterparties");
		viewCounterParties.addActionListener(actionListener);
        viewCounterParties.setActionCommand(ComponentMap.COUNTERPARTIES);
		readCoda = new JButton("Read Coda file(s)");
		readCoda.addActionListener(this);
        readCsv = new JButton("Read CSV file(s)");
        readCsv.addActionListener(this);
		saveToAccounting = new JButton("Save movements (all/selection)");
		saveToAccounting.addActionListener(this);
		JPanel north = new JPanel();
		north.add(readCoda);
        north.add(readCsv);
		north.add(viewCounterParties);
		getContentPane().add(north, BorderLayout.NORTH);
		exportToJournal = new JButton("Export selected movements to a Journal");
		// exportToJournal.setEnabled(false);
		exportToJournal.addActionListener(this);
//		JPanel south = new JPanel();
		getContentPane().add(exportToJournal, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == readCoda) {
			readCodaFiles();
		} else if (e.getSource() == readCsv) {
            readCsvFiles();
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

    private void readCsvFiles(){
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            CsvParser codaParser = new CsvParser();
            codaParser.parseFile(files, accounting);
        }
        refresh();
    }

	private void readCodaFiles() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File[] files = chooser.getSelectedFiles();
			CodaParser codaParser = new CodaParser();
			codaParser.parseFile(files, accounting);
		}
		refresh();
	}

	private boolean checkAccountAndSelection(int[] rows) {
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
		List<Statement> list = new ArrayList<Statement>();
		for(int i : rows) {
			CounterParty counterParty = (CounterParty) tabel.getValueAt(i, 5);
			if (counterParty == null) {
				Statements statements = accounting.getStatements();
				list.add(statements.getBusinessObjects().get(i));
			} else if (counterParty.getAccount() == null) {
				set.add(counterParty);
			}
		}
		if (!list.isEmpty()) {
			System.err.println(list.size() + " movements have no counterparty:");
			StringBuilder builder = new StringBuilder(list.size() + " movements have no counterparty:");
			for(Statement statement : list) {
				System.err.println(statement);
				builder.append("\r\n").append(statement);
			}
			JOptionPane.showMessageDialog(this, builder.toString());
            SearchOptions searchOptions = new SearchOptions();
            searchOptions.searchForCounterParty(null);
			GenericStatementTable gui = new GenericStatementTable(searchOptions, accounting);
			gui.setVisible(true);
			return false;
		}
		if (!set.isEmpty()) {
			System.err.println(set.size() + " counterparties have no account:");
			StringBuilder builder = new StringBuilder(set.size() + " counterparties have no account:");
			for(CounterParty counterParty : set) {
				System.err.println(counterParty);
				builder.append("\r\n").append(counterParty);
			}
			JOptionPane.showMessageDialog(this, builder.toString());
            // TODO: this is an existing Action in AccountingActionListener
            String key = accounting.toString()+ComponentMap.COUNTERPARTIES;
            ComponentMap.getDisposableComponent(key).setVisible(true);
            // until here
			return false;
		}
		return true;
	}

	private void exportToJournal() {
		// TODO save movements (and counterparties) into accounting [or with different button]
		int[] rows = tabel.getSelectedRows();
		if (checkAccountAndSelection(rows)) {
			if (checkCounterParties(rows)) {
				Object[] accounts = accounting.getAccounts().getBusinessObjects().toArray();
				Account bankAccount = (Account) JOptionPane.showInputDialog(this, "Select Bank account",
						"Select account", JOptionPane.INFORMATION_MESSAGE, null, accounts, null);
				Journal journal;
				Object[] journals = accounting.getJournals().getBusinessObjects().toArray();
				if (accounting.getJournals().getBusinessObjects().size() == 1) {
					journal = (Journal) journals[0];
				} else {
					journal = (Journal) JOptionPane.showInputDialog(this, "Select Journal", "Select journal",
							JOptionPane.INFORMATION_MESSAGE, null, journals, null);
				}
				if (bankAccount != null && journal != null) {
                    for(int i : rows) {
                        CounterParty counterParty = (CounterParty) tabel.getValueAt(i, 5);
                        Account account = counterParty.getAccount();
                        boolean debet = tabel.getValueAt(i, 3).equals("D");
                        if (account == null) {
                            CounterParty counterParty2 = accounting.getCounterParties().getBusinessObject(counterParty.getName());
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
                        Transaction transaction = accounting.getJournals().getCurrentJournal().getCurrentTransaction();
                        Booking booking1 = new Booking(account, amount, debet);
                        Booking booking2 = new Booking(bankAccount, amount, !debet);
                        transaction.addBooking(booking1);
                        transaction.addBooking(booking2);
                        String cal = (String) tabel.getValueAt(i, 2);
                        Calendar date = Utils.toCalendar(cal);
                        transaction.setDate(date);
                        String description = (String) tabel.getValueAt(i, 7);
                        transaction.setDescription(description);
                        journal.book(transaction);

                        transaction = new Transaction();
                        transaction.setDate(date); // take the same date as previous transaction
                        // leave the description empty

                        accounting.getJournals().getCurrentJournal().setCurrentTransaction(transaction);
                    }
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
				Statements statements = accounting.getStatements();
				CounterParty counterParty = (CounterParty) tabel.getValueAt(row, col);
				if (counterParty == null) {
					CounterPartySelector sel = new CounterPartySelector(statements.getBusinessObjects().get(row), accounting);
					sel.setVisible(true);
					counterParty = sel.getSelection();
				}
				if (counterParty != null) {
					Statement statement = statements.getBusinessObjects().get(row);
					statement.setCounterParty(counterParty);
					super.refresh();
					System.out.println(counterParty.getName());
					for(BankAccount account : counterParty.getBankAccounts().values()) {
						System.out.println(account.getAccountNumber());
						System.out.println(account.getBic());
						System.out.println(account.getCurrency());
					}
                    SearchOptions searchOptions = new SearchOptions();
                    searchOptions.searchForCounterParty(counterParty);
					RefreshableTable refreshableTable = new GenericStatementTable(searchOptions, accounting);
                    refreshableTable.setVisible(true);
					// parent.addChildFrame(refreshableTable);
				}
			} else if (col == 6) {
				String transactionCode = (String) tabel.getValueAt(row, 6);
                SearchOptions searchOptions = new SearchOptions();
                searchOptions.searchForTransactionCode(transactionCode);
				RefreshableTable refreshableTable = new GenericStatementTable(searchOptions, accounting);
                refreshableTable.setVisible(true);
				// parent.addChildFrame(refreshableTable);
            } else if (col == 7){
                String communication = (String) tabel.getValueAt(row, 7);
                SearchOptions searchOptions = new SearchOptions();
                searchOptions.searchForCommunication(communication);
                RefreshableTable refreshableTable = new GenericStatementTable(searchOptions, accounting);
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
