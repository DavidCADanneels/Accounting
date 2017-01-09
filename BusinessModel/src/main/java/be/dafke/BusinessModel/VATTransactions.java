package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.MustBeRead;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static be.dafke.BusinessModel.MortgageTransaction.NR;
import static be.dafke.BusinessModel.VATField.AMOUNT;

/**
 * Created by ddanneels on 25/12/2016.
 */
public class VATTransactions extends BusinessCollection<VATTransaction> implements MustBeRead {
    public static final String VATFIELD = "VATField";
    public static final String VAT_FIELDS = "VATFields";
    public static final String VAT_TRANSACTIONS = "VATTransactions";
    public static final String DEBIT_ACCOUNT = "DebitAccount";
    public static final String CREDIT_ACCOUNT = "CreditAccount";
    public static final String DEBIT_CN_ACCOUNT = "DebitCNAccount";
    public static final String CREDIT_CN_ACCOUNT = "CreditCNAccount";
    private final VATFields vatFields;
    private Account creditAccount, debitAccount, creditCNAccount, debitCNAccount;
    private Accounts accounts;
    private Integer[] vatPercentages = new Integer[]{0, 6, 12, 21};
//    private VATTransaction vatTransaction = new VATTransaction();

    public Integer[] getVatPercentages() {
        return vatPercentages;
    }

    public void setCreditAccount(Account creditAccount) {
        this.creditAccount = creditAccount;
    }

    public void setDebitAccount(Account debitAccount) {
        this.debitAccount = debitAccount;
    }

    public Account getCreditAccount() {
        return creditAccount;
    }

    public Account getDebitAccount() {
        return debitAccount;
    }

    public Account getCreditCNAccount() {
        return creditCNAccount;
    }

    public void setCreditCNAccount(Account creditCNAccount) {
        this.creditCNAccount = creditCNAccount;
    }

    public Account getDebitCNAccount() {
        return debitCNAccount;
    }

    public void setDebitCNAccount(Account debitCNAccount) {
        this.debitCNAccount = debitCNAccount;
    }

