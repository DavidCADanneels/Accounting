package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Contacts.ContactSelectorDialog;
import be.dafke.BasicAccounting.Journals.JournalEditPanel;
import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.BusinessModel.Transaction;
import be.dafke.BusinessModel.VATTransaction;
import be.dafke.BusinessModel.VATTransactions;

import javax.swing.JOptionPane;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 1/05/2017.
 */
public class AccountActions {
    public static final String SELECT_TAX_CREDIT_ACCOUNT = "select Tax Credit Account";
    public static final String SELECT_TAX_DEBIT_ACCOUNT = "select Tax Debit Account";
    public static final String SELECT_TAX_CREDIT_CN_ACCOUNT = "select Tax Credit CN Account";
    public static final String SELECT_TAX_DEBIT_CN_ACCOUNT = "select Tax Debit CN Account";

    public static void book(Account account, boolean debit, VATTransaction.VATType vatType, VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes, Contacts contacts, Component component){
        Transaction transaction = Main.getTransaction();
        BigDecimal amount = askAmount(account, debit, transaction, component);
        if (amount != null) {
            Booking booking = new Booking(account, amount, debit);
            Main.addBooking(booking);

            //
            if (vatType == VATTransaction.VATType.PURCHASE) {
                purchaseAny(transaction, booking, vatTransactions, accounts, accountTypes, component);
            } else if (vatType == VATTransaction.VATType.CUSTOMER){
                Contact contact = getContact(account, contacts, Contact.ContactType.CUSTOMERS, component);
                transaction.setContact(contact);
            } else if (vatType == VATTransaction.VATType.SALE){
                saleAny(transaction, booking, vatTransactions, accounts, accountTypes, component);
            }
            Main.fireTransactionInputDataChanged();
        }
    }

    public static BigDecimal askAmount(Account account, boolean debit, Transaction transaction, Component component) {
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
        return askAmount(account, suggestedAmount, component);
    }

