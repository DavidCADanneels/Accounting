package be.dafke.Mortgage.Objects;

import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.AccountType;
import be.dafke.ObjectModel.BusinessCollectionProvider;
import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.BusinessTypeProvider;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.WriteableBusinessCollection;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 11:06
 */
public class Mortgages extends WriteableBusinessCollection<Mortgage> implements BusinessTypeProvider<AccountType>, BusinessCollectionProvider<Account> {
    private BusinessTypeCollection<AccountType> businessTypeCollection;
    private WriteableBusinessCollection<Account> businessCollection;

    @Override
    public Mortgage createNewChild(String name) {
        return new Mortgage();
    }

    public Mortgage addBusinessObject(Mortgage value) throws EmptyNameException, DuplicateNameException {
        // TODO create pseudoAccount here and add this one to Accounts (= businessCollection)
        businessCollection.addBusinessObject(value);
        super.addBusinessObject(value);
        return value;
    }

    @Override
    public void readCollection() {
        readCollection("Mortgage",true);
    }

    @Override
    public void setBusinessTypeCollection(BusinessTypeCollection<AccountType> businessTypeCollection) {
        this.businessTypeCollection = businessTypeCollection;
    }

    @Override
    public BusinessTypeCollection<AccountType> getBusinessTypeCollection() {
        return businessTypeCollection;
    }

    @Override
    public WriteableBusinessCollection<Account> getBusinessCollection() {
        return businessCollection;
    }

    @Override
    public void setBusinessCollection(WriteableBusinessCollection<Account> businessCollection) {
        this.businessCollection = businessCollection;
    }
}