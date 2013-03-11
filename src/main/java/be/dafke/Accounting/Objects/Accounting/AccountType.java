package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.BusinessType;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 6:25
 */
public class AccountType extends BusinessType {
    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    boolean inverted = false;

}
