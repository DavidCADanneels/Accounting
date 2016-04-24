package be.dafke.BusinessModel;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by ddanneels on 7/12/2015.
 */
public class MovementTest {

    public static final BigDecimal AMOUNT = BigDecimal.TEN;
    public static final BigDecimal ANOTHER_AMOUNT = BigDecimal.ONE;

    @Test
    public void getId(){
        Movement movement = new Movement(AMOUNT, true);
        int id = movement.getId();

        movement = new Movement(AMOUNT, false);
        assertEquals(id+1,movement.getId().intValue());
    }

    @Test
    public void getAmount(){
        Movement movement = new Movement(AMOUNT, true);
        assertEquals(AMOUNT,movement.getAmount());
        movement.setAmount(ANOTHER_AMOUNT);
        assertEquals(ANOTHER_AMOUNT,movement.getAmount());

        movement = new Movement(AMOUNT, false);
        assertEquals(AMOUNT,movement.getAmount());
        movement.setAmount(ANOTHER_AMOUNT);
        assertEquals(ANOTHER_AMOUNT,movement.getAmount());
    }

    @Test
    public void isDebit(){
        Movement movement = new Movement(AMOUNT, true);
        assertTrue(movement.isDebit());
        movement.setDebit(false);
        assertFalse(movement.isDebit());

        movement = new Movement(AMOUNT, false);
        assertFalse(movement.isDebit());
        movement.setDebit(true);
        assertTrue(movement.isDebit());
    }

    @Test
    public void getBooking(){
        Movement movement = new Movement(AMOUNT, true);
        assertNull(movement.getBooking());
        Accounts accounts = new Accounts(new Accounting());
        Booking booking = new Booking(accounts);
        movement.setBooking(booking);
        assertEquals(booking,movement.getBooking());
    }

    @Test
    public void getUniqueProperties(){
        Movement movement = new Movement(AMOUNT,true);
        TreeMap<String, String> uniqueProperties = movement.getUniqueProperties();
        assertTrue(uniqueProperties.isEmpty());
    }

    @Test
    public void getInitKeySet(){
        Movement movement = new Movement(AMOUNT,true);
        Set<String> initKeySet = movement.getInitKeySet();
        assertTrue(initKeySet.contains(Movement.DEBIT));
        assertTrue(initKeySet.contains(Movement.CREDIT));
    }

    @Test
    public void getInitProperties(){
        Movement movement = new Movement(AMOUNT,true);
        TreeMap<String, String> uniqueProperties = movement.getUniqueProperties();
        assertTrue(uniqueProperties.isEmpty());
    }
}
