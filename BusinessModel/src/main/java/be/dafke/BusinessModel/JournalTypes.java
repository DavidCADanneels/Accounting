package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

public class JournalTypes extends BusinessCollection<JournalType> {
    private Accounting accounting;

    public JournalTypes(Accounting accounting){
        super();
        this.accounting = accounting;
    }

    public JournalTypes(JournalTypes journalTypes){
        this(journalTypes.accounting);
        for(JournalType journalType:journalTypes.getBusinessObjects()){
            try {
                addBusinessObject(new JournalType(journalType, accounting.getAccountTypes()));
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public Accounting getAccounting() {
        return accounting;
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }

    public void addDefaultType(AccountTypes accountTypes) {
        JournalType defaultType = new JournalType("default", accountTypes);
        try {
            addBusinessObject(defaultType);
        } catch (EmptyNameException | DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
