package be.dafke.Accounting.BusinessModel

class BankAccount implements Serializable {
    /*	enum AccountType{
            Belgian, Foreign, Belgian_IBAN, Foreign_IBAN
        }
    */final String accountNumber
    // AccountType accountType
    String bic
    String currency

    BankAccount(String accountNumber) {// , AccountType accountType){
        this.accountNumber = accountNumber
        // this.accountType = accountType
    }

    void setBic(String bic) {
        this.bic = bic
    }

    void setCurrency(String currency) {
        this.currency = currency
    }

    @Override
    String toString() {
        StringBuilder builder = new StringBuilder(accountNumber + "\n")
        builder.append(bic + "\n")
        builder.append(currency + "\n")
        builder.toString()
    }

    String getAccountNumber() {
        accountNumber
    }

    String getBic() {
        bic
    }

    String getCurrency() {
        currency
    }

}
