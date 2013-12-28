package be.dafke.Mortgage.Objects;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

/**
 * User: david
 * Date: 28-12-13
 * Time: 1:26
 */
public class AccountingWithMortgage extends Accounting{
//    private final Mortgages mortgages;

    public AccountingWithMortgage(String name) {
        super(name);
        Mortgages mortgages = new Mortgages();
        mortgages.setBusinessTypeCollection(getAccountTypes());
        mortgages.setBusinessCollection(getAccounts());
        mortgages.setName(mortgages.getBusinessObjectType());
        try{
            addBusinessObject((BusinessCollection)mortgages);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        addKey(mortgages.getBusinessObjectType());
    }

    @Override
    public String getBusinessObjectType(){
        return "Accounting";
    }

//    public Mortgages getMortgages(){
//        return mortgages;
//    }
}
