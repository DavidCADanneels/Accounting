package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection

import java.util.function.Predicate
import java.util.stream.Stream

class Journal extends BusinessCollection<Transaction> {
    String abbreviation
    protected final HashMap<Integer,Transaction> transactions = new HashMap()
    JournalType type
    Transaction currentTransaction
    Accounting accounting
    Account baseAccount = null

    Journal(Journal journal) {
        this(journal.name, journal.abbreviation)
        type = journal.type
    }

    Journal(String name, String abbreviation) {
        setName(name)
        setAbbreviation(abbreviation)
        currentTransaction = new Transaction(Calendar.getInstance(),"")
    }

    Account getBaseAccount() {
        return baseAccount
    }

    void setBaseAccount(Account baseAccount) {
        this.baseAccount = baseAccount
    }

    @Override
    boolean isDeletable(){
        transactions.isEmpty()
    }

    @Override
    String toString() {
        getName() + " (" + abbreviation + ")"
    }

    @Override
    ArrayList<Transaction> getBusinessObjects() {
        Collection<Transaction> values = this.transactions.values()
        Stream<Transaction> sorted = values.stream().sorted()
        sorted.collect() as ArrayList<Transaction>
    }

    int getId() {
        transactions.values().size()+1
    }

    String getAbbreviation() {
        abbreviation
    }

    void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation
    }

    int getId(Transaction transaction){
        getBusinessObjects().indexOf(transaction)+1
    }

    Transaction getTransaction(int id){
        List<Transaction> transactions = getBusinessObjects({ transaction -> transaction.transactionId == id })
        transactions.get(0)
    }

    List<Transaction> getBusinessObjects(Predicate<Transaction> predicate) {
        getBusinessObjects().stream().filter(predicate).collect().toList()
    }

    Transaction getBusinessObject(Integer transactionId){
        transactions.get(transactionId)
    }

    void removeBusinessObject(Transaction transaction) {
        transactions.remove(transaction.transactionId)
    }

    Transaction addBusinessObject(Transaction transaction) {
        // TODD: refactor: call super method to add (sorted per ID) and remove the 'MultiValueMap transactions'
        // (sort on date in UI, do not store journal id nr, e.g. DIV25, store transaction ID instead and apply dynamic numbering to calculate 'DIV25')
//        super.addBusinessObject(transaction)
        if (transaction == null){
            System.err.println("error")
        }
        transactions.put(transaction.transactionId, transaction)
    }

    static Predicate<Journal> withAbbr(String abbreviation){
        { journal -> journal.abbreviation.equals(abbreviation) }
    }

    @Override
    TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> keyMap = super.getUniqueProperties()
        keyMap.put(Journals.ABBREVIATION, abbreviation)
        keyMap
    }
}