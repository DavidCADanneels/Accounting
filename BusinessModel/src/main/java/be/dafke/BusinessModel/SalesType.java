package be.dafke.BusinessModel;

import java.math.BigDecimal;

public enum SalesType {
    VAT_0 ("0% BTW", "0", 0),
    VAT_1 ("6% BTW", "1", 6),
    VAT_2 ("12% BTW", "2", 12),
    VAT_3 ("21% BTW", "3", 21),
    VAT_46 ("Intra-comm", "46", 0);

    private final String message;
    private final VATField vatField;
    // TODO: fetch from vatFields iso new Field
    private final VATField salesCnRevenueField = new VATField("49");
    private final VATField salesCnVatField = new VATField("64");
    private final Integer pct;

    SalesType(String message, String nr, Integer pct) {
        this.message = message;
        vatField = new VATField(nr);
        this.pct = pct;
    }

    public String getMessage() {
        return message;
    }

    public VATField getVatField() {
        return vatField;
    }

    public Integer getPct() {
        return pct;
    }

    @Override
    public String toString(){
        return message;
    }

    public VATBooking getRevenueBooking(BigDecimal revenueAmount){
        return new VATBooking(vatField, new VATMovement(revenueAmount));
    }

    public VATBooking getSalesCnRevenueBooking(BigDecimal amount){
        return new VATBooking(salesCnRevenueField, new VATMovement(amount));
    }

    public VATBooking getSalesCnVatBooking(BigDecimal amount){
        return new VATBooking(salesCnVatField, new VATMovement(amount));
    }
}
