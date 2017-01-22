package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Properties;

public class Statement extends BusinessObject {
    public static final String DATE = "Date";
    public static final String SIGN = "Sign";
    public static final String AMOUNT = "Amount";
    public static final String COUNTERPARTY = "CounterParty";
    public static final String TRANSACTIONCODE = "TransactionCode";
    public static final String COMMUNICATION = "Communication";
    private String transactionCode;
    private String communication;
	private boolean debit;
    private boolean structured;
	private BigDecimal amount;
	private Calendar date;

	private CounterParty counterParty;
	private TmpCounterParty tmpCounterParty;

    @Override
	public String toString() {
		return getName();
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

    //
    public Properties getOutputProperties(){
        Properties properties = new Properties();
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

}