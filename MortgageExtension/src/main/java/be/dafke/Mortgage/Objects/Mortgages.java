package be.dafke.Mortgage.Objects;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountType;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessCollectionProvider;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.BusinessTypeProvider;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 11:06
 */
public class Mortgages extends BusinessCollection<BusinessObject> implements BusinessTypeProvider<AccountType>, BusinessCollectionProvider<Account> {
    private BusinessTypeCollection<AccountType> businessTypeCollection;
    private BusinessCollection<Account> businessCollection;

    public static final String MORTGAGES = "Mortgages";
    public static final String MORTGAGE = "Mortgage";

    public Mortgages(Accounting accounting){
        setName(MORTGAGES);
        setBusinessTypeCollection(accounting.getAccountTypes());
        setBusinessCollection(accounting.getAccounts());
        try{
            accounting.addBusinessObject(this);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        accounting.addKey(MORTGAGES);
    }

    @Override
        public String getChildType(){
            return MORTGAGE;
    }

    @Override
    public Mortgage createNewChild() {
        return new Mortgage();
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

    public void setBusinessTypeCollection(BusinessTypeCollection<AccountType> businessTypeCollection) {
        this.businessTypeCollection = businessTypeCollection;
    }

    public BusinessTypeCollection<AccountType> getBusinessTypeCollection() {
        return businessTypeCollection;
    }

    public BusinessCollection<Account> getBusinessCollection() {
        return businessCollection;
    }

    public void setBusinessCollection(BusinessCollection<Account> businessCollection) {
        this.businessCollection = businessCollection;
    }
}