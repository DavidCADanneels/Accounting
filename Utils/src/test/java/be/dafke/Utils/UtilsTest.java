package be.dafke.Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * User: david
 * Date: 17/08/14
 * Time: 12:59
 */
public class UtilsTest {

    @Test
    public void stringToCalendar(){
        Calendar calendar = Utils.toCalendar("01/03/2000");
        assertEquals(1, calendar.get(Calendar.DATE));
        assertEquals(2, calendar.get(Calendar.MONTH));  // 2+1=3 (March)
        assertEquals(2000, calendar.get(Calendar.YEAR));
        assertEquals("1/3/2000", Utils.toString(calendar));

        Calendar calendar2 = Utils.toCalendar("5/6/1998");
        assertEquals(5, calendar2.get(Calendar.DATE));
        assertEquals(5, calendar2.get(Calendar.MONTH));  // 2+1=3 (March)
        assertEquals(1998, calendar2.get(Calendar.YEAR));
        assertEquals("5/6/1998", Utils.toString(calendar2));
    }

    @Test
    public void calendarToString(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 17);
        calendar.set(Calendar.MONTH, 7);    //7+1=8 (August)
        calendar.set(Calendar.YEAR, 2014);
        assertEquals("17/8/2014", Utils.toString(calendar));
    }

    @Test
    public void parseBigDecimal() {
        assertEquals("25.45", Utils.parseBigDecimal("25.45").toPlainString());
    }

    @Test
    public void parseBigDecimal_Invalid(){
        assertNull(Utils.parseBigDecimal("15.368.65"));
        assertNull(Utils.parseBigDecimal("text"));
    }

    @Test
    public void parseInt() {
        assertEquals(25, Utils.parseInt("25"));
    }

    @Test
    public void parseInt_Decimal() {
        assertEquals(0, Utils.parseInt("25.45"));
    }

    @Test
    public void parseInt_Invalid(){
        assertEquals(0, Utils.parseInt("text"));
    }

    @Test
    public void parseStringList(){
        String text = "A | B | C";
        ArrayList<String> stringArrayList = Utils.parseStringList(text);
        assertEquals(3, stringArrayList.size());
        assertEquals("A", stringArrayList.get(0));
        assertEquals("B", stringArrayList.get(1));
        assertEquals("C", stringArrayList.get(2));
    }

    @Test
    public void ArrayListToString(){
        ArrayList<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        assertEquals("A | B | C", Utils.toString(list));
    }
}
