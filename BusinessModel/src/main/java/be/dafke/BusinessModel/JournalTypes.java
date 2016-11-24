package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.TreeMap;

public class JournalTypes extends BusinessCollection<JournalType> {

    @Override
    public String getChildType(){
        return "JournalType";
    }

    @Override
    public JournalType createNewChild(TreeMap<String, String> properties) {
        String name = properties.get(NAME)==null?"":properties.get(NAME);
        return new JournalType(name);
    }

    public void addDefaultType(AccountTypes accountTypes) {
        JournalType defaultType = new JournalType("default");
        defaultType.setDebetTypes(accountTypes.getBusinessObjects());
        defaultType.setCreditTypes(accountTypes.getBusinessObjects());
        try {
            addBusinessObject(defaultType);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
