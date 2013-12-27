package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

public class JournalTypes extends BusinessTypeCollection<JournalType> {

    @Override
    public String getChildType(){
        return "JournalType";
    }

    @Override
    public JournalType createNewChild(String name) {
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
