package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class VATBooking extends BusinessObject {
    private VATField vatField
    private VATMovement vatMovement
    private VATTransaction vatTransaction

    VATBooking(VATBooking booking) {
        this.vatField = booking.vatField
        this.vatMovement = booking.vatMovement
    }

    VATBooking(VATField vatField, VATMovement vatMovement) {
        this.vatField = vatField
        this.vatMovement = vatMovement
    }

    VATField getVatField() {
        vatField
    }

    VATMovement getVatMovement() {
        vatMovement
    }

    VATTransaction getVatTransaction() {
        vatTransaction
    }

    void setVatTransaction(VATTransaction vatTransaction) {
        this.vatTransaction = vatTransaction
    }
}
