package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection

import java.util.function.Predicate
import java.util.stream.Collectors

class Journal extends BusinessCollection<Transaction> {
    private String abbreviation
    protected final HashMap<Integer,Transaction> transactions = new HashMap()
    private JournalType type
    private Transaction currentTransaction
    private Accounting accounting

    Journal(Journal journal) {
        this(journal.getName(), journal.abbreviation)
        setType(journal.type)
    }

    Journal(String name, String abbreviation) {
        setName(name)
        setAbbreviation(abbreviation)
        currentTransaction = new Transaction(Calendar.getInstance(),"")
    }

    Accounting getAccounting() {
        accounting
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
    }

    @Override
    boolean isDeletable(){
        transactions.isEmpty()
    }

    Transaction getCurrentTransaction() {
        currentTransaction
    }

    void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction
    }

    JournalType getType() {
        type
    }

    void setType(JournalType type) {
        this.type = type
    }

    @Override
    String toString() {
        getName() + " (" + abbreviation + ")"
    }

    @Override
    ArrayList<Transaction> getBusinessObjects() {
        transactions.values().stream().sorted().collect().toList()
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
        List<Transaction> transactions = getBusinessObjects({ transaction -> transaction.getTransactionId() == id })
        transactions.get(0)
    }

    List<Transaction> getBusinessObjects(Predicate<Transaction> predicate) {
        getBusinessObjects().stream().filter(predicate).collect(Collectors.toCollection(ArrayList.&new))
    }

    Transaction getBusinessObject(Integer transactionId){
        transactions.get(transactionId)
    }

    void removeBusinessObject(Transaction transaction) {
        transactions.remove(transaction.getTransactionId())
    }

    Transaction addBusinessObject(Transaction transaction) {
        // TODD: refactor: call super method to add (sorted per ID) and remove the 'MultiValueMap transactions'
        // (sort on date in UI, do not store journal id nr, e.g. DIV25, store transaction ID instead and apply dynamic numbering to calculate 'DIV25')
//        super.addBusinessObject(transaction)
        if (transaction == null){
            System.err.println("error")
        }
        transactions.put(transaction.getTransactionId(), transaction)
    }

    static Predicate<Journal> withAbbr(String abbreviation){
        { journal -> journal.getAbbreviation().equals(abbreviation) }
    }

    @Override
    TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> keyMap = super.getUniqueProperties()
        keyMap.put(Journals.ABBREVIATION, abbreviation)
        keyMap
    }
}