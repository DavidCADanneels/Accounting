package be.dafke.Accounting.Objects.Accounting;

import junitx.util.PrivateAccessor;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * User: Dafke
 * Date: 15/01/13
 * Time: 7:17
 */
public class AccountTest {

    @Test
    public void test_init(){
        Account account = new Account("NAME", Account.AccountType.Active);

        assertEquals("NAME",account.getName());
        assertEquals("NAME",account.toString());
        assertEquals(Account.AccountType.Active,account.getType());

        BigDecimal ZERO = BigDecimal.ZERO.setScale(2);
        assertEquals(ZERO,account.getDebetTotal());
        assertEquals(ZERO,account.getCreditTotal());
        assertEquals(ZERO,account.saldo());

        assertNull(account.getProject());
        assertNotNull(account.getBookings());
        assertEquals(0,account.getBookings().size());

        account.setName("NEWNAME");
        assertEquals("NEWNAME", account.getName());
        assertEquals("NEWNAME",account.toString());

        account.setType(Account.AccountType.Cost);
        assertEquals(Account.AccountType.Cost, account.getType());

        account.setProject(new Project("PROJECT"));
        assertNotNull(account.getProject());
        assertEquals("NEWNAME", account.getName());
        assertEquals("NEWNAME [PROJECT]", account.toString());
    }

    @Test
    public void test_debit(){
        Account account = new Account("NAME", Account.AccountType.Active);
//        account.toXML();
        Booking booking = new Booking(null,"",account,BigDecimal.ONE,true, Calendar.getInstance());
        Class[] classes= new Class[1];
        classes[0] = Booking.class;
        Booking[] args = new Booking[1];
        args[0] = booking;
        try{
            PrivateAccessor.invoke(account, "addBooking",classes,args );
        }catch (Throwable e){
             fail("error");
        }
        assertEquals(1,account.getBookings().size());
        BigDecimal ZERO = BigDecimal.ZERO.setScale(2);
        BigDecimal ONE = BigDecimal.ONE.setScale(2);
        assertEquals(ZERO,account.getDebetTotal());
        assertEquals(ZERO,account.getCreditTotal());
        assertEquals(ZERO,account.saldo());
        try{
            PrivateAccessor.invoke(account, "book",classes,args );
        }catch (Throwable e){
            fail("error");
        }
        assertEquals(ONE,account.getDebetTotal());
        assertEquals(ZERO,account.getCreditTotal());
        assertEquals(ONE,account.saldo());
    }
}
