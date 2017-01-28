package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Contacts.ContactSelector;
import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;

import javax.swing.*;
import javax.swing.JSplitPane;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import static java.awt.BorderLayout.CENTER;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.JSplitPane.*;

/**
 * Created by ddanneels on 28/01/2017.
 */
public class AccountInputPanel extends JPanel{
    private final JournalInputGUI journalInputGUI;
    private AccountTypes accountTypes;
    private JournalType journalType;
//    private AccountsTableGUI accountsGUI1, accountsGUI2;
    private AccountsGUI accountsGUI1, accountsGUI2;
    private VATTransactions vatTransactions = null;
    private VATTransaction.VATType vatType = null;
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

    public AccountTypes getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
    }

    public JournalType getJournalType() {
        return journalType;
    }

    public void setJournalType(JournalType journalType) {
        this.journalType = journalType;
        if(journalType==null) {
            accountsGUI1.setAccountTypes(accountTypes);
            accountsGUI2.setAccountTypes(accountTypes);
            setVatType(VATTransaction.VATType.NONE);
        } else {
            accountsGUI1.setAccountTypes(journalType.getDebetTypes());
            accountsGUI2.setAccountTypes(journalType.getCreditTypes());
            VATTransaction.VATType vatType = journalType.getVatType();
            if (vatType == VATTransaction.VATType.SALE) {
                setVatType(VATTransaction.VATType.SALE); // 2 -> BTW
            } else if (vatType == VATTransaction.VATType.PURCHASE) {
                setVatType(VATTransaction.VATType.PURCHASE); // 1 -> BTW
            } else {
                setVatType(VATTransaction.VATType.NONE);
            }
        }
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
                    ok = true;
                } catch (NumberFormatException nfe) {
                    ActionUtils.showErrorMessage(ActionUtils.INVALID_INPUT);
                }
            }
        }
        return amount;
    }

    public void book(Account account, boolean debit, AccountsGUI source){
        Transaction transaction = journalInputGUI.getTransaction();
        BigDecimal amount = askAmount(account, debit, transaction);
        if (amount != null) {
            journalInputGUI.addBooking(new Booking(account, amount, debit));
            if (vatType != VATTransaction.VATType.NONE) {
                // Read percentage
                Integer[] percentages = vatTransactions.getVatPercentages();
                int nr = JOptionPane.showOptionDialog(null, "BTW %", "BTW %",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, percentages, null);
                if (nr != -1) {
                    BigDecimal percentage = new BigDecimal(percentages[nr]).divide(new BigDecimal(100));
                    BigDecimal suggestedAmount = amount.multiply(percentage).setScale(2,BigDecimal.ROUND_HALF_UP);

                    if (vatType == VATTransaction.VATType.PURCHASE && source == accountsGUI1) {
                        if (amount.compareTo(BigDecimal.ZERO) >= 0) {
                            purchase(amount, suggestedAmount, debit);
                        } else {
                            VATTransaction.PurchaseType purchaseType = getPurchaseType();
                            Account btwAccount = getCreditCNAccount();
                            if (btwAccount != null) {
                                BigDecimal btwAmount = journalInputGUI.askAmount(btwAccount, suggestedAmount);
                                if (btwAmount != null) {
                                    journalInputGUI.addBooking(new Booking(btwAccount, btwAmount, debit));
                                    ArrayList<VATBooking> vatBookings = vatTransactions.purchaseCN(amount, btwAmount, purchaseType);
                                    journalInputGUI.addVATBookings(vatBookings);
                                }
                            }
                        }
                    } else if (vatType == VATTransaction.VATType.SALE && source == accountsGUI2) {
                        // FIXME: contact should be linked to Revenue/Cost (==account) but to Debit(Supplier) / Credit(Customer)
                        // but amount to add to TurnOver is the Revenue/Cost amount (VAT excl.)
                        Contact contact = getContact(account);
                        if(amount.compareTo(BigDecimal.ZERO)>=0) {
                            sell(contact, amount, suggestedAmount, debit, percentages[nr]);
                        } else {
                            Account btwAccount = getDebitCNAccount();
                            if(btwAccount!=null) {
                                BigDecimal btwAmount = journalInputGUI.askAmount(btwAccount, suggestedAmount);
                                if(btwAmount!=null) {
                                    journalInputGUI.addBooking(new Booking(btwAccount, btwAmount, debit));
                                    ArrayList<VATBooking> vatBookings = vatTransactions.saleCN(amount, btwAmount);
                                    journalInputGUI.addVATBookings(vatBookings);
                                }
                            }
                            sell(contact, amount, suggestedAmount, debit, percentages[nr]);
                        }
                    }
                }
            }
        }
    }

    private void sell(Contact contact, BigDecimal amount, BigDecimal suggestedAmount, boolean debit, int pct) {
        Account vatAccount = getDebitAccount();
        if(vatAccount!=null) {
            BigDecimal vatAmount = journalInputGUI.askAmount(vatAccount, suggestedAmount);
            if(vatAmount!=null) {
                journalInputGUI.addBooking(new Booking(vatAccount, vatAmount, debit));
                ArrayList<VATBooking> vatBookings = vatTransactions.sale(amount, vatAmount, pct);
                journalInputGUI.addVATBookings(vatBookings);
                journalInputGUI.setTurnOverAmount(amount);
                journalInputGUI.setVATAmount(vatAmount);
                journalInputGUI.setContact(contact);
            }
        }
    }

    private void purchase(BigDecimal amount, BigDecimal suggestedAmount, boolean debit) {
        VATTransaction.PurchaseType purchaseType = getPurchaseType();
        Account btwAccount = getCreditAccount();
        if(btwAccount!=null) {
            BigDecimal btwAmount = journalInputGUI.askAmount(btwAccount, suggestedAmount);
            if(btwAmount!=null) {
                journalInputGUI.addBooking(new Booking(btwAccount, btwAmount, debit));
                ArrayList<VATBooking> vatBookings = vatTransactions.purchase(amount, btwAmount, purchaseType);
                journalInputGUI.addVATBookings(vatBookings);
            }
        }
    }

    private Account getCreditAccount(){
        Account btwAccount = vatTransactions.getCreditAccount();
        if(btwAccount==null){
            AccountSelector accountSelector = AccountSelector.getAccountSelector(accounts, accountTypes);
            accountSelector.setVisible(true);
            btwAccount = accountSelector.getSelection();
            vatTransactions.setCreditAccount(btwAccount);
        }
        return btwAccount;
    }

    private Account getDebitAccount(){
        Account btwAccount = vatTransactions.getDebitAccount();
        if(btwAccount==null){
            AccountSelector accountSelector = AccountSelector.getAccountSelector(accounts, accountTypes);
            accountSelector.setVisible(true);
            btwAccount = accountSelector.getSelection();
            vatTransactions.setDebitAccount(btwAccount);
        }
        return btwAccount;
    }

    private VATTransaction.PurchaseType getPurchaseType(){
        VATTransaction.PurchaseType[] purchaseTypes = VATTransaction.PurchaseType.values();
        int nr2 = JOptionPane.showOptionDialog(null, "Purchase Type", "Purchase Type",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, purchaseTypes, null);
        return purchaseTypes[nr2];
    }

    private Contact getContact(Account account){
        Contact contact = account.getContact();
        if(contact!=null){
            return contact;
        } else {
            ContactSelector contactSelector = ContactSelector.getContactSelector(contacts);
            contactSelector.setVisible(true);
            contact =  contactSelector.getSelection();
            // TODO: null check needed here?
            account.setContact(contact);
            return contact;
        }
    }

    public void setContacts(Contacts contacts){
        this.contacts = contacts;
    }

    private Account getCreditCNAccount(){
        Account btwAccount = vatTransactions.getCreditCNAccount();
        if(btwAccount==null){
            AccountSelector accountSelector = AccountSelector.getAccountSelector(accounts, accountTypes);
            accountSelector.setVisible(true);
            btwAccount = accountSelector.getSelection();
            vatTransactions.setCreditCNAccount(btwAccount);
        }
        return btwAccount;
    }

    private Account getDebitCNAccount(){
        Account btwAccount = vatTransactions.getDebitCNAccount();
        if(btwAccount==null){
            AccountSelector accountSelector = AccountSelector.getAccountSelector(accounts, accountTypes);
            accountSelector.setVisible(true);
            btwAccount = accountSelector.getSelection();
            vatTransactions.setDebitCNAccount(btwAccount);
        }
        return btwAccount;
    }

//    public VATTransactions getVatTransactions() {
//        return vatTransactions;
//    }

    public void setVatTransactions(VATTransactions vatTransactions) {
        this.vatTransactions = vatTransactions;
    }

    public VATTransaction.VATType getVatType() {
        return vatType;
    }

    public void setVatType(VATTransaction.VATType vatType) {
        this.vatType = vatType;
    }

    public void setAccounting(Accounting accounting) {
        setContacts(accounting == null ? null : accounting.getContacts());
        setVatTransactions(accounting == null ? null : accounting.getVatTransactions());
        setAccountTypes(accounting == null ? null : accounting.getAccountTypes());
        setAccounts(accounting == null ? null : accounting.getAccounts());
        setContacts(accounting == null ? null : accounting.getContacts());
        setJournals(accounting == null ? null : accounting.getJournals());
        accountsGUI1.setAccounting(accounting);
        accountsGUI2.setAccounting(accounting);
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
}
