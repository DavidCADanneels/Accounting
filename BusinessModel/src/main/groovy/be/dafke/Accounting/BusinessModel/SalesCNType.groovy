package be.dafke.Accounting.BusinessModel

enum SalesCNType {
    VAT_49 ("CN on Sales", "49")

    private final String message
    private final VATField revenueCnField
    private static final VATField vatCnField = new VATField("64")

    SalesCNType(String message, String nr) {
        this.message = message
        revenueCnField = new VATField(nr)
    }

    String getMessage() {
        message
    }

    VATField getRevenueCnField() {
        revenueCnField
    }

    @Override
    String toString(){
        message
    }

    VATBooking getSalesCnRevenueBooking(BigDecimal amount){
        new VATBooking(revenueCnField, new VATMovement(amount))
    }

    static VATBooking getSalesCnVatBooking(BigDecimal amount){
        new VATBooking(vatCnField, new VATMovement(amount))
    }
}
