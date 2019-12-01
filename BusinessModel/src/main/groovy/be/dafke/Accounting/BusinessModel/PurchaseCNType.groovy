package be.dafke.Accounting.BusinessModel

enum PurchaseCNType {
    VAT_85 ("CN on Purchases", "85")

    final String message
    VATField costCnField
    static final VATField vatCnField = new VATField("63")

    PurchaseCNType(String message, String nr) {
        this.message = message
        costCnField = new VATField(nr)
    }

    VATField getCostCnField() {
        costCnField
    }

    VATBooking getCostBooking(BigDecimal amount){
        new VATBooking(costCnField, new VATMovement(amount))
    }


    static VATBooking getPurchaseCnVatBooking(BigDecimal amount) {
        new VATBooking(vatCnField, new VATMovement(amount))
    }
}
