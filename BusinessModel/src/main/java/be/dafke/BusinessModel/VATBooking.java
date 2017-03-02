package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATBooking extends BusinessObject {
    private VATField vatField;
    private VATMovement vatMovement;
    private VATTransaction vatTransaction;

    public VATBooking(VATField vatField, VATMovement vatMovement) {
        this.vatField = vatField;
        this.vatMovement = vatMovement;
    }

    public VATField getVatField() {
        return vatField;
    }

    public VATMovement getVatMovement() {
        return vatMovement;
    }

    public VATTransaction getVatTransaction() {
        return vatTransaction;
    }

    public void setVatTransaction(VATTransaction vatTransaction) {
        this.vatTransaction = vatTransaction;
    }
}
