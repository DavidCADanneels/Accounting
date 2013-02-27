package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Exceptions.NotEmptyException;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Serialiseerbare map die alle rekeningen bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Accounts extends HashMap<String, Account> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final Accounting accounting;
    private File locationXml;
    private File locationHtml;
//    private File xmlFile, htmlFile;

    public Accounts(Accounting accounting) {
		super();
        this.accounting = accounting;
	}

	public Account addAccount(String accountName, AccountType accountType) throws DuplicateNameException, EmptyNameException {
        if(accountName==null || "".equals(accountName.trim())){
            throw new EmptyNameException();
        }
        if(containsKey(accountName.trim())){
            throw new DuplicateNameException();
        }
        Account account = new Account(accountName.trim(), accountType);
        File xmlFile = FileSystemView.getFileSystemView().getChild(locationXml, account.getName() + ".xml");
        File xslFile = FileSystemView.getFileSystemView().getChild(accounting.getLocationXSL(), "Account.xsl");
        File htmlFile = FileSystemView.getFileSystemView().getChild(locationHtml, account.getName() + ".html");
        account.setXmlFile(xmlFile);
        account.setXslFile(xslFile);
        account.setHtmlFile(htmlFile);
        return put(account.getName(), account);
	}

	@Override
	public Account put(String key, Account value) {
		if (key == null) return null;
		if (!containsKey(key)) {
			return super.put(key, value);
		}
		Account result = get(key);
		if (result.equals(value)) {
			return result;
		}
		System.err.println("account already exists with different data");
		System.err.println(value);
		System.err.println(result);
		return super.put(key, value);
		// return value;
	}

	/**
	 * Geeft alle rekeningen terug van het gegeven type
	 * @param type het type van de gevraagde rekeningen
	 * <ul>
	 * <li>0 : Actief</li>
	 * <li>1 : Passief</li>
	 * <li>2 : Kost</li>
	 * <li>3 : Opbrengst</li>
	 * <li>4 : Tegoed van Klant</li>
	 * <li>5 : Schuld aan Leverancier</li>
	 * </ul>
	 * @return alle rekeningen van het gevraagde type
	 */
	public ArrayList<Account> getAccounts(AccountType type) {
		ArrayList<Account> col = new ArrayList<Account>();
		for(Account account : values()) {
			if (account.getType() == type) col.add(account);
		}
		return col;
	}

	public ArrayList<Account> getAccounts(ArrayList<AccountType> types) {
		ArrayList<Account> list = new ArrayList<Account>();
		for(AccountType type : types) {
			list.addAll(getAccounts(type));
		}
		return list;
	}

	public ArrayList<Account> getAccountsNotEmpty(AccountType type) {
		ArrayList<Account> col = new ArrayList<Account>();
		for(Account account : values()) {
			if (account.getType() == type && account.saldo().compareTo(BigDecimal.ZERO) != 0) col.add(account);
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
		for(Account account : values()) {
			if (account.getProject() != project) result.add(account);
		}
		return result;
	}

	public Account modifyAccountName(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        if(newName==null || "".equals(newName.trim())){
            throw new EmptyNameException();
        }
        Account account = get(oldName);
        remove(oldName);
        if(containsKey(newName.trim())){
            put(oldName, account);
            throw new DuplicateNameException();
        }
        put(newName, account);
        account.setName(newName.trim());
        return account;
	}

	public ArrayList<Account> getAccounts() {
		ArrayList<Account> col = new ArrayList<Account>();
		for(Account account : values()) {
			col.add(account);
		}
		return col;
	}

    public void removeAccount(Account account) throws NotEmptyException {
        if(account.getBookings().isEmpty()){
            remove(account.getName());
        } else {
            throw new NotEmptyException();
        }
    }

    public void setLocationXml(File locationXml) {
        this.locationXml = locationXml;
        if(!this.locationXml.exists()){
            this.locationXml.mkdir();
        }
//        xmlFile = FileSystemView.getFileSystemView().getChild(this.locationXml, "Accounts.xml");
    }

    public File getLocationXml(){
        return locationXml;
    }

    public void setLocationHtml(File locationHtml) {
        this.locationHtml = locationHtml;
        if(!this.locationHtml.exists()){
            this.locationHtml.mkdir();
        }
//        htmlFile = FileSystemView.getFileSystemView().getChild(this.locationHtml, "Accounts.html");
    }

    public File getLocationHtml(){
        return locationHtml;
    }
}