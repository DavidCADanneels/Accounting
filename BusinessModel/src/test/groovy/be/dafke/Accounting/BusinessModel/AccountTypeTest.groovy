package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject
import org.junit.Ignore
import org.junit.Test

import java.util.stream.Collectors

import static org.junit.Assert.*

class AccountTypeTest {
    @Test
    void accountType() {
        AccountType accountType = new AccountType()
        assertFalse(accountType.isInverted())
        accountType.setInverted(true)
        assertTrue(accountType.isInverted())
    }

    @Test
    void numberOfAccountTypes() {
        AccountTypes accountTypes = new AccountTypes()
        accountTypes.businessObjects
        ArrayList<AccountType> businessObjects = accountTypes.businessObjects
        assertEquals(0, businessObjects.size())
        accountTypes.addDefaultTypes()
        businessObjects = accountTypes.businessObjects
        assertEquals(8, businessObjects.size())
    }

    @Test
    void accountTypesNames() {
        AccountTypes accountTypes = new AccountTypes()
        accountTypes.businessObjects
        ArrayList<AccountType> businessObjects = accountTypes.businessObjects
        assertTrue(businessObjects.isEmpty())
        accountTypes.addDefaultTypes()
        assert accountTypes.businessObjects.name.contains(AccountTypes.ASSET)
        assert accountTypes.businessObjects.name.contains(AccountTypes.COST)
        assert accountTypes.businessObjects.name.contains(AccountTypes.CREDIT)
        assert accountTypes.businessObjects.name.contains(AccountTypes.CREDIT)
        assert accountTypes.businessObjects.name.contains(AccountTypes.TAXCREDIT)
        assert accountTypes.businessObjects.name.contains(AccountTypes.TAXDEBIT)
        assert accountTypes.businessObjects.name.contains(AccountTypes.LIABILITY)
        assert accountTypes.businessObjects.name.contains(AccountTypes.REVENUE)
    }
}
