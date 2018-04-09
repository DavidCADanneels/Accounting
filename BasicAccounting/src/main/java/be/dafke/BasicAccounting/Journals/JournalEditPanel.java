package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;
import be.dafke.Utils.Utils;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import static java.util.ResourceBundle.getBundle;



/**
 * Created by ddanneels on 29/04/2016.
 */
public class JournalEditPanel extends JPanel implements ActionListener {
    private JTextField debet, credit, ident;
    private JButton singleBook, save, clear;
    private JCheckBox balanceTransaction;

    private final SelectableTable<Booking> table;
    private TableColumn debitAccount, creditAccount;
    private JComboBox<Account> comboBox;
    private final JournalGUIPopupMenu popup;
    private final JournalDataModel journalDataModel;
    private BigDecimal debettotaal, credittotaal;

    private Journal journal;
    private Transaction transaction;
    private Accounts accounts;
    private VATTransactions vatTransactions;
    private DateAndDescriptionPanel dateAndDescriptionPanel;
    private Transactions transactions;

    public JournalEditPanel() {
        setLayout(new BorderLayout());
        debettotaal = new BigDecimal(0);
        credittotaal = new BigDecimal(0);

        journalDataModel = new JournalDataModel();

        table = new SelectableTable<>(journalDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 200));

