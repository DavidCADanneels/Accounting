package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class AccountType extends BusinessObject implements Comparable<AccountType> {

    boolean inverted = false

    boolean isDeletable(){
        true
    }

    boolean isInverted() {
        inverted
    }

    void setInverted(boolean inverted) {
        this.inverted = inverted
    }

    @Override
    int compareTo(AccountType o) {
        getName().compareTo(o.name)
    }
}
