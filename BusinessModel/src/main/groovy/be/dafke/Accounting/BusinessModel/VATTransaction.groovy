package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection

class VATTransaction extends BusinessCollection<VATBooking>{
    ArrayList<VATBooking> vatBookings = new ArrayList()
    Transaction transaction
    Integer id
    boolean registered = false

    VATTransaction() {
    }

    Integer getId() {
        id
    }

    void setId(Integer id) {
        this.id = id
    }

    void setRegistered() {
        registered = true
    }

    boolean isRegistered() {
        registered
    }

    enum VATType{
        SALE, PURCHASE, CUSTOMER
    }

    ArrayList<VATBooking> getBusinessObjects(){
        vatBookings
    }

    @Override
    void removeBusinessObject(VATBooking value) {
        vatBookings.remove(value)
    }

    @Override
    VATBooking addBusinessObject(VATBooking value) {
        vatBookings.add(value)
        value.setVatTransaction(this)
        value
    }

    Transaction getTransaction() {
        transaction
    }

    void setTransaction(Transaction transaction) {
        this.transaction = transaction
    }

}
