package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Boekhoudkundig dagboek
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journal extends BusinessCollection<Transaction> {
    private String abbreviation;
    protected final HashMap<Integer,Transaction> transactions = new HashMap<>();
    private JournalType type;
    private Transaction currentTransaction;
    private Accounting accounting;
    private HashMap<AccountType, Boolean> checkedAccountTypesLeft = new HashMap<>();
    private HashMap<AccountType, Boolean> checkedAccountTypesRight = new HashMap<>();

    public Journal(Journal journal) {
        this(journal.getName(), journal.abbreviation);
        setType(journal.type);
    }

    public Journal(String name, String abbreviation) {
        setName(name);
        setAbbreviation(abbreviation);
        currentTransaction = new Transaction(Calendar.getInstance(),"");
	}

    public Accounting getAccounting() {
        return accounting;
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }

    @Override
    public boolean isDeletable(){
        return transactions.isEmpty();
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public JournalType getType() {
        return type;
    }

    public void setType(JournalType type) {
        this.type = type;
    }

	@Override
	public String toString() {
		return getName() + " (" + abbreviation + ")";
	}

    @Override
	public ArrayList<Transaction> getBusinessObjects() {
        return transactions.values().stream().sorted().collect(Collectors.toCollection(ArrayList::new));
    }

	public int getId() {
		return transactions.values().size()+1;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getId(Transaction transaction){
        return getBusinessObjects().indexOf(transaction)+1;
    }

    public Transaction getTransaction(int id){
        List<Transaction> transactions = getBusinessObjects(transaction -> transaction.getTransactionId()==id);
        return transactions.get(0);
    }

    public List<Transaction> getBusinessObjects(Predicate<Transaction> predicate) {
        return getBusinessObjects().stream().filter(predicate).collect(Collectors.toCollection(ArrayList::new));
    }

    public Transaction getBusinessObject(Integer transactionId){
        return transactions.get(transactionId);
    }

	public void removeBusinessObject(Transaction transaction) {
        transactions.remove(transaction.getTransactionId());
    }

	public Transaction addBusinessObject(Transaction transaction) {
        // TODD: refactor: call super method to add (sorted per ID) and remove the 'MultiValueMap transactions'
        // (sort on date in UI, do not store journal id nr, e.g. DIV25, store transaction ID instead and apply dynamic numbering to calculate 'DIV25')
//        super.addBusinessObject(transaction);
        if (transaction == null){
            System.err.println("error");
        }
        return transactions.put(transaction.getTransactionId(), transaction);
	}

	public static Predicate<Journal> withAbbr(String abbreviation){
        return journal -> journal.getAbbreviation().equals(abbreviation);
    }

    @Override
    public TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> keyMap = super.getUniqueProperties();
        keyMap.put(Journals.ABBREVIATION, abbreviation);
        return keyMap;
    }

    public ArrayList<AccountType> getCheckedTypesLeft() {
        ArrayList<AccountType> accountTypes = new ArrayList<>();
        for(Map.Entry<AccountType, Boolean> entry : checkedAccountTypesLeft.entrySet()){
            if(entry.getValue()){
                accountTypes.add(entry.getKey());
            }
        }
        return accountTypes;
    }

    public ArrayList<AccountType> getCheckedTypesRight() {
        ArrayList<AccountType> accountTypes = new ArrayList<>();
        for(Map.Entry<AccountType, Boolean> entry : checkedAccountTypesRight.entrySet()){
            if(entry.getValue()){
                accountTypes.add(entry.getKey());
            }
        }
        return accountTypes;
    }

    public void setTypeCheckedLeft(AccountType accountType, boolean available){
        checkedAccountTypesLeft.put(accountType, available);
    }

    public void setTypeCheckedRight(AccountType accountType, boolean available){
        checkedAccountTypesRight.put(accountType, available);
    }
}