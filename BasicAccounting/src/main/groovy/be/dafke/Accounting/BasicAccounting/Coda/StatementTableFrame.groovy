package be.dafke.Accounting.BasicAccounting.Coda

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.BusinessObject
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.CodaParser
import be.dafke.Accounting.BusinessModelDao.CsvParser
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.Utils.Utils

import javax.swing.*
import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.util.List

class StatementTableFrame extends JFrame implements MouseListener {
    final JButton viewCounterParties, exportToJournal, readCoda, readCsv, saveToAccounting
    Statements statements
    CounterParties counterParties
    JTable tabel
    StatementDataModel dataModel
    Accounts accounts
    Journals journals
    static final HashMap<Statements, StatementTableFrame> statementsGuis = new HashMap<>()
    Accounting accounting

    StatementTableFrame(Statements statements, CounterParties counterParties) {
        super("Statements")
        this.statements = statements
        this.counterParties = counterParties

        dataModel = new StatementDataModel(statements)
        tabel = new JTable(dataModel)
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200))
        tabel.setRowSorter(null)

        JScrollPane scrollPane = new JScrollPane(tabel)
        JPanel contentPanel = new JPanel(new BorderLayout())
        contentPanel.add(scrollPane, BorderLayout.CENTER)

        tabel.addMouseListener(this)
        viewCounterParties = new JButton("View Counterparties")
        viewCounterParties.addActionListener({ e -> CounterPartyTableFrame.showStatements(statements, counterParties).visible = true })
        readCoda = new JButton("Read Coda file(s)")
        readCoda.addActionListener({ e -> readCodaFiles() })
        readCsv = new JButton("Read CSV file(s)")
        readCsv.addActionListener({ e -> readCsvFiles() })
        saveToAccounting = new JButton("Save movements (all/selection)")
        saveToAccounting.addActionListener({ e -> saveToAccounting() })
        JPanel north = new JPanel()
        north.add(readCoda)
        north.add(readCsv)
        north.add(viewCounterParties)
        getContentPane().add(north, BorderLayout.NORTH)
        exportToJournal = new JButton("Export selected movements to a Journal")
        // exportToJournal.enabled = false
        exportToJournal.addActionListener({ e -> exportToJournal() })
