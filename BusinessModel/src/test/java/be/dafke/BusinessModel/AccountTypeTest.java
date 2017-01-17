package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ddanneels on 14/07/2015.
 */
public class AccountTypeTest {
    @Test
    public void accountType() {
        AccountType accountType = new AccountType();
        assertFalse(accountType.isInverted());
        accountType.setInverted(true);
        assertTrue(accountType.isInverted());
    }

    @Test
    public void numberOfAccountTypes() {
        AccountTypes accountTypes = new AccountTypes();
        accountTypes.getBusinessObjects();
        ArrayList<AccountType> businessObjects = accountTypes.getBusinessObjects();
        assertEquals(0, businessObjects.size());
        accountTypes.addDefaultTypes();
        businessObjects = accountTypes.getBusinessObjects();
        assertEquals(8, businessObjects.size());
    }


    @Test
    public void accountTypesNames() {
        AccountTypes accountTypes = new AccountTypes();
        accountTypes.getBusinessObjects();
        ArrayList<AccountType> businessObjects = accountTypes.getBusinessObjects();
        assertTrue(businessObjects.isEmpty());
        accountTypes.addDefaultTypes();
        businessObjects = accountTypes.getBusinessObjects();
        // make more performant
        List<String> names = businessObjects.stream().map(BusinessObject::toString).collect(Collectors.toList());
        assertTrue(names.contains(AccountTypes.ASSET));
        assertTrue(names.contains(AccountTypes.COST));
        assertTrue(names.contains(AccountTypes.CREDIT));
        assertTrue(names.contains(AccountTypes.DEBIT));
        assertTrue(names.contains(AccountTypes.TAXCREDIT));
        assertTrue(names.contains(AccountTypes.TAXDEBIT));
        assertTrue(names.contains(AccountTypes.LIABILITY));
        assertTrue(names.contains(AccountTypes.REVENUE));
        // FIXME: temp. removed Mortgages from AccountTypes
//        assertTrue(names.contains(AccountTypes.MORTGAGE));
    }
}
