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
        BigDecimal debitTotal = account.debitTotal
        BigDecimal creditTotal = account.creditTotal
        BigDecimal saldo = account.saldo

        assertEquals(scaledZero, debitTotal)
        assertEquals(2,debitTotal.scale())
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

    @Test
    void initValues() {
        Account account = new Account("")
        assertEquals(ZERO.setScale(2), account.saldo)
        assertEquals(ZERO.setScale(2), account.debitTotal)
        assertEquals(ZERO.setScale(2), account.creditTotal)

    }

    @Test
    void debit() {
        Account account = new Account("")
        Movement debit = new Movement(TWENTY, true)
        account.book(TIME, debit, true)
        assertEquals(TWENTY.setScale(2), account.saldo)  // 0 + 20 = 20
        assertEquals(TWENTY.setScale(2), account.debitTotal)  // 20
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
        assertEquals(ZERO.setScale(2), account.debitTotal)  // 0
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
        assertEquals(TWENTY.setScale(2), account.debitTotal) // 20
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
        assertEquals(TWENTY.setScale(2), account.debitTotal) // 0 + 20 = 20
        assertEquals(ZERO.setScale(2), account.creditTotal) // 0 + 0 = 0

        account.book(TIME, credit, true)

        movements = account.businessObjects
        assertEquals(2, movements.size())
        assertTrue(movements.contains(debit))
        assertTrue(movements.contains(credit))
        //
        assertEquals(TEN.setScale(2), account.saldo) // 20 - 10 = 10
        assertEquals(TWENTY.setScale(2), account.debitTotal) // 20 + 0 = 20
        assertEquals(TEN.setScale(2), account.creditTotal) // 0 + 10 = 10

        account.unbook(TIME,debit, true)

        movements = account.businessObjects
        assertEquals(1, movements.size())
        assertFalse(movements.contains(debit))
        assertTrue(movements.contains(credit))
        //
        assertEquals(TEN.negate().setScale(2), account.saldo) // 20-20 - 10 = -10
        assertEquals(ZERO.setScale(2), account.debitTotal) // 20 - 20 = 0
        assertEquals(TEN.setScale(2), account.creditTotal) // 0 + 10 = 10

        account.unbook(TIME,credit, true)

        movements = account.businessObjects
        assertTrue(movements.isEmpty())
        //
        assertEquals(ZERO.negate().setScale(2), account.saldo)
        assertEquals(ZERO.setScale(2), account.debitTotal)
        assertEquals(ZERO.setScale(2), account.creditTotal)
    }
}
