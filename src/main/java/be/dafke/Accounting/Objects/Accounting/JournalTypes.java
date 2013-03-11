package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.BusinessTypeCollection;

public class JournalTypes extends BusinessTypeCollection<JournalType> {

    public void addDefaultType(AccountTypes accountTypes) {
        JournalType defaultType = new JournalType();
        defaultType.setName("default");
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
