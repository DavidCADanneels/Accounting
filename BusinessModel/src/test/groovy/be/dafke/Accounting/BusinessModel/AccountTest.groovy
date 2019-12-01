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
    private final BigDecimal TWENTY = new BigDecimal(20)
    private final BigDecimal TEN = BigDecimal.TEN
    private final BigDecimal ZERO = BigDecimal.ZERO

    @Test
    void defaultValues(){
        Account account = new Account("")
//        assertNull(account.createNewChild())
        assertEquals(account.getName(), account.toString())
//        Set<String> initKeySet = account.getInitKeySet()
//        assertTrue(initKeySet.contains(Account.TYPE))
//        assertTrue(initKeySet.contains(Account.DEFAULTAMOUNT))
    }

    @Test
    void initialAmounts(){
        BigDecimal scaledZero = new BigDecimal(0)
        scaledZero = scaledZero.setScale(2)

        Account account = new Account("")
        BigDecimal debetTotal = account.getDebetTotal()
        BigDecimal creditTotal = account.getCreditTotal()
        BigDecimal saldo = account.getSaldo()

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
        assertEquals("a",copy.getName())

        AccountType type = new AccountType()
        account.setType(type)
        assertNull(copy.getType())
    }

    @Test
    void deletable(){
        Account account = new Account("")
        assertTrue(account.isDeletable())
        // add movements
//        assertFalse(account.isDeletable())
    }

    @Test
    void defaultAmount() throws EmptyNameException, DuplicateNameException {
        Account account = new Account("")
        assertNull(account.getDefaultAmount())
        BigDecimal amount = BigDecimal.TEN
        account.setDefaultAmount(amount)
        assertEquals(amount, account.getDefaultAmount())
    }

    @Test
    void type() {
        Account account = new Account("")
        assertNull(account.getType())

        AccountType type = new AccountType()
        account.setType(type)
        assertEquals(type, account.getType())
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
//        assertEquals(account.getName(), initProperties.get(BusinessObject.NAME))
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
//        assertEquals(new BigDecimal(correctValue), account.getDefaultAmount())
//        initProperties.put(Account.DEFAULTAMOUNT, wrongValue)
//        account.setInitProperties(initProperties)
//        assertNull(account.getDefaultAmount())
//        initProperties.put(Account.DEFAULTAMOUNT, null)
//        account.setInitProperties(initProperties)
//        assertNull(account.getDefaultAmount())
//    }

    @Test
    void initValues() {
        Account account = new Account("")
        assertEquals(ZERO.setScale(2), account.getSaldo())
        assertEquals(ZERO.setScale(2), account.getDebetTotal())
        assertEquals(ZERO.setScale(2), account.getCreditTotal())

    }

    @Test
    void debit() {
        Account account = new Account("")
        Movement debit = new Movement(TWENTY, true)
        account.book(TIME, debit, true)
        assertEquals(TWENTY.setScale(2), account.getSaldo())  // 0 + 20 = 20
        assertEquals(TWENTY.setScale(2), account.getDebetTotal())  // 20
        assertEquals(ZERO.setScale(2), account.getCreditTotal())  // 0

        ArrayList<Movement> movements = account.getBusinessObjects()
        assertEquals(1, movements.size())
        assertTrue(movements.contains(debit))
    }

    @Test
    void credit() {
        Account account = new Account("")
        Movement credit = new Movement(TEN, false)
        account.book(TIME, credit, true)
        assertEquals(TEN.negate().setScale(2), account.getSaldo())  // 0 - 10 = -10
        assertEquals(ZERO.setScale(2), account.getDebetTotal())  // 0
        assertEquals(TEN.setScale(2), account.getCreditTotal())  // 10

        ArrayList<Movement> movements = account.getBusinessObjects()
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

        assertEquals(TEN.setScale(2), account.getSaldo()) // 20 - 10 = 10
        assertEquals(TWENTY.setScale(2), account.getDebetTotal()) // 20
        assertEquals(TEN.setScale(2), account.getCreditTotal()) // 10

        ArrayList<Movement> movements = account.getBusinessObjects()
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

        ArrayList<Movement> movements = account.getBusinessObjects()
        assertEquals(1, movements.size())
        assertTrue(movements.contains(debit))
        assertFalse(movements.contains(credit))
        //
        assertEquals(TWENTY.setScale(2), account.getSaldo()) // 0 + 20 = 20
        assertEquals(TWENTY.setScale(2), account.getDebetTotal()) // 0 + 20 = 20
        assertEquals(ZERO.setScale(2), account.getCreditTotal()) // 0 + 0 = 0

        account.book(TIME, credit, true)

        movements = account.getBusinessObjects()
        assertEquals(2, movements.size())
        assertTrue(movements.contains(debit))
        assertTrue(movements.contains(credit))
        //
        assertEquals(TEN.setScale(2), account.getSaldo()) // 20 - 10 = 10
        assertEquals(TWENTY.setScale(2), account.getDebetTotal()) // 20 + 0 = 20
        assertEquals(TEN.setScale(2), account.getCreditTotal()) // 0 + 10 = 10

        account.unbook(TIME,debit, true)

        movements = account.getBusinessObjects()
        assertEquals(1, movements.size())
        assertFalse(movements.contains(debit))
        assertTrue(movements.contains(credit))
        //
        assertEquals(TEN.negate().setScale(2), account.getSaldo()) // 20-20 - 10 = -10
        assertEquals(ZERO.setScale(2), account.getDebetTotal()) // 20 - 20 = 0
        assertEquals(TEN.setScale(2), account.getCreditTotal()) // 0 + 10 = 10

        account.unbook(TIME,credit, true)

        movements = account.getBusinessObjects()
        assertTrue(movements.isEmpty())
        //
        assertEquals(ZERO.negate().setScale(2), account.getSaldo())
        assertEquals(ZERO.setScale(2), account.getDebetTotal())
        assertEquals(ZERO.setScale(2), account.getCreditTotal())
    }
}
