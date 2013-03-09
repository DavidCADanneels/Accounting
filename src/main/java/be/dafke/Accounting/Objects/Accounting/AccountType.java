package be.dafke.Accounting.Objects.Accounting;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 6:25
 */
public class AccountType extends WriteableBusinessObject {
    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    boolean inverted = false;

}
