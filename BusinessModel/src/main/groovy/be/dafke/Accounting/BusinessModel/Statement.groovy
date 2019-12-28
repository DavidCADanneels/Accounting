package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject
import be.dafke.Utils.Utils

class Statement extends BusinessObject {
    static final String DATE = "Date"
    static final String SIGN = "Sign"
    static final String AMOUNT = "Amount"
    static final String COUNTERPARTY = "CounterParty"
    static final String TRANSACTIONCODE = "TransactionCode"
    static final String COMMUNICATION = "Communication"
    String transactionCode
    String communication
    boolean debit
    boolean structured
    BigDecimal amount
    Calendar date

    CounterParty counterParty
    TmpCounterParty tmpCounterParty

    @Override
    String toString() {
        getName()
    }

    // SETTERS
    void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode
    }

    void setCommunication(String communication) {
        this.communication = communication
    }

    void setDebit(boolean debit) {
        this.debit = debit
    }

    void setStructured(boolean structured) {
        this.structured = structured
    }

    void setAmount(BigDecimal amount) {
        this.amount = amount
    }

    void setDate(Calendar date) {
        this.date = date
    }

    void setCounterParty(CounterParty counterParty) {
        this.counterParty = counterParty
    }

    void setTmpCounterParty(TmpCounterParty counterParty) {
        this.tmpCounterParty = counterParty
    }

    // GETTERS
    BigDecimal getAmount() {
        amount
    }

    boolean isDebit() {
        debit
    }

    Calendar getDate() {
        date
    }

    String getTransactionCode() {
        transactionCode
    }

    String getCommunication() {
        communication
    }

    CounterParty getCounterParty() {
        counterParty
    }

    TmpCounterParty getTmpCounterParty() {
        tmpCounterParty
    }

    boolean isStructured() {
        structured
    }

    //
    Properties getOutputProperties(){
        Properties properties = new Properties()
        properties.put(NAME,getName())
        properties.put(DATE,Utils.toString(date))
        properties.put(AMOUNT, amount.toString())
        properties.put(COMMUNICATION, communication)
        if(counterParty){
            properties.put(COUNTERPARTY,counterParty.name)
        }
        properties.put(TRANSACTIONCODE,transactionCode)
        properties.put(SIGN,isDebit()?"D":"C")
        properties
    }

}