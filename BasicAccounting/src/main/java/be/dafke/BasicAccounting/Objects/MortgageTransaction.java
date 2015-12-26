package be.dafke.BasicAccounting.Objects;

/**
 * Created by ddanneels on 26/12/2015.
 */
public class MortgageTransaction extends Transaction{
    Mortgage mortgage;

    public Mortgage getMortgage() {
        return mortgage;
    }

    public void setMortgage(Mortgage mortgage) {
        this.mortgage = mortgage;
    }
}
