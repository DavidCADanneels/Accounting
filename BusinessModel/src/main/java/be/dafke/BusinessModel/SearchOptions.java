package be.dafke.BusinessModel;

/**
 * User: Dafke
 * Date: 23/02/13
 * Time: 5:47
 */
public class SearchOptions {

    private boolean searchOnTransactionCode = false;
    private boolean searchOnCommunication = false;
    private boolean searchOnCounterParty = false;
    private CounterParty counterParty = null;
    private String communication = null;
    private String transactionCode = null;

    // Getters
    public boolean isSearchOnCommunication() {
        return searchOnCommunication;
    }

    public boolean isSearchOnCounterParty() {
        return searchOnCounterParty;
    }

    public boolean isSearchOnTransactionCode() {
        return searchOnTransactionCode;
    }

    public String getCommunication() {
        return communication;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public CounterParty getCounterParty() {
        return counterParty;
    }

    // Setters
    public void setSearchOnCounterParty(boolean searchOnCounterParty) {
        this.searchOnCounterParty = searchOnCounterParty;
    }

    public void setSearchOnCommunication(boolean searchOnCommunication) {
        this.searchOnCommunication = searchOnCommunication;
    }
    public void setSearchOnTransactionCode(boolean searchOnTransactionCode) {
        this.searchOnTransactionCode = searchOnTransactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public void setCounterParty(CounterParty counterParty) {
        this.counterParty = counterParty;
    }

    // DoubleSetters
    public void searchForCounterParty(CounterParty counterParty){
        this.counterParty = counterParty;
        this.searchOnCounterParty = true;
    }

    public void searchForTransactionCode(String transactionCode){
        this.transactionCode = transactionCode;
        this.searchOnTransactionCode = true;
    }

    public void searchForCommunication(String communication){
        this.communication = communication;
        this.searchOnCommunication = true;
    }
}
