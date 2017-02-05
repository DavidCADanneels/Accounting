package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

import java.util.*;

public class CounterParties extends BusinessCollection<BusinessObject> {

    public static final String COUNTERPARTIES = "CounterParties";
    public static final String COUNTERPARTY = "CounterParty";
    public static final String ACCOUNTNUMBER = "AccountNumber";
    public static final String BIC = "Bic";
    public static final String CURRENCY = "Currency";
    public static final String ALIAS = "Alias";
    public static final String ADDRESS = "Address";

    public CounterParties(){
        addSearchKey(ACCOUNTNUMBER);
        setName(COUNTERPARTIES);
    }

    public Set<String> getInitKeySet() {
        Set<String> keySet = new TreeSet<String>();
        keySet.add(NAME);
        keySet.add(ACCOUNTNUMBER);
        keySet.add(ADDRESS);
        keySet.add(ALIAS);
        keySet.add(BIC);
        keySet.add(CURRENCY);
        return keySet;
    }

    public CounterParty createNewChild(TreeMap<String, String> properties) {
        CounterParty counterParty = new CounterParty();
        counterParty.setName(properties.get(NAME));
        String aliasesString = properties.get(CounterParties.ALIAS);
        if(aliasesString!=null){
            counterParty.setAliases(Utils.parseStringList(aliasesString));
        }
        String accountNumberString = properties.get(CounterParties.ACCOUNTNUMBER);
        if(accountNumberString!=null){
            ArrayList<String> numberList = Utils.parseStringList(accountNumberString);
            for(String s: numberList){
                counterParty.addAccount(new BankAccount(s));
            }
        }
        String addressLinesString = properties.get(CounterParties.ADDRESS);
        if(addressLinesString!=null){
            counterParty.setAddressLines(Utils.parseStringList(addressLinesString));
        }
        String bicString = properties.get(CounterParties.BIC);
        if(bicString!=null){
            parseBicsString(counterParty, bicString);
        }
        String currenciesString = properties.get(CounterParties.CURRENCY);
        if(currenciesString!=null){
            parseCurrenciesString(counterParty, currenciesString);
        }
        return counterParty;
    }

    private void parseBicsString(CounterParty counterParty, String bicsString){
        if(bicsString!=null){
            String[] bicsStrings = bicsString.split("\\Q | \\E");
            for(int i=0;i<bicsStrings.length;i++){
                counterParty.getAccountsList().get(i).setBic(bicsStrings[i]);
            }
        }
    }

    private void parseCurrenciesString(CounterParty counterParty, String currenciesString){
        if(currenciesString!=null){
            String[] currenciesStrings = currenciesString.split("\\Q | \\E");
            for(int i=0;i<currenciesStrings.length;i++){
                counterParty.getAccountsList().get(i).setCurrency(currenciesStrings[i]);
            }
        }
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
