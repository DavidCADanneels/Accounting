package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Exceptions.NotEmptyException;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Serialiseerbare map die alle rekeningen bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Accounts extends HashMap<String, Account> {

    /**
     *
     */
    private final Accounting accounting;
    private String folder;
    private File xmlFile;
    private File htmlFile;
    private File xsl2XmlFile;
    private File xsl2HtmlFile;
    private File dtdFile;

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

        File xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), folder);
        File htmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getHtmlFolder(), folder);

        File xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, account.getName() + ".xml");
        File xslFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Account.xsl");
        File htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, account.getName() + ".html");

        account.setXmlFile(xmlFile);
        account.setXslFile(xslFile);
        account.setHtmlFile(htmlFile);
        super.put(account.getName(), account);
        return account;
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

	public ArrayList<Account> getAccountsNotEmpty(ArrayList<AccountType> types) {
		ArrayList<Account> col = new ArrayList<Account>();
		for(AccountType type : types) {
			col.addAll(getAccountsNotEmpty(type));
		}
		return col;
	}

    private ArrayList<Account> getAccountsNotEmpty(AccountType type) {
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
            super.put(oldName, account);
            throw new DuplicateNameException();
        }
        super.put(newName, account);
        account.setName(newName.trim());
        return account;
	}

	public ArrayList<Account> getAllAccounts() {
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

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public File getXmlFile() {
        return xmlFile;
    }

    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }

    public File getHtmlFile() {
        return htmlFile;
    }

    public void setXsl2XmlFile(File xsl2XmlFile) {
        this.xsl2XmlFile = xsl2XmlFile;
    }

    public File getXsl2XmlFile() {
        return xsl2XmlFile;
    }

    public File getXsl2HtmlFile() {
        return xsl2HtmlFile;
    }

    public void setXsl2HtmlFile(File xsl2HtmlFile) {
        this.xsl2HtmlFile = xsl2HtmlFile;
    }

    public void setDtdFile(File dtdFile) {
        this.dtdFile = dtdFile;
    }

    public File getDtdFile() {
        return dtdFile;
    }

    public void setDefaultHtmlFolderAndFiles(File htmlFolder, String name, boolean overwrite){
        if(overwrite || htmlFile == null || htmlFile.getPath().equals("null")){
            htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, name);
        }
        File subFolder = FileSystemView.getFileSystemView().getChild(htmlFolder, folder);
        subFolder.mkdirs();
        for(Account account: getAllAccounts()){
            account.setHtmlFile(FileSystemView.getFileSystemView().getChild(subFolder, account.getName() + ".html"));
        }
    }

    public void setDefaultXmlFolderAndFiles(File xmlFolder, File xslFolder, String name, boolean overwrite) {
        if(overwrite || folder == null || folder.equals("null")){
            folder = name;
        }
        if(overwrite || xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, name + ".xml");
        }
        if(overwrite || xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounts2xml.xsl");
        }
        if(overwrite || xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounts2html.xsl");
        }
        if(overwrite || dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounts.dtd");
        }
        File subFolder = FileSystemView.getFileSystemView().getChild(xmlFolder, folder);
        subFolder.mkdirs();
        for(Account account: getAllAccounts()){
            account.setXmlFile(FileSystemView.getFileSystemView().getChild(subFolder, account.getName() + ".xml"));
            account.setXslFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Account.xsl"));
        }

    }
}