    public VATTransactions(Accounts accounts, VATFields vatFields) {
        setName(VAT_TRANSACTIONS);
        this.accounts = accounts;
        this.vatFields = vatFields;
    }

    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<>();
        keySet.add(NR);
        keySet.add(AMOUNT);
        return keySet;
    }

    @Override
    public String getChildType() {
        return VATFIELD;
    }

    @Override
    public VATTransaction createNewChild(TreeMap<String, String> properties) {

        return null;
    }

    @Override
    public Set<String> getExtraFields() {
        Set<String> set = new TreeSet<>();
        set.add(DEBIT_ACCOUNT);
        set.add(CREDIT_ACCOUNT);
        set.add(DEBIT_CN_ACCOUNT);
        set.add(CREDIT_CN_ACCOUNT);
        return set;
    }

    @Override
    public void setExtraProperties(TreeMap<String,String> extraProperties) {
        String debitAccountString = extraProperties.get(DEBIT_ACCOUNT);
        if(debitAccountString!=null) {
            debitAccount = accounts.getBusinessObject(debitAccountString);
        }
        String creditAccountString = extraProperties.get(CREDIT_ACCOUNT);
        if(creditAccountString!=null) {
            creditAccount = accounts.getBusinessObject(creditAccountString);
        }
        String debitCNAccountString = extraProperties.get(DEBIT_CN_ACCOUNT);
        if(debitCNAccountString!=null) {
            debitCNAccount = accounts.getBusinessObject(debitCNAccountString);
        }
        String creditCNAccountString = extraProperties.get(CREDIT_CN_ACCOUNT);
        if(creditCNAccountString!=null) {
            creditCNAccount = accounts.getBusinessObject(creditCNAccountString);
        }
    }

    public Properties getOutputProperties(){
        Properties outputProperties = super.getOutputProperties();
        if(debitAccount!=null) {
            outputProperties.put(DEBIT_ACCOUNT, debitAccount);
        }
        if(creditAccount!=null) {
            outputProperties.put(CREDIT_ACCOUNT, creditAccount);
        }
        if(debitCNAccount!=null) {
            outputProperties.put(DEBIT_CN_ACCOUNT, debitCNAccount);
        }
        if(creditCNAccount!=null) {
            outputProperties.put(CREDIT_CN_ACCOUNT, creditCNAccount);
        }
        return outputProperties;
    }

    @Override
    public VATTransaction addBusinessObject(VATTransaction vatTransaction) {
        for(VATBooking vatBooking:vatTransaction.getBusinessObjects()){
            VATField vatField = vatBooking.getVatField();
            if(vatField!=null) {
                vatField.addBusinessObject(vatBooking.getVatMovement());
            }
        }
        return vatTransaction;
    }

    @Override
    public void removeBusinessObject(VATTransaction vatTransaction){
        for(VATBooking vatBooking:vatTransaction.getBusinessObjects()){
            VATField vatField = vatBooking.getVatField();
            if(vatField!=null) {
                vatField.removeBusinessObject(vatBooking.getVatMovement());
            }
        }
    }

    public VATTransaction purchaseCN(BigDecimal amount, BigDecimal btwAmount, VATTransaction.PurchaseType purchaseType) {
        // We assume amount is negative !!!
        VATTransaction vatTransaction = new VATTransaction();

        VATBooking vatBooking1 = new VATBooking(vatFields.getBusinessObject("85"), new VATMovement(amount, false));
        VATBooking vatBooking2 = new VATBooking(vatFields.getBusinessObject("63"), new VATMovement(btwAmount, false));
        VATBooking vatBooking3 = null;

        if(purchaseType== VATTransaction.PurchaseType.GOODS){
            vatBooking3 = new VATBooking(vatFields.getBusinessObject("81"), new VATMovement(amount, false));
        } else if(purchaseType==VATTransaction.PurchaseType.SERVICES){
            vatBooking3 = new VATBooking(vatFields.getBusinessObject("82"), new VATMovement(amount, false));
        } else if(purchaseType==VATTransaction.PurchaseType.INVESTMENTS){
            vatBooking3 = new VATBooking(vatFields.getBusinessObject("83"), new VATMovement(amount, false));
        }
        vatTransaction.addBusinessObject(vatBooking1);
        vatTransaction.addBusinessObject(vatBooking2);
        vatTransaction.addBusinessObject(vatBooking3);
        return vatTransaction;
    }

    public VATTransaction saleCN(BigDecimal amount, BigDecimal btwAmount) {
        // We assume amount is negative !!!
        VATTransaction vatTransaction = new VATTransaction();
        VATBooking vatBooking1 = new VATBooking(vatFields.getBusinessObject("49"), new VATMovement(amount, false));
        VATBooking vatBooking2 = new VATBooking(vatFields.getBusinessObject("64"), new VATMovement(btwAmount, false));
        vatTransaction.addBusinessObject(vatBooking1);
        vatTransaction.addBusinessObject(vatBooking2);
        return vatTransaction;
    }

    public VATTransaction purchase(BigDecimal amount, BigDecimal btwAmount, VATTransaction.PurchaseType purchaseType) {
        VATTransaction vatTransaction = new VATTransaction();

        VATBooking vatBooking1 = null;
        if(purchaseType== VATTransaction.PurchaseType.GOODS){
            vatBooking1 = new VATBooking(vatFields.getBusinessObject("81"), new VATMovement(amount, true));
        } else if(purchaseType==VATTransaction.PurchaseType.SERVICES){
            vatBooking1 = new VATBooking(vatFields.getBusinessObject("82"), new VATMovement(amount, true));
        } else if(purchaseType==VATTransaction.PurchaseType.INVESTMENTS){
            vatBooking1 = new VATBooking(vatFields.getBusinessObject("83"), new VATMovement(amount, true));
        }
        VATBooking vatBooking2 = new VATBooking(vatFields.getBusinessObject("59"), new VATMovement(btwAmount, true));
        vatTransaction.addBusinessObject(vatBooking1);
        vatTransaction.addBusinessObject(vatBooking2);
        return vatTransaction;
    }

    public VATTransaction sale(BigDecimal amount, BigDecimal btwAmount, Integer pct) {
        VATTransaction vatTransaction = new VATTransaction();
        VATBooking vatBooking1 = null;
        if(pct==0){
            vatBooking1 = new VATBooking(vatFields.getBusinessObject("0"), new VATMovement(amount, true));
        } else if(pct==6){
            vatBooking1 = new VATBooking(vatFields.getBusinessObject("1"), new VATMovement(amount, true));
        } else if(pct==12){
            vatBooking1 = new VATBooking(vatFields.getBusinessObject("2"), new VATMovement(amount, true));
        } else if(pct==21){
            vatBooking1 = new VATBooking(vatFields.getBusinessObject("3"), new VATMovement(amount, true));
        }
        VATBooking vatBooking2 = new VATBooking(vatFields.getBusinessObject("54"), new VATMovement(btwAmount, true));
        vatTransaction.addBusinessObject(vatBooking1);
        vatTransaction.addBusinessObject(vatBooking2);
        return vatTransaction;
    }
}
