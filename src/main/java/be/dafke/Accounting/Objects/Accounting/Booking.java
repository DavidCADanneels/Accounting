package be.dafke.Accounting.Objects.Accounting;

import java.math.BigDecimal;

/**
 * @author David Danneels
 * @since 01/10/2010
 * @see Transaction
 */
public class Booking {
	private Account account;
	private BigDecimal amount;
	private boolean debit;
	private Transaction transaction;

	public Booking(Account account, BigDecimal amount, boolean debit) {
		this.account = account;
		this.amount = amount;
		this.debit = debit;
	}

    // Getters

	public Transaction getTransaction() {
		return transaction;
	}

	public Account getAccount() {
		return account;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public boolean isDebit() {
		return debit;
	}

    // Setters

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDebit(boolean debit) {
        this.debit = debit;
    }

}