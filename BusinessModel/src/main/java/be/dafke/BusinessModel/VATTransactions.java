package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VATTransactions extends BusinessCollection<VATTransaction> {
    private final VATFields vatFields;
    private Account creditAccount, debitAccount, creditCNAccount, debitCNAccount;
    private Integer[] vatPercentages = new Integer[]{0, 6, 12, 21};
    private final HashMap<Integer,VATTransaction> vatTransactionsPerId = new HashMap<>();
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

    public VATTransactions(VATFields vatFields) {
        this.accounting = null;
        this.vatFields = vatFields;
    }

    @Override
    public VATTransaction addBusinessObject(VATTransaction vatTransaction){
        if(vatTransaction!=null) {
            vatTransactionsPerId.put(vatTransaction.getId(), vatTransaction);
            if(!vatTransaction.isRegistered()) {
                for (VATBooking vatBooking : vatTransaction.getBusinessObjects()) {
                    VATField vatField = vatBooking.getVatField();
                    if (vatField != null) {
                        vatField.addBusinessObject(vatBooking.getVatMovement());
                    }
                }
            }
        }
        return vatTransaction;
    }

    public void registerVATTransactions(List<VATTransaction> vatTransactions){
        if(vatTransactions!=null) {
            vatTransactions.forEach(vatTransaction -> {
                vatTransaction.setRegistered();
                ArrayList<VATBooking> businessObjects = vatTransaction.getBusinessObjects();
                for (VATBooking vatBooking : businessObjects){
                    VATField vatField = vatBooking.getVatField();
                    if (vatField != null) {
                        vatField.setRegistered(vatBooking.getVatMovement());
                    }
                }
            });
        }
    }

    public Accounting getAccounting() {
        return accounting;
    }


    @Override
    public ArrayList<VATTransaction> getBusinessObjects(){
        return new ArrayList<>(vatTransactionsPerId.values());
    }

    public VATTransaction getBusinessObject(Integer id){
        return vatTransactionsPerId.get(id);
    }

    @Override
    public void removeBusinessObject(VATTransaction vatTransaction){
        vatTransactionsPerId.remove(vatTransaction.getId());
        for(VATBooking vatBooking:vatTransaction.getBusinessObjects()){
            VATField vatField = vatBooking.getVatField();
            if(vatField!=null) {
                vatField.removeBusinessObject(vatBooking.getVatMovement());
            }
        }
    }
}
