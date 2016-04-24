package be.dafke.ObjectModel;

import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * Created by David Danneels on 16/08/2014.
 */
public class BusinessCollectionAsCollectionTest{
    public static final String EMPTY_STRING = "";
    BusinessCollection<BusinessObject> businessCollection;
    String businessCollectionType;
    final String NAME1 = "Just a name";
    final String NAME2 = "Just another name";

    @Before
    public void createCollection() {
        businessCollection = new BusinessCollection<BusinessObject>() {
            @Override
            public String getChildType() {
                return null;
            }

            public BusinessObject createNewChild(TreeMap<String, String> properties) {
                return null;
            }
        };
        businessCollectionType = businessCollection.getClass().getSimpleName();
    }

    @Test
    public void defaultToString(){
        assertNull(businessCollection.getName());
        assertEquals(businessCollectionType + ":\r\n", businessCollection.toString());
        businessCollection.setName(NAME1);
        assertEquals(NAME1, businessCollection.getName());
        assertEquals(businessCollectionType + ":\r\n", businessCollection.toString());
    }

    @Test (expected = EmptyNameException.class)
    public void addBusinessObjectsEmptyName() throws EmptyNameException, DuplicateNameException {
        BusinessObject businessObject = new BusinessObject();
        businessCollection.addBusinessObject(businessObject);
    }

    @Test (expected = DuplicateNameException.class)
    public void addBusinessObjectsDuplicateName() throws EmptyNameException, DuplicateNameException {
        BusinessObject businessObject1 = new BusinessObject();
        BusinessObject businessObject2 = new BusinessObject();
        businessObject1.setName(NAME1);
        businessObject2.setName(NAME1);
        businessCollection.addBusinessObject(businessObject1);
        businessCollection.addBusinessObject(businessObject2);
    }

    private BusinessObject createBusinessObject(String name){
        BusinessObject businessObject = new BusinessObject();
        if(name != null){
            businessObject.setName(name);
        }
        return businessObject;
    }

    @Test
    public void addOneBusinessObjectToString() throws EmptyNameException, DuplicateNameException {
        BusinessObject businessObject = createBusinessObject(NAME1);
        businessCollection.addBusinessObject(businessObject);
        assertEquals(businessCollectionType + ":\r\n"
                        + businessObject.toString(),
                businessCollection.toString());
    }

    @Test
    public void addMultipleBusinessObjectsToString() throws EmptyNameException, DuplicateNameException {
        BusinessObject businessObject1 = createBusinessObject(NAME1);
        BusinessObject businessObject2 = createBusinessObject(NAME2);
        businessCollection.addBusinessObject(businessObject1);
        businessCollection.addBusinessObject(businessObject2);
        assertEquals(businessCollectionType + ":\r\n"
                        + businessObject1.toString()
                        + businessObject2.toString(),
                businessCollection.toString());
    }

    @Test
    public void getBusinessObjects() throws EmptyNameException, DuplicateNameException {
        BusinessObject businessObject1 = createBusinessObject(NAME1);
        BusinessObject businessObject2 = createBusinessObject(NAME2);
        businessCollection.addBusinessObject(businessObject1);
        businessCollection.addBusinessObject(businessObject2);
        assertEquals(businessObject1, businessCollection.getBusinessObject(NAME1));
        assertEquals(businessObject2, businessCollection.getBusinessObject(NAME2));
        ArrayList<BusinessObject> businessObjects = businessCollection.getBusinessObjects();
        assertTrue(businessObjects.contains(businessObject1));
        assertTrue(businessObjects.contains(businessObject2));
    }

    @Test (expected = DuplicateNameException.class)
     public void modifyDuplicateNameException() throws EmptyNameException, DuplicateNameException {
        BusinessObject businessObject1 = createBusinessObject(NAME1);
        BusinessObject businessObject2 = createBusinessObject(NAME2);
        businessCollection.addBusinessObject(businessObject1);
        businessCollection.addBusinessObject(businessObject2);
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<String,String>(businessCollection.NAME, NAME1);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<String,String>(businessCollection.NAME, NAME2);
        businessCollection.modify(oldEntry, newEntry);
    }

    @Test (expected = EmptyNameException.class)
     public void modifyEmptyNameException() throws EmptyNameException, DuplicateNameException {
        BusinessObject businessObject1 = createBusinessObject(NAME1);
        businessCollection.addBusinessObject(businessObject1);
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<String,String>(businessCollection.NAME, NAME1);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<String,String>(businessCollection.NAME, EMPTY_STRING);
        businessCollection.modify(oldEntry, newEntry);
    }

    @Test (expected = EmptyNameException.class)
    public void modifyNull() throws EmptyNameException, DuplicateNameException {
        BusinessObject businessObject1 = createBusinessObject(NAME1);
        businessCollection.addBusinessObject(businessObject1);
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<String,String>(businessCollection.NAME, NAME1);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<String,String>(businessCollection.NAME, null);
        businessCollection.modify(oldEntry, newEntry);
    }

    @Test
    public void modifySameValue() throws EmptyNameException, DuplicateNameException {
        BusinessObject businessObject1 = createBusinessObject(NAME1);
        businessCollection.addBusinessObject(businessObject1);
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<String,String>(businessCollection.NAME, NAME1);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<String,String>(businessCollection.NAME, NAME1);
        businessCollection.modify(oldEntry, newEntry);
    }

    @Test (expected = RuntimeException.class)
    public void modifyDifferentKey() throws EmptyNameException, DuplicateNameException {
        BusinessObject businessObject1 = createBusinessObject(NAME1);
        businessCollection.addBusinessObject(businessObject1);
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<String,String>(businessCollection.NAME, NAME1);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<String,String>("other key", NAME1);
        businessCollection.modify(oldEntry, newEntry);
    }

    @Test (expected = NotEmptyException.class)
    public void removeDeletableBusinessObject() throws EmptyNameException, DuplicateNameException, NotEmptyException {
        BusinessObject businessObject = createBusinessObject(NAME1);
        businessCollection.addBusinessObject(businessObject);
        businessCollection.removeBusinessObject(businessObject);
    }

    private class DeletableBusinessObject extends BusinessObject{
        public DeletableBusinessObject(String name){
            super();
            setName(name);
        }

        @Override
        public boolean isDeletable(){
            return true;
        }
    }

    @Test
    public void removeNondeletableBusinessObject() throws EmptyNameException, DuplicateNameException, NotEmptyException {
        BusinessObject deletableBusinessObject = new DeletableBusinessObject(NAME1);
        businessCollection.addBusinessObject(deletableBusinessObject);
        businessCollection.removeBusinessObject(deletableBusinessObject);
    }

    @Test
    public void defaultCollectionValues() {
//        assertFalse(businessCollection.writeGrandChildren());
    }

    @Test
    public void currentObject(){
        assertNull(businessCollection.getCurrentObject());
        BusinessObject object = new BusinessObject();
        businessCollection.setCurrentObject(object);
        assertEquals(object, businessCollection.getCurrentObject());
    }
}
