package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.BusinessObject
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

class CounterParties extends BusinessCollection<BusinessObject> {

    static final String COUNTERPARTIES = "CounterParties"
    static final String COUNTERPARTY = "CounterParty"
    static final String ACCOUNTNUMBER = "AccountNumber"
    static final String BIC = "Bic"
    static final String CURRENCY = "Currency"
    static final String ALIAS = "Alias"
    static final String ADDRESS = "Address"

    CounterParties(){
        addSearchKey(ACCOUNTNUMBER)
        setName(COUNTERPARTIES)
    }

    Set<String> getInitKeySet() {
        Set<String> keySet = new TreeSet<String>()
        keySet.add(NAME)
        keySet.add(ACCOUNTNUMBER)
        keySet.add(ADDRESS)
        keySet.add(ALIAS)
        keySet.add(BIC)
        keySet.add(CURRENCY)
        keySet
    }

    CounterParty createNewChild(TreeMap<String, String> properties) {
        CounterParty counterParty = new CounterParty()
        counterParty.setName(properties.get(NAME))
        String aliasesString = properties.get(CounterParties.ALIAS)
        if(aliasesString){
            counterParty.setAliases(Utils.parseStringList(aliasesString))
        }
        String accountNumberString = properties.get(CounterParties.ACCOUNTNUMBER)
        if(accountNumberString){
            ArrayList<String> numberList = Utils.parseStringList(accountNumberString)
            for(String s: numberList){
                counterParty.addAccount(new BankAccount(s))
            }
        }
        String addressLinesString = properties.get(CounterParties.ADDRESS)
        if(addressLinesString){
            counterParty.setAddressLines(Utils.parseStringList(addressLinesString))
        }
        String bicString = properties.get(CounterParties.BIC)
        if(bicString){
            parseBicsString(counterParty, bicString)
        }
        String currenciesString = properties.get(CounterParties.CURRENCY)
        if(currenciesString){
            parseCurrenciesString(counterParty, currenciesString)
        }
        counterParty
    }

    void parseBicsString(CounterParty counterParty, String bicsString){
        if(bicsString){
            String[] bicsStrings = bicsString.split("\\Q | \\E")
            for(int i=0;i<bicsStrings.length;i++){
                counterParty.getAccountsList().get(i).setBic(bicsStrings[i])
            }
        }
    }

    void parseCurrenciesString(CounterParty counterParty, String currenciesString){
        if(currenciesString){
            String[] currenciesStrings = currenciesString.split("\\Q | \\E")
            for(int i=0;i<currenciesStrings.length;i++){
                counterParty.getAccountsList().get(i).setCurrency(currenciesStrings[i])
            }
        }
    }

    @Override
    final protected CounterParty addBusinessObject(BusinessObject value, Map<String,String> keyMap) throws EmptyNameException, DuplicateNameException {
        CounterParty counterParty = (CounterParty)value
        if(counterParty.name==null || "".equals(counterParty.name.trim())){
            throw new EmptyNameException()
        }

        // check for duplicate Accounts
        for(Map.Entry<String,String> entry:keyMap.entrySet()){
            String type = entry.getKey()
            String key = entry.getValue()

            TreeMap<String, BusinessObject> map = dataTables.get(type)
            CounterParty foundValue = (CounterParty)map.get(key)

            if(foundValue){
                if(counterParty.mergeable){
                    // update Accounts
                    counterParty = merge(foundValue, counterParty)
//                } else {
//                    throw new DuplicateNameException()
                }
            } else {
                map.put(key, counterParty)
            }
        }
        CounterParty foundByName = (CounterParty)getBusinessObject(counterParty.name)
        if(foundByName){
            if(counterParty.mergeable){
                counterParty = merge(foundByName,counterParty)
            } else {
                throw new DuplicateNameException()
            }
        }
        dataTables.get(NAME).put(counterParty.name,counterParty)


        counterParty
    }

//    @Override
//    void readCollection() {
//        readCollection("CounterParty", false)
//    }

    CounterParty merge(CounterParty toKeep, CounterParty toRemove) {
        if(!toKeep.name.equals(toRemove.name)){
            toKeep.addAlias(toRemove.name)
            System.out.println("Alias (" + toRemove.name + ") added for " + toKeep.name)
            toKeep.getAliases().addAll(toRemove.getAliases())
        }
        Map.Entry<String,String> entry = new AbstractMap.SimpleImmutableEntry<String, String>(NAME,toRemove.name)
        removeBusinessObject(entry)
        for(BankAccount bankAccountToRemove : toRemove.getBankAccounts().values()){
            BankAccount bankAccountToKeep = toKeep.getBankAccounts().get(bankAccountToRemove.getAccountNumber())
            if(bankAccountToKeep == null){
                toKeep.addAccount(bankAccountToRemove)
                System.out.println("BankAccount (" + bankAccountToRemove.getAccountNumber() + ") added for " + toKeep.name)
            } else{
                if(bankAccountToKeep.getBic() == null || bankAccountToKeep.getBic().trim().equals("")){
                    if(bankAccountToRemove.getBic() != null && !bankAccountToRemove.getBic().equals("") ){
                        bankAccountToKeep.setBic(bankAccountToRemove.getBic())
                        System.out.println("BIC (" + bankAccountToRemove.getBic() + ") updated for " + toKeep.name)
                    }
                }
                if(bankAccountToKeep.getCurrency() == null || bankAccountToKeep.getCurrency().trim().equals("")){
                    if(bankAccountToRemove.getCurrency() != null && !bankAccountToRemove.getCurrency().equals("") ){
                        bankAccountToKeep.setCurrency(bankAccountToRemove.getCurrency())
                        System.out.println("Currency (" + bankAccountToRemove.getCurrency() + ") updated for " + toKeep.name)
                    }
                }
            }
        }
        toKeep
    }
}
