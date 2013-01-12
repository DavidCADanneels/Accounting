package be.dafke.Coda.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import be.dafke.Accounting.Objects.Transaction;

public class Statement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Header header;
	private OldBalance oldBalance;
	private NewBalance newBalance;
	private Trailer trailer;
	private final Collection<Movement> movements;

	public Statement() {
		movements = new ArrayList<Movement>();
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public void setOldBalance(OldBalance oldBalance) {
		this.oldBalance = oldBalance;
	}

	public void setNewBalance(NewBalance newBalance) {
		this.newBalance = newBalance;
	}

	public void setTrailer(Trailer trailer) {
		this.trailer = trailer;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(header);
		builder.append(oldBalance);
		for(Movement m : movements)
			builder.append(m);
		builder.append(newBalance);
		builder.append(trailer);
		return builder.toString();
	}

	public void addMovement(Movement movement1) {
		movements.add(movement1);
	}

	public Collection<Movement> getMovements() {
		return movements;
	}

	public Transaction toTransaction() {
		// Transaction trans = Transaction.newInstance(header, description)
		return null;
	}
}
