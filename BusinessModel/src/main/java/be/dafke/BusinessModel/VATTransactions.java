package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VATTransactions extends BusinessCollection<VATTransaction> {
    private Account creditAccount, debitAccount, creditCNAccount, debitCNAccount;
    private Integer[] vatPercentages = new Integer[]{0, 6, 12, 21};

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

    public void registerVATTransactions(List<Transaction> transactions){
        transactions.forEach(transaction -> {
            ArrayList<VATBooking> vatBookings = transaction.getVatBookings();
            if(vatBookings!=null) {
                vatBookings.forEach(vatBooking -> {
                    VATField vatField = vatBooking.getVatField();
                    if (vatField != null) {
                        VATMovement vatMovement = vatBooking.getVatMovement();
                        vatMovement.setRegistered(true);
                        vatField.setRegistered(vatMovement);
                    }
                });
            }
        });
    }
}
