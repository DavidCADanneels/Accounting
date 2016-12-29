package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATMovement extends BusinessObject {
    private BigDecimal amount;
    private boolean increase;

    public VATMovement(BigDecimal amount, boolean increase) {
        this.amount = amount;
        this.increase = increase;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isIncrease() {
        return increase;
    }
}
