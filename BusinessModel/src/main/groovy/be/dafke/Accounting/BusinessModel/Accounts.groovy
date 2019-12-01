package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

import java.util.function.Predicate

class Accounts extends BusinessCollection<Account> {
    static final String ACCOUNT = "Account"

    Accounts() {
        super()
    }

    Accounts(Accounts accounts) {
        for(Account account:accounts.businessObjects){
            try {
                addBusinessObject(new Account(account))
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    BigDecimal getSumOfAccountsByNumber(String prefix){
        BigDecimal result = BigDecimal.ZERO
        for (Account account : getAccountsByNumber(prefix)){
            BigDecimal saldo = account.saldo
            if(account.type.isInverted()){
                result = result.subtract(saldo)
            } else {
                result = result.add(saldo)
            }
        }
        result.setScale(2)
        result
    }

    ArrayList<Account> getAccountsByName(String prefix){
        getBusinessObjects(Account.namePrefix(prefix))
    }

    ArrayList<Account> getAccountsByNumber(String prefix){
        getBusinessObjects(Account.numberPrefix(prefix))
    }

    ArrayList<Account> getAccountsByType(AccountType type) {
        getBusinessObjects(Account.ofType(type))
    }

    List<Account> getBusinessObjects(Predicate<Account> filter){
        getBusinessObjects().stream()
                .filter(filter)
//                .sorted(Comparator.comparing(BusinessObject.name))
                .collect().toList()
    }

    List<Account> getAccountsByType(List<AccountType> types) {
        if(types==null) getBusinessObjects()
        ArrayList<Account> list = new ArrayList()
        for(AccountType type : types) {
            list.addAll(getAccountsByType(type))
        }
        list.stream()
//                .sorted(Comparator.comparing(BusinessObject.name))
                .collect().toList()
    }

    Accounts getSubAccounts(Predicate<Movement> predicate){
        ArrayList<Account> accounts = getBusinessObjects()
        Accounts newAccounts = new Accounts()
        for(Account account: accounts){
            Account newAccount = account.getSubAccount(predicate)
            try {
                newAccounts.addBusinessObject(newAccount)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
        newAccounts
    }

}