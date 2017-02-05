package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 11:06
 */
public class Mortgages extends BusinessCollection<Mortgage> {
    private Accounting accounting;

    public Mortgages(Accounting accounting) {
        this.accounting = accounting;
    }

    public Accounting getAccounting() {
        return accounting;
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }
}