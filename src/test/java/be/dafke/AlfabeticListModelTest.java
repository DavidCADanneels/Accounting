package be.dafke;

import org.testng.annotations.Test;

import static junit.framework.Assert.assertEquals;
//import junit.framework.Test;
//import org.springframework.

/**
 * User: Dafke
 * Date: 14/01/13
 * Time: 6:33
 */
public class AlfabeticListModelTest {

    @Test
    public void addElement_test_correct_order(){
        AlfabeticListModel model = new AlfabeticListModel();
        model.addElement("AAP");
        model.addElement("BEER");
        assertEquals("AAP",model.get(0));
        assertEquals("BEER",model.get(1));
    }

    @Test
    public void addElement_test_reversed_order(){
        AlfabeticListModel model = new AlfabeticListModel();
        model.addElement("BEER");
        model.addElement("AAP");
        assertEquals("AAP",model.get(0));
        assertEquals("BEER",model.get(1));
    }
}
