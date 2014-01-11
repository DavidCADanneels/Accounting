package be.dafke.Coda.Objects;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessCollectionDependent;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Statement extends BusinessObject implements BusinessCollectionDependent<CounterParty>{
    private static final String DATE = "Date";
    private static final String SIGN = "Sign";
    private static final String AMOUNT = "Amount";
    private static final String COUNTERPARTY = "CounterParty";
    private static final String TRANSACTIONCODE = "TransactionCode";
    private static final String COMMUNICATION = "Communication";
    private String transactionCode;
    private String communication;
	private boolean debit;
    private boolean structured;
	private BigDecimal amount;
	private Calendar date;


	private CounterParty counterParty;
	private TmpCounterParty tmpCounterParty;
    private BusinessCollection<CounterParty> businessCollection;

    @Override
	public String toString() {
		return getName();
	}

    @Override
    public boolean separateFile(){
        return false;
    }

    // SETTERS


    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public void setDebit(boolean debit) {
        this.debit = debit;
    }

    public void setStructured(boolean structured) {
        this.structured = structured;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setCounterParty(CounterParty counterParty) {
        this.counterParty = counterParty;
    }

    public void setTmpCounterParty(TmpCounterParty counterParty) {
        this.tmpCounterParty = counterParty;
    }

    // GETTERS
	public BigDecimal getAmount() {
		return amount;
	}

	public boolean isDebit() {
		return debit;
	}

	public Calendar getDate() {
		return date;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public String getCommunication() {
        return communication;
	}

    public CounterParty getCounterParty() {
        return counterParty;
    }

    public TmpCounterParty getTmpCounterParty() {
		return tmpCounterParty;
	}

    public boolean isStructured() {
        return structured;
    }

    // KeySet and Properties
    //
    // Keys found in the CollectionFile e.g. Account.NAME in Accounts.xml file
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(NAME);
        keySet.add(DATE);
        keySet.add(SIGN);
        keySet.add(AMOUNT);
        keySet.add(COUNTERPARTY);
        keySet.add(TRANSACTIONCODE);
        keySet.add(COMMUNICATION);
        return keySet;
    }
    //
    public void setInitProperties(TreeMap<String, String> properties){
        setName(properties.get(NAME));
        setDate(Utils.toCalendar(properties.get(DATE)));
        setAmount(Utils.parseBigDecimal(properties.get(AMOUNT)));
        setCommunication(properties.get(COMMUNICATION));
        setTransactionCode(properties.get(TRANSACTIONCODE));
        String sign = properties.get(SIGN);
        setDebit("D".equals(sign));
        String counterPartyString = properties.get(COUNTERPARTY);
        if(counterPartyString!=null && !counterPartyString.equals("")){
            setCounterParty(businessCollection.getBusinessObject(counterPartyString));
        }
    }
    //
    @Override
    public TreeMap<String, String> getInitProperties(BusinessCollection collection){
        TreeMap<String,String> properties = new TreeMap<String, String>();
        properties.put(NAME,getName());
        properties.put(DATE,Utils.toString(date));
        properties.put(AMOUNT, amount.toString());
        properties.put(COMMUNICATION, communication);
        if(counterParty!=null){
            properties.put(COUNTERPARTY,counterParty.getName());
        }
        properties.put(TRANSACTIONCODE,transactionCode);
        properties.put(SIGN,isDebit()?"D":"C");
        return properties;
    }

    @Override
    public void setBusinessCollection(BusinessCollection<CounterParty> businessCollection) {
        this.businessCollection = businessCollection;
    }
}