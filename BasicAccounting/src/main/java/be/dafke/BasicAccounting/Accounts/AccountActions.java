package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Contacts.ContactSelectorDialog;
import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;

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

    public static PurchaseType askPurchaseType(Component component){
        PurchaseType[] purchaseTypes = PurchaseType.values();
        int nr = JOptionPane.showOptionDialog(component, "BTW %", "BTW %",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, purchaseTypes, null);
        if (nr != -1) {
            return purchaseTypes[nr];
        } else return null;
    }

    // PURCHASE



    public static void purchaseAny(Transaction transaction, Booking booking, VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes, Component component){
        BigDecimal amount = booking.getAmount();
        boolean debit = booking.isDebit();
        VATTransaction vatTransaction = new VATTransaction();
        if(debit) {
            PurchaseType purchaseType = askPurchaseType(component);

            if (purchaseType != null && purchaseType != PurchaseType.VR) {
                int choice = JOptionPane.showConfirmDialog(component, "Intracommunautair?", "Intracommunautair?", JOptionPane.YES_NO_OPTION);
                boolean intracom = JOptionPane.YES_OPTION==choice;

                VATBooking costBooking = purchaseType.getCostBooking(amount);
                booking.addVatBooking(costBooking);
                vatTransaction.addBusinessObject(costBooking);

                if(intracom){
                    VATBooking intraComBooking = PurchaseType.getIntraComBooking(amount);
                    booking.addVatBooking(intraComBooking);
                    vatTransaction.addBusinessObject(intraComBooking);
                }

                Integer pct = getPercentage(vatTransactions, component);
                if (pct != null && pct != 0) {
                    BigDecimal suggestedAmount = getTaxOnNet(amount, pct);

                    Account vatAccount = getCreditAccount(vatTransactions, accounts, accountTypes);
                    BigDecimal vatAmount = askAmount(vatAccount, suggestedAmount, component);

                    if (vatAmount != null) {
                        Booking bookingVat = new Booking(vatAccount, vatAmount, debit);

                        VATBooking vatBooking = purchaseType.getVatBooking(vatAmount);
                        bookingVat.addVatBooking(vatBooking);
                        vatTransaction.addBusinessObject(vatBooking);

                        Main.addBooking(bookingVat);
                    }
                }
            }
        } else {
            PurchaseType purchaseType = askPurchaseType(component);
            if (purchaseType != null && purchaseType != PurchaseType.VR) {
                // 81/82/83
                VATBooking costBooking = purchaseType.getCostBooking(amount.negate());
                booking.addVatBooking(costBooking);
                vatTransaction.addBusinessObject(costBooking);

                VATBooking CNCostBooking = PurchaseCNType.VAT_85.getCostBooking(amount);
                booking.addVatBooking(CNCostBooking);
                vatTransaction.addBusinessObject(CNCostBooking);

                Account vatAccount = getCreditCNAccount(vatTransactions, accounts, accountTypes);
                BigDecimal vatAmount = askAmount(vatAccount, null, component);
                if (vatAmount != null) {
                    Booking bookingVat = new Booking(vatAccount, vatAmount, debit);

                    VATBooking CNVATBooking = PurchaseCNType.getPurchaseCnVatBooking(vatAmount);
                    bookingVat.addVatBooking(CNVATBooking);
                    vatTransaction.addBusinessObject(CNVATBooking);

                    Main.addBooking(bookingVat);
                }
            }
        }
        transaction.addVatTransaction(vatTransaction);
        vatTransaction.setTransaction(transaction);
    }

    // SALE

    public static SalesType askSalesType(Component component){
        SalesType[] salesTypes = SalesType.values();
        int nr = JOptionPane.showOptionDialog(component, "BTW %", "BTW %",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, salesTypes, null);
        if (nr != -1) {
            return salesTypes[nr];
        } else return null;
    }

    public static void saleAny(Transaction transaction, Booking booking, VATTransactions vatTransactions, Accounts accounts, ArrayList<AccountType> accountTypes, Component component) {
        BigDecimal amount = booking.getAmount();
        boolean debit = booking.isDebit();
        VATTransaction vatTransaction = new VATTransaction();
        BigDecimal vatAmount = BigDecimal.ZERO.setScale(2);
        if (debit){
            // CN

            VATBooking cnRevenueBooking = SalesCNType.VAT_49.getSalesCnRevenueBooking(amount);
            booking.addVatBooking(cnRevenueBooking);
            vatTransaction.addBusinessObject(cnRevenueBooking); // TODO: get rid of this

            Account vatAccount = getDebitAccount(vatTransactions, accounts, accountTypes);
            // TODO? ask percentage and calculate suggested amount ?
            vatAmount = askAmount(vatAccount, null, component);

            if(vatAmount!=null) {
                Booking bookingVat = new Booking(vatAccount, vatAmount, booking.isDebit());

                VATBooking cnVatBooking = SalesCNType.getSalesCnVatBooking(vatAmount);
                bookingVat.addVatBooking(cnVatBooking);
                vatTransaction.addBusinessObject(cnVatBooking);

                Main.addBooking(bookingVat);
            }
        } else {
            SalesType salesType = askSalesType(component);
            if (salesType != null) {

                VATBooking revenueBooking = salesType.getRevenueBooking(amount);
                booking.addVatBooking(revenueBooking);
                vatTransaction.addBusinessObject(revenueBooking);

                Integer pct = salesType.getPct();

                if (pct != null && pct != 0) {
                    Account vatAccount = getDebitCNAccount(vatTransactions, accounts, accountTypes);

                    BigDecimal suggestedAmount = getTaxOnNet(amount, pct);
                    vatAmount = askAmount(vatAccount, suggestedAmount, component);

                    if (vatAmount != null) {
                        Booking bookingVat = new Booking(vatAccount, vatAmount, booking.isDebit());

                        VATBooking vatBooking = salesType.getVatBooking(vatAmount);
                        bookingVat.addVatBooking(vatBooking);
                        vatTransaction.addBusinessObject(vatBooking);

                        Main.addBooking(bookingVat);
                    }
                }
            }
        }
        transaction.addVatTransaction(vatTransaction);
        vatTransaction.setTransaction(transaction);

        if (debit){
            transaction.setTurnOverAmount(amount.negate());
            if(vatAmount!=null)
                transaction.setVATAmount(vatAmount.negate());
        } else {
            transaction.setTurnOverAmount(amount);
            if(vatAmount!=null)
                transaction.setVATAmount(vatAmount);
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
