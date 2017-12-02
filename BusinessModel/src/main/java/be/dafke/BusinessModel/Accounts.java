package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Serialiseerbare map die alle rekeningen bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Accounts extends BusinessCollection<Account> {
    public static final String ACCOUNT = "Account";

    public Accounts() {
        super();
    }

    public Accounts(Accounts accounts) {
        for(Account account:accounts.getBusinessObjects()){
            try {
                addBusinessObject(new Account(account));
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public BigDecimal getSumOfAccountsByNumber(String prefix){
        BigDecimal result = BigDecimal.ZERO;
        for (Account account : getAccountsByNumber(prefix)){
            BigDecimal saldo = account.getSaldo();
            if(account.getType().isInverted()){
                result = result.subtract(saldo);
            } else {
                result = result.add(saldo);
            }
        }
        result.setScale(2);
        return result;
    }

    public ArrayList<Account> getAccountsByName(String prefix){
        return getBusinessObjects(Account.namePrefix(prefix));
    }

    public ArrayList<Account> getAccountsByNumber(String prefix){
        return getBusinessObjects(Account.numberPrefix(prefix));
    }

    public ArrayList<Account> getAccountsByType(AccountType type) {
        return getBusinessObjects(Account.ofType(type));
	}

    public ArrayList<Account> getBusinessObjects(Predicate<Account> filter){
        return getBusinessObjects().stream()
                .filter(filter)
                .sorted(Comparator.comparing(BusinessObject::getName))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Account> getAccountsByType(List<AccountType> types) {
        if(types==null) return getBusinessObjects();
		ArrayList<Account> list = new ArrayList<>();
		for(AccountType type : types) {
			list.addAll(getAccountsByType(type));
		}
		list = list.stream().sorted(Comparator.comparing(BusinessObject::getName)).collect(Collectors.toCollection(ArrayList::new));
		return list;
	}
}