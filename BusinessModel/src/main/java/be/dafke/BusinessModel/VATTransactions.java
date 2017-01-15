package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.math.BigDecimal;

/**
 * Created by ddanneels on 25/12/2016.
 */
public class VATTransactions extends BusinessCollection<VATTransaction> {
    private final VATFields vatFields;
    private Account creditAccount, debitAccount, creditCNAccount, debitCNAccount;
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

    public VATTransactions(VATFields vatFields) {
        this.vatFields = vatFields;
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