    public static BigDecimal askAmount(Account account, BigDecimal suggestedAmount, Component component){
        boolean ok = false;
        BigDecimal amount = null;
        while (!ok) {
            String s;
            if(suggestedAmount!=null){
                // TODO: add title ...
                s = JOptionPane.showInputDialog(component, getBundle("BusinessActions").getString(
                        "ENTER_AMOUNT")+ account.getName(), suggestedAmount.toString());
            } else {
                s = JOptionPane.showInputDialog(component, getBundle("BusinessActions").getString(
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
                    ActionUtils.showErrorMessage(component, ActionUtils.INVALID_INPUT);
                }
            }
        }
        return amount;
    }

    public static Integer getPercentage(VATTransactions vatTransactions, Component component){
        Integer[] percentages = vatTransactions.getVatPercentages();
        int nr = JOptionPane.showOptionDialog(component, "BTW %", "BTW %",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, percentages, null);
        if (nr != -1) {
            return percentages[nr];
        } else return null;
    }

    public static BigDecimal getTaxOnNet(BigDecimal amount, Integer pct){
        BigDecimal percentage = new BigDecimal(pct).divide(new BigDecimal(100));
        return amount.multiply(percentage).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private static VATTransaction.PurchaseType getPurchaseType(Component component){
        VATTransaction.PurchaseType[] purchaseTypes = VATTransaction.PurchaseType.values();
        int nr2 = JOptionPane.showOptionDialog(component, "Purchase Type", "Purchase Type",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, purchaseTypes, null);
        return purchaseTypes[nr2];
    }

    // PURCHASE

    public static void purchaseAny(Transaction transaction, Booking booking, VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes, Component component){
        BigDecimal amount = booking.getAmount();
        boolean debit = booking.isDebit();
        // Read percentage
        Integer pct = getPercentage(vatTransactions, component);
        if (pct != null && pct != 0) {
            BigDecimal suggestedAmount = getTaxOnNet(amount, pct);
//            if (amount.compareTo(BigDecimal.ZERO) >= 0) {
            if(debit){
                purchase(suggestedAmount, transaction, booking, vatTransactions, accounts, accountTypes, component);
            } else {
                purchaseCN(suggestedAmount, transaction, booking, vatTransactions, accounts, accountTypes, component);
            }
        }
    }

    public static void purchase(BigDecimal suggestedVatAmount, Transaction transaction, Booking booking, VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes, Component component) {
        boolean debit = booking.isDebit();
        VATTransaction.PurchaseType purchaseType = getPurchaseType(component);
        Account btwAccount = getCreditAccount(vatTransactions, accounts, accountTypes);
        if(btwAccount!=null) {
            BigDecimal btwAmount = askAmount(btwAccount, suggestedVatAmount, component);
            if(btwAmount!=null) {
                Booking vatBooking = new Booking(btwAccount, btwAmount, debit);
                transaction.addBusinessObject(vatBooking);

                VATTransaction vatTransaction = vatTransactions.purchase(booking, vatBooking, purchaseType);
                transaction.addVatTransaction(vatTransaction);
                vatTransaction.setTransaction(transaction);
            }
        }
    }

    public static void purchaseCN(BigDecimal suggestedVatAmount, Transaction transaction, Booking booking, VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes, Component component){
        boolean debit = booking.isDebit();
        VATTransaction.PurchaseType purchaseType = getPurchaseType(component);

        Account btwAccount = getCreditCNAccount(vatTransactions, accounts, accountTypes);
        if (btwAccount != null) {
            BigDecimal btwAmount = askAmount(btwAccount, suggestedVatAmount, component);
            if (btwAmount != null) {
                Booking bookingVat = new Booking(btwAccount, btwAmount, debit);
                transaction.addBusinessObject(bookingVat);

                VATTransaction vatTransaction = vatTransactions.purchaseCN(booking, bookingVat, purchaseType);
                transaction.addVatTransaction(vatTransaction);
                vatTransaction.setTransaction(transaction);
            }
        }
    }

    // SALE

    public static void saleAny(Transaction transaction, Booking booking, VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes, Component component) {
        BigDecimal amount = booking.getAmount();
        boolean debit = booking.isDebit();
        Integer pct = getPercentage(vatTransactions, component);
        if (pct != null) {
            BigDecimal suggestedAmount = getTaxOnNet(amount, pct);
            if (!debit) {
                sell(transaction, booking, suggestedAmount, pct, vatTransactions, accounts, accountTypes, component);
            } else {
                sellCN(transaction, booking, suggestedAmount, pct, vatTransactions, accounts, accountTypes, component);
            }
        }
    }

    private static void sell(Transaction transaction, Booking booking, BigDecimal suggestedVATAmount, int pct, VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes, Component component) {
        BigDecimal amount = booking.getAmount();
        boolean debit = booking.isDebit();

        Account vatAccount = getDebitAccount(vatTransactions, accounts, accountTypes);
        if(vatAccount!=null) {
            BigDecimal vatAmount = askAmount(vatAccount, suggestedVATAmount, component);
            if(vatAmount!=null) {
                Booking vatBooking = new Booking(vatAccount, vatAmount, debit);
                transaction.addBusinessObject(vatBooking);

                VATTransaction vatTransaction = vatTransactions.sale(booking, vatBooking, pct);
                transaction.addVatTransaction(vatTransaction);
                vatTransaction.setTransaction(transaction);

                transaction.setTurnOverAmount(amount);
                transaction.setVATAmount(vatAmount);
            }
        }
    }

    public static void sellCN(Transaction transaction, Booking booking, BigDecimal suggestedVATAmount, int pct, VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes, Component component){
        BigDecimal amount = booking.getAmount();
        boolean debit = booking.isDebit();
        Account btwAccount = getDebitCNAccount(vatTransactions, accounts, accountTypes);
        if (btwAccount != null) {
            BigDecimal vatAmount = askAmount(btwAccount, suggestedVATAmount, component);
            if (vatAmount != null) {
                Booking vatBooking = new Booking(btwAccount, vatAmount, debit);
                transaction.addBusinessObject(vatBooking);

                VATTransaction vatTransaction = vatTransactions.saleCN(booking, vatBooking, pct);
                transaction.addVatTransaction(vatTransaction);
                vatTransaction.setTransaction(transaction);

                transaction.setTurnOverAmount(amount.negate());
                transaction.setVATAmount(vatAmount.negate());
            }
        }
    }

    // Get Accounts

    private static Account getCreditAccount(VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes){
        Account btwAccount = vatTransactions.getCreditAccount();
        if(btwAccount==null){
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, SELECT_TAX_CREDIT_ACCOUNT);
            accountSelectorDialog.setVisible(true);
            btwAccount = accountSelectorDialog.getSelection();
            vatTransactions.setCreditAccount(btwAccount);
        }
        return btwAccount;
    }

    private static Account getDebitAccount(VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes){
        Account btwAccount = vatTransactions.getDebitAccount();
        if(btwAccount==null){
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, SELECT_TAX_DEBIT_ACCOUNT);
            accountSelectorDialog.setVisible(true);
            btwAccount = accountSelectorDialog.getSelection();
            vatTransactions.setDebitAccount(btwAccount);
        }
        return btwAccount;
    }

    private static Account getCreditCNAccount(VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes){
        Account btwAccount = vatTransactions.getCreditCNAccount();
        if(btwAccount==null){
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, SELECT_TAX_CREDIT_CN_ACCOUNT);
            accountSelectorDialog.setVisible(true);
            btwAccount = accountSelectorDialog.getSelection();
            vatTransactions.setCreditCNAccount(btwAccount);
        }
        return btwAccount;
    }

    private static Account getDebitCNAccount(VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes){
        Account btwAccount = vatTransactions.getDebitCNAccount();
        if(btwAccount==null){
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, SELECT_TAX_DEBIT_CN_ACCOUNT);
            accountSelectorDialog.setVisible(true);
            btwAccount = accountSelectorDialog.getSelection();
            vatTransactions.setDebitCNAccount(btwAccount);
        }
        return btwAccount;
    }

    public static Contact getContact(Account account, Contacts contacts, Contact.ContactType contactType, Component component){
        Contact contact = account.getContact();
        if(contact!=null){
            return contact;
        } else {
            ContactSelectorDialog contactSelectorDialog = ContactSelectorDialog.getContactSelector(contacts, contactType);
            contactSelectorDialog.setLocation(component.getLocationOnScreen());
            contactSelectorDialog.setVisible(true);
            contact = contactSelectorDialog.getSelection();
            // TODO: null check needed here?
            account.setContact(contact);
            return contact;
        }
    }
}
