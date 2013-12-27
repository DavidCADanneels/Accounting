package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.BusinessTypeProvider;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.WriteableBusinessCollection;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Serialiseerbare map die alle rekeningen bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Accounts extends WriteableBusinessCollection<Account> implements BusinessTypeProvider<AccountType> {

    private BusinessTypeCollection<AccountType> businessTypeCollection;

    public Accounts(File xmlFolder) {
        super(xmlFolder);
    }

    public ArrayList<Account> getAccounts(AccountType type) {
		ArrayList<Account> col = new ArrayList<Account>();
		for(Account account : getBusinessObjects()) {
			if (account.getType() == type) col.add(account);
		}
		return col;
	}

	public ArrayList<Account> getAccounts(List<AccountType> types) {
		ArrayList<Account> list = new ArrayList<Account>();
		for(AccountType type : types) {
			list.addAll(getAccounts(type));
		}
		return list;
	}

	public ArrayList<Account> getAccountNoMatchProject(Project project) {
		ArrayList<Account> result = new ArrayList<Account>();
		for(Account account : getBusinessObjects()) {
            if (!project.getAccounts().contains(account)){
                result.add(account);
            }
		}
		return result;
	}

	public Account modifyAccountName(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<String,String>(NAME, oldName);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<String,String>(NAME, newName);
//        Name is modified in modify Function
//        account.setName(newName.trim());
        return modify(oldEntry, newEntry);
	}

    @Override
    public void setBusinessTypeCollection(BusinessTypeCollection<AccountType> businessTypeCollection) {
        this.businessTypeCollection = businessTypeCollection;
    }

    @Override
    public BusinessTypeCollection<AccountType> getBusinessTypeCollection() {
        return businessTypeCollection;
    }

    @Override
    public Account createNewChild(String name) {
        return new Account();
    }

    @Override
    public void readCollection() {
        readCollection("Account", false);
    }
}