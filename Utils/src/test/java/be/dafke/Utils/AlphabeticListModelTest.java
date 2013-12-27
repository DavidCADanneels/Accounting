package be.dafke.Utils;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: Dafke
 * Date: 14/01/13
 * Time: 6:33
 */
public class AlphabeticListModelTest {

    @Test
    public void addElement_test_correct_order(){
        AlphabeticListModel model = new AlphabeticListModel();
        model.addElement("AAP");
        model.addElement("BEER");
        assertEquals("AAP",model.get(0));
        assertEquals("BEER",model.get(1));
    }

    @Test
    public void addElement_test_reversed_order(){
        AlphabeticListModel model = new AlphabeticListModel();
        model.addElement("BEER");
        model.addElement("AAP");
        assertEquals("AAP",model.get(0));
        assertEquals("BEER",model.get(1));
    }
}
