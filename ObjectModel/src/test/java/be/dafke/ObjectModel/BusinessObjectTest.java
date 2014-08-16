package be.dafke.ObjectModel;

import org.junit.Test;

import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Created by David Danneels on 16/08/2014.
 */
public class BusinessObjectTest {
    @Test
    public void defaultObjectType(){
        BusinessObject businessObject=new BusinessObject();
        assertEquals(businessObject.getClass().getSimpleName(), businessObject.getBusinessObjectType());
        // HERE:
//        assertEquals("BusinessObject", businessObject.getBusinessObjectType());
    }

    @Test
    public void toStringReturnsName(){
        BusinessObject businessObject=new BusinessObject();
        assertNull(businessObject.getName());
        assertNull( businessObject.toString());
        final String NAME = "Just a name";
        businessObject.setName(NAME);
        assertEquals(NAME,businessObject.getName());
        assertEquals(NAME, businessObject.toString());
    }

    @Test
    public void defaultValues(){
        BusinessObject businessObject=new BusinessObject();
        assertEquals("BusinessObject", businessObject.getBusinessObjectType());
        assertNull(businessObject.getName());
        assertFalse(businessObject.isDeletable());
        assertFalse(businessObject.isMergeable());
    }

    @Test
    public void defaultKeysAndProperties(){
        final String NAME="name";
        final int NR_OF_KEYS = 1;

        BusinessObject businessObject=new BusinessObject();

        final Set<String> initKeySet = businessObject.getInitKeySet();
        assertEquals(NR_OF_KEYS,initKeySet.size());
        assertEquals(NAME,initKeySet.iterator().next());

        final TreeMap<String, String> initProperties = businessObject.getInitProperties(null);
        assertEquals(NR_OF_KEYS,initProperties.size());
        assertEquals(NAME,initProperties.firstKey());
        assertNull(initProperties.get(NAME));

        final TreeMap<String, String> uniqueProperties = businessObject.getUniqueProperties();
        assertEquals(NR_OF_KEYS,uniqueProperties.size());
        assertEquals(NAME,uniqueProperties.firstKey());
        assertNull(uniqueProperties.get(NAME));
    }
}
