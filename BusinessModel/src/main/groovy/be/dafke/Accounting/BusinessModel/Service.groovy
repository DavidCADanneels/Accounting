package be.dafke.Accounting.BusinessModel

class Service extends Article {

    Contact supplier
    Account costAccount
    Integer purchaseVatRate = 0

    Service(String name) {
        setName(name)
    }

    Service(Service service, Contacts contacts){
        this(service.name)
        String supplierName = service.supplier.name
        supplier = contacts.getBusinessObject(supplierName)
    }

    Contact getSupplier() {
        return supplier
    }

    void setSupplier(Contact supplier) {
        this.supplier = supplier
    }

    Account getCostAccount() {
        return costAccount
    }

    void setCostAccount(Account costAccount) {
        this.costAccount = costAccount
    }

    Integer getPurchaseVatRate() {
        return purchaseVatRate
    }

    void setPurchaseVatRate(Integer purchaseVatRate) {
        this.purchaseVatRate = purchaseVatRate
    }
}
