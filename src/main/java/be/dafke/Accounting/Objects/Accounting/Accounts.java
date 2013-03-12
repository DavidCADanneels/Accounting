package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.BusinessTypeCollection;
import be.dafke.Accounting.Objects.BusinessTypeProvider;
import be.dafke.Accounting.Objects.WriteableBusinessCollection;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Serialiseerbare map die alle rekeningen bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Accounts extends WriteableBusinessCollection<Account> implements BusinessTypeProvider<AccountType> {

//    private static final String CURRENT = "CurrentAccount";
//    private Account currentAccount;
    private BusinessTypeCollection<AccountType> businessTypeCollection;

    /**
	 * Geeft alle rekeningen terug van het gegeven businessObjectType
	 * @param type het businessObjectType van de gevraagde rekeningen
	 * <ul>
	 * <li>0 : Actief</li>
	 * <li>1 : Passief</li>
	 * <li>2 : Kost</li>
	 * <li>3 : Opbrengst</li>
	 * <li>4 : Tegoed van Klant</li>
	 * <li>5 : Schuld aan Leverancier</li>
	 * </ul>
	 * @return alle rekeningen van het gevraagde businessObjectType
	 */
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

	public ArrayList<Account> getAccountsNotEmpty(ArrayList<AccountType> types) {
		ArrayList<Account> col = new ArrayList<Account>();
		for(AccountType type : types) {
			col.addAll(getAccountsNotEmpty(type));
		}
		return col;
	}

    private ArrayList<Account> getAccountsNotEmpty(AccountType type) {
        ArrayList<Account> col = new ArrayList<Account>();
        for(Account account : getBusinessObjects()) {
            if (account.getType() == type && account.getSaldo().compareTo(BigDecimal.ZERO) != 0) col.add(account);
        }
        return col;
    }

	/**
	 * Geeft alle rekeningen terug die niet tot het gegeven project behoren
	 * @param project het project waarvan we de rekeningen willen uitsluiten
	 * @return alle rekeningen die niet tot het gegeven project behoren
	 */
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
        Map.Entry<String,String> oldEntry = new AbstractMap.SimpleImmutableEntry<String,String>(Account.NAME, oldName);
        Map.Entry<String,String> newEntry = new AbstractMap.SimpleImmutableEntry<String,String>(Account.NAME, newName);
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