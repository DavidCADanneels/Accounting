package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

class JournalTypes extends BusinessCollection<JournalType> {

    JournalTypes(){
        super()
    }

    JournalTypes(JournalTypes journalTypes){
        for(JournalType journalType:journalTypes.getBusinessObjects()){
            try {
                // TODO: review copy of accounting (copyConstructors ...?)
                addBusinessObject(new JournalType(journalType))
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    void addDefaultType(AccountTypes accountTypes) {
        JournalType defaultType = new JournalType("default")
        defaultType.addAllAccountTypes(accountTypes)
        try {
            addBusinessObject(defaultType)
        } catch (EmptyNameException | DuplicateNameException e) {
            e.printStackTrace()  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
