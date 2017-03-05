package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATMovement extends BusinessObject {
    private BigDecimal amount;

    public VATMovement(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
