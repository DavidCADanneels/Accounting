package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;

import java.util.ArrayList;
import java.util.List;

public class CounterParties extends BusinessCollection<CounterParty>{

    private static final String ACCOUNTNUMBER = "accountNumber";

    public CounterParties(){
        addKey(ACCOUNTNUMBER);
    }

    @Override
    public CounterParty addBusinessObject(CounterParty value) throws EmptyNameException, DuplicateNameException {
        List<String> types = new ArrayList<String>();
        List<String> keys = new ArrayList<String>();
        types.add(NAME);
        keys.add(value.getName());

        for(BankAccount bankAccount : value.getBankAccounts().values()){
            types.add(ACCOUNTNUMBER);
            keys.add(bankAccount.getAccountNumber());
        }
        addBusinessObject(value, types, keys);
        return value;
    }

    @Override
    protected CounterParty merge(CounterParty toKeep, CounterParty toRemove) throws EmptyNameException, DuplicateNameException {
        if(!toKeep.getName().equals(toRemove.getName())){
            toKeep.addAlias(toRemove.getName());
            System.out.println("Alias (" + toRemove.getName() + ") added for " + toKeep.getName());
            toKeep.getAliases().addAll(toRemove.getAliases());
        }
        removeBusinessObject(NAME, toRemove.getName());
        for(BankAccount bankAccountToRemove : toRemove.getBankAccounts().values()){
            BankAccount bankAccountToKeep = toKeep.getBankAccounts().get(bankAccountToRemove.getAccountNumber());
            if(bankAccountToKeep == null){
                toKeep.addAccount(bankAccountToRemove);
                System.out.println("BankAccount (" + bankAccountToRemove.getAccountNumber() + ") added for " + toKeep.getName());
            } else{
                if(bankAccountToKeep.getBic() == null || bankAccountToKeep.getBic().trim().equals("")){
                    if(bankAccountToRemove.getBic() != null && !bankAccountToRemove.getBic().equals("") ){
                        bankAccountToKeep.setBic(bankAccountToRemove.getBic());
                        System.out.println("BIC (" + bankAccountToRemove.getBic() + ") updated for " + toKeep.getName());
                    }
                }
                if(bankAccountToKeep.getCurrency() == null || bankAccountToKeep.getCurrency().trim().equals("")){
                    if(bankAccountToRemove.getCurrency() != null && !bankAccountToRemove.getCurrency().equals("") ){
                        bankAccountToKeep.setCurrency(bankAccountToRemove.getCurrency());
                        System.out.println("Currency (" + bankAccountToRemove.getCurrency() + ") updated for " + toKeep.getName());
                    }
                }
                removeBusinessObject(ACCOUNTNUMBER, bankAccountToRemove.getAccountNumber());
            }
        }
        return toKeep;
    }
}
