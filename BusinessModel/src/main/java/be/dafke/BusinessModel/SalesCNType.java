package be.dafke.BusinessModel;

import java.math.BigDecimal;

public enum SalesCNType {
    VAT_49 ("CN on Sales", "49");

    private final String message;
    private final VATField salesCnRevenueField;
    private final VATField salesCnVatField = new VATField("64");


    SalesCNType(String message, String nr) {
        this.message = message;
        salesCnRevenueField = new VATField(nr);
    }

    public String getMessage() {
        return message;
    }

    public VATField getSalesCnRevenueField() {
        return salesCnRevenueField;
    }

    @Override
    public String toString(){
        return message;
    }

    public VATBooking getSalesCnRevenueBooking(BigDecimal amount){
        return new VATBooking(salesCnRevenueField, new VATMovement(amount));
    }

    public VATBooking getSalesCnVatBooking(BigDecimal amount){
        return new VATBooking(salesCnVatField, new VATMovement(amount));
    }
}
