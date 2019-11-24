package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection

class VATField extends BusinessCollection<VATMovement> {
    private BigDecimal amount = BigDecimal.ZERO

    VATField(String name) {
        setName(name)
    }

    BigDecimal getSaldo() {
        amount
    }

    void setAmount(BigDecimal amount) {
        this.amount = amount
    }

    @Override
    VATMovement addBusinessObject(VATMovement vatMovement) {
        BigDecimal vatAmount = vatMovement.getAmount()
        if (vatAmount != null) {
            amount = amount.add(vatAmount)
        }
        vatMovement
    }

    void removeBusinessObject(VATMovement vatMovement) {
        BigDecimal vatAmount = vatMovement.getAmount()
        if (vatAmount != null) {
            amount = amount.subtract(vatAmount)
        }
    }

    void setRegistered(VATMovement vatMovement) {
        BigDecimal vatAmount = vatMovement.getAmount()
        if (vatAmount != null) {
            amount = amount.subtract(vatAmount)
        }
    }
}
