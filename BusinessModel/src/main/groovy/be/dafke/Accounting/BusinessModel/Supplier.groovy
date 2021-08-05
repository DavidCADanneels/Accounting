package be.dafke.Accounting.BusinessModel

class Supplier {
    Account supplierAccount = null
    ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<>()

    Account getSupplierAccount() {
        return supplierAccount
    }

    void setSupplierAccount(Account supplierAccount) {
        this.supplierAccount = supplierAccount
    }
}
