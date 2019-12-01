package be.dafke.Accounting.BusinessModel

class SearchOptions {

    boolean searchOnTransactionCode = false
    boolean searchOnCommunication = false
    boolean searchOnCounterParty = false
    CounterParty counterParty = null
    String communication = null
    String transactionCode = null

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
