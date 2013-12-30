package be.dafke.BasicAccounting.Objects;

/**
 * @author David Danneels
 * @since 01/10/2010
 * @see Transaction
 */
public class Booking {
    // TODO: extend from BusinessObject
	private Account account;
    private Movement movement;
	private Transaction transaction;

	public Booking(Account account) {
		this.account = account;
	}

    // Getters

	public Transaction getTransaction() {
		return transaction;
	}

    public void setMovement(Movement movement){
        movement.setBooking(this);
        this.movement = movement;
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
}