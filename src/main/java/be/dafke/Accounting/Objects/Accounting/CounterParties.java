package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.WriteableBusinessCollection;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CounterParties extends WriteableBusinessCollection<CounterParty> {

    public CounterParties(){
        addSearchKey(CounterParty.ACCOUNTNUMBER);
    }

    @Override
    final protected CounterParty addBusinessObject(CounterParty value, Map<String,String> keyMap) throws EmptyNameException, DuplicateNameException {
        if(value.getName()==null || "".equals(value.getName().trim())){
            throw new EmptyNameException();
        }

        // check for duplicate Accounts
        for(Map.Entry<String,String> entry:keyMap.entrySet()){
            String type = entry.getKey();
            String key = entry.getValue();

            TreeMap<String, CounterParty> map = dataTables.get(type);
            CounterParty foundValue = map.get(key);

            if(foundValue!=null){
                if(value.isMergeable()){
                    // update Accounts
                    value = merge(foundValue, value);
//                } else {
//                    throw new DuplicateNameException();
                }
            } else {
                map.put(key, value);
            }
        }
        CounterParty foundByName = getBusinessObject(value.getName());
        if(foundByName!=null){
            if(value.isMergeable()){
                value = merge(foundByName,value);
            } else {
                throw new DuplicateNameException();
            }
        }
        dataTables.get(NAME).put(value.getName(),value);


        return value;
    }

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

    // KeySets and Properties

    @Override
    public Set<String> getInitKeySet() {
        Set<String> keySet = super.getInitKeySet();
        return keySet;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String, String> properties = super.getUniqueProperties();
        return properties;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
    }

    @Override
    public TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> properties = super.getUniqueProperties();
        return properties;
    }

    @Override
    public Set<String> getCollectionKeySet(){
        Set<String> collectionKeySet = super.getCollectionKeySet();
        return collectionKeySet;
    }

    @Override
    public TreeMap<String,String> getProperties() {
        TreeMap<String, String> outputMap = super.getProperties();
        return outputMap;
    }

    @Override
    public void setProperties(TreeMap<String, String> properties){
        super.setProperties(properties);
    }
}
