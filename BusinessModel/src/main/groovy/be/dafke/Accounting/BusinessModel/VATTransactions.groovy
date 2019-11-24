package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection

class VATTransactions extends BusinessCollection<VATTransaction> {
    private Account creditAccount, debitAccount, creditCNAccount, debitCNAccount
    private Integer[] vatPercentages = [0, 6, 12, 21]

    Integer[] getVatPercentages() {
        vatPercentages
    }

    void setCreditAccount(Account creditAccount) {
        this.creditAccount = creditAccount
    }

    void setDebitAccount(Account debitAccount) {
        this.debitAccount = debitAccount
    }

    Account getCreditAccount() {
        creditAccount
    }

    Account getDebitAccount() {
        debitAccount
    }

    Account getCreditCNAccount() {
        creditCNAccount
    }

    void setCreditCNAccount(Account creditCNAccount) {
        this.creditCNAccount = creditCNAccount
    }

    Account getDebitCNAccount() {
        debitCNAccount
    }

    void setDebitCNAccount(Account debitCNAccount) {
        this.debitCNAccount = debitCNAccount
    }

    void registerVATTransactions(List<Transaction> transactions){
        transactions.forEach({ transaction ->
            ArrayList<VATBooking> vatBookings = transaction.getVatBookings()
            if (vatBookings != null) {
                vatBookings.forEach({ vatBooking ->
                    VATField vatField = vatBooking.getVatField()
                    if (vatField != null) {
                        VATMovement vatMovement = vatBooking.getVatMovement()
                        vatMovement.setRegistered(true)
                        vatField.setRegistered(vatMovement)
                    }
                })
            }
        })
    }
}