//		JPanel south = new JPanel()
        contentPanel.add(exportToJournal, BorderLayout.SOUTH)

        setContentPane(contentPanel)
        pack()
    }

    static StatementTableFrame showStatements(Statements statements, CounterParties counterParties) {
        StatementTableFrame gui = statementsGuis.get(statements)
        if(gui == null){
            gui = new StatementTableFrame(statements, counterParties)
            statementsGuis.put(statements,gui)
            Main.addFrame(gui)
        }
        gui
    }

    void saveToAccounting() {
        // TODO Auto-generated method stub

    }

    void refresh() {
//		tabel.refresh()
        dataModel.fireTableDataChanged()
    }

    void readCsvFiles(){
        JFileChooser chooser = new JFileChooser()
        chooser.setMultiSelectionEnabled(true)
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles()
            CsvParser codaParser = new CsvParser()
            codaParser.parseFile(files, counterParties, statements)
        }
        refresh()
    }

    void readCodaFiles() {
        JFileChooser chooser = new JFileChooser()
        chooser.setMultiSelectionEnabled(true)
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles()
            CodaParser codaParser = new CodaParser()
            codaParser.parseFile(files, counterParties, statements)
        }
        refresh()
    }

    boolean checkAccountAndSelection(int[] rows) {
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Select some lines first")
            false
        }
        true
    }

    boolean checkCounterParties(int[] rows) {
        // Step 1: check if each movement has a counterparty
        // ok, because transactioncode is used
        // TODO: not use transactioncodes as counterparty,
        // but as the user for input.
        JOptionPane.showMessageDialog(this, "TODO: link transactioncode + extra data to counterparties")
        Set<CounterParty> set = new HashSet<CounterParty>()
        List<BusinessObject> list = new ArrayList<BusinessObject>()
        for(int i : rows) {
            CounterParty counterParty = (CounterParty) tabel.getValueAt(i, 4)
            if (counterParty == null) {
                list.add(statements.businessObjects.get(i))
            } else if (counterParty.account == null) {
                set.add(counterParty)
            }
        }
        if (!list.empty) {
            System.err.println(list.size() + " movements have no counterparty:")
            StringBuilder builder = new StringBuilder(list.size() + " movements have no counterparty:")
            for(BusinessObject statement : list) {
                System.err.println(statement)
                builder.append("\n").append(statement)
            }
            JOptionPane.showMessageDialog(this, builder.toString())
            SearchOptions searchOptions = new SearchOptions()
            searchOptions.searchForCounterParty(null)
            GenericStatementTableFrame gui = new GenericStatementTableFrame(searchOptions, statements)
            gui.visible = true
            false
        }
        if (!set.empty) {
            System.err.println(set.size() + " counterparties have no account:")
            StringBuilder builder = new StringBuilder(set.size() + " counterparties have no account:")
            for(CounterParty counterParty : set) {
                System.err.println(counterParty)
                builder.append("\n").append(counterParty)
            }
            JOptionPane.showMessageDialog(this, builder.toString())
            CounterPartyTableFrame.showStatements(statements,counterParties).visible = true
            false
        }
        true
    }

    void exportToJournal() {
        // TODO save movements (and counterparties) into accounting [or with different button]
        int[] rows = tabel.selectedRows
        if (checkAccountAndSelection(rows)) {
            if (checkCounterParties(rows)) {
                Object[] accountList = accounts.businessObjects.toArray()
                Account bankAccount = (Account) JOptionPane.showInputDialog(this, "Select Bank account",
                        "Select account", JOptionPane.INFORMATION_MESSAGE, null, accountList, null)
                Journal journal
                Object[] journalsList = journals.businessObjects.toArray()
                if (journals.businessObjects.size() == 1) {
                    journal = (Journal) journalsList[0]
                } else {
                    journal = (Journal) JOptionPane.showInputDialog(this, "Select Journal", "Select journal",
                            JOptionPane.INFORMATION_MESSAGE, null, journalsList, null)
                }
                if (bankAccount != null && journal != null) {
                    for(int i : rows) {
                        BusinessObject counterParty = (BusinessObject) tabel.getValueAt(i, 4)
                        Account account = ((CounterParty)counterParty).account
                        boolean debet = tabel.getValueAt(i, 2).equals("D")
                        if (account == null) {
                            BusinessObject counterParty2 = counterParties.getBusinessObject(counterParty.name)
                            if (counterParty2 != null) {
                                counterParty = counterParty2
                                account = ((CounterParty)counterParty2).account
                            }
                        }
                        while (account == null) {
                            account = (Account) JOptionPane.showInputDialog(this, "Select account", "Select account",
                                    JOptionPane.INFORMATION_MESSAGE, null, accountList, null)
                            ((CounterParty)counterParty).setAccount(account)
                        }
                        BigDecimal amount = (BigDecimal) tabel.getValueAt(i, 3)
                        AccountingSession accountingSession = Session.getAccountingSession(accounting)
                        Journal activeJournal = accountingSession.activeJournal
                        Transaction transaction = activeJournal.currentTransaction
                        Booking booking1 = new Booking(account, amount, debet)
                        Booking booking2 = new Booking(bankAccount, amount, !debet)
                        transaction.addBusinessObject booking1
                        transaction.addBusinessObject booking2
                        String cal = (String) tabel.getValueAt(i, 1)
                        Calendar date = Utils.toCalendar cal
                        transaction.setDate(date)
                        String description = (String) tabel.getValueAt(i, 6)
                        transaction.description = description
                        journal.addBusinessObject transaction
                        // use current journal, correct ?
//						Accounting accounting = journal.accounting
                        Transactions transactions = accounting.transactions
                        transactions.setId(transaction)
                        transactions.addBusinessObject transaction

                        transaction = new Transaction(date, "")
                        // take the same date as previous transaction
                        // leave the description empty

                        activeJournal.currentTransaction = transaction
                    }
                }
            }
        }
        refresh()
    }

    void mouseClicked(MouseEvent me) {
        Point cell = me.getPoint()
        if (me.getClickCount() == 1) {
            int col = tabel.columnAtPoint(cell)
            int row = tabel.rowAtPoint(cell)
            if (col == 4) {
                CounterParty counterParty = (CounterParty) tabel.getValueAt(row, col)
                if (counterParty == null) {
                    CounterPartySelector sel = new CounterPartySelector((Statement)statements.businessObjects.get(row), statements, counterParties)
                    sel.visible = true
                    counterParty = sel.getSelection()
                }
                if (counterParty != null) {
                    Statement statement = (Statement)statements.businessObjects.get(row)
                    statement.setCounterParty(counterParty)
                    refresh()
                    System.out.println(counterParty.name)
                    for(BankAccount account : counterParty.getBankAccounts().values()) {
                        System.out.println(account.getAccountNumber())
                        System.out.println(account.getBic())
                        System.out.println(account.getCurrency())
                    }
                    SearchOptions searchOptions = new SearchOptions()
                    searchOptions.searchForCounterParty(counterParty)
                    new GenericStatementTableFrame(searchOptions, statements).visible = true
                    // parent.addChildFrame(refreshableTable)
                }
            } else if (col == 5) {
                String transactionCode = (String) tabel.getValueAt(row, 6)
                SearchOptions searchOptions = new SearchOptions()
                searchOptions.searchForTransactionCode(transactionCode)
                new GenericStatementTableFrame(searchOptions, statements).visible = true
                // parent.addChildFrame(refreshableTable)
            } else if (col == 6){
                String communication = (String) tabel.getValueAt(row, 7)
                SearchOptions searchOptions = new SearchOptions()
                searchOptions.searchForCommunication(communication)
                new GenericStatementTableFrame(searchOptions, statements).visible = true
                // parent.addChildFrame(refreshableTable)
            }
        }
    }

    void mouseEntered(MouseEvent e) {
    }

    void mouseExited(MouseEvent e) {
    }

    void mousePressed(MouseEvent e) {
    }

    void mouseReleased(MouseEvent e) {
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        counterParties = accounting.counterParties
        statements = accounting.statements
        accounts=accounting?accounting.accounts:null
        journals=accounting?accounting.journals:null
    }
}
