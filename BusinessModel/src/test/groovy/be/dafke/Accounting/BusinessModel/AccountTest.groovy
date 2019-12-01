package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import org.junit.Test

import static org.junit.Assert.*

class AccountTest {

    static final String ACCOUNT_TYPE_NAME = "AccountTypeName"
    static final String NEW_KEY = "new key"
    static final String NEW_VALUE = "new value"
    static final Calendar TIME = Calendar.getInstance()
    static final String NAME = "name"
    final BigDecimal TWENTY = new BigDecimal(20)
    final BigDecimal TEN = BigDecimal.TEN
    final BigDecimal ZERO = BigDecimal.ZERO

    @Test
    void defaultValues(){
        Account account = new Account("")
//        assertNull(account.createNewChild())
        assertEquals(account.name, account.toString())
//        Set<String> initKeySet = account.getInitKeySet()
//        assertTrue(initKeySet.contains(Account.TYPE))
//        assertTrue(initKeySet.contains(Account.DEFAULTAMOUNT))
    }

    @Test
    void initialAmounts(){
        BigDecimal scaledZero = new BigDecimal(0)
        scaledZero = scaledZero.setScale(2)

        Account account = new Account("")
        BigDecimal debetTotal = account.debetTotal
        BigDecimal creditTotal = account.creditTotal
        BigDecimal saldo = account.saldo

        assertEquals(scaledZero, debetTotal)
        assertEquals(2,debetTotal.scale())
        assertEquals(scaledZero,creditTotal)
        assertEquals(2,creditTotal.scale())
        assertEquals(scaledZero,saldo)
        assertEquals(2,saldo.scale())
    }

    @Test
    void copyConstructor(){
        Account account = new Account("a")
        Account copy = new Account(account)

        account.setName("b")
        assertEquals("a",copy.name)

        AccountType type = new AccountType()
        account.type = type
        assertNull(copy.type)
    }

    @Test
    void deletable(){
        Account account = new Account("")
        assertTrue(account.deletable)
        // add movements
//        assertFalse(account.deletable)
    }

    @Test
    void defaultAmount() throws EmptyNameException, DuplicateNameException {
        Account account = new Account("")
        assertNull(account.defaultAmount)
        BigDecimal amount = BigDecimal.TEN
        account.defaultAmount = amount
        assertEquals(amount, account.defaultAmount)
    }

    @Test
    void type() {
        Account account = new Account("")
        assertNull(account.type)

        AccountType type = new AccountType()
        account.type = type
        assertEquals(type, account.type)
    }

//    @Test (expected = NullPointerException.class)
//    void initPropertiesNameOnly() {
//        Account account = new Account("")
//        // TODO: Account must be typed
//        account.getOutputProperties()
//    }

//    @Test
//    void initPropertiesNameAndType() {
//        Account account = new Account("")
//        account.setName(NAME)
//        AccountType accountType = new AccountType()
//        accountType.setName(ACCOUNT_TYPE_NAME)
//        account.setType(accountType)
//        Properties initProperties = account.getOutputProperties()
//        assertEquals(2, initProperties.size())
//        assertTrue(initProperties.containsKey(BusinessObject.NAME))
//        assertEquals(account.name, initProperties.get(BusinessObject.NAME))
//        assertTrue(initProperties.containsKey(Account.TYPE))
//        assertEquals(ACCOUNT_TYPE_NAME, initProperties.get(Account.TYPE))
//    }

//    @Test
//    void initPropertiesNameTypeAndDefaultAmount() {
//        Account account = new Account("")
//        account.setName(NAME)
//        AccountType accountType = new AccountType()
//        accountType.setName(ACCOUNT_TYPE_NAME)
//        account.setType(accountType)
//        BigDecimal amount = BigDecimal.TEN
//        account.setDefaultAmount(amount)
//        Properties initProperties = account.getOutputProperties()
//        assertEquals(3, initProperties.size())
//        assertTrue(initProperties.containsKey(Account.TYPE))
//        assertEquals(ACCOUNT_TYPE_NAME, initProperties.get(Account.TYPE))
//        assertTrue(initProperties.containsKey(Account.DEFAULTAMOUNT))
//        assertEquals(amount.toString(), initProperties.get(Account.DEFAULTAMOUNT))
//    }

//    @Test
//    void setInitProperties(){
//        // TODO: check dependencies
//        Account account = new Account("")
//        account.setName(NAME)
//        AccountTypes accountTypes = new AccountTypes()
//        AccountType active = new AccountType()
//        active.setName(AccountTypes.ASSET)
//        account.setType(active)
//        Properties initProperties = account.getOutputProperties()
//        initProperties.put(NEW_KEY, NEW_VALUE)
//        account.setInitProperties(initProperties)
//        assertEquals(3, initProperties.size())
//        assertTrue(initProperties.containsKey(NEW_KEY))
//        assertEquals(NEW_VALUE, initProperties.get(NEW_KEY))
//        String correctValue = "30.25"
//        String wrongValue = "30+25"
//        initProperties.put(Account.DEFAULTAMOUNT, correctValue)
//        account.setInitProperties(initProperties)
//        assertEquals(new BigDecimal(correctValue), account.defaultAmount)
//        initProperties.put(Account.DEFAULTAMOUNT, wrongValue)
//        account.setInitProperties(initProperties)
//        assertNull(account.defaultAmount)
//        initProperties.put(Account.DEFAULTAMOUNT, null)
//        account.setInitProperties(initProperties)
//        assertNull(account.defaultAmount)
//    }

