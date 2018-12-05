package be.dafke.BusinessModel;

import java.math.BigDecimal;

public enum SalesType {
    VAT_0 ("0% BTW", "0", 0),
    VAT_1 ("6% BTW", "1", 6),
    VAT_2 ("12% BTW", "2", 12),
    VAT_3 ("21% BTW", "3", 21),
    VAT_46 ("Intra-comm", "46", 0);

    private final String message;
    private final VATField revenueField;
    // TODO: fetch from vatFields iso new Field
    private static final VATField vatField = new VATField("54");
    private final Integer pct;

    SalesType(String message, String nr, Integer pct) {
        this.message = message;
        revenueField = new VATField(nr);
        this.pct = pct;
    }

    public static VATBooking getRevenueBookingByPct(BigDecimal amount, int pct) {
        if(pct == 6){
            return VAT_1.getRevenueBooking(amount);
        } else if(pct == 21){
            return VAT_3.getRevenueBooking(amount);
        } else return null;
    }

    public String getMessage() {
        return message;
    }

    public VATField getRevenueField() {
        return revenueField;
    }

    public Integer getPct() {
        return pct;
    }

    @Override
    public String toString(){
        return message;
    }

    public VATBooking getRevenueBooking(BigDecimal revenueAmount){
        return new VATBooking(revenueField, new VATMovement(revenueAmount));
    }

    public static VATBooking getVatBooking(BigDecimal revenueAmount){
        return new VATBooking(vatField, new VATMovement(revenueAmount));
    }

}
