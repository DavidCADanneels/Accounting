package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
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
    private VATBooking vatBooking = null;

    public Booking(Account account, BigDecimal amount, boolean debit, int id) {
        this.account = account;
        movement = new Movement(amount, debit, id);
        movement.setBooking(this);
    }
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

    public Integer getId(){
        return movement.getId();
    }

    @Override
    public TreeMap<String, String> getUniqueProperties(){
        return new TreeMap<>();
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

    public void setVatBooking(VATBooking vatBooking) {
        this.vatBooking = vatBooking;
    }

    public VATBooking getVatBooking() {
        return vatBooking;
    }
}