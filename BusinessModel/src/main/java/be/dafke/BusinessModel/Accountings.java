package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.ChildrenNeedSeparateFile;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static be.dafke.BusinessModel.Accounting.CREDIT_ACCOUNT;
import static be.dafke.BusinessModel.Accounting.DEBIT_ACCOUNT;

public class Accountings extends BusinessCollection<Accounting> implements ChildrenNeedSeparateFile {

    public static final String ACCOUNTINGS = "Accountings";
    public static final String ACCOUNTING = "Accounting";
    private final File xmlFolder;
    private final File xslFolder;
    private final File htmlFolder;

    @Override
    public String getChildType(){
        return ACCOUNTING;
    }

    public Accountings(File xmlFolder, File xslFolder, File htmlFolder){
        this.xmlFolder = xmlFolder;
        this.xslFolder = xslFolder;
        this.htmlFolder = htmlFolder;
        setName(ACCOUNTINGS);
    }

    public File getXmlFolder(){
        return xmlFolder;
    }

    public File getHtmlFolder(){
        return htmlFolder;
    }

    public File getXslFolder() {
        return xslFolder;
    }

    public Set<String> getInitKeySet(){
        Set<String> keySet = super.getInitKeySet();
        keySet.add(DEBIT_ACCOUNT);
        keySet.add(CREDIT_ACCOUNT);
        keySet.add("VAT1");
        keySet.add("VAT2");
        keySet.add("VAT3");
        keySet.add("VAT81");
        keySet.add("VAT82");
        keySet.add("VAT83");
        keySet.add("VAT54");
        keySet.add("VAT59");
        return keySet;
    }

    @Override
    public Accounting createNewChild(TreeMap<String, String> properties) {
        Accounting accounting = new Accounting();
        accounting.setName(properties.get(NAME));
        VATTransactions vatTransactions = accounting.getVatTransactions();
        HashMap<Integer,BigDecimal> vatTransaction = new HashMap<>();
        for(Map.Entry<String,String> entry: properties.entrySet()){
            String key = entry.getKey();
            if(key.startsWith("VAT")){
                int nr = Integer.parseInt(key.replace("VAT", ""));
                String value = entry.getValue();
                if(value!=null) {
                    BigDecimal amount = Utils.parseBigDecimal(value);
                    vatTransaction.put(nr, amount);
                }
            }
        }
        vatTransactions.book(vatTransaction);
        String debitAccountString = properties.get(DEBIT_ACCOUNT);
        if(debitAccountString!=null) {
            Account debitAccount = accounting.getAccounts().getBusinessObject(debitAccountString);
            vatTransactions.setDebitAccount(debitAccount);
        }
        String creditAccountString = properties.get(CREDIT_ACCOUNT);
        if(creditAccountString!=null) {
            Account creditAccount = accounting.getAccounts().getBusinessObject(creditAccountString);
            vatTransactions.setCreditAccount(creditAccount);
        }
        return accounting;
    }

	public void setCurrentObject(String name) {
		currentObject = dataTables.get(NAME).get(name);
	}

    @Override
    public Accounting addBusinessObject(Accounting value) throws EmptyNameException, DuplicateNameException {
        TreeMap<String, Accounting> map = dataTables.get(NAME);
        if(value.getName()==null || value.getName().trim().equals("")){
            throw new EmptyNameException();
        } else if(map.containsKey(value.getName().trim())){
            throw new DuplicateNameException();
        }
        map.put(value.getName().trim(), value);
        return value;
    }
}