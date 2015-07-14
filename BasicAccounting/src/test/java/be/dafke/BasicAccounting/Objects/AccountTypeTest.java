package be.dafke.BasicAccounting.Objects;

import org.junit.Test;

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
    public void accountTypes() {
        AccountTypes accountTypes = new AccountTypes();
        assertEquals(AccountTypes.ACCOUNT_TYPE,accountTypes.getChildType());
    }
}
