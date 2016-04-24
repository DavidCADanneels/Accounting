package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by ddanneels on 14/07/2015.
 */

public class AccountTest {

    public static final String ACCOUNT_TYPE_NAME = "AccountTypeName";
    public static final String NEW_KEY = "new key";
    public static final String NEW_VALUE = "new value";
    public static final Calendar TIME = Calendar.getInstance();
    public static final String NAME = "name";
    private final BigDecimal TWENTY = new BigDecimal(20);
    private final BigDecimal TEN = BigDecimal.TEN;
    private final BigDecimal ZERO = BigDecimal.ZERO;
    private Accounts accounts = new Accounts();

    @Test
    public void defaultValues(){
        Account account = new Account(accounts);
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

        Account account = new Account(accounts);
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
        Account account = new Account(accounts);
        assertTrue(account.isDeletable());
        // add movements
//        assertFalse(account.isDeletable());
    }

    @Test
      public void currentObject() throws EmptyNameException, DuplicateNameException {
        Account account = new Account(accounts);
        assertNull(account.getCurrentObject());
        Movement movement = new Movement(BigDecimal.ONE, true);
        account.setCurrentObject(movement);
        assertEquals(movement, account.getCurrentObject());
    }

    @Test
    public void defaultAmount() throws EmptyNameException, DuplicateNameException {
        Account account = new Account(accounts);
        assertNull(account.getDefaultAmount());
        BigDecimal amount = BigDecimal.TEN;
        account.setDefaultAmount(amount);
        assertEquals(amount, account.getDefaultAmount());
    }

    @Test
     public void type() {
        Account account = new Account(accounts);
        assertNull(account.getType());

        AccountType type = new AccountType();
        account.setType(type);
        assertEquals(type, account.getType());
    }

    @Test (expected = NullPointerException.class)
    public void initPropertiesNameOnly() {
        Account account = new Account(accounts);
        // TODO: Account must be typed
        account.getInitProperties();
    }

    @Test
    public void initPropertiesNameAndType() {
        Account account = new Account(accounts);
        account.setName(NAME);
        AccountType accountType = new AccountType();
        accountType.setName(ACCOUNT_TYPE_NAME);
        account.setType(accountType);
        Properties initProperties = account.getInitProperties();
        assertEquals(2, initProperties.size());
        assertTrue(initProperties.containsKey(BusinessObject.NAME));
        assertEquals(account.getName(), initProperties.get(BusinessObject.NAME));
        assertTrue(initProperties.containsKey(Account.TYPE));
        assertEquals(ACCOUNT_TYPE_NAME, initProperties.get(Account.TYPE));
    }

    @Test
    public void initPropertiesNameTypeAndDefaultAmount() {
        Account account = new Account(accounts);
        account.setName(NAME);
        AccountType accountType = new AccountType();
        accountType.setName(ACCOUNT_TYPE_NAME);
        account.setType(accountType);
        BigDecimal amount = BigDecimal.TEN;
        account.setDefaultAmount(amount);
        Properties initProperties = account.getInitProperties();
        assertEquals(3, initProperties.size());
        assertTrue(initProperties.containsKey(Account.TYPE));
        assertEquals(ACCOUNT_TYPE_NAME, initProperties.get(Account.TYPE));
        assertTrue(initProperties.containsKey(Account.DEFAULTAMOUNT));
        assertEquals(amount.toString(), initProperties.get(Account.DEFAULTAMOUNT));
    }

