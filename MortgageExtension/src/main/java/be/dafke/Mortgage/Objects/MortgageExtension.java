package be.dafke.Mortgage.Objects;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.AccountingExtension;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.Mortgage.Dao.MortgagesSAXParser;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 16:09
 */
public class MortgageExtension implements AccountingExtension{
    @Override
    public void extendConstructor(Accounting accounting){
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

    public void extendReadCollection(Accountings accountings, File xmlFolder){
        for(Accounting accounting : accountings.getBusinessObjects()){
            File rootFolder = new File(xmlFolder, accounting.getName());
            BusinessCollection<BusinessObject> mortgages = accounting.getBusinessObject("Mortgages");
            for(BusinessObject businessObject : mortgages.getBusinessObjects()){
                Mortgage mortgage = (Mortgage) businessObject;
                File mortgagesFolder = new File(rootFolder, "Mortgages");
                MortgagesSAXParser.readMortgage(mortgage, new File(mortgagesFolder, mortgage.getName() + ".xml"));
            }

        }
    }
}
