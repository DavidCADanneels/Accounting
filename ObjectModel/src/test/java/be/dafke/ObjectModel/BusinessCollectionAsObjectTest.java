package be.dafke.ObjectModel;

import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by David Danneels on 16/08/2014.
 */
public class BusinessCollectionAsObjectTest extends BusinessObjectTest{
    @Before
    public void createCollectionAsObject() {
        businessObject = new BusinessCollection<BusinessObject>() {
            @Override
            public String getChildType() {
                return null;
            }

            public BusinessObject createNewChild(TreeMap<String, String> properties) {
                return null;
            }

        };
        businessObjectType = businessObject.getClass().getSimpleName();
    }

    @Test
    public void defaultToString(){
        assertNull(businessObject.getName());
        assertEquals(businessObjectType +":\n" ,businessObject.toString());
        final String NAME = "Just a name";
        businessObject.setName(NAME);
        assertEquals(NAME,businessObject.getName());
        assertEquals(businessObjectType +":\n" ,businessObject.toString());
    }
}
