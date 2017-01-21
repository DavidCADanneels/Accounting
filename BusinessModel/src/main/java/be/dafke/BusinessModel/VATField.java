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
            if (vatMovement.isIncrease()) {
                amount = amount.add(vatAmount);
            } else {
                amount = amount.subtract(vatAmount);
            }
        }
        return vatMovement;
    }

    public void removeBusinessObject(VATMovement vatMovement) {
        BigDecimal vatAmount = vatMovement.getAmount();
        if (vatAmount != null) {
            if (vatMovement.isIncrease()) {
                amount = amount.subtract(vatAmount);
            } else {
                amount = amount.add(vatAmount);
            }
        }
    }
}
