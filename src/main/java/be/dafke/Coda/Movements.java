package be.dafke.Coda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import be.dafke.Coda.Objects.Movement;

public class Movements implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ArrayList<Movement> movements = new ArrayList<Movement>();

	public static void add(Movement value) {
		movements.add(value);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("CounterParties:\r\n");
		Iterator<Movement> it = movements.iterator();
		while (it.hasNext()) {
			builder.append(it.next());
		}
		return builder.toString();
	}

	public static Movement getMovement(int nr) {
		return movements.get(nr);
	}

	public static ArrayList<Movement> getAllMovements() {
		return movements;
	}

	public static void setAllMovements(ArrayList<Movement> newMovements) {
		movements = newMovements;
	}

	public static ArrayList<Movement> getMovements(CounterParty counterParty) {
		ArrayList<Movement> result = new ArrayList<Movement>();
		for(Movement movement : movements) {
			if (movement.getCounterParty() == counterParty) {
				result.add(movement);
			}
		}
		return result;
	}

	public static ArrayList<Movement> getMovements(String transactionCode) {
		ArrayList<Movement> result = new ArrayList<Movement>();
		for(Movement movement : movements) {
			if (movement.getTransactionCode().equals(transactionCode)) {
				result.add(movement);
			}
		}
		return result;
	}

	public static ArrayList<Movement> getMovements(CounterParty counterParty, String transactionCode, boolean allowNull) {
		if (counterParty == null && !allowNull) {
			return getMovements(transactionCode);
		}
		if (transactionCode == null) {
			return getMovements(counterParty);
		}
		ArrayList<Movement> result = new ArrayList<Movement>();
		for(Movement movement : movements) {
			if (movement.getTransactionCode().equals(transactionCode) && movement.getCounterParty() == counterParty) {
				result.add(movement);
			}
		}
		return result;
	}

	public static int getSize() {
		return movements.size();
	}
}
