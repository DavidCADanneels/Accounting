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
    protected BusinessObject businessObject = new BusinessObject();
    protected String businessObjectType = businessObject.getClass().getSimpleName();

    @Test
    public void defaultObjectType(){
        assertEquals(businessObjectType, businessObject.getBusinessObjectType());
    }

    @Test
    public void defaultToString(){
        assertNull(businessObject.getName());
        assertNull(businessObject.toString());
        final String NAME = "Just a name";
        businessObject.setName(NAME);
        assertEquals(NAME,businessObject.getName());
        assertEquals(NAME, businessObject.toString());
    }

    @Test
    public void defaultValues(){
        assertNull(businessObject.getName());
        assertFalse(businessObject.isDeletable());
        assertFalse(businessObject.isMergeable());
    }

    @Test
    public void defaultKeysAndProperties(){
        final String NAME="name";
        final int NR_OF_KEYS = 1;

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

    @Test
    public void setInitProperties(){
        TreeMap<String, String> initProperties = new TreeMap<String, String>();
        businessObject.setInitProperties(initProperties);
        assertEquals(null, businessObject.getName());
        final String NEW_NAME = "new name";
        initProperties.put(businessObject.NAME, NEW_NAME);
        businessObject.setInitProperties(initProperties);
        assertEquals(NEW_NAME, businessObject.getName());
    }
}