        popup = new JournalGUIPopupMenu(table);
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                popup.setVisible(false);
            }
        });
        JPanel onder = createInputPanel();
        add(onder, BorderLayout.SOUTH);
    }

    public JPanel createInputPanel(){
        ident = new JTextField(4);
        ident.setEditable(false);
        balanceTransaction = new JCheckBox("balanceTransaction",false);
        balanceTransaction.addActionListener(e -> transaction.setBalanceTransaction(balanceTransaction.isSelected()));
        singleBook = new JButton(getBundle("Accounting").getString("OK"));
        singleBook.addActionListener(this);
        singleBook.setMnemonic(KeyEvent.VK_B);
        save = new JButton(getBundle("Accounting").getString("SAVE"));
        save.addActionListener(this);
        clear = new JButton(getBundle("Accounting").getString("CLEAR_PANEL"));
        clear.addActionListener(this);

        JPanel paneel2 = new JPanel();
        paneel2.add(new JLabel(getBundle("Accounting").getString("TRANSACTION")));
        paneel2.add(ident);
        paneel2.add(balanceTransaction);

        JPanel paneel3 = new JPanel();
        paneel3.add(singleBook);
        paneel3.add(clear);
        paneel3.add(save);
        debet = new JTextField(8);
        credit = new JTextField(8);
        debet.setEditable(false);
        credit.setEditable(false);

        JPanel paneel4 = new JPanel();
        paneel4.add(new JLabel(getBundle("Accounting").getString("TOTAL_DEBIT")));
        paneel4.add(debet);
        paneel4.add(new JLabel(getBundle("Accounting").getString("TOTAL_CREDIT")));
        paneel4.add(credit);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
//        mainPanel.setLayout(new GridLayout(0, 1));
        dateAndDescriptionPanel = new DateAndDescriptionPanel();
        mainPanel.add(paneel4);
        mainPanel.add(paneel2);
        mainPanel.add(dateAndDescriptionPanel);
        mainPanel.add(paneel3);
        return mainPanel;
    }

    public void moveTransaction(Set<Transaction> transactions, Journals journals) {
        // ask journal only once
        Journal newJournal = getNewJournal(transaction, journals);
        moveTransaction(transactions, newJournal);
    }

    public void moveBookings(ArrayList<Booking> bookings, Journals journals) {
        Set<Transaction> transactions = getTransactions(bookings);
        moveTransaction(transactions, journals);
    }

    public void moveTransaction(Set<Transaction> transactions, Journal newJournal) {
        Set<Journal> updatedJournals = new HashSet<>();
        Set<Account> updatedAccounts = new HashSet<>();
        if(newJournal!=null){
            updatedJournals.add(newJournal);
        }
        for (Transaction transaction : transactions) {
            if (transaction != null) {
                Journal oldJournal = transaction.getJournal();
                if (oldJournal != null) {
                    oldJournal.removeBusinessObject(transaction);
                }

                if (newJournal != null) { // e.g. when Cancel has been clicked
                    newJournal.addBusinessObject(transaction);
                }
                transaction.setJournal(newJournal);
                for (Account account : transaction.getAccounts()) {
                    updatedAccounts.add(account);
                }
            }
        }

        for (Journal journal:updatedJournals) {
            Main.fireJournalDataChanged(journal);
        }

        for (Account account : updatedAccounts) {
            Main.fireAccountDataChanged(account);
        }
//        ActionUtils.showErrorMessage(ActionUtils.TRANSACTION_MOVED, oldJournal.getName(), newJournal.getName());
    }

    public Set<Transaction> getTransactions(ArrayList<Booking> bookings){
        Set<Transaction> transactions = new HashSet<>();
        for (Booking booking:bookings) {
            transactions.add(booking.getTransaction());
        }
        return transactions;
    }

    private Journal getNewJournal(Transaction transaction, Journals journals){
        Journal journal = transaction.getJournal();
        ArrayList<Journal> dagboeken = journals.getAllJournalsExcept(journal);
        Object[] lijst = dagboeken.toArray();
        int keuze = JOptionPane.showOptionDialog(null,
                getBundle("BusinessActions").getString("CHOOSE_JOURNAL"),
                getBundle("BusinessActions").getString("JOURNAL_CHOICE"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, lijst, lijst[0]);
        if(keuze!=JOptionPane.CLOSED_OPTION){
            return (Journal) lijst[keuze];
        }else return null;
    }

    public void deleteBookings(ArrayList<Booking> bookings) {
        Set<Transaction> transactions = getTransactions(bookings);
        for (Transaction transaction : transactions) {
            deleteTransaction(transaction);
        }
    }

    private void deleteTransaction(Transaction transaction) {//throws NotEmptyException{
        Journal journal = transaction.getJournal();
        journal.removeBusinessObject(transaction);
        transactions.removeBusinessObject(transaction);
        transaction.setJournal(null);

        Main.fireJournalDataChanged(journal);
        for (Account account : transaction.getAccounts()) {
            Main.fireAccountDataChanged(account);
        }

        VATTransaction vatTransaction = transaction.getVatTransaction();
        if (vatTransaction != null && !vatTransaction.getBusinessObjects().isEmpty()) {
            Main.fireVATFieldsUpdated();
        }
//        ActionUtils.showErrorMessage(TRANSACTION_REMOVED, journal.getName());
    }

    public void addTransaction(Transaction transaction){
        Accounting accounting = journal.getAccounting();
        Transactions transactions = accounting.getTransactions();
        transactions.setId(transaction);
        transactions.addBusinessObject(transaction);
        journal.addBusinessObject(transaction);
        transaction.setJournal(journal);

        VATTransaction vatTransaction = transaction.getVatTransaction();
        if (vatTransaction != null && !vatTransaction.getBusinessObjects().isEmpty()) {
            Main.fireVATFieldsUpdated(/*vatFields*/);
        }

        Contact contact = transaction.getContact();
        if(contact!=null){
            Main.fireCustomerDataChanged();
        }

        Main.fireJournalDataChanged(journal);
        for (Account account : transaction.getAccounts()) {
            Main.fireAccountDataChanged(account);
        }

        Main.selectTransaction(transaction);
    }

    public void editTransaction(Transaction transaction) {//throws NotEmptyException{
        // deleteTransaction should throw NotEmptyException if not deletable/editable !
        Journal journal = transaction.getJournal();
        deleteTransaction(transaction);
        //TODO: GUI with question where to open the transaction? (only usefull if multiple input GUIs are open)
        // set Journal before Transaction: setJournal sets transaction to currentObject !!!

        Main.setAccounting(journal.getAccounting());
        Main.setJournal(journal);
        journal.setCurrentTransaction(transaction);
        // TODO: when calling setTransaction we need to check if the currentTransaction is empty (see switchJournal() -> checkTransfer)
        setTransaction(transaction);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear) {
            clear();
        } else if (e.getSource() == save) {
            saveTransaction();
        } else if (e.getSource() == singleBook){
            saveTransaction();
            if(journal!=null && transaction!=null && transaction.isBookable()){
                addTransaction(transaction);
                Mortgage mortgage = transaction.getMortgage();
                if(mortgage !=null){
                    Main.fireMortgageEditedPayButton(mortgage);
                    Main.fireMortgageEdited(mortgage);
                }
                clear();
            }
        }
    }

    public void clear() {
        transaction = new Transaction(dateAndDescriptionPanel.getDate(), "");
        transaction.setJournal(journal);
        balanceTransaction.setSelected(false);
        journal.setCurrentTransaction(transaction);
        setTransaction(transaction);
        ident.setText(journal==null?"":journal.getAbbreviation() + " " + journal.getId());
    }

    public Transaction saveTransaction(){
        if(transaction!=null){
            Calendar date = dateAndDescriptionPanel.getDate();
            if(date == null){
                ActionUtils.showErrorMessage(ActionUtils.FILL_IN_DATE);
                return null;
            } else {
                // TODO Encode text for XML / HTML (not here, but in toXML() / here escaping ?)
                transaction.setDescription(dateAndDescriptionPanel.getDescription());
                transaction.setDate(dateAndDescriptionPanel.getDate());
            }
        }
        return transaction;
    }

    private JComboBox<Account> createComboBox() {
        JComboBox<Account> comboBox = new JComboBox<>();
        comboBox.removeAllItems();
        accounts.getBusinessObjects().forEach(account -> comboBox.addItem(account));
        return comboBox;
    }

    public void setAccounting(Accounting accounting){
        popup.setAccounting(accounting);
        setAccounts(accounting==null?null:accounting.getAccounts());
        setJournal(accounting==null?null:accounting.getActiveJournal());
        setVatTransactions(accounting==null?null:accounting.getVatTransactions());
        setTransactions(accounting==null?null:accounting.getTransactions());

        comboBox=createComboBox();
        debitAccount = table.getColumnModel().getColumn(JournalDataModel.DEBIT_ACCOUNT);
        debitAccount.setCellEditor(new DefaultCellEditor(comboBox));
        creditAccount = table.getColumnModel().getColumn(JournalDataModel.CREDIT_ACCOUNT);
        creditAccount.setCellEditor(new DefaultCellEditor(comboBox));
    }

    private void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }

    public void setVatTransactions(VATTransactions vatTransactions) {
        this.vatTransactions = vatTransactions;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public void setJournal(Journal journal) {
        this.journal=journal;
        dateAndDescriptionPanel.setJournal(journal);
        ident.setText(journal==null?"":journal.getAbbreviation() + " " + journal.getId());
        setTransaction(journal==null?null:journal.getCurrentTransaction());
    }

    public void setTransaction(Transaction transaction){
        this.transaction = transaction;
        dateAndDescriptionPanel.setTransaction(transaction);
        journalDataModel.setTransaction(transaction);
        balanceTransaction.setSelected(transaction!=null && transaction.isBalanceTransaction());
        fireTransactionDataChanged();
    }

    public void addBooking(Booking booking){
        transaction.addBusinessObject(booking);
        fireTransactionDataChanged();
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void addMortgageTransaction(Mortgage mortgage){
        if (mortgage.isPayedOff()) {
            System.out.println("Payed Off already");
            return;
        }
        if (transaction.getMortgage()!=null){
            System.out.println("Transaction already contains a mortgage");
            return;
        }
        Account capitalAccount = mortgage.getCapitalAccount();
        Account intrestAccount = mortgage.getIntrestAccount();
        if(capitalAccount==null || intrestAccount==null){
            return;
        }
        Booking capitalBooking = new Booking(capitalAccount, mortgage.getNextCapitalAmount(),true);
        Booking intrestBooking = new Booking(intrestAccount, mortgage.getNextIntrestAmount(),true);

        transaction.addBusinessObject(capitalBooking);
        transaction.addBusinessObject(intrestBooking);

        transaction.setMortgage(mortgage);
        // TODO: pass function "increaseNrPayed/decreaseNrPayed" to call after journal.addBusinessObject(transaction)

        fireTransactionDataChanged();
    }

    public Journal switchJournal(Journal newJournal) {
        if(newJournal!=null){
            journal = checkTransfer(newJournal);
        } else {
            journal = newJournal;
        }
        return journal;
    }

    private Journal checkTransfer(Journal newJournal){
        Transaction newTransaction = newJournal.getCurrentTransaction();
        if(transaction!=null && !transaction.getBusinessObjects().isEmpty() && journal!=newJournal){
            StringBuilder builder = new StringBuilder("Do you want to transfer the current transaction from ")
                    .append(journal).append(" to ").append(newJournal);
            if(newTransaction!=null && !newTransaction.getBusinessObjects().isEmpty()){
                builder.append("\nWARNING: ").append(newJournal).append(" also has an open transactions, which will be lost if you select transfer");
            }
            int answer = JOptionPane.showConfirmDialog(null, builder.toString());
            if(answer == JOptionPane.YES_OPTION){
                moveTransactionToNewJournal(newJournal);
                return newJournal;
            } else if(answer == JOptionPane.NO_OPTION){
                saveCurrentTransaction();
                return newJournal;
            } else {
                return journal;
            }
        } else {
            return newJournal;
        }
    }

    public void moveTransactionToNewJournal(Journal newJournal){
        newJournal.setCurrentTransaction(transaction);
        journal.setCurrentTransaction(new Transaction(Calendar.getInstance(), ""));
    }

    public void saveCurrentTransaction(){
        journal.setCurrentTransaction(transaction);
    }

    public void fireTransactionDataChanged() {
        journalDataModel.fireTableDataChanged();

        debettotaal = (transaction==null)?BigDecimal.ZERO:transaction.getDebetTotaal();//.setScale(2);
        credittotaal = (transaction==null)?BigDecimal.ZERO:transaction.getCreditTotaal();//.setScale(2);
        debet.setText(debettotaal.toString());
        credit.setText(credittotaal.toString());
        balanceTransaction.setSelected(transaction!=null&&transaction.isBalanceTransaction());
        dateAndDescriptionPanel.fireTransactionDataChanged();

        boolean okEnabled = journal!=null && transaction!=null && transaction.isBookable();
        boolean clearEnabled = journal!=null && transaction!=null && !transaction.getBusinessObjects().isEmpty();
        clear.setEnabled(clearEnabled);
        save.setEnabled(clearEnabled);
        singleBook.setEnabled(okEnabled);
    }
}
