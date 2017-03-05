package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Contacts.ContactSelector;
import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

import static java.awt.BorderLayout.CENTER;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.JSplitPane.*;

/**
 * Created by ddanneels on 28/01/2017.
 */
public class AccountInputPanel extends JPanel{
    public static final String SELECT_TAX_CREDIT_ACCOUNT = "select Tax Credit Account";
    public static final String SELECT_TAX_DEBIT_ACCOUNT = "select Tax Debit Account";
    public static final String SELECT_TAX_CREDIT_CN_ACCOUNT = "select Tax Credit CN Account";
    public static final String SELECT_TAX_DEBIT_CN_ACCOUNT = "select Tax Debit CN Account";
    private final JournalInputGUI journalInputGUI;
    private AccountTypes accountTypes;
//    private AccountsTableGUI accountsGUI1, accountsGUI2;
    private AccountsGUI accountsGUI1, accountsGUI2;
    private VATTransactions vatTransactions = null;

    private Contacts contacts;
    private Accounts accounts;

    public AccountInputPanel(JournalInputGUI journalInputGUI) {
        this.journalInputGUI = journalInputGUI;
        JSplitPane accountsPanel = createSplitPane();
        setLayout(new BorderLayout());
        add(accountsPanel,CENTER);
    }

    private JSplitPane createSplitPane() {
//        accountsGUI1 = new AccountsTableGUI(journalInputGUI);
//        accountsGUI2 = new AccountsTableGUI(journalInputGUI);
        accountsGUI1 = new AccountsGUI( this);
        accountsGUI2 = new AccountsGUI(this);
        JSplitPane splitPane = new JSplitPane(VERTICAL_SPLIT);
        splitPane.add(accountsGUI1,TOP);
        splitPane.add(accountsGUI2,BOTTOM);
        return splitPane;
    }

    public BigDecimal askAmount(Account account, boolean debit, Transaction transaction) {
        if (transaction == null) return null;
        BigDecimal creditTotal = transaction.getCreditTotaal();
        BigDecimal debitTotal = transaction.getDebetTotaal();
        BigDecimal suggestedAmount = null;
        if (creditTotal.compareTo(debitTotal) > 0 && debit) {
            suggestedAmount = creditTotal.subtract(debitTotal);
        } else if (debitTotal.compareTo(creditTotal) > 0 && !debit) {
            suggestedAmount = debitTotal.subtract(creditTotal);
        } else {
            BigDecimal defaultAmount = account.getDefaultAmount();
            if (defaultAmount != null) {
                suggestedAmount = defaultAmount;
            }
        }
        return askAmount(account, suggestedAmount);
    }

    public BigDecimal askAmount(Account account, BigDecimal suggestedAmount){
        boolean ok = false;
        BigDecimal amount = null;
        while (!ok) {
            String s;
            if(suggestedAmount!=null){
                // TODO: add title ...
                s = JOptionPane.showInputDialog(getBundle("BusinessActions").getString(
                        "ENTER_AMOUNT")+ account.getName(), suggestedAmount.toString());
            } else {
                s = JOptionPane.showInputDialog(getBundle("BusinessActions").getString(
                        "ENTER_AMOUNT")+ account.getName());
            }
            if (s == null || s.equals("")) {
                ok = true;
                amount = null;
            } else {
                try {
                    amount = new BigDecimal(s);
                    amount = amount.setScale(2);
                    ok = amount.compareTo(BigDecimal.ZERO)>=0;
                } catch (NumberFormatException nfe) {
                    ActionUtils.showErrorMessage(ActionUtils.INVALID_INPUT);
                }
            }
        }
        return amount;
    }

    public Integer getPercentage(){
        Integer[] percentages = vatTransactions.getVatPercentages();
        int nr = JOptionPane.showOptionDialog(null, "BTW %", "BTW %",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, percentages, null);
        if (nr != -1) {
            return percentages[nr];
        } else return null;
    }

