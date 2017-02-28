package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ddanneels on 25/12/2016.
 */
public class VATTransactions extends BusinessCollection<VATTransaction> {
    private final VATFields vatFields;
    private Account creditAccount, debitAccount, creditCNAccount, debitCNAccount;
    private Integer[] vatPercentages = new Integer[]{0, 6, 12, 21};
    private HashMap<Integer,VATTransaction> vatTransactions = new HashMap<>();
    private Accounting accounting;

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

    public VATTransactions(Accounting accounting) {
        this.accounting = accounting;
        this.vatFields = accounting.getVatFields();
    }

    @Override
    public VATTransaction addBusinessObject(VATTransaction vatTransaction) {
        vatTransactions.put(vatTransaction.getId(), vatTransaction);
        for(VATBooking vatBooking:vatTransaction.getBusinessObjects()){
            VATField vatField = vatBooking.getVatField();
            if(vatField!=null) {
                vatField.addBusinessObject(vatBooking.getVatMovement());
            }
        }
        return vatTransaction;
    }

    @Override
    public ArrayList<VATTransaction> getBusinessObjects(){
        return new ArrayList<>(vatTransactions.values());
    }

    public VATTransaction getBusinessObject(Integer id){
        return vatTransactions.get(id);
    }

    @Override
    public void removeBusinessObject(VATTransaction vatTransaction){
        Integer id = vatTransaction.getId();
        vatTransactions.remove(id);
        for(VATBooking vatBooking:vatTransaction.getBusinessObjects()){
            VATField vatField = vatBooking.getVatField();
            if(vatField!=null) {
                vatField.removeBusinessObject(vatBooking.getVatMovement());
            }
        }
    }

    public ArrayList<VATBooking> purchaseCN(BigDecimal amount, BigDecimal btwAmount, VATTransaction.PurchaseType purchaseType) {
        // We assume amount is negative !!!
        ArrayList<VATBooking> vatTransaction = new ArrayList<>();

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
        vatTransaction.add(vatBooking1);
        vatTransaction.add(vatBooking2);
        vatTransaction.add(vatBooking3);
        return vatTransaction;
    }

    public ArrayList<VATBooking> saleCN(BigDecimal amount, BigDecimal btwAmount) {
        // We assume amount is negative !!!
        ArrayList<VATBooking> vatTransaction = new ArrayList<>();
        VATBooking vatBooking1 = new VATBooking(vatFields.getBusinessObject("49"), new VATMovement(amount, false));
        VATBooking vatBooking2 = new VATBooking(vatFields.getBusinessObject("64"), new VATMovement(btwAmount, false));
        vatTransaction.add(vatBooking1);
        vatTransaction.add(vatBooking2);
        return vatTransaction;
    }

    public ArrayList<VATBooking> purchase(BigDecimal amount, BigDecimal btwAmount, VATTransaction.PurchaseType purchaseType) {
        ArrayList<VATBooking> vatTransaction = new ArrayList<>();

        VATBooking vatBooking1 = null;
        if(purchaseType== VATTransaction.PurchaseType.GOODS){
            vatBooking1 = new VATBooking(vatFields.getBusinessObject("81"), new VATMovement(amount, true));
        } else if(purchaseType==VATTransaction.PurchaseType.SERVICES){
            vatBooking1 = new VATBooking(vatFields.getBusinessObject("82"), new VATMovement(amount, true));
        } else if(purchaseType==VATTransaction.PurchaseType.INVESTMENTS){
            vatBooking1 = new VATBooking(vatFields.getBusinessObject("83"), new VATMovement(amount, true));
        }
        VATBooking vatBooking2 = new VATBooking(vatFields.getBusinessObject("59"), new VATMovement(btwAmount, true));
        vatTransaction.add(vatBooking1);
        vatTransaction.add(vatBooking2);
        return vatTransaction;
    }

    public ArrayList<VATBooking> sale(BigDecimal amount, BigDecimal btwAmount, Integer pct) {
        ArrayList<VATBooking> vatTransaction = new ArrayList<>();
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
        vatTransaction.add(vatBooking1);
        vatTransaction.add(vatBooking2);
        return vatTransaction;
    }

    public Accounting getAccounting() {
        return accounting;
    }
}
