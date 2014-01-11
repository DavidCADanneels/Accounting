package be.dafke.Mortgage.Objects;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountType;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessCollectionProvider;
import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.BusinessTypeProvider;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 11:06
 */
public class Mortgages extends BusinessCollection<Mortgage> implements BusinessTypeProvider<AccountType>, BusinessCollectionProvider<Account> {
    private BusinessTypeCollection<AccountType> businessTypeCollection;
    private BusinessCollection<Account> businessCollection;

    public Mortgages(){
        setName("Mortgages");
    }

    @Override
    public String getChildType(){
        return "Mortgage";
    }

    @Override
    public Mortgage createNewChild(String name) {
        return new Mortgage(name);
    }

    public Mortgage addBusinessObject(Mortgage value) throws EmptyNameException, DuplicateNameException {
        // TODO create pseudoAccount here and add this one to Accounts (= businessCollection)
        businessCollection.addBusinessObject(value);
        super.addBusinessObject(value);
        return value;
    }

//    @Override
//    public void readCollection() {
//        readCollection("Mortgage",true);
//    }

    @Override
    public void setBusinessTypeCollection(BusinessTypeCollection<AccountType> businessTypeCollection) {
        this.businessTypeCollection = businessTypeCollection;
    }

    @Override
    public BusinessTypeCollection<AccountType> getBusinessTypeCollection() {
        return businessTypeCollection;
    }

    @Override
    public BusinessCollection<Account> getBusinessCollection() {
        return businessCollection;
    }

    @Override
    public void setBusinessCollection(BusinessCollection<Account> businessCollection) {
        this.businessCollection = businessCollection;
    }
}