package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.WriteableBusinessObject;

import java.math.BigDecimal;
import java.util.Calendar;

public class Statement extends WriteableBusinessObject {
    private String statementNr;
    private String sequenceNumber;
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


    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public void setStatementNr(String statementNr) {
        this.statementNr = statementNr;
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
	public String getStatementNr() {
		return statementNr;
	}

	public String getSequenceNr() {
		return sequenceNumber;
	}

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
}