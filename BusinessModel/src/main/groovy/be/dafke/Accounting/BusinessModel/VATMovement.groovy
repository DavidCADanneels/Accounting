package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class VATMovement extends BusinessObject {
    BigDecimal amount
    boolean registered = false

    VATMovement(BigDecimal amount) {
        this.amount = amount
    }

    BigDecimal getAmount() {
        amount
    }

    boolean isRegistered() {
        registered
    }

    void setRegistered(boolean registered) {
        this.registered = registered
    }
}
