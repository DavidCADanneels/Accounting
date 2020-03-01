package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class Service extends BusinessObject{

    BigDecimal unitPrice
    Contact supplier
//    Contact customer

    Service(String name) {
        setName(name)
    }

    Service(Service service, Contacts contacts){
        this(service.name)
        String supplierName = service.supplier.name
        supplier = contacts.getBusinessObject(supplierName)
    }

    BigDecimal getUnitPrice() {
        return unitPrice
    }

    void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice
    }

    Contact getSupplier() {
        return supplier
    }

    void setSupplier(Contact supplier) {
        this.supplier = supplier
    }
}
