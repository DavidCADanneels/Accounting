package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.function.Predicate;

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
    private ArrayList<VATBooking> vatBookings = new ArrayList<>();

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

    public Booking(Booking booking) {
        this.account = booking.account;
        this.movement = new Movement(booking.movement);
        movement.setBooking(this);
    }

    public static Predicate<Booking> withAccount(Account account){
        return booking -> booking.account == account;
    }

    public static Predicate<Booking> vatBooking(){
        return booking -> !booking.vatBookings.isEmpty();
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

    public void addVatBooking(VATBooking vatBooking) {
        vatBookings.add(vatBooking);
    }

    public ArrayList<VATBooking> getVatBookings() {
        return vatBookings;
    }

    public String getVATBookingsString(){
        if(vatBookings == null || vatBookings.isEmpty()){
            return "";
        } else {
            StringBuffer buffer = new StringBuffer("(");
            for (VATBooking vatBooking:vatBookings) {
                VATField vatField = vatBooking.getVatField();
                if(vatField != null){
                    VATMovement vatMovement = vatBooking.getVatMovement();
                    if(vatMovement != null){
                        BigDecimal amount = vatMovement.getAmount();
                        boolean plus = amount.compareTo(BigDecimal.ZERO) >= 0;
                        buffer.append(plus ? "+" : "-");
                        buffer.append(vatField.getName());
                    }
                }
            }
            buffer.append(")");
            return buffer.toString();
        }

    }
}