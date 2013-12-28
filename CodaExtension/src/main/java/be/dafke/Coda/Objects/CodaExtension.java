package be.dafke.Coda.Objects;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.AccountingExtension;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 16:22
 */
public class CodaExtension implements AccountingExtension{
    public void extendConstructor(Accounting accounting){
        CounterParties counterParties = new CounterParties();

        Statements statements = new Statements();
        statements.setBusinessCollection(counterParties);
        counterParties.setName(counterParties.getBusinessObjectType());
        statements.setName(statements.getBusinessObjectType());
        try {
            accounting.addBusinessObject((BusinessCollection)statements);
            accounting.addBusinessObject((BusinessCollection)counterParties);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        accounting.addKey(counterParties.getBusinessObjectType());
        accounting.addKey(statements.getBusinessObjectType());
    }

    public void extendReadCollection(Accountings accountings, File xmlFolder){

    }
}