    @Test
    void initValues() {
        Account account = new Account("")
        assertEquals(ZERO.setScale(2), account.saldo)
        assertEquals(ZERO.setScale(2), account.debetTotal)
        assertEquals(ZERO.setScale(2), account.creditTotal)

    }

    @Test
    void debit() {
        Account account = new Account("")
        Movement debit = new Movement(TWENTY, true)
        account.book(TIME, debit, true)
        assertEquals(TWENTY.setScale(2), account.saldo)  // 0 + 20 = 20
        assertEquals(TWENTY.setScale(2), account.debetTotal)  // 20
        assertEquals(ZERO.setScale(2), account.creditTotal)  // 0

        ArrayList<Movement> movements = account.businessObjects
        assertEquals(1, movements.size())
        assertTrue(movements.contains(debit))
    }

    @Test
    void credit() {
        Account account = new Account("")
        Movement credit = new Movement(TEN, false)
        account.book(TIME, credit, true)
        assertEquals(TEN.negate().setScale(2), account.saldo)  // 0 - 10 = -10
        assertEquals(ZERO.setScale(2), account.debetTotal)  // 0
        assertEquals(TEN.setScale(2), account.creditTotal)  // 10

        ArrayList<Movement> movements = account.businessObjects
        assertEquals(1, movements.size())
        assertTrue(movements.contains(credit))
    }

    @Test
    void debitAndCredit(){
        Account account = new Account("")
        Movement debit = new Movement(TWENTY, true)
        Movement credit = new Movement(TEN, false)
        account.book(TIME, debit, true)
        account.book(TIME, credit, true)

        assertEquals(TEN.setScale(2), account.saldo) // 20 - 10 = 10
        assertEquals(TWENTY.setScale(2), account.debetTotal) // 20
        assertEquals(TEN.setScale(2), account.creditTotal) // 10

        ArrayList<Movement> movements = account.businessObjects
        assertEquals(2, movements.size())
        assertTrue(movements.contains(debit))
        assertTrue(movements.contains(credit))
    }

    @Test
    void bookAndUnbook() {
        Account account = new Account("")
        Movement debit = new Movement(TWENTY, true)
        Movement credit = new Movement(TEN, false)

        account.book(TIME, debit, true)

        ArrayList<Movement> movements = account.businessObjects
        assertEquals(1, movements.size())
        assertTrue(movements.contains(debit))
        assertFalse(movements.contains(credit))
        //
        assertEquals(TWENTY.setScale(2), account.saldo) // 0 + 20 = 20
        assertEquals(TWENTY.setScale(2), account.debetTotal) // 0 + 20 = 20
        assertEquals(ZERO.setScale(2), account.creditTotal) // 0 + 0 = 0

        account.book(TIME, credit, true)

        movements = account.businessObjects
        assertEquals(2, movements.size())
        assertTrue(movements.contains(debit))
        assertTrue(movements.contains(credit))
        //
        assertEquals(TEN.setScale(2), account.saldo) // 20 - 10 = 10
        assertEquals(TWENTY.setScale(2), account.debetTotal) // 20 + 0 = 20
        assertEquals(TEN.setScale(2), account.creditTotal) // 0 + 10 = 10

        account.unbook(TIME,debit, true)

        movements = account.businessObjects
        assertEquals(1, movements.size())
        assertFalse(movements.contains(debit))
        assertTrue(movements.contains(credit))
        //
        assertEquals(TEN.negate().setScale(2), account.saldo) // 20-20 - 10 = -10
        assertEquals(ZERO.setScale(2), account.debetTotal) // 20 - 20 = 0
        assertEquals(TEN.setScale(2), account.creditTotal) // 0 + 10 = 10

        account.unbook(TIME,credit, true)

        movements = account.businessObjects
        assertTrue(movements.empty)
        //
        assertEquals(ZERO.negate().setScale(2), account.saldo)
        assertEquals(ZERO.setScale(2), account.debetTotal)
        assertEquals(ZERO.setScale(2), account.creditTotal)
    }
}
