package be.dafke.Mortgage.Objects;

import be.dafke.BasicAccounting.Objects.Accountings;

import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 10:02
 */
public class AccountingsWithMortgage extends Accountings {

    public AccountingsWithMortgage(File xmlFolder) {
        super(xmlFolder);
    }

    @Override
    public AccountingWithMortgage createNewChild(String name) {
        return new AccountingWithMortgage(name);
    }

    @Override
    public String getBusinessObjectType(){
        return "Accountings";
    }
}
