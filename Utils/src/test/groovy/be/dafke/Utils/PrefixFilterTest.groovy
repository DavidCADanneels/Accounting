package be.dafke.Utils

import org.junit.Test

import javax.swing.DefaultListModel

import static org.junit.Assert.assertEquals

class PrefixFilterTest {

    PrefixFilter<String> setUp(DefaultListModel<String> model){
        ArrayList<String> map = new ArrayList<String>()
        map.add("APPEL")
        map.add("BEER")
        map.add("AAP")
        new PrefixFilter<String>(model, map)
    }

    @Test
    void testDefaultModel(){
        DefaultListModel<String> model = new DefaultListModel<String>()
        PrefixFilter<String> filter = setUp(model)
        filter.filter("A")
        assertEquals(2, model.getSize())
        assertEquals("APPEL", model.get(0))
        assertEquals("AAP", model.get(1))
    }

    @Test
    void testAlphabeticModel(){
        AlphabeticListModel model = new AlphabeticListModel()
        PrefixFilter<String> filter = setUp(model)
        filter.filter("A")
        assertEquals(2, model.getSize())
        assertEquals("AAP",model.get(0))
        assertEquals("APPEL",model.get(1))
    }
}
