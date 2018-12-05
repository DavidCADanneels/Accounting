package be.dafke.BusinessModel;

import java.math.BigDecimal;

public enum PurchaseCNType {
    VAT_85 ("CN on Purchases", "85");

    private final String message;
    private VATField costCnField;
    private static final VATField vatCnField = new VATField("63");

    PurchaseCNType(String message, String nr) {
        this.message = message;
        costCnField = new VATField(nr);
    }

    public VATField getCostCnField() {
        return costCnField;
    }

    public VATBooking getCostBooking(BigDecimal amount){
        return new VATBooking(costCnField, new VATMovement(amount));
    }


    public static VATBooking getPurchaseCnVatBooking(BigDecimal amount) {
        return new VATBooking(vatCnField, new VATMovement(amount));
    }
}