    @Test
    public void setInitProperties(){
        // TODO: check dependencies
        Account account = new Account(accounts);
        account.setName(NAME);
        AccountTypes accountTypes = new AccountTypes();
        AccountType active = accountTypes.createNewChild();
        active.setName(AccountTypes.ASSET);
        account.setType(active);
        Properties initProperties = account.getInitProperties();
        initProperties.put(NEW_KEY, NEW_VALUE);
//        account.setInitProperties(initProperties);
//        assertEquals(3, initProperties.size());
//        assertTrue(initProperties.containsKey(NEW_KEY));
//        assertEquals(NEW_VALUE, initProperties.get(NEW_KEY));
//        String correctValue = "30.25";
//        String wrongValue = "30+25";
//        initProperties.put(Account.DEFAULTAMOUNT, correctValue);
//        account.setInitProperties(initProperties);
//        assertEquals(new BigDecimal(correctValue), account.getDefaultAmount());
//        initProperties.put(Account.DEFAULTAMOUNT, wrongValue);
//        account.setInitProperties(initProperties);
//        assertNull(account.getDefaultAmount());
//        initProperties.put(Account.DEFAULTAMOUNT, null);
//        account.setInitProperties(initProperties);
//        assertNull(account.getDefaultAmount());
    }

    @Test
    public void initValues() {
        Account account = new Account(accounts);
        assertEquals(ZERO.setScale(2), account.getSaldo());
        assertEquals(ZERO.setScale(2), account.getDebetTotal());
        assertEquals(ZERO.setScale(2), account.getCreditTotal());

    }

    private void book(Account account, Movement movement){
        account.book(TIME, movement);
    }

    private Movement debit(Account account, BigDecimal amount){
        Movement movement = new Movement(amount, true);
        book(account, movement);
        return movement;
    }

    private Movement credit(Account account, BigDecimal amount){
        Movement movement = new Movement(amount, false);
        book(account, movement);
        return movement;
    }

    @Test
    public void debit() {
        Account account = new Account(accounts);
        Movement debit = debit(account, TWENTY);
        assertEquals(TWENTY.setScale(2), account.getSaldo());  // 0 + 20 = 20
        assertEquals(TWENTY.setScale(2), account.getDebetTotal());  // 20
        assertEquals(ZERO.setScale(2), account.getCreditTotal());  // 0

        ArrayList<Movement> movements = account.getBusinessObjects();
        assertEquals(1, movements.size());
        assertTrue(movements.contains(debit));
    }

    @Test
    public void credit() {
        Account account = new Account(accounts);
        Movement credit = credit(account, TEN);
        assertEquals(TEN.negate().setScale(2), account.getSaldo());  // 0 - 10 = -10
        assertEquals(ZERO.setScale(2), account.getDebetTotal());  // 0
        assertEquals(TEN.setScale(2), account.getCreditTotal());  // 10

        ArrayList<Movement> movements = account.getBusinessObjects();
        assertEquals(1, movements.size());
        assertTrue(movements.contains(credit));
    }

    @Test
    public void debitAndCredit(){
        Account account = new Account(accounts);
        Movement debit = debit(account, TWENTY);
        Movement credit = credit(account, TEN);

        assertEquals(TEN.setScale(2), account.getSaldo()); // 20 - 10 = 10
        assertEquals(TWENTY.setScale(2), account.getDebetTotal()); // 20
        assertEquals(TEN.setScale(2), account.getCreditTotal()); // 10

        ArrayList<Movement> movements = account.getBusinessObjects();
        assertEquals(2, movements.size());
        assertTrue(movements.contains(debit));
        assertTrue(movements.contains(credit));
    }

    @Test
    public void unbook() {
        Account account = new Account(accounts);
        Movement debit = debit(account, TWENTY);
        Movement credit = credit(account, TEN);

        account.unbook(TIME, debit);

        ArrayList<Movement> movements = account.getBusinessObjects();
        assertEquals(1, movements.size());
        assertFalse(movements.contains(debit));
        assertTrue(movements.contains(credit));

        assertEquals(TEN.negate().setScale(2), account.getSaldo()); // 10 - 20 = -10
        assertEquals(ZERO.setScale(2), account.getDebetTotal()); // 20 - 20 = 0
        assertEquals(TEN.setScale(2), account.getCreditTotal()); // stays 10

        account.unbook(TIME,credit);
        movements = account.getBusinessObjects();
        assertTrue(movements.isEmpty());

        assertEquals(ZERO.negate().setScale(2), account.getSaldo());
        assertEquals(ZERO.setScale(2), account.getDebetTotal());
        assertEquals(ZERO.setScale(2), account.getCreditTotal());
    }
}
