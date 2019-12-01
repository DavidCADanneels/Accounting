package be.dafke.Utils

import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

class AlphabeticListModelTest {
    static final NamedObject AAP = new NamedObject("AAP")
    static final NamedObject AAP_DUPLICATE = new NamedObject("AAP")
    static final NamedObject AAP_TRIPLICATE = new NamedObject("AAP")

    static final NamedObject BEER = new NamedObject("BEER")
    static final NamedObject BEER_DUPLICATE = new NamedObject("BEER")

    static final NamedObject CLOWN = new NamedObject("CLOWN")
    static final NamedObject CLOWN_DUPLICATE = new NamedObject("CLOWN")

    AlphabeticListModel model

    @Before
    void init(){
        model = new AlphabeticListModel()
    }

    void createNormalList(){
        model.addElement(AAP)
        model.addElement(BEER)
        model.addElement(CLOWN)
    }

    void createReversedList(){
        model.addElement(CLOWN)
        model.addElement(BEER)
        model.addElement(AAP)
    }

    void assertOrder(){
        assertEquals(0, model.indexOf(AAP))
        assertEquals(1, model.indexOf(BEER))
        assertEquals(2, model.indexOf(CLOWN))
    }

    @Test
    void addCorrectOrder(){
        createNormalList()
        assertOrder()
    }

    @Test
    void addReversedOrder(){
        createReversedList()
        assertOrder()
    }

    @Test
    void insertFirstHalf(){
        createNormalList()
        NamedObject appel = new NamedObject("APPEL")
        model.addElement(appel)
        assertEquals(1,model.indexOf(appel))
    }

    @Test
    void insertSecondHalf(){
        createNormalList()
        NamedObject bib = new NamedObject("BIB")
        model.addElement(bib)
        assertEquals(2,model.indexOf(bib))
    }

    @Test
    void addDuplicateOfFirstElement(){
        createNormalList()
        model.addElement(AAP_DUPLICATE)
        assertEquals(AAP_DUPLICATE,model.getElementAt(0))
        // element is inserted before original
    }

    @Test
    void addDuplicateOfRandomElement(){
        createNormalList()
        int originalIndex = model.indexOf(BEER)
        model.addElement(BEER_DUPLICATE)
        assertEquals(originalIndex, model.indexOf(BEER_DUPLICATE))
        assertEquals(originalIndex+1, model.indexOf(BEER))
    }

    @Test
    void addDuplicateOfLastElement(){
        createNormalList()
        model.addElement(CLOWN_DUPLICATE)
        assertEquals(CLOWN_DUPLICATE, model.getElementAt(model.size()-1))
    }

    @Test
    void addNewDuplicateBeforeOriginal(){
        model.addElement(AAP)
        assertEquals(AAP, model.get(0))

        model.addElement(AAP_DUPLICATE)
        assertEquals(AAP_DUPLICATE, model.get(0))
        assertEquals(AAP, model.get(1))
        // 2 - 1

        model.addElement(AAP_TRIPLICATE)
        assertEquals(AAP_TRIPLICATE, model.get(0))
        assertEquals(AAP_DUPLICATE, model.get(1))
        assertEquals(AAP, model.get(2))
        // 3 - 2 - 1

        NamedObject aap = new NamedObject("AAP")
        model.addElement(aap)
        assertEquals(aap, model.get(0))
        assertEquals(AAP_TRIPLICATE, model.get(1))
        assertEquals(AAP_DUPLICATE, model.get(2))
        assertEquals(AAP, model.get(3))
        // 4 - 3 - 2 - 1
    }

    static class NamedObject {
        String name

        NamedObject(String name) {
            this.name = name
        }

        @Override
        String toString(){
            name
        }
    }
}