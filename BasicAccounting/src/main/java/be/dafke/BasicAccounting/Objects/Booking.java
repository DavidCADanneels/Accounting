package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessCollectionDependent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author David Danneels
 * @since 01/10/2010
 * @see Transaction
 */
public class Booking extends BusinessCollection<Movement> implements BusinessCollectionDependent<Account>{
    private static final String ACCOUNT = "Account";
    public static final String ID = "id";
    public static final String DEBIT = "debit";
    public static final String CREDIT = "credit";
    private Account account;
    private ArrayList<Movement> movements;
	private Transaction transaction;
    private BusinessCollection<Account> businessCollection;

    public Booking(Account account) {
        super();
		this.account = account;
    }

    public Booking(){
        movements = new ArrayList<Movement>();
	}

    @Override
    public ArrayList<Movement> getBusinessObjects(){
        return movements;
    }

    @Override
    public Movement createNewChild(){
        // TODO: refactor Movement -> default constructor (no params)
        return new Movement(BigDecimal.ZERO, true);
    }

    @Override
    public String getChildType(){
        return "Movement";
    }

    @Override
    public TreeMap<String, String> getUniqueProperties(){
        return new TreeMap<String, String>();
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String,String> properties = new TreeMap<String, String>();
        properties.put(ACCOUNT, account.getName());
        properties.put(ID, movements.get(0).getId().toString());
        if(movements.get(0).isDebit()){
            properties.put(DEBIT, movements.get(0).getAmount().toString());
        } else {
            properties.put(CREDIT, movements.get(0).getAmount().toString());
        }
        return properties;
    }

    // FOR READING
    // Define keys to read from xml, required to initialize Object attributes
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(ACCOUNT);
        keySet.add(DEBIT);
        keySet.add(CREDIT);
        return keySet;
    }
    // Set initial values for each key in InitKeySet, while reading xml
    public void setInitProperties(TreeMap<String, String> properties){
        account = businessCollection.getBusinessObject(properties.get(ACCOUNT));
        Movement movement = createNewChild();
        movement.setInitProperties(properties);
        addBusinessObject(movement);
    }

    // Getters

	public Transaction getTransaction() {
		return transaction;
	}

    @Override
    public Movement addBusinessObject(Movement movement){
        movement.setBooking(this);
        //movements.clear(); // clear to ensure only Booking contains only 1 Movement
        movements.add(movement);
        return movement;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
		return account;
	}

    // Setters

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setBusinessCollection(BusinessCollection<Account> businessCollection) {
        this.businessCollection = businessCollection;
    }
}