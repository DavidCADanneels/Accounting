package be.dafke.Utils;


import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * User: Dafke
 * Date: 14/01/13
 * Time: 6:44
 */
public class PrefixFilterTest {

    public PrefixFilter<String> setUp(DefaultListModel<String> model){
        ArrayList<String> map = new ArrayList<String>();
        map.add("APPEL");
        map.add("BEER");
        map.add("AAP");
        return new PrefixFilter<String>(model, map);
    }

    @Test
    public void testDefaultModel(){
        DefaultListModel<String> model = new DefaultListModel<String>();
        PrefixFilter<String> filter = setUp(model);
        filter.filter("A");
        assertEquals(2, model.getSize());
        assertEquals("APPEL", model.get(0));
        assertEquals("AAP", model.get(1));
    }

    @Test
    public void testAlphabeticModel(){
        AlphabeticListModel model = new AlphabeticListModel();
        PrefixFilter<String> filter = setUp(model);
        filter.filter("A");
        assertEquals(2, model.getSize());
        assertEquals("AAP",model.get(0));
        assertEquals("APPEL",model.get(1));
    }
}
