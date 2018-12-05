package be.dafke.BusinessModel;

import java.math.BigDecimal;

public enum PurchaseType {
    VR ("Vrijstelling (VR)", null),
    VAT_81 ("Handelsgoederen (81)", "81"),
    VAT_82 ("Diverse goederen en diensten (82)", "82"),
    VAT_83 ("Investeringen (83)", "83");
//    VAT_86 ("Intra-comm", "86");

    private final String message;
    private VATField costField = null;
    private static VATField vatField = new VATField("59");
    private static VATField intraComField = new VATField("86");

    PurchaseType(String message, String nr) {
        this.message = message;
        if(nr!=null) costField = new VATField(nr);
    }

    public VATField getCostField() {
        return costField;
    }

    public static VATField getVatField() {
        return vatField;
    }

    public VATBooking getCostBooking(BigDecimal amount){
        return new VATBooking(costField, new VATMovement(amount));
    }

    @Override
    public String toString(){
        return message;
    }

    public static VATBooking getVatBooking(BigDecimal amount) {
        return new VATBooking(vatField, new VATMovement(amount));
    }

    public static VATBooking getIntraComBooking(BigDecimal amount) {
        return new VATBooking(intraComField, new VATMovement(amount));
    }
}
