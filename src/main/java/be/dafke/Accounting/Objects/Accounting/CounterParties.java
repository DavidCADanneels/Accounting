package be.dafke.Accounting.Objects.Accounting;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class CounterParties {

    private ArrayList<CounterParty> counterParties;
    private HashMap<String, CounterParty> counterPartiesByName, counterPartiesByAccountNumber;
    private File xmlFile;
    private File xsl2XmlFile;
    private File xsl2HtmlFile;
    private File dtdFile;
    private File htmlFile;

    public CounterParties(){
        counterParties = new ArrayList<CounterParty>();
        counterPartiesByName = new HashMap<String, CounterParty>();
        counterPartiesByAccountNumber = new HashMap<String, CounterParty>();
    }

	public ArrayList<CounterParty> getCounterParties() {
        return counterParties;
	}

    public CounterParty getCounterPartyByName(String name){
        return counterPartiesByName.get(name);
    }

    public CounterParty getCounterPartyByAccountNumber(String accountNumber){
        return counterPartiesByAccountNumber.get(accountNumber);
    }

    public CounterParty addCounterParty(String name, BankAccount bankAccount){
        if(name == null){
            throw new RuntimeException("Counterparty should have a least a name");
        }
        if(bankAccount == null){
            // no BankAccount provided: add new Counterparty without BankAccount (only name)
            CounterParty counterParty = new CounterParty(name);
            if(!counterPartiesByName.containsKey(name)){
                counterParties.add(counterParty);
                counterPartiesByName.put(name, counterParty);
            }
            return counterParty;
        }
        String accountNumber = bankAccount.getAccountNumber();
        CounterParty counterPartyByName = counterPartiesByName.get(name);
        CounterParty counterPartyByAccountNumber = counterPartiesByAccountNumber.get(accountNumber);
        if(counterPartyByName == null && counterPartyByAccountNumber == null){
            // Neither the Name nor the BankAccount are found: add new Counterparty with BankAccount in both Maps
            CounterParty counterParty = new CounterParty(name);
            counterParty.addAccount(bankAccount);
            counterParties.add(counterParty);
            counterPartiesByName.put(name, counterParty);
            counterPartiesByAccountNumber.put(accountNumber,counterParty);
            return counterParty;
        } else if(counterPartyByName != null && counterPartyByAccountNumber == null){
            // Counterparty was found in the named list: add the BankAccount and add this counterparty in the second Map
            counterPartyByName.addAccount(bankAccount);
            counterPartiesByAccountNumber.put(accountNumber,counterPartyByName);
            return counterPartyByName;
        } else if(counterPartyByName == null){
            // Counterparty was found in the account list: do nothing, this case should be impossible
            counterPartyByAccountNumber.addAlias(name);
            counterPartiesByName.put(name,counterPartyByAccountNumber);
            return counterPartyByAccountNumber;
        } else{
            // Finally: a counterparty was found in both lists: trust the one from the named list and
            // check whether the existing BankAccount should be updated or a new one should be added.

            if(counterPartyByAccountNumber != counterPartyByName){
                System.err.println("Both must refer to the same object --> Merge");
                merge(counterPartyByAccountNumber,counterPartyByName);
            }

            return counterPartyByAccountNumber;
        }
    }

    private void merge(CounterParty counterPartyByAccountNumber, CounterParty counterPartyByName){
        if(!counterPartyByAccountNumber.getName().equals(counterPartyByName.getName())){
            counterPartyByAccountNumber.addAlias(counterPartyByName.getName());
            counterPartyByAccountNumber.getAliases().addAll(counterPartyByName.getAliases());
        }
        for(BankAccount bankAccount : counterPartyByName.getBankAccounts().values()){
            if(!counterPartyByAccountNumber.getBankAccounts().containsKey(bankAccount.getAccountNumber())){
                counterPartyByAccountNumber.getBankAccounts().put(bankAccount.getAccountNumber(),bankAccount);
            } else{
                BankAccount oldBankAccount = counterPartyByAccountNumber.getBankAccounts().get(bankAccount.getAccountNumber());
                if(oldBankAccount.getBic() == null || oldBankAccount.getBic().trim().equals("")){
                    oldBankAccount.setBic(bankAccount.getBic());
                }
                if(oldBankAccount.getCurrency() == null || oldBankAccount.getCurrency().trim().equals("")){
                    oldBankAccount.setCurrency(bankAccount.getCurrency());
                }
            }
        }
        counterPartiesByName.remove(counterPartyByName.getName());
        counterParties.remove(counterPartyByName);
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("CounterParties:\r\n");
        for(CounterParty counterParty : counterParties){
			builder.append(counterParty);
		}
		return builder.toString();
	}

    public void addCounterParty(CounterParty counterParty) {
        counterParties.add(counterParty);
        counterPartiesByName.put(counterParty.getName(), counterParty);
        for(BankAccount bankAccount : counterParty.getBankAccounts().values()){
            counterPartiesByAccountNumber.put(bankAccount.getAccountNumber(),counterParty);
        }
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public File getXmlFile() {
        return xmlFile;
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

    public File getDtdFile() {
        return dtdFile;
    }

    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }

    public File getHtmlFile() {
        return htmlFile;
    }

    public void setDefaultHtmlFolderAndFiles(Accounting accounting, String name, boolean overwrite){
        File htmlFolder = accounting.getHtmlFolder();
        if(overwrite || htmlFile == null || htmlFile.getPath().equals("null")){
            htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, name + ".html");
        }
    }

    public void setDefaultXmlFolderAndFiles(Accounting accounting, String name, boolean overwrite) {
        File xmlFolder = accounting.getXmlFolder();
        File xslFolder = accounting.getXslFolder();
        File dtdFolder = accounting.getDtdFolder();
        if(overwrite || xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, name + ".xml");
        }
        if(overwrite || xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "CounterParties2xml.xsl");
        }
        if(overwrite || xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "CounterParties2html.xsl");
        }
        if(overwrite || dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(dtdFolder, "CounterParties.dtd");
        }
    }
}
