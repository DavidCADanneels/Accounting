package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
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

import static be.dafke.BasicAccounting.MainApplication.ActionUtils.TRANSACTION_REMOVED;
import static java.util.ResourceBundle.getBundle;



/**
 * Created by ddanneels on 29/04/2016.
 */
public class JournalInputGUI extends JPanel implements FocusListener, ActionListener {
    private static final long serialVersionUID = 1L;

    private JTextField debet, credit, dag, maand, jaar, bewijs, ident;
    private JButton singleBook, save, clear;

    private final SelectableTable<Booking> table;
    private TableColumn debitAccount, creditAccount;
    private JComboBox<Account> comboBox;
    private final JournalGUIPopupMenu popup;
    private final JournalDataModel journalDataModel;
    private BigDecimal debettotaal, credittotaal;

    private Journal journal;
    private Transaction transaction;
    private Accounts accounts;

    public JournalInputGUI() {
        setLayout(new BorderLayout());
        debettotaal = new BigDecimal(0);
        credittotaal = new BigDecimal(0);

        journalDataModel = new JournalDataModel();

        table = new SelectableTable<>(journalDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 200));

        popup = new JournalGUIPopupMenu(table, this);
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
        dag = new JTextField(2);
        maand = new JTextField(2);
        jaar = new JTextField(4);
        dag.addFocusListener(this);
        maand.addFocusListener(this);
        jaar.addFocusListener(this);
        bewijs = new JTextField(30);
        bewijs.addFocusListener(this);

        singleBook = new JButton(getBundle("Accounting").getString("OK"));
        singleBook.addActionListener(this);
        save = new JButton(getBundle("Accounting").getString("SAVE"));
        save.addActionListener(this);
        clear = new JButton(getBundle("Accounting").getString("CLEAR_PANEL"));
        clear.addActionListener(this);

        JPanel paneel1 = new JPanel();
        paneel1.add(new JLabel(
                getBundle("Accounting").getString("TRANSACTION")));
        paneel1.add(ident);
        paneel1.add(new JLabel(getBundle("Accounting").getString("DATE")));
        paneel1.add(dag);
        paneel1.add(new JLabel("/"));
        paneel1.add(maand);
        paneel1.add(new JLabel("/"));
        paneel1.add(jaar);
        paneel1.add(new JLabel("(d/m/yyyy)"));

        JPanel paneel2 = new JPanel();
        paneel2.add(new JLabel(getBundle("Accounting").getString(
                "MESSAGE")));
        paneel2.add(bewijs);

        JPanel paneel3 = new JPanel();
        paneel3.add(singleBook);
        paneel3.add(clear);
        paneel3.add(save);
        debet = new JTextField(8);
        credit = new JTextField(8);
        debet.setEditable(false);
        credit.setEditable(false);
        paneel3.add(new JLabel(
                getBundle("Accounting").getString("TOTAL_DEBIT")));
        paneel3.add(debet);
        paneel3.add(new JLabel(getBundle("Accounting").getString(
                "TOTAL_CREDIT")));
        paneel3.add(credit);

