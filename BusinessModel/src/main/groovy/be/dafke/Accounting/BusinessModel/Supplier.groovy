package be.dafke.Accounting.BusinessModel

class Supplier {
    Account supplierAccount = null

    Account getSupplierAccount() {
        return supplierAccount
    }

    void setSupplierAccount(Account supplierAccount) {
        this.supplierAccount = supplierAccount
    }
}
