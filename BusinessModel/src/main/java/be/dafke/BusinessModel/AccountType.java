package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 6:25
 */
public class AccountType extends BusinessObject {

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
}
