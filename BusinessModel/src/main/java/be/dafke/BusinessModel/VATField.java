package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.math.BigDecimal;

/**
 * Created by ddanneels on 27/12/2016.
 */
public class VATField extends BusinessCollection<VATMovement> {
    private BigDecimal amount = BigDecimal.ZERO;

    public VATField(String name) {
        setName(name);
    }

    public BigDecimal getSaldo() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public VATMovement addBusinessObject(VATMovement vatMovement) {
        BigDecimal vatAmount = vatMovement.getAmount();
        if (vatAmount != null) {
            amount = amount.add(vatAmount);
        }
        return vatMovement;
    }

    public void removeBusinessObject(VATMovement vatMovement) {
        BigDecimal vatAmount = vatMovement.getAmount();
        if (vatAmount != null) {
            amount = amount.subtract(vatAmount);
        }
    }

    public void setRegistered(VATMovement vatMovement) {
        BigDecimal vatAmount = vatMovement.getAmount();
        if (vatAmount != null) {
            amount = amount.subtract(vatAmount);
        }
    }
}
