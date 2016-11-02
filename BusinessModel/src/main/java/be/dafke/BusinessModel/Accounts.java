package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.ChildrenNeedSeparateFile;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Serialiseerbare map die alle rekeningen bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Accounts extends BusinessCollection<Account> implements ChildrenNeedSeparateFile {
    public static final String ACCOUNT = "Account";
    private Accounting accounting;

    public Accounts(Accounting accounting) {
        this.accounting = accounting;
        setName("Accounts");
    }

    @Override
    public String getChildType(){
        return ACCOUNT;
    }

    public ArrayList<Account> getAccounts(AccountType type) {
		ArrayList<Account> col = getBusinessObjects().stream()
                .filter(account -> account.getType() == type)
                .collect(Collectors.toCollection(ArrayList::new));
        return col;
	}

	public ArrayList<Account> getAccounts(List<AccountType> types) {
		ArrayList<Account> list = new ArrayList<Account>();
		for(AccountType type : types) {
			list.addAll(getAccounts(type));
		}
		return list;
	}

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(NAME);
        keySet.add(Account.TYPE);
        keySet.add(Account.DEFAULTAMOUNT);
        return keySet;
    }

	public Account modifyAccountName(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<String,String>(NAME, oldName);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<String,String>(NAME, newName);
//        Name is modified in modify Function
//        account.setName(newName.trim());
        return modify(oldEntry, newEntry);
	}

    @Override
    public Account createNewChild(TreeMap<String, String> properties) {
        String name = properties.get(NAME);
        Account account = new Account(name);
        String typeName = properties.get(Account.TYPE);
        if(typeName!=null){
            account.setType(accounting.getAccountTypes().getBusinessObject(typeName));
        }
        String defaultAmountString = properties.get(Account.DEFAULTAMOUNT);
        if(defaultAmountString!=null){
            try{
                account.setDefaultAmount(new BigDecimal(defaultAmountString));
            } catch (NumberFormatException nfe){
                account.setDefaultAmount(null);
            }
        }
        return account;
    }
}