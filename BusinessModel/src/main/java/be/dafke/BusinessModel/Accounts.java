package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.ChildrenNeedSeparateFile;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serialiseerbare map die alle rekeningen bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Accounts extends BusinessCollection<Account> implements ChildrenNeedSeparateFile {
    public static final String ACCOUNT = "Account";
    private AccountTypes accountTypes;

    public Accounts(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
        setName("Accounts");
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

    public ArrayList<Account> getAccountsByNumber(String prefix){
        return getBusinessObjects().stream()
                .filter(account -> account.getNumber()!=null && account.getNumber().toString().startsWith(prefix))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Account> getAccounts(AccountType type) {
		return getBusinessObjects().stream()
                .filter(account -> account.getType() == type)
                .collect(Collectors.toCollection(ArrayList::new));
	}

	public ArrayList<Account> getAccounts(List<AccountType> types) {
		ArrayList<Account> list = new ArrayList<>();
		for(AccountType type : types) {
			list.addAll(getAccounts(type));
		}
		return list;
	}

	public Account modifyAccountName(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<>(NAME, oldName);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<>(NAME, newName);
//        Name is modified in modify Function
//        account.setName(newName.trim());
        return modify(oldEntry, newEntry);
	}
}