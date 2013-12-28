package be.dafke.Mortgage.Objects;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.AccountingExtension;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

/**
 * User: david
 * Date: 28-12-13
 * Time: 16:09
 */
public class MortgageExtension implements AccountingExtension{
    @Override
    public void extend(Accounting accounting){
        Mortgages mortgages = new Mortgages();
        mortgages.setBusinessTypeCollection(accounting.getAccountTypes());
        mortgages.setBusinessCollection(accounting.getAccounts());
        mortgages.setName(mortgages.getBusinessObjectType());
        try{
            accounting.addBusinessObject((BusinessCollection) mortgages);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        accounting.addKey(mortgages.getBusinessObjectType());
    }
}
