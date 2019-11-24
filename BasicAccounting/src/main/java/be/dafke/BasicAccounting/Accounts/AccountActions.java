package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Contacts.ContactSelectorDialog;
import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.*;

import javax.swing.JOptionPane;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class AccountActions {
    public static final String SELECT_TAX_CREDIT_ACCOUNT = "Select VAT Account for Purchases";
    public static final String SELECT_TAX_DEBIT_ACCOUNT = "Select VAT Account for Sales";
    public static final String SELECT_TAX_CREDIT_CN_ACCOUNT = "Select VAT Account for Purchases CN's";
    public static final String SELECT_TAX_DEBIT_CN_ACCOUNT = "Select VAT Account for Sales CN's";

    public static void book(Account account, boolean debit, VATTransaction.VATType vatType, Accounting accounting, Component component){
        Transaction transaction = Main.getTransaction();
        BigDecimal amount = askAmount(account, debit, transaction, component);
        if (amount != null) {
            Booking booking = new Booking(account, amount, debit);
            Main.addBooking(booking);

            //
            if (vatType == VATTransaction.VATType.PURCHASE) {
                purchaseAny(booking, accounting, component);
            } else if (vatType == VATTransaction.VATType.CUSTOMER){
                Contact contact = getContact(account, accounting, Contact.ContactType.CUSTOMERS, component);
                transaction.setContact(contact);
            } else if (vatType == VATTransaction.VATType.SALE){
                saleAny(transaction, booking, accounting, component);
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

    public static Integer getPercentage(Accounting accounting, Component component){
        VATTransactions vatTransactions = accounting.getVatTransactions();
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
    public static void addPurchaseVatTransaction(Booking booking, PurchaseType purchaseType){
        BigDecimal amount = booking.getAmount();
        VATBooking costBooking = purchaseType.getCostBooking(amount);
        booking.addVatBooking(costBooking);
    }

    public static void addPurchaseVatVatTransaction(Booking booking){
        BigDecimal amount = booking.getAmount();
        VATBooking vatBooking = PurchaseType.getVatBooking(amount);
        booking.addVatBooking(vatBooking);
    }

    public static void addPurchaseCnVatTransaction(Booking booking, PurchaseType purchaseType){
        BigDecimal amount = booking.getAmount();

        VATBooking costBooking = purchaseType.getCostBooking(amount.negate());
        booking.addVatBooking(costBooking);

        VATBooking CNCostBooking = PurchaseCNType.VAT_85.getCostBooking(amount);
        booking.addVatBooking(CNCostBooking);
    }

    public static Booking createPurchaseVatBooking(Accounting accounting, BigDecimal vatAmount){
        Account vatAccount = getVatCreditAccount(accounting);
        Booking booking = new Booking(vatAccount, vatAmount, true);
        addPurchaseVatVatTransaction(booking);
        return booking;
    }

    public static Booking createPurchaseCnVatBooking(Accounting accounting, BigDecimal vatAmount){
        Account vatAccount = getVatCreditAccount(accounting);
        Booking bookingVat = new Booking(vatAccount, vatAmount, false);
        VATBooking vatBooking = PurchaseCNType.getPurchaseCnVatBooking(vatAmount);
        bookingVat.addVatBooking(vatBooking);
        return bookingVat;
    }


    public static void addIntraComPurchaseVatBooking(Booking booking){
        BigDecimal amount = booking.getAmount();
        VATBooking intraComBooking = PurchaseType.getIntraComBooking(amount);
        booking.addVatBooking(intraComBooking);
    }

    public static Booking createIntraComPurchaseVatBooking(Accounting accounting,BigDecimal vatAmount){
        Account vatAccount = getVatDebitAccount(accounting);
        Booking bookingVatIntracom = new Booking(vatAccount, vatAmount, false);
        VATBooking intraComVatBooking = PurchaseType.getIntraComVatBooking(vatAmount);
        bookingVatIntracom.addVatBooking(intraComVatBooking);
        return bookingVatIntracom;
    }

    public static void purchaseAny(Booking booking, Accounting accounting, Component component){
        BigDecimal amount = booking.getAmount();
        boolean debit = booking.isDebit();
        if(debit) {
            // Ordinary Purchase
            PurchaseType purchaseType = askPurchaseType(component);

            if (purchaseType != null && purchaseType != PurchaseType.VR) {
                addPurchaseVatTransaction(booking, purchaseType);

                int choice = JOptionPane.showConfirmDialog(component, "Intracommunautair?", "Intracommunautair?", JOptionPane.YES_NO_OPTION);
                boolean intracom = JOptionPane.YES_OPTION==choice;

                if(intracom) {
                    addIntraComPurchaseVatBooking(booking);
                }

                Integer pct = intracom?21:getPercentage(accounting, component);
                if (pct != 0) {
                    BigDecimal suggestedAmount = getTaxOnNet(amount, pct);

                    Account vatAccount = getVatCreditAccount(accounting);
                    BigDecimal vatAmount = askAmount(vatAccount, suggestedAmount, component);

                    if (vatAmount != null) {
                        Booking bookingVat = createPurchaseVatBooking(accounting, vatAmount);
                        Main.addBooking(bookingVat);

                        if(intracom){
                            Booking bookingVatIntracom = createIntraComPurchaseVatBooking(accounting, vatAmount);
                            Main.addBooking(bookingVatIntracom);
                        }
                    }
                }
            }
        } else {
            // CN
            PurchaseType purchaseType = askPurchaseType(component);
            if (purchaseType != null && purchaseType != PurchaseType.VR) {
                // 81/82/83
                addPurchaseCnVatTransaction(booking, purchaseType);

                Account vatAccount = getVatCreditCNAccount(accounting);
                BigDecimal vatAmount = askAmount(vatAccount, null, component);
                if (vatAmount != null) {
                    Booking bookingVat = createPurchaseCnVatBooking(accounting, vatAmount);
                    Main.addBooking(bookingVat);
                }
            }
        }
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

    public static void saleAny(Transaction transaction, Booking booking, Accounting accounting, Component component) {
        boolean debit = booking.isDebit();
        BigDecimal vatAmount = BigDecimal.ZERO.setScale(2);
        if (!debit){
            // Ordinary Sale
            SalesType salesType = askSalesType(component);
            if (salesType != null) {
                addSalesVatTransaction(booking, salesType);

                Integer pct = salesType.getPct();
                if (pct != null && pct != 0) {
                    Account vatAccount = getVatDebitAccount(accounting);
                    BigDecimal amount = booking.getAmount();
                    BigDecimal suggestedAmount = getTaxOnNet(amount, pct);

                    vatAmount = askAmount(vatAccount, suggestedAmount, component);
                    if (vatAmount != null) {
                        Booking bookingVat = createSalesVatBooking(accounting, vatAmount);
                        Main.addBooking(bookingVat);
                    }
                }
            }
        } else {
            // CN
            addSalesCnVatTransaction(booking);

            Account vatAccount = getVatDebitAccount(accounting);

            // TODO? ask percentage and calculate suggested amount ?
            vatAmount = askAmount(vatAccount, null, component);
            if(vatAmount!=null) {
                Booking bookingVat = createSalesCnVatBooking(accounting, vatAmount);
                Main.addBooking(bookingVat);
            }
        }

        if (debit){
            BigDecimal amount = booking.getAmount();
            transaction.setTurnOverAmount(amount.negate());
            if(vatAmount!=null)
                transaction.setVATAmount(vatAmount.negate());
        } else {
            BigDecimal amount = booking.getAmount();
            transaction.setTurnOverAmount(amount);
            if(vatAmount!=null)
                transaction.setVATAmount(vatAmount);
        }
    }

    public static Booking createSalesVatBooking(Accounting accounting, BigDecimal vatAmount){
        Account vatAccount = getVatDebitAccount(accounting);
        Booking booking = new Booking(vatAccount, vatAmount, false);
        addSalesVatVatTransaction(booking);
        return booking;
    }

    public static Booking createSalesCnVatBooking(Accounting accounting, BigDecimal vatAmount){
        Account vatAccount = getVatDebitAccount(accounting);
        Booking bookingVat = new Booking(vatAccount, vatAmount, true);
        VATBooking vatBooking = SalesCNType.getSalesCnVatBooking(vatAmount);
        bookingVat.addVatBooking(vatBooking);
        return bookingVat;
    }

    public static void addSalesVatTransaction(Booking booking, SalesType salesType){
        BigDecimal amount = booking.getAmount();
        VATBooking revenueBooking = salesType.getRevenueBooking(amount);
        booking.addVatBooking(revenueBooking);
    }

    public static void addSalesVatVatTransaction(Booking booking){
        BigDecimal amount = booking.getAmount();
        VATBooking vatBooking = SalesType.getVatBooking(amount);
        booking.addVatBooking(vatBooking);
    }

    public static void addSalesCnVatTransaction(Booking booking){
        BigDecimal amount = booking.getAmount();
        VATBooking cnRevenueBooking = SalesCNType.VAT_49.getSalesCnRevenueBooking(amount);
        booking.addVatBooking(cnRevenueBooking);
    }

    // Get Accounts

    public static ArrayList<AccountType> getTaxDebitAccounts(Accounting accounting){
        AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.TAXDEBIT);
        ArrayList<AccountType> list = new ArrayList<>();
        list.add(accountType);
        return list;
    }

    public static ArrayList<AccountType> getTaxCreditAccounts(Accounting accounting){
        AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.TAXCREDIT);
        ArrayList<AccountType> list = new ArrayList<>();
        list.add(accountType);
        return list;
    }

    public static Account getVatDebitAccount(Accounting accounting){
        VATTransactions vatTransactions = accounting.getVatTransactions();
        Account debitAccount = vatTransactions.getDebitAccount();
        if (debitAccount == null){
            ArrayList<AccountType> list = getTaxDebitAccounts(accounting);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, SELECT_TAX_DEBIT_ACCOUNT);
            dialog.setVisible(true);
            debitAccount = dialog.getSelection();
            vatTransactions.setDebitAccount(debitAccount);
        }
        return debitAccount;
    }

    public static Account getVatCreditAccount(Accounting accounting){
        VATTransactions vatTransactions = accounting.getVatTransactions();
        Account creditAccount = vatTransactions.getCreditAccount();
        if (creditAccount == null){
            ArrayList<AccountType> list = getTaxCreditAccounts(accounting);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, SELECT_TAX_CREDIT_ACCOUNT);
            dialog.setVisible(true);
            creditAccount = dialog.getSelection();
            vatTransactions.setCreditAccount(creditAccount);
        }
        return creditAccount;
    }

    private static Account getDebitCNAccount(Accounting accounting){
        VATTransactions vatTransactions = accounting.getVatTransactions();
        Account debitCNAccount = vatTransactions.getDebitCNAccount();
        if(debitCNAccount==null){
            ArrayList<AccountType> list = getTaxDebitAccounts(accounting);
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounting.getAccounts(), list, SELECT_TAX_DEBIT_CN_ACCOUNT);
            accountSelectorDialog.setVisible(true);
            debitCNAccount = accountSelectorDialog.getSelection();
            vatTransactions.setDebitCNAccount(debitCNAccount);
        }
        return debitCNAccount;
    }

    public static Account getVatCreditCNAccount(Accounting accounting){
        VATTransactions vatTransactions = accounting.getVatTransactions();
        Account creditCNAccount = vatTransactions.getCreditCNAccount();
        if(creditCNAccount==null){
            ArrayList<AccountType> list = getTaxCreditAccounts(accounting);
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounting.getAccounts(), list, SELECT_TAX_CREDIT_CN_ACCOUNT);
            accountSelectorDialog.setVisible(true);
            creditCNAccount = accountSelectorDialog.getSelection();
            vatTransactions.setCreditCNAccount(creditCNAccount);
        }
        return creditCNAccount;
    }

    public static Contact getContact(Account account, Accounting accounting, Contact.ContactType contactType, Component component){
        Contact contact = account.getContact();
        if(contact!=null){
            return contact;
        } else {
            ContactSelectorDialog contactSelectorDialog = ContactSelectorDialog.getContactSelector(accounting, contactType);
            contactSelectorDialog.setLocation(component.getLocationOnScreen());
            contactSelectorDialog.setVisible(true);
            contact = contactSelectorDialog.getSelection();
            // TODO: null check needed here?
            account.setContact(contact);
            return contact;
        }
    }
}
