package be.dafke.Accounting.BasicAccounting.Journals.Edit


import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.table.TableColumn
import java.awt.*
import java.awt.event.*

import static java.util.ResourceBundle.getBundle

class JournalEditPanel extends JPanel implements ActionListener {
    JTextField debet, credit, ident
    JButton singleBook, save, clear
    JCheckBox balanceTransaction

    final SelectableTable<Booking> table
    TableColumn debitAccount, creditAccount
    JComboBox<Account> comboBox
    final JournalGUIPopupMenu popup
    final JournalDataModel journalDataModel
    BigDecimal debettotaal, credittotaal

    Journal journal
    Transaction transaction
    Accounting accounting
    DateAndDescriptionPanel dateAndDescriptionPanel

    JournalEditPanel() {
        setLayout(new BorderLayout())
        debettotaal = new BigDecimal(0)
        credittotaal = new BigDecimal(0)

        journalDataModel = new JournalDataModel()

        table = new SelectableTable<>(journalDataModel)
        table.setPreferredScrollableViewportSize(new Dimension(800, 200))

        popup = new JournalGUIPopupMenu(table)
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table))

        JScrollPane scrollPane = new JScrollPane(table)
        add(scrollPane, BorderLayout.CENTER)

        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            void mouseClicked(MouseEvent me) {
                popup.visible = false
            }
        })
        JPanel onder = createInputPanel()
        add(onder, BorderLayout.SOUTH)
    }

    JPanel createInputPanel(){
        ident = new JTextField(4)
        ident.setEditable(false)
        balanceTransaction = new JCheckBox("balanceTransaction",false)
        balanceTransaction.addActionListener({ e -> transaction.balanceTransaction = balanceTransaction.selected })
        singleBook = new JButton(getBundle("Accounting").getString("OK"))
        singleBook.addActionListener(this)
        singleBook.setMnemonic(KeyEvent.VK_B)
        save = new JButton(getBundle("Accounting").getString("SAVE"))
        save.addActionListener(this)
        clear = new JButton(getBundle("Accounting").getString("CLEAR_PANEL"))
        clear.addActionListener(this)

        JPanel paneel2 = new JPanel()
        paneel2.add(new JLabel(getBundle("Accounting").getString("TRANSACTION")))
        paneel2.add(ident)
        paneel2.add(balanceTransaction)

        JPanel paneel3 = new JPanel()
        paneel3.add(singleBook)
        paneel3.add(clear)
        paneel3.add(save)
        debet = new JTextField(8)
        credit = new JTextField(8)
        debet.setEditable(false)
        credit.setEditable(false)

        JPanel paneel4 = new JPanel()
        paneel4.add(new JLabel(getBundle("Accounting").getString("TOTAL_DEBIT")))
        paneel4.add(debet)
        paneel4.add(new JLabel(getBundle("Accounting").getString("TOTAL_CREDIT")))
        paneel4.add(credit)

        JPanel mainPanel = new JPanel()
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS))
//        mainPanel.setLayout(new GridLayout(0, 1))
        dateAndDescriptionPanel = new DateAndDescriptionPanel()
        mainPanel.add(paneel4)
        mainPanel.add(paneel2)
        mainPanel.add(dateAndDescriptionPanel)
        mainPanel.add(paneel3)
        mainPanel
    }

    void moveTransaction(Set<Transaction> transactions, Journals journals) {
        // ask journal only once
        Journal newJournal = getNewJournal(transaction, journals)
        moveTransaction(transactions, newJournal)
    }

    void moveBookings(ArrayList<Booking> bookings, Journals journals) {
        Set<Transaction> transactions = getTransactions(bookings)
        moveTransaction(transactions, journals)
    }

    void moveTransaction(Set<Transaction> transactions, Journal newJournal) {
        Set<Journal> updatedJournals = new HashSet<>()
        Set<Account> updatedAccounts = new HashSet<>()
        if(newJournal){
            updatedJournals.add(newJournal)
        }
        for (Transaction transaction : transactions) {
            if (transaction != null) {
                Journal oldJournal = transaction.journal
                if (oldJournal != null) {
                    oldJournal.removeBusinessObject(transaction)
                }

                if (newJournal != null) { // e.g. when Cancel has been clicked
                    newJournal.addBusinessObject(transaction)
                }
                transaction.journal = newJournal
                for (Account account : transaction.accounts) {
                    updatedAccounts.add(account)
                }
            }
        }

        for (Journal journal:updatedJournals) {
            Main.fireJournalDataChanged(journal)
        }

        for (Account account : updatedAccounts) {
            Main.fireAccountDataChanged(account)
        }
//        ActionUtils.showErrorMessage(ActionUtils.TRANSACTION_MOVED, oldJournal.name, newJournal.name)
    }

    Set<Transaction> getTransactions(ArrayList<Booking> bookings){
        Set<Transaction> transactions = new HashSet<>()
        for (Booking booking:bookings) {
            transactions.add(booking.transaction)
        }
        transactions
    }

    Journal getNewJournal(Transaction transaction, Journals journals){
        Journal journal = transaction.journal
        ArrayList<Journal> dagboeken = journals.getAllJournalsExcept(journal)
        Object[] lijst = dagboeken.toArray()
        int keuze = JOptionPane.showOptionDialog(null,
                getBundle("BusinessActions").getString("CHOOSE_JOURNAL"),
                getBundle("BusinessActions").getString("JOURNAL_CHOICE"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, lijst, lijst[0])
        if(keuze!=JOptionPane.CLOSED_OPTION){
            (Journal) lijst[keuze]
        }else null
    }

    void deleteBookings(ArrayList<Booking> bookings) {
        Set<Transaction> transactions = getTransactions(bookings)
        deleteTransactions(transactions)
    }

    void deleteTransactions(Set<Transaction> transactions){
        for (Transaction transaction : transactions) {
            deleteTransaction(transaction)
        }
    }

    void deleteTransaction(Transaction transaction) {//throws NotEmptyException{
        Journal journal = transaction.journal
        journal.removeBusinessObject(transaction)
        accounting.transactions.removeBusinessObject(transaction)
        transaction.journal = null

        Main.fireJournalDataChanged(journal)
        for (Account account : transaction.accounts) {
            Main.fireAccountDataChanged(account)
        }

        ArrayList<VATBooking> vatBookings = transaction.vatBookings
        if (vatBookings != null && !vatBookings.empty) {
            Main.fireVATFieldsUpdated()
        }
//        ActionUtils.showErrorMessage(TRANSACTION_REMOVED, journal.name)
    }

    void addTransaction(Transaction transaction){
        Accounting accounting = journal.accounting
        Transactions transactions = accounting.transactions
        transactions.setId(transaction)
        transactions.addBusinessObject transaction
        journal.addBusinessObject transaction
        transaction.journal = journal

        ArrayList<VATBooking> vatBookings = transaction.vatBookings
        if (vatBookings != null && !vatBookings.empty) {
            Main.fireVATFieldsUpdated(/*vatFields*/)
        }

        Contact contact = transaction.contact
        if(contact){
            Main.fireCustomerDataChanged()
        }

        Main.fireJournalDataChanged(journal)
        for (Account account : transaction.accounts) {
            Main.fireAccountDataChanged(account)
        }

        Main.selectTransaction(transaction)
    }

    void editTransaction(Transaction transaction) {//throws NotEmptyException{
        // deleteTransaction should throw NotEmptyException if not deletable/editable !
        Journal journal = transaction.journal
        deleteTransaction(transaction)
        //TODO: GUI with question where to open the transaction? (only usefull if multiple input GUIs are open)
        // set Journal before Transaction: setJournal sets transaction to currentObject !!!

        Main.setAccounting(journal.accounting)
        Main.journal = journal
        journal.currentTransaction = transaction
        // TODO: when calling setTransaction we need to check if the currentTransaction is empty (see switchJournal() -> checkTransfer)
        setTransaction(transaction)
    }

    void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear) {
            clear()
        } else if (e.getSource() == save) {
            saveTransaction()
        } else if (e.getSource() == singleBook){
            saveTransaction()
            if(journal && transaction && transaction.bookable){
                addTransaction(transaction)
                Mortgage mortgage = transaction.mortgage
                if(mortgage){
                    Main.fireMortgageEditedPayButton mortgage
                    Main.fireMortgageEdited mortgage
                }
                clear()
            }
        }
    }

    void clear() {
        transaction = new Transaction(dateAndDescriptionPanel.date, "")
        transaction.journal = journal
        balanceTransaction.selected = false
        journal.currentTransaction = transaction
        setTransaction(transaction)
        ident.setText(journal==null?"":journal.abbreviation + " " + journal.id)
    }

    Transaction saveTransaction(){
        if(transaction){
            Calendar date = dateAndDescriptionPanel.date
            if(date == null){
                ActionUtils.showErrorMessage(this, ActionUtils.FILL_IN_DATE)
                null
            } else {
                // TODO Encode text for XML / HTML (not here, but in toXML() / here escaping ?)
                transaction.description = dateAndDescriptionPanel.description
                transaction.date = dateAndDescriptionPanel.date
            }
        }
        transaction
    }

    JComboBox<Account> createComboBox() {
        JComboBox<Account> comboBox = new JComboBox<>()
        comboBox.removeAllItems()
        accounting.accounts.businessObjects.forEach({ account -> comboBox.addItem(account) })
        comboBox
    }

    void setAccounting(Accounting accounting){
        this.accounting = accounting
        popup.accounting = accounting
//        setAccounts(accounting?accounting.accounts:null)
        AccountingSession accountingSession = Session.getAccountingSession(accounting)
        setJournal(accounting?accountingSession.activeJournal:null)
//        setVatTransactions(accounting?accounting.vatTransactions:null)
//        setTransactions(accounting?accounting.transactions:null)

        comboBox=createComboBox()
        debitAccount = table.getColumnModel().getColumn(JournalDataModel.DEBIT_ACCOUNT)
        debitAccount.setCellEditor(new DefaultCellEditor(comboBox))
        creditAccount = table.getColumnModel().getColumn(JournalDataModel.CREDIT_ACCOUNT)
        creditAccount.setCellEditor(new DefaultCellEditor(comboBox))
    }

    void setJournal(Journal journal) {
        this.journal=journal
        dateAndDescriptionPanel.journal = journal
        ident.setText(journal==null?"":journal.abbreviation + " " + journal.id)
        setTransaction(journal==null?null:journal.currentTransaction)
    }

    void setTransaction(Transaction transaction){
        this.transaction = transaction
        dateAndDescriptionPanel.setTransaction(transaction)
        journalDataModel.setTransaction(transaction)
        balanceTransaction.setSelected(transaction && transaction.balanceTransaction)
        fireTransactionDataChanged()
    }

    void addBooking(Booking booking){
        transaction.addBusinessObject(booking)
        fireTransactionDataChanged()
    }

    Transaction getTransaction() {
        transaction
    }

    void addMortgageTransaction(Mortgage mortgage){
        if (mortgage.isPayedOff()) {
            System.out.println("Payed Off already")
            return
        }
        if (transaction.mortgage){
            System.out.println("Transaction already contains a mortgage")
            return
        }
        Account capitalAccount = mortgage.getCapitalAccount()
        Account intrestAccount = mortgage.getIntrestAccount()
        if(capitalAccount==null || intrestAccount==null){
            return
        }
        Booking capitalBooking = new Booking(capitalAccount, mortgage.getNextCapitalAmount(),true)
        Booking intrestBooking = new Booking(intrestAccount, mortgage.getNextIntrestAmount(),true)

        transaction.addBusinessObject(capitalBooking)
        transaction.addBusinessObject(intrestBooking)

        transaction.mortgage = mortgage
        // TODO: pass function "increaseNrPayed/decreaseNrPayed" to call after journal.addBusinessObject(transaction)

        fireTransactionDataChanged()
    }

    Journal switchJournal(Journal newJournal) {
        if(newJournal){
            journal = checkTransfer(newJournal)
        } else {
            journal = newJournal
        }
        journal
    }

    Journal checkTransfer(Journal newJournal){
        Transaction newTransaction = newJournal.currentTransaction
        if(transaction && !transaction.businessObjects.empty && journal!=newJournal){
            StringBuilder builder = new StringBuilder("Do you want to transfer the current transaction from ")
                    .append(journal).append(" to ").append(newJournal)
            if(newTransaction && !newTransaction.businessObjects.empty){
                builder.append("\nWARNING: ").append(newJournal).append(" also has an open transactions, which will be lost if you select transfer")
            }
            int answer = JOptionPane.showConfirmDialog(null, builder.toString())
            if(answer == JOptionPane.YES_OPTION){
                moveTransactionToNewJournal(newJournal)
                newJournal
            } else if(answer == JOptionPane.NO_OPTION){
                saveCurrentTransaction()
                newJournal
            } else {
                journal
            }
        } else {
            newJournal
        }
    }

    void moveTransactionToNewJournal(Journal newJournal){
        newJournal.currentTransaction = transaction
        journal.currentTransaction = new Transaction(Calendar.getInstance(), "")
    }

    void saveCurrentTransaction(){
        journal.currentTransaction = transaction
    }

    void fireTransactionDataChanged() {
        journalDataModel.fireTableDataChanged()

        debettotaal = (transaction)?transaction.debitTotal:BigDecimal.ZERO//.setScale(2)
        credittotaal = (transaction)?transaction.creditTotal:BigDecimal.ZERO//.setScale(2)
        debet.setText(debettotaal.toString())
        credit.setText(credittotaal.toString())
        balanceTransaction.selected = transaction&&transaction.balanceTransaction
        dateAndDescriptionPanel.fireTransactionDataChanged()

        boolean okEnabled = journal && transaction && transaction.bookable
        boolean clearEnabled = journal && transaction && !transaction.businessObjects.empty
        clear.enabled = clearEnabled
        save.enabled = clearEnabled
        singleBook.enabled = okEnabled
    }
}
