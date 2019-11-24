package be.dafke.Accounting.BusinessModel

class SearchOptions {

    private boolean searchOnTransactionCode = false
    private boolean searchOnCommunication = false
    private boolean searchOnCounterParty = false
    private CounterParty counterParty = null
    private String communication = null
    private String transactionCode = null

    // Getters
    boolean isSearchOnCommunication() {
        searchOnCommunication
    }

    boolean isSearchOnCounterParty() {
        searchOnCounterParty
    }

    boolean isSearchOnTransactionCode() {
        searchOnTransactionCode
    }

    String getCommunication() {
        communication
    }

    String getTransactionCode() {
        transactionCode
    }

    CounterParty getCounterParty() {
        counterParty
    }

    // Setters
    void setSearchOnCounterParty(boolean searchOnCounterParty) {
        this.searchOnCounterParty = searchOnCounterParty
    }

    void setSearchOnCommunication(boolean searchOnCommunication) {
        this.searchOnCommunication = searchOnCommunication
    }
    void setSearchOnTransactionCode(boolean searchOnTransactionCode) {
        this.searchOnTransactionCode = searchOnTransactionCode
    }

    void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode
    }

    void setCommunication(String communication) {
        this.communication = communication
    }

    void setCounterParty(CounterParty counterParty) {
        this.counterParty = counterParty
    }

    // DoubleSetters
    void searchForCounterParty(CounterParty counterParty){
        this.counterParty = counterParty
        this.searchOnCounterParty = true
    }

    void searchForTransactionCode(String transactionCode){
        this.transactionCode = transactionCode
        this.searchOnTransactionCode = true
    }

    void searchForCommunication(String communication){
        this.communication = communication
        this.searchOnCommunication = true
    }
}
