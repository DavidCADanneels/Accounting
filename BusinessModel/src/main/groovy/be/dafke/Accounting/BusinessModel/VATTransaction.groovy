package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection

class VATTransaction extends BusinessCollection<VATBooking>{
    private ArrayList<VATBooking> vatBookings = new ArrayList()
    private Transaction transaction
    private Integer id
    private boolean registered = false

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
