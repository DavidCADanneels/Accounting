package be.dafke.BasicAccounting.Objects;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
    public void childType() {
        AccountTypes accountTypes = new AccountTypes();
        assertEquals(AccountTypes.ACCOUNT_TYPE, accountTypes.getChildType());
    }

    @Test
    public void numberOfAccountTypes() {
        AccountTypes accountTypes = new AccountTypes();
        accountTypes.getBusinessObjects();
        ArrayList<AccountType> businessObjects = accountTypes.getBusinessObjects();
        assertEquals(6, businessObjects.size());
    }


    @Test
    public void accountTypesNames() {
        AccountTypes accountTypes = new AccountTypes();
        accountTypes.getBusinessObjects();
        ArrayList<AccountType> businessObjects = accountTypes.getBusinessObjects();
        // make more performant
        List<String> names = new ArrayList<String>();
        for(AccountType type : businessObjects){
            names.add(type.toString());
        }
        assertTrue(names.contains(AccountTypes.ASSET));
        assertTrue(names.contains(AccountTypes.COST));
        assertTrue(names.contains(AccountTypes.CREDIT));
        assertTrue(names.contains(AccountTypes.DEBIT));
        assertTrue(names.contains(AccountTypes.LIABILITY));
        assertTrue(names.contains(AccountTypes.REVENUE));
        // FIXME: temp. removed Mortgages from AccountTypes
//        assertTrue(names.contains(AccountTypes.MORTGAGE));
    }
}