        JPanel mainPanel = new JPanel(new GridLayout(0, 1));
        mainPanel.add(paneel1);
        mainPanel.add(paneel2);
        mainPanel.add(paneel3);
        return mainPanel;
    }

    public void moveTransaction(ArrayList<Booking> bookings, Journals journals) {
        moveTransaction(getTransactions(bookings),journals);
    }

    public void moveTransaction(Set<Transaction> transactions, Journals journals) {
        for (Transaction transaction : transactions) {
            moveTransaction(transaction, journals);
        }
    }

    public void moveTransaction(Transaction transaction, Journals journals) {
        Journal oldJournal = transaction.getJournal();

        Journal newJournal = getNewJournal(transaction, journals);
        if(newJournal!=null) { // e.g. when Cancel has been clicked
            oldJournal.removeBusinessObject(transaction);
            newJournal.addBusinessObject(transaction);
//            newJournal.addBusinessObject(transaction, true);
            Main.fireJournalDataChanged(oldJournal);
            Main.fireJournalDataChanged(newJournal);
            for (Account account : transaction.getAccounts()) {
                Main.fireAccountDataChanged(account);
            }

//                ActionUtils.showErrorMessage(ActionUtils.TRANSACTION_MOVED, oldJournal.getName(), newJournal.getName());
        }
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

    public void deleteTransaction(Set<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            deleteTransaction(transaction);
        }
    }

    public void deleteTransaction(Transaction transaction) {
        Journal journal = transaction.getJournal();
        journal.removeBusinessObject(transaction);

        Main.fireJournalDataChanged(journal);
        for (Account account : transaction.getAccounts()) {
            Main.fireAccountDataChanged(account);
        }

        // FIXME: link between transaction and mortgage is gone after restart (not saved in XML) ???
        Mortgage mortgage = transaction.getMortgage();
        if (mortgage != null) {
            mortgage.decreaseNrPayed();
        }

        VATTransaction vatTransaction = transaction.getVatTransaction();
        if (vatTransaction != null && !vatTransaction.getBusinessObjects().isEmpty()) {
            Main.fireVATFieldsUpdated();
        }

        Contact contact = transaction.getContact();
        BigDecimal turnOverAmount = transaction.getTurnOverAmount();
        BigDecimal vatAmount = transaction.getVATAmount();
        if (contact != null && turnOverAmount != null && vatAmount != null) {
            contact.decreaseTurnOver(turnOverAmount);
            contact.decreaseVATTotal(vatAmount);
        }

//        ActionUtils.showErrorMessage(TRANSACTION_REMOVED, journal.getName());
    }

    public void addTransaction(Transaction transaction){
        Accounting accounting = journal.getAccounting();
        accounting.addTransaction(transaction);
        journal.addBusinessObject(transaction);

        Mortgage mortgage = transaction.getMortgage();
        if (mortgage != null) {
            mortgage.raiseNrPayed();
        }

        VATTransaction vatTransaction = transaction.getVatTransaction();
        if (vatTransaction != null && !vatTransaction.getBusinessObjects().isEmpty()) {
            Main.fireVATFieldsUpdated(/*vatFields*/);
        }

        Contact contact = transaction.getContact();
        if(contact!=null){
            Main.fireContactDataChanged();
        }

        Main.fireJournalDataChanged(journal);
        for (Account account : transaction.getAccounts()) {
            Main.fireAccountDataChanged(account);
        }
    }

    // TODO: remove this method: edit only 1 transaction
    public void editTransaction(Set<Transaction> transactions) {
        for(Transaction transaction:transactions){
            editTransaction(transaction);
        }
    }

    public void editTransaction(Transaction transaction) {
        deleteTransaction(transaction);
//        deleteTransaction(bookings);
        Journal journal = transaction.getJournal();
        //TODO: GUI with question where to open the transaction? (only usefull if multiple input GUIs are open)
        // set Journal before Transaction: setJournal sets transaction to currentObject !!!

        Main.setAccounting(journal.getAccounting());
        Main.setJournal(journal);
        journal.setCurrentObject(transaction);
        // TODO: when calling setTransaction we need to check if the currentTransaction is empty (see switchJournal() -> checkTransfer)
        setTransaction(transaction);
    }

    private void setDate(Calendar date){
        if (date == null){
            dag.setText("");
            maand.setText("");
            jaar.setText("");
        }else{
            dag.setText(Utils.toDay(date)+"");
            maand.setText(Utils.toMonth(date)+"");
            jaar.setText(Utils.toYear(date)+"");
        }
    }

    public void focusGained(FocusEvent fe) {
        JTextField field = (JTextField)fe.getComponent();
        field.selectAll();
    }

    public void focusLost(FocusEvent fe) {
        if(transaction!=null){
            Object source = fe.getSource();
            if(source == dag || source == maand || source == jaar){
                Calendar date = getDate();
                if (date != null){
                    transaction.setDate(date);
                    dag.setText(Utils.toDay(date)+"");
                    maand.setText(Utils.toMonth(date)+"");
                    jaar.setText(Utils.toYear(date)+"");
                }
            } else if (source == bewijs){
                // TODO Encode text for XML / HTML (not here, but in toXML() / here escaping ?)
                transaction.setDescription(bewijs.getText().trim());
            }
        }
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
                clear();
            }
        }
    }

    public void clear() {
        transaction = new Transaction(getDate(), "");
        transaction.setJournal(journal);
        journal.setCurrentObject(transaction);
        setTransaction(transaction);
    }

    public Transaction saveTransaction(){
        if(transaction!=null){
            Calendar date = getDate();
            if(date == null){
                ActionUtils.showErrorMessage(ActionUtils.FILL_IN_DATE);
                return null;
            } else {
                // TODO Encode text for XML / HTML (not here, but in toXML() / here escaping ?)
                transaction.setDescription(getDescription());
                transaction.setDate(getDate());
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
        setJournals(accounting==null?null:accounting.getJournals());

        comboBox=createComboBox();
        debitAccount = table.getColumnModel().getColumn(JournalDataModel.DEBIT_ACCOUNT);
        debitAccount.setCellEditor(new DefaultCellEditor(comboBox));
        creditAccount = table.getColumnModel().getColumn(JournalDataModel.CREDIT_ACCOUNT);
        creditAccount.setCellEditor(new DefaultCellEditor(comboBox));
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public void setJournals(Journals journals){
        setJournal(journals==null?null:journals.getCurrentObject());
    }

    public void setJournal(Journal journal) {
        this.journal=journal;
        ident.setText(journal==null?"":journal.getAbbreviation() + " " + journal.getId());
        setTransaction(journal==null?null:journal.getCurrentObject());
    }

    public void setTransaction(Transaction transaction){
        this.transaction = transaction;
        journalDataModel.setTransaction(transaction);
        fireTransactionDataChanged();
    }

    private Calendar getDate(){
        return Utils.toCalendar(dag.getText(),maand.getText(),jaar.getText());
    }

    private String getDescription(){
        return bewijs.getText().trim();
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
            journal = checkTransfer(journal, newJournal);
        } else {
            journal = newJournal;
        }
        return journal;
    }

    private Journal checkTransfer(Journal oldJournal, Journal newJournal){
        Transaction newTransaction = newJournal.getCurrentObject();
        if(transaction!=null && !transaction.getBusinessObjects().isEmpty() && oldJournal!=newJournal){
            StringBuilder builder = new StringBuilder("Do you want to transfer the current transaction from ")
                    .append(oldJournal).append(" to ").append(newJournal);
            if(newTransaction!=null && !newTransaction.getBusinessObjects().isEmpty()){
                builder.append("\nWARNING: ").append(newJournal).append(" also has an open transactions, which will be lost if you select transfer");
            }
            int answer = JOptionPane.showConfirmDialog(null, builder.toString());
            if(answer == JOptionPane.YES_OPTION){
                newJournal.setCurrentObject(transaction);
                oldJournal.setCurrentObject(new Transaction(Calendar.getInstance(), ""));
                return newJournal;
            } else if(answer == JOptionPane.NO_OPTION){
                oldJournal.setCurrentObject(transaction);
                return newJournal;
            } else {
                return oldJournal;
            }
        } else {
            return newJournal;
        }
    }

    public void fireTransactionDataChanged() {
        journalDataModel.fireTableDataChanged();

        debettotaal = (transaction==null)?BigDecimal.ZERO:transaction.getDebetTotaal();//.setScale(2);
        credittotaal = (transaction==null)?BigDecimal.ZERO:transaction.getCreditTotaal();//.setScale(2);
        debet.setText(debettotaal.toString());
        credit.setText(credittotaal.toString());
        setDate(transaction==null?Calendar.getInstance():transaction.getDate());
        bewijs.setEnabled((transaction!=null));
        dag.setEnabled((transaction!=null));
        maand.setEnabled((transaction!=null));
        jaar.setEnabled((transaction!=null));
        bewijs.setText(transaction==null?"":transaction.getDescription());

        boolean okEnabled = journal!=null && transaction!=null && transaction.isBookable();
        boolean clearEnabled = journal!=null && transaction!=null && !transaction.getBusinessObjects().isEmpty();
        clear.setEnabled(clearEnabled);
        save.setEnabled(clearEnabled);
        singleBook.setEnabled(okEnabled);
    }
}