    public BigDecimal getTaxOnNet(BigDecimal amount, Integer pct){
        BigDecimal percentage = new BigDecimal(pct).divide(new BigDecimal(100));
        return amount.multiply(percentage).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private VATTransaction.PurchaseType getPurchaseType(){
        VATTransaction.PurchaseType[] purchaseTypes = VATTransaction.PurchaseType.values();
        int nr2 = JOptionPane.showOptionDialog(null, "Purchase Type", "Purchase Type",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, purchaseTypes, null);
        return purchaseTypes[nr2];
    }

    public Contact getContact(Account account){
        Contact contact = account.getContact();
        if(contact!=null){
            return contact;
        } else {
            ContactSelector contactSelector = ContactSelector.getContactSelector(contacts);
            contactSelector.setVisible(true);
            contact = contactSelector.getSelection();
            // TODO: null check needed here?
            account.setContact(contact);
            return contact;
        }
    }

    public Transaction getTransaction() {
        return journalInputGUI.getTransaction();
    }

    public AccountDetails getAccountDetails(Account account, Journals journals){
        return AccountDetails.getAccountDetails(account, journals, journalInputGUI);
    }

    public void fireAccountDataChanged() {
        accountsGUI1.fireAccountDataChanged();
        accountsGUI2.fireAccountDataChanged();
    }

    // Setters

    public void setAccounting(Accounting accounting) {
        setContacts(accounting == null ? null : accounting.getContacts());
        setVatTransactions(accounting == null ? null : accounting.getVatTransactions());
        setAccountTypes(accounting == null ? null : accounting.getAccountTypes());
        setAccounts(accounting == null ? null : accounting.getAccounts());
//        setContacts(accounting == null ? null : accounting.getContacts());
        setJournals(accounting == null ? null : accounting.getJournals());
        accountsGUI1.setAccounting(accounting);
        accountsGUI2.setAccounting(accounting);
    }

    public void setContacts(Contacts contacts){
        this.contacts = contacts;
    }

    public void setVatTransactions(VATTransactions vatTransactions) {
        this.vatTransactions = vatTransactions;
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public void setJournals(Journals journals){
        setJournal(journals == null ? null : journals.getCurrentObject());
    }
    public void setJournal(Journal journal){
        setJournalType(journal == null ? null : journal.getType());
    }

    public void setJournalType(JournalType journalType) {
        if(journalType==null) {
            accountsGUI1.setAccountTypes(accountTypes);
            accountsGUI1.setVatType(null);
            accountsGUI2.setAccountTypes(accountTypes);
            accountsGUI2.setVatType(null);
//            setVatType(VATTransaction.VATType.NONE);
        } else {
            accountsGUI1.setAccountTypes(journalType.getDebetTypes());
            accountsGUI2.setAccountTypes(journalType.getCreditTypes());
            VATTransaction.VATType vatType = journalType.getVatType();
            if (vatType == VATTransaction.VATType.SALE) {
                accountsGUI1.setVatType(VATTransaction.VATType.CUSTOMER);
                accountsGUI2.setVatType(VATTransaction.VATType.SALE);
//                setVatType(VATTransaction.VATType.SALE); // 2 -> BTW
            } else if (vatType == VATTransaction.VATType.PURCHASE) {
                accountsGUI1.setVatType(VATTransaction.VATType.PURCHASE);
                accountsGUI2.setVatType(null);
//                setVatType(VATTransaction.VATType.PURCHASE); // 1 -> BTW
//            } else {
//                setVatType(VATTransaction.VATType.NONE);
            }
        }
    }

    // BOOK
    public void book(Account account, boolean debit, VATTransaction.VATType vatType){
        Transaction transaction = journalInputGUI.getTransaction();
        BigDecimal amount = askAmount(account, debit, transaction);
        if (amount != null) {
            Booking booking = new Booking(account, amount, debit);
            journalInputGUI.addBooking(booking);

            //
            if (vatType == VATTransaction.VATType.PURCHASE) {
                purchaseAny(transaction, booking);
            } else if (vatType == VATTransaction.VATType.CUSTOMER){
                Contact contact = getContact(account);
                transaction.setContact(contact);
            } else if (vatType == VATTransaction.VATType.SALE){
                saleAny(transaction, booking);
            }

            journalInputGUI.fireTransactionDataChanged();
        }
    }

    // PURCHASE

    public void purchaseAny(Transaction transaction, Booking booking){
        BigDecimal amount = booking.getAmount();
        boolean debit = booking.isDebit();
        // Read percentage
        Integer pct = getPercentage();
        if (pct != null) {
            BigDecimal suggestedAmount = getTaxOnNet(amount, pct);
//            if (amount.compareTo(BigDecimal.ZERO) >= 0) {
            if(debit){
                purchase(suggestedAmount, transaction, booking);
            } else {
                purchaseCN(suggestedAmount, transaction, booking);
            }
        }
    }

    public void purchase(BigDecimal suggestedVatAmount, Transaction transaction, Booking booking) {
        boolean debit = booking.isDebit();
        VATTransaction.PurchaseType purchaseType = getPurchaseType();
        Account btwAccount = getCreditAccount();
        if(btwAccount!=null) {
            BigDecimal btwAmount = askAmount(btwAccount, suggestedVatAmount);
            if(btwAmount!=null) {
                Booking vatBooking = new Booking(btwAccount, btwAmount, debit);
                transaction.addBusinessObject(vatBooking);

                VATTransaction vatTransaction = vatTransactions.purchase(booking, vatBooking, purchaseType);
                transaction.setVatTransaction(vatTransaction);
                vatTransaction.setTransaction(transaction);
            }
        }
    }

    public void purchaseCN(BigDecimal suggestedVatAmount, Transaction transaction, Booking booking){
        boolean debit = booking.isDebit();
        VATTransaction.PurchaseType purchaseType = getPurchaseType();

        Account btwAccount = getCreditCNAccount();
        if (btwAccount != null) {
            BigDecimal btwAmount = askAmount(btwAccount, suggestedVatAmount);
            if (btwAmount != null) {
                Booking bookingVat = new Booking(btwAccount, btwAmount, debit);
                transaction.addBusinessObject(bookingVat);

                VATTransaction vatTransaction = vatTransactions.purchaseCN(booking, bookingVat, purchaseType);
                transaction.setVatTransaction(vatTransaction);
                vatTransaction.setTransaction(transaction);
            }
        }
    }

    // SALE

    public void saleAny(Transaction transaction, Booking booking) {
        BigDecimal amount = booking.getAmount();
        boolean debit = booking.isDebit();
        Integer pct = getPercentage();
        if (pct != null) {
            BigDecimal suggestedAmount = getTaxOnNet(amount, pct);
            if (!debit) {
                sell(transaction, booking, suggestedAmount, pct);
            } else {
                sellCN(transaction, booking, suggestedAmount, pct);
            }
        }
    }

    private void sell(Transaction transaction, Booking booking, BigDecimal suggestedVATAmount, int pct) {
        BigDecimal amount = booking.getAmount();
        boolean debit = booking.isDebit();

        Account vatAccount = getDebitAccount();
        if(vatAccount!=null) {
            BigDecimal vatAmount = askAmount(vatAccount, suggestedVATAmount);
            if(vatAmount!=null) {
                Booking vatBooking = new Booking(vatAccount, vatAmount, debit);
                transaction.addBusinessObject(vatBooking);

                VATTransaction vatTransaction = vatTransactions.sale(booking, vatBooking, pct);
                transaction.setVatTransaction(vatTransaction);
                vatTransaction.setTransaction(transaction);

                transaction.setTurnOverAmount(amount);
                transaction.setVATAmount(vatAmount);
            }
        }
    }

    public void sellCN(Transaction transaction, Booking booking, BigDecimal suggestedVATAmount, int pct){
        BigDecimal amount = booking.getAmount();
        boolean debit = booking.isDebit();
        Account btwAccount = getDebitCNAccount();
        if (btwAccount != null) {
            BigDecimal vatAmount = askAmount(btwAccount, suggestedVATAmount);
            if (vatAmount != null) {
                Booking vatBooking = new Booking(btwAccount, vatAmount, debit);
                transaction.addBusinessObject(vatBooking);

                VATTransaction vatTransaction = vatTransactions.saleCN(booking, vatBooking, pct);
                transaction.setVatTransaction(vatTransaction);
                vatTransaction.setTransaction(transaction);

                transaction.setTurnOverAmount(amount.negate());
                transaction.setVATAmount(vatAmount.negate());
            }
        }
    }

    // Get Accounts

    private Account getCreditAccount(){
        Account btwAccount = vatTransactions.getCreditAccount();
        if(btwAccount==null){
            AccountSelector accountSelector = AccountSelector.getAccountSelector(accounts, accountTypes, SELECT_TAX_CREDIT_ACCOUNT);
            accountSelector.setVisible(true);
            btwAccount = accountSelector.getSelection();
            vatTransactions.setCreditAccount(btwAccount);
        }
        return btwAccount;
    }

    private Account getDebitAccount(){
        Account btwAccount = vatTransactions.getDebitAccount();
        if(btwAccount==null){
            AccountSelector accountSelector = AccountSelector.getAccountSelector(accounts, accountTypes, SELECT_TAX_DEBIT_ACCOUNT);
            accountSelector.setVisible(true);
            btwAccount = accountSelector.getSelection();
            vatTransactions.setDebitAccount(btwAccount);
        }
        return btwAccount;
    }

    private Account getCreditCNAccount(){
        Account btwAccount = vatTransactions.getCreditCNAccount();
        if(btwAccount==null){
            AccountSelector accountSelector = AccountSelector.getAccountSelector(accounts, accountTypes, SELECT_TAX_CREDIT_CN_ACCOUNT);
            accountSelector.setVisible(true);
            btwAccount = accountSelector.getSelection();
            vatTransactions.setCreditCNAccount(btwAccount);
        }
        return btwAccount;
    }

    private Account getDebitCNAccount(){
        Account btwAccount = vatTransactions.getDebitCNAccount();
        if(btwAccount==null){
            AccountSelector accountSelector = AccountSelector.getAccountSelector(accounts, accountTypes, SELECT_TAX_DEBIT_CN_ACCOUNT);
            accountSelector.setVisible(true);
            btwAccount = accountSelector.getSelection();
            vatTransactions.setDebitCNAccount(btwAccount);
        }
        return btwAccount;
    }
}
