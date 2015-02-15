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
    private Account account;
    private ArrayList<Movement> movements;
	private Transaction transaction;
    private BusinessCollection<Account> businessCollection;

    public Booking(Account account) {
		this.account = account;
        movements = new ArrayList<Movement>();
    }
    public Booking(){
        movements = new ArrayList<Movement>();
//        account = businessCollection.getBusinessObject(name);
	}

    @Override
    public ArrayList<Movement> getBusinessObjects(){
        return movements;
    }

    @Override
    public boolean writeGrandChildren(){
        return true;
    }

    @Override
    public Movement createNewChild(){
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
    public TreeMap<String,String> getInitProperties(BusinessCollection collection) {
        TreeMap<String,String> properties = new TreeMap<String, String>();
        properties.put(ACCOUNT, account.getName());
        return properties;
    }

    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(ACCOUNT);
        return keySet;
    }
    //
    public void setInitProperties(TreeMap<String, String> properties){
        account = businessCollection.getBusinessObject(properties.get(ACCOUNT));
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

    @Override
    public void setBusinessCollection(BusinessCollection<Account> businessCollection) {
        this.businessCollection = businessCollection;
    }
}