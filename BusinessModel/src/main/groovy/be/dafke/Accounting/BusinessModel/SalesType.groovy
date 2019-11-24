package be.dafke.Accounting.BusinessModel

enum SalesType {
    VAT_0 ("0% BTW", "0", 0),
    VAT_1 ("6% BTW", "1", 6),
    VAT_2 ("12% BTW", "2", 12),
    VAT_3 ("21% BTW", "3", 21),
    VAT_46 ("Intra-comm", "46", 0)

    private final String message
    private final VATField revenueField
    // TODO: fetch from vatFields iso new Field
    private static final VATField vatField = new VATField("54")
    private final Integer pct

    SalesType(String message, String nr, Integer pct) {
        this.message = message
        revenueField = new VATField(nr)
        this.pct = pct
    }

    static VATBooking getRevenueBookingByPct(BigDecimal amount, int pct) {
        if(pct == 6){
            VAT_1.getRevenueBooking(amount)
        } else if(pct == 21){
            VAT_3.getRevenueBooking(amount)
        } else null
    }

    String getMessage() {
        message
    }

    VATField getRevenueField() {
        revenueField
    }

    Integer getPct() {
        pct
    }

    @Override
    String toString(){
        message
    }

    VATBooking getRevenueBooking(BigDecimal revenueAmount){
        new VATBooking(revenueField, new VATMovement(revenueAmount))
    }

    static VATBooking getVatBooking(BigDecimal revenueAmount){
        new VATBooking(vatField, new VATMovement(revenueAmount))
    }

}
