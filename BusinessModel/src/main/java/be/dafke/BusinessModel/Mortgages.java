package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessCollectionProvider;
import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.BusinessTypeProvider;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 11:06
 */
public class Mortgages extends BusinessCollection<Mortgage> implements BusinessTypeProvider<AccountType>, BusinessCollectionProvider<Account> {
    private BusinessTypeCollection<AccountType> businessTypeCollection;
    private BusinessCollection<Account> businessCollection;

    public static final String MORTGAGES = "Mortgages";
    public static final String MORTGAGE = "Mortgage";

    public Mortgages(){
        setName(MORTGAGES);
    }

    @Override
        public String getChildType(){
            return MORTGAGE;
    }

    @Override
    public Mortgage createNewChild() {
        return new Mortgage();
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