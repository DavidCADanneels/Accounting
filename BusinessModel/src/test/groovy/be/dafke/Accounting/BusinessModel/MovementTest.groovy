package be.dafke.Accounting.BusinessModel

import org.junit.Test

import static org.junit.Assert.*

class MovementTest {

    static final BigDecimal AMOUNT = BigDecimal.TEN
    static final BigDecimal ANOTHER_AMOUNT = BigDecimal.ONE

    @Test
    void getId(){
        Movement movement = new Movement(AMOUNT, true)
        int id = movement.getId()

        movement = new Movement(AMOUNT, false)
        assertEquals(id+1,movement.getId().intValue())
    }

    @Test
    void getAmount(){
        Movement movement = new Movement(AMOUNT, true)
        assertEquals(AMOUNT,movement.getAmount())
        movement.setAmount(ANOTHER_AMOUNT)
        assertEquals(ANOTHER_AMOUNT,movement.getAmount())

        movement = new Movement(AMOUNT, false)
        assertEquals(AMOUNT,movement.getAmount())
        movement.setAmount(ANOTHER_AMOUNT)
        assertEquals(ANOTHER_AMOUNT,movement.getAmount())
    }

    @Test
    void isDebit(){
        Movement movement = new Movement(AMOUNT, true)
        assertTrue(movement.isDebit())
        movement.setDebit(false)
        assertFalse(movement.isDebit())

        movement = new Movement(AMOUNT, false)
        assertFalse(movement.isDebit())
        movement.setDebit(true)
        assertTrue(movement.isDebit())
    }

//    @Test
//    void getBooking(){
//        Movement movement = new Movement(AMOUNT, true)
//        assertNull(movement.getBooking())
//        Accounts accounts = new Accounts(new Accounting())
//        Booking booking = new Booking()
//        movement.setBooking(booking)
//        assertEquals(booking,movement.getBooking())
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
