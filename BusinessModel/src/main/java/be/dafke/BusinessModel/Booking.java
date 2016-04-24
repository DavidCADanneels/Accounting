package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.TreeMap;

/**
 * @author David Danneels
 * @since 01/10/2010
 * @see Transaction
 */
public class Booking extends BusinessObject {
    public static final String ID = "id";
    private Account account;
    private Movement movement;
	private Transaction transaction;

    public static final String DEBIT = "debit";
    public static final String CREDIT = "credit";
    public static final String ACCOUNT = "Account";

    public Booking(Account account, BigDecimal amount, boolean debit) {
        this.account = account;
        movement = new Movement(amount, debit);
        movement.setBooking(this);
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
    public Properties getOutputProperties() {
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