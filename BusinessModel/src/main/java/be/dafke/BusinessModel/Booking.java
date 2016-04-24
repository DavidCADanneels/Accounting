package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author David Danneels
 * @since 01/10/2010
 * @see Transaction
 */
public class Booking extends BusinessObject {
    private static final String ACCOUNT = "Account";
    public static final String ID = "id";
    public static final String DEBIT = "debit";
    public static final String CREDIT = "credit";
    private Account account;
    private Movement movement;
	private Transaction transaction;
    private Accounts accounts;

    public Booking(Accounts accounts, Account account, BigDecimal amount, boolean debit) {
        this.accounts = accounts;
        this.account = account;
        movement = new Movement(amount, debit);
        movement.setBooking(this);
    }

    public Booking(Accounts accounts){
        this.accounts = accounts;
	}

    public boolean isDebit(){
        return movement.isDebit();
    }

    public void setDebit(boolean debit){
        movement.setDebit(debit);
    }

    public BigDecimal getAmount(){
        return movement.getAmount();
    }

    public void setAmount(BigDecimal amount){
        movement.setAmount(amount);
    }

    public Movement getMovement(){
        return movement;
    }

    @Override
    public TreeMap<String, String> getUniqueProperties(){
        return new TreeMap<String, String>();
    }

    @Override
    public Properties getInitProperties() {
        Properties properties = new Properties();
        properties.put(ACCOUNT, account.getName());
        properties.put(ID, movement.getId().toString());
        if(movement.isDebit()){
            properties.put(DEBIT, movement.getAmount().toString());
        } else {
            properties.put(CREDIT, movement.getAmount().toString());
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
        account = accounts.getBusinessObject(properties.get(ACCOUNT));
        movement = createMovement(properties);
        movement.setBooking(this);
    }

    private Movement createMovement(TreeMap<String, String> properties){
        String debitString = properties.get(DEBIT);
        String creditString = properties.get(CREDIT);
        boolean debit= true;
        BigDecimal amount = null;
        if(debitString!=null){
            debit = true;
            amount = new BigDecimal(debitString);
            if(creditString!=null){
                System.err.println("Movement cannot contain both 'debit' and 'credit' !!!");
            }
        } else if(creditString!=null){
            debit = false;
            amount = new BigDecimal(creditString);
        } else {
            System.err.println("No 'debit' or 'credit' tag found in Movement !!!");
        }
        return new Movement(amount,debit);
    }

    // Getters

	public Transaction getTransaction() {
		return transaction;
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

}