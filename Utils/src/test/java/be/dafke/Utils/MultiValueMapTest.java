package be.dafke.Utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MultiValueMapTest {
    public static final String KEY1 = "Key1";
    public static final String KEY2 = "Key2";
    public static final String KEY3 = "Key3";
    public static final String KEY4 = "Key4";
    public static final String VALUE1 = "Value1";
    public static final String VALUE2 = "Value2";
    public static final String VALUE3 = "Value3";
    public static final String VALUE4 = "Value4";
    public static final String VALUE5 = "Value5";
    public static final String VALUE6 = "Value6";
    public static final String VALUE7 = "Value7";
    public static final String VALUE8 = "Value8";
    public static final String VALUE9 = "Value9";
    private MultiValueMap<String, String> map;

    @Before
    public void init(){
        map = new MultiValueMap<String, String>();
    }

    private void createDefaultMap(){
        // K1: V(1-2)
        // K2: V(3-4)
        // K3: V(5-6-7)
        // K4: V(8-9)
        map.addValue(KEY1, VALUE1);
        map.addValue(KEY1, VALUE2);

        map.addValue(KEY2, VALUE3);
        map.addValue(KEY2, VALUE4);

        map.addValue(KEY3, VALUE5);
        map.addValue(KEY3, VALUE6);
        map.addValue(KEY3, VALUE7);

        map.addValue(KEY4, VALUE8);
        map.addValue(KEY4, VALUE9);
    }

    @Test
    public void getKeyMaps(){
        createDefaultMap();
        assertEquals(2, map.get(KEY1).size());
        assertTrue(map.get(KEY1).contains(VALUE1));
        assertTrue(map.get(KEY1).contains(VALUE2));

        assertEquals(2, map.get(KEY2).size());
        assertTrue(map.get(KEY2).contains(VALUE3));
        assertTrue(map.get(KEY2).contains(VALUE4));

        assertEquals(3, map.get(KEY3).size());
        assertTrue(map.get(KEY3).contains(VALUE5));
        assertTrue(map.get(KEY3).contains(VALUE6));
        assertTrue(map.get(KEY3).contains(VALUE7));

        assertEquals(2, map.get(KEY4).size());
        assertTrue(map.get(KEY4).contains(VALUE8));
        assertTrue(map.get(KEY4).contains(VALUE9));
    }

    @Test
    public void totalSizeOfMap(){
        createDefaultMap();
        assertEquals(9, map.values().size());
    }

    @Test
    public void removeKeys(){
        createDefaultMap();
        map.removeValue(KEY3, VALUE6);
        assertEquals(2, map.get(KEY3).size());
        assertTrue(map.get(KEY3).contains(VALUE5));
        assertFalse(map.get(KEY3).contains(VALUE6));
        assertTrue(map.get(KEY3).contains(VALUE7));
    }

    @Test
    public void tailList(){
        map.addValue(KEY1, VALUE1);
        map.addValue(KEY2, VALUE2);
        map.addValue(KEY2, VALUE3);
        assertEquals(3, map.tailList(KEY1, true).size());
        assertEquals(2, map.tailList(KEY1, false).size());
        assertEquals(2, map.tailList(KEY2,true).size());
        assertEquals(0, map.tailList(KEY2,false).size());
        map.removeValue(KEY2, VALUE2);

    }

}
