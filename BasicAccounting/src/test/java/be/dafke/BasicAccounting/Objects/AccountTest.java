package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * Created by ddanneels on 14/07/2015.
 */

public class AccountTest {

    public static final String ACCOUNT_TYPE_NAME = "AccountTypeName";
    public static final String NEW_KEY = "new key";
    public static final String NEW_VALUE = "new value";

    @Test
    public void defaultValues(){
        Account account = new Account();
        assertTrue(account.writeGrandChildren());
        assertNull(account.createNewChild());
        assertEquals(account.getName(), account.toString());
        assertEquals(Account.MOVEMENT, account.getChildType());
        Set<String> initKeySet = account.getInitKeySet();
        assertTrue(initKeySet.contains(Account.TYPE));
        assertTrue(initKeySet.contains(Account.DEFAULTAMOUNT));
    }

    @Test
    public void initialAmounts(){
        BigDecimal scaledZero = new BigDecimal(0);
        scaledZero = scaledZero.setScale(2);

        Account account = new Account();
        BigDecimal debetTotal = account.getDebetTotal();
        BigDecimal creditTotal = account.getCreditTotal();
        BigDecimal saldo = account.getSaldo();

        assertEquals(scaledZero, debetTotal);
        assertEquals(2,debetTotal.scale());
        assertEquals(scaledZero,creditTotal);
        assertEquals(2,creditTotal.scale());
        assertEquals(scaledZero,saldo);
        assertEquals(2,saldo.scale());
    }

    @Test
    public void deletable(){
        Account account = new Account();
        assertTrue(account.isDeletable());
        // add movements
//        assertFalse(account.isDeletable());
    }

    @Test
      public void currentObject() throws EmptyNameException, DuplicateNameException {
        Account account = new Account();
        assertNull(account.getCurrentObject());
        Movement movement = new Movement(BigDecimal.ONE, true);
        account.setCurrentObject(movement);
        assertEquals(movement, account.getCurrentObject());
    }

    @Test
    public void defaultAmount() throws EmptyNameException, DuplicateNameException {
        Account account = new Account();
        assertNull(account.getDefaultAmount());
        BigDecimal amount = BigDecimal.TEN;
        account.setDefaultAmount(amount);
        assertEquals(amount, account.getDefaultAmount());
    }

    @Test
     public void type() {
        Account account = new Account();
        assertNull(account.getType());

        AccountType type = new AccountType();
        account.setType(type);
        assertEquals(type, account.getType());
    }

    @Test (expected = NullPointerException.class)
    public void initPropertiesNameOnly() {
        Account account = new Account();
        // TODO: Account must be typed
        account.getInitProperties(null);
    }

    @Test
    public void initPropertiesNameAndType() {
        Account account = new Account();
        AccountType accountType = new AccountType();
        accountType.setName(ACCOUNT_TYPE_NAME);
        account.setType(accountType);
        TreeMap<String, String> initProperties = account.getInitProperties(null);
        assertEquals(2, initProperties.size());
        assertTrue(initProperties.containsKey(BusinessObject.NAME));
        assertEquals(account.getName(), initProperties.get(BusinessObject.NAME));
        assertTrue(initProperties.containsKey(Account.TYPE));
        assertEquals(ACCOUNT_TYPE_NAME, initProperties.get(Account.TYPE));
    }

    @Test
    public void initPropertiesNameTypeAndDefaultAmount() {
        Account account = new Account();
        AccountType accountType = new AccountType();
        accountType.setName(ACCOUNT_TYPE_NAME);
        account.setType(accountType);
        BigDecimal amount = BigDecimal.TEN;
        account.setDefaultAmount(amount);
        TreeMap<String, String> initProperties = account.getInitProperties(null);
        assertEquals(3, initProperties.size());
        assertTrue(initProperties.containsKey(Account.TYPE));
        assertEquals(ACCOUNT_TYPE_NAME, initProperties.get(Account.TYPE));
        assertTrue(initProperties.containsKey(Account.DEFAULTAMOUNT));
        assertEquals(amount.toString(), initProperties.get(Account.DEFAULTAMOUNT));
    }

    @Test
    public void setInitProperties(){
        // TODO: check dependencies
        Account account = new Account();
        account.setBusinessTypeCollection(new AccountTypes());
        AccountType active = new AccountType();
        active.setName(AccountTypes.ASSET);
        account.setType(active);
        TreeMap<String, String> initProperties = account.getInitProperties(null);
        initProperties.put(NEW_KEY, NEW_VALUE);
        account.setInitProperties(initProperties);
        assertEquals(3, initProperties.size());
        assertTrue(initProperties.containsKey(NEW_KEY));
        assertEquals(NEW_VALUE, initProperties.get(NEW_KEY));
    }

    @Test
    public void bookAndUnbook(){
        Account account = new Account();
        BigDecimal TWENTY = new BigDecimal(20);
        BigDecimal TEN = BigDecimal.TEN;
        BigDecimal ZERO = BigDecimal.ZERO;
        Movement debitMovement = new Movement(TWENTY,true);
        Movement creditMovement = new Movement(TEN,false);
        Calendar time = Calendar.getInstance();

        account.book(time, debitMovement);   // 0 + 20 (D) = 20
        assertEquals(TWENTY.setScale(2), account.getSaldo());  // 0 + 20 = 20
        assertEquals(TWENTY.setScale(2), account.getDebetTotal());  // 20
        assertEquals(ZERO.setScale(2), account.getCreditTotal());  // 0

        ArrayList<Movement> movements = account.getBusinessObjects();
        assertEquals(1, movements.size());
        assertTrue(movements.contains(debitMovement));

        account.book(time, creditMovement);   // 20 - 10 (C) = 10
        assertEquals(TEN.setScale(2), account.getSaldo()); // 20 - 10 = 10
        assertEquals(TWENTY.setScale(2), account.getDebetTotal()); // stays 20
        assertEquals(TEN.setScale(2), account.getCreditTotal()); // 0 + 10 = 10

        movements = account.getBusinessObjects();
        assertEquals(2, movements.size());
        assertTrue(movements.contains(creditMovement));

        account.unbook(time,debitMovement);
        movements = account.getBusinessObjects();
        assertEquals(1, movements.size());
        assertFalse(movements.contains(debitMovement));
        assertTrue(movements.contains(creditMovement));

        assertEquals(TEN.negate().setScale(2), account.getSaldo()); // 10 - 20 = -10
        assertEquals(ZERO.setScale(2), account.getDebetTotal()); // 20 - 20 = 0
        assertEquals(TEN.setScale(2), account.getCreditTotal()); // stays 10

        account.unbook(time,creditMovement);
        movements = account.getBusinessObjects();
        assertTrue(movements.isEmpty());

        assertEquals(ZERO.negate().setScale(2), account.getSaldo());
        assertEquals(ZERO.setScale(2), account.getDebetTotal());
        assertEquals(ZERO.setScale(2), account.getCreditTotal());
    }
}
