package be.dafke.Accounting.BusinessModel

enum PurchaseType {
    VR ("Vrijstelling (VR)", null),
    VAT_81 ("Handelsgoederen (81)", "81"),
    VAT_82 ("Diverse goederen en diensten (82)", "82"),
    VAT_83 ("Investeringen (83)", "83")
//    VAT_86 ("Intra-comm", "86")

    final String message
    VATField costField = null
    static VATField vatField = new VATField("59")
    static VATField intraComField = new VATField("86")
    static VATField intraComVatField = new VATField("55")

    PurchaseType(String message, String nr) {
        this.message = message
        if(nr) costField = new VATField(nr)
    }

    VATField getCostField() {
        costField
    }

    static VATField getVatField() {
        vatField
    }

    VATBooking getCostBooking(BigDecimal amount){
        new VATBooking(costField, new VATMovement(amount))
    }

    @Override
    String toString(){
        message
    }

    static VATBooking getVatBooking(BigDecimal amount) {
        new VATBooking(vatField, new VATMovement(amount))
    }

    static VATBooking getIntraComBooking(BigDecimal amount) {
        new VATBooking(intraComField, new VATMovement(amount))
    }

    static VATBooking getIntraComVatBooking(BigDecimal amount) {
        new VATBooking(intraComVatField, new VATMovement(amount))
    }
}
