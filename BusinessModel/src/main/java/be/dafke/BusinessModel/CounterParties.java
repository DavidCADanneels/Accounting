package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class CounterParties extends BusinessCollection<BusinessObject> {

    public static final String COUNTERPARTIES = "CounterParties";
    public static final String COUNTERPARTY = "CounterParty";

    @Override
    public String getChildType(){
        return COUNTERPARTY;
    }

    public CounterParties(Accounting accounting){
        addSearchKey(CounterParty.ACCOUNTNUMBER);
        setName(COUNTERPARTIES);
        try {
            accounting.addBusinessObject(this);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        accounting.addKey(COUNTERPARTIES);
    }

    @Override
    public CounterParty createNewChild() {
        return new CounterParty();
    }

    @Override
    final protected CounterParty addBusinessObject(BusinessObject value, Map<String,String> keyMap) throws EmptyNameException, DuplicateNameException {
        CounterParty counterParty = (CounterParty)value;
        if(counterParty.getName()==null || "".equals(counterParty.getName().trim())){
            throw new EmptyNameException();
        }

        // check for duplicate Accounts
        for(Map.Entry<String,String> entry:keyMap.entrySet()){
            String type = entry.getKey();
            String key = entry.getValue();

            TreeMap<String, BusinessObject> map = dataTables.get(type);
            CounterParty foundValue = (CounterParty)map.get(key);

            if(foundValue!=null){
                if(counterParty.isMergeable()){
                    // update Accounts
                    counterParty = merge(foundValue, counterParty);
//                } else {
//                    throw new DuplicateNameException();
                }
            } else {
                map.put(key, counterParty);
            }
        }
        CounterParty foundByName = (CounterParty)getBusinessObject(counterParty.getName());
        if(foundByName!=null){
            if(counterParty.isMergeable()){
                counterParty = merge(foundByName,counterParty);
            } else {
                throw new DuplicateNameException();
            }
        }
        dataTables.get(NAME).put(counterParty.getName(),counterParty);


        return counterParty;
    }

//    @Override
//    public void readCollection() {
//        readCollection("CounterParty", false);
//    }

    private CounterParty merge(CounterParty toKeep, CounterParty toRemove) {
        if(!toKeep.getName().equals(toRemove.getName())){
            toKeep.addAlias(toRemove.getName());
            System.out.println("Alias (" + toRemove.getName() + ") added for " + toKeep.getName());
            toKeep.getAliases().addAll(toRemove.getAliases());
        }
        Map.Entry<String,String> entry = new AbstractMap.SimpleImmutableEntry<String, String>(NAME,toRemove.getName());
        removeBusinessObject(entry);
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
            }
        }
        return toKeep;
    }
}
