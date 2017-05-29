package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 6:25
 */
public class AccountType extends BusinessObject implements Comparable<AccountType> {

    private boolean inverted = false;

    public boolean isDeletable(){
        return true;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public int compareTo(AccountType o) {
        return getName().compareTo(o.getName());
    }
}
