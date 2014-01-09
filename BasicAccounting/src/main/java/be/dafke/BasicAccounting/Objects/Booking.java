package be.dafke.BasicAccounting.Objects;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessCollectionDependent;

/**
 * @author David Danneels
 * @since 01/10/2010
 * @see Transaction
 */
public class Booking extends BusinessCollection<Movement> implements BusinessCollectionDependent<Account>{
    private static final String ACCOUNT = "Account";
    private Account account;
    private Movement movement;
	private Transaction transaction;
    private BusinessCollection<Account> businessCollection;

    public Booking(Account account) {
		this.account = account;
    }
    public Booking(){
//        account = businessCollection.getBusinessObject(name);
	}

    @Override
    public Movement createNewChild(String name){
        return new Movement(BigDecimal.ZERO, true);
    }

    @Override
    public boolean mustBeRead(){
        return false;
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
        this.movement = movement;
        return movement;
    }

    public Account getAccount() {
		return account;
	}

    // Setters

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Movement getMovement() {
        return movement;
    }

    @Override
    public void setBusinessCollection(BusinessCollection<Account> businessCollection) {
        this.businessCollection = businessCollection;
    }
}