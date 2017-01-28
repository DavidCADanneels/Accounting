package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

public class JournalTypes extends BusinessCollection<JournalType> {

    public JournalTypes(){
        super();
    }

    public JournalTypes(JournalTypes journalTypes){
        for(JournalType journalType:journalTypes.getBusinessObjects()){
            try {
                addBusinessObject(new JournalType(journalType));
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public void addDefaultType(AccountTypes accountTypes) {
        JournalType defaultType = new JournalType("default");
        defaultType.setDebetTypes(accountTypes);
        defaultType.setCreditTypes(accountTypes);
        try {
            addBusinessObject(defaultType);
        } catch (EmptyNameException | DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
