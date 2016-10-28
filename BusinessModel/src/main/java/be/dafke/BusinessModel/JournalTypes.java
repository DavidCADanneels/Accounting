package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.TreeMap;

public class JournalTypes extends BusinessTypeCollection<JournalType> {

    @Override
    public String getChildType(){
        return "JournalType";
    }

    @Override
    public JournalType createNewChild(TreeMap<String, String> properties) {
        return new JournalType();
    }

    public void addDefaultType(AccountTypes accountTypes) {
        JournalType defaultType = new JournalType();
        defaultType.setName("default");
        defaultType.setDebetTypes(accountTypes.getBusinessObjects());
        defaultType.setCreditTypes(accountTypes.getBusinessObjects());
        try {
            addBusinessObject(defaultType);
        } catch (EmptyNameException | DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
