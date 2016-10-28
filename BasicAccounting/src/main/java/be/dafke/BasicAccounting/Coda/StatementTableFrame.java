package be.dafke.BasicAccounting.Coda;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.BusinessModel.BankAccount;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.CounterParties;
import be.dafke.BusinessModel.CounterParty;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.SearchOptions;
import be.dafke.BusinessModel.Statement;
import be.dafke.BusinessModel.Statements;
import be.dafke.BusinessModel.Transaction;
import be.dafke.BusinessModelDao.CodaParser;
import be.dafke.BusinessModelDao.CsvParser;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ComponentModel.RefreshableTableFrame;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.Utils.Utils;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Point;
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

public class StatementTableFrame extends RefreshableTableFrame<Statement> implements ActionListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton viewCounterParties, exportToJournal, readCoda, readCsv, saveToAccounting;
    private final Statements statements;
    private final CounterParties counterParties;
    private final Accounting accounting;
//    private final Accountings accountings;

    public StatementTableFrame(Accountings accountings, Accounting accounting, Statements statements, CounterParties counterParties) {
		super("Statements", new StatementDataModel(statements));
//		this.accountings = accountings;
		this.statements = statements;
        this.counterParties = counterParties;
        this.accounting = accounting;
		// tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);
		tabel.addMouseListener(this);
		viewCounterParties = new JButton("View Counterparties");
		viewCounterParties.addActionListener(this);
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
		} else if (e.getSource() == viewCounterParties) {
//			Accounting accounting = accountings.getCurrentObject();
			BusinessCollection<BusinessObject> counterParties = accounting.getBusinessObject(CounterParties.COUNTERPARTIES);
			BusinessCollection<BusinessObject> statements = accounting.getBusinessObject(Statements.STATEMENTS);
			String key = accounting.toString() + CounterParties.COUNTERPARTIES;
			DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
			if (gui == null) {
				gui = new CounterPartyTableFrame(accounting, (CounterParties) counterParties, (Statements) statements);
				ComponentMap.addDisposableComponent(key, gui); // DETAILS
			}
			gui.setVisible(true);
		}
	}

	private void saveToAccounting() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectObject(Statement statement) {

	}

	@Override
	public Statement getSelectedObject() {
		return null;
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
            codaParser.parseFile(files, counterParties, statements);
        }
        refresh();
    }

	private void readCodaFiles() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File[] files = chooser.getSelectedFiles();
			CodaParser codaParser = new CodaParser();
			codaParser.parseFile(files, counterParties, statements);
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
		Set<CounterParty> set = new HashSet<>();
		List<BusinessObject> list = new ArrayList<>();
		for(int i : rows) {
			CounterParty counterParty = (CounterParty) tabel.getValueAt(i, 4);
			if (counterParty == null) {
				list.add(statements.getBusinessObjects().get(i));
			} else if (counterParty.getAccount() == null) {
				set.add(counterParty);
			}
		}
		if (!list.isEmpty()) {
			System.err.println(list.size() + " movements have no counterparty:");
			StringBuilder builder = new StringBuilder(list.size() + " movements have no counterparty:");
			for(BusinessObject statement : list) {
				System.err.println(statement);
				builder.append("\r\n").append(statement);
			}
			JOptionPane.showMessageDialog(this, builder.toString());
            SearchOptions searchOptions = new SearchOptions();
            searchOptions.searchForCounterParty(null);
			GenericStatementTableFrame gui = new GenericStatementTableFrame(searchOptions, statements);
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
            // TODO: this is an existing Action in CodaActionListener
            String key = accounting.toString()+ CounterParties.COUNTERPARTIES;
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
                        BusinessObject counterParty = (BusinessObject) tabel.getValueAt(i, 4);
                        Account account = ((CounterParty)counterParty).getAccount();
                        boolean debet = tabel.getValueAt(i, 2).equals("D");
                        if (account == null) {
                            BusinessObject counterParty2 = counterParties.getBusinessObject(counterParty.getName());
                            if (counterParty2 != null) {
                                counterParty = counterParty2;
                                account = ((CounterParty)counterParty2).getAccount();
                            }
                        }
                        while (account == null) {
                            account = (Account) JOptionPane.showInputDialog(this, "Select account", "Select account",
                                    JOptionPane.INFORMATION_MESSAGE, null, accounts, null);
                            ((CounterParty)counterParty).setAccount(account);
                        }
                        BigDecimal amount = (BigDecimal) tabel.getValueAt(i, 3);
                        Transaction transaction = accounting.getJournals().getCurrentObject().getCurrentObject();
                        Booking booking1 = new Booking(account, amount, debet);
                        Booking booking2 = new Booking(bankAccount, amount, !debet);
                        transaction.addBusinessObject(booking1);
                        transaction.addBusinessObject(booking2);
                        String cal = (String) tabel.getValueAt(i, 1);
                        Calendar date = Utils.toCalendar(cal);
                        transaction.setDate(date);
                        String description = (String) tabel.getValueAt(i, 6);
                        transaction.setDescription(description);
                        journal.addBusinessObject(transaction);

                        transaction = new Transaction(accounting.getAccounts(), date, "");
                        // take the same date as previous transaction
                        // leave the description empty

                        accounting.getJournals().getCurrentObject().setCurrentObject(transaction);
                    }
                }
			}
		}
		super.refresh();
	}

	public void mouseClicked(MouseEvent me) {
		Point cell = me.getPoint();
		if (me.getClickCount() == 1) {
			int col = tabel.columnAtPoint(cell);
			int row = tabel.rowAtPoint(cell);
			if (col == 4) {
                CounterParty counterParty = (CounterParty) tabel.getValueAt(row, col);
				if (counterParty == null) {
					CounterPartySelector sel = new CounterPartySelector((Statement)statements.getBusinessObjects().get(row), statements, counterParties);
					sel.setVisible(true);
					counterParty = sel.getSelection();
				}
				if (counterParty != null) {
					Statement statement = (Statement)statements.getBusinessObjects().get(row);
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
					RefreshableTableFrame refreshableTableFrame = new GenericStatementTableFrame(searchOptions, statements);
                    refreshableTableFrame.setVisible(true);
					// parent.addChildFrame(refreshableTable);
				}
			} else if (col == 5) {
				String transactionCode = (String) tabel.getValueAt(row, 6);
                SearchOptions searchOptions = new SearchOptions();
                searchOptions.searchForTransactionCode(transactionCode);
				RefreshableTableFrame refreshableTableFrame = new GenericStatementTableFrame(searchOptions, statements);
                refreshableTableFrame.setVisible(true);
				// parent.addChildFrame(refreshableTable);
            } else if (col == 6){
                String communication = (String) tabel.getValueAt(row, 7);
                SearchOptions searchOptions = new SearchOptions();
                searchOptions.searchForCommunication(communication);
                RefreshableTableFrame refreshableTableFrame = new GenericStatementTableFrame(searchOptions, statements);
                refreshableTableFrame.setVisible(true);
                // parent.addChildFrame(refreshableTable);
            }
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
