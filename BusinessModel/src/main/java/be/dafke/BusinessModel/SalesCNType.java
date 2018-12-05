package be.dafke.BusinessModel;

import java.math.BigDecimal;

public enum SalesCNType {
    VAT_49 ("CN on Sales", "49");

    private final String message;
    private final VATField revenueCnField;
    private static final VATField vatCnField = new VATField("64");

    SalesCNType(String message, String nr) {
        this.message = message;
        revenueCnField = new VATField(nr);
    }

    public String getMessage() {
        return message;
    }

    public VATField getRevenueCnField() {
        return revenueCnField;
    }

    @Override
    public String toString(){
        return message;
    }

    public VATBooking getSalesCnRevenueBooking(BigDecimal amount){
        return new VATBooking(revenueCnField, new VATMovement(amount));
    }

    public static VATBooking getSalesCnVatBooking(BigDecimal amount){
        return new VATBooking(vatCnField, new VATMovement(amount));
    }
}
