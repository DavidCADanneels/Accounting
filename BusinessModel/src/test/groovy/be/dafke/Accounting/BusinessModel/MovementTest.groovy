package be.dafke.Accounting.BusinessModel

import org.junit.Test

import static org.junit.Assert.*

class MovementTest {

    static final BigDecimal AMOUNT = BigDecimal.TEN
    static final BigDecimal ANOTHER_AMOUNT = BigDecimal.ONE

    @Test
    void getId(){
        Movement movement = new Movement(AMOUNT, true)
        int id = movement.id

        movement = new Movement(AMOUNT, false)
        assertEquals(id+1,movement.id.intValue())
    }

    @Test
    void getAmount(){
        Movement movement = new Movement(AMOUNT, true)
        assertEquals(AMOUNT,movement.amount)
        movement.amount = ANOTHER_AMOUNT
        assertEquals(ANOTHER_AMOUNT,movement.amount)

        movement = new Movement(AMOUNT, false)
        assertEquals(AMOUNT,movement.amount)
        movement.amount = ANOTHER_AMOUNT
        assertEquals(ANOTHER_AMOUNT,movement.amount)
    }

    @Test
    void isDebit(){
        Movement movement = new Movement(AMOUNT, true)
        assertTrue(movement.debit)
        movement.debit = false
        assertFalse(movement.debit)

        movement = new Movement(AMOUNT, false)
        assertFalse(movement.debit)
        movement.debit = true
        assertTrue(movement.debit)
    }

//    @Test
//    void getBooking(){
//        Movement movement = new Movement(AMOUNT, true)
//        assertNull(movement.booking)
//        Accounts accounts = new Accounts(new Accounting())
//        Booking booking = new Booking()
//        movement.setBooking(booking)
//        assertEquals(booking,movement.booking)
//    }

//    @Test
//    void getUniqueProperties(){
//        Movement movement = new Movement(AMOUNT,true)
//        TreeMap<String, String> uniqueProperties = movement.getUniqueProperties()
//        assertTrue(uniqueProperties.isEmpty())
//    }
//
//    @Test
//    void getInitProperties(){
//        Movement movement = new Movement(AMOUNT,true)
//        TreeMap<String, String> uniqueProperties = movement.getUniqueProperties()
//        assertTrue(uniqueProperties.isEmpty())
//    }
}
