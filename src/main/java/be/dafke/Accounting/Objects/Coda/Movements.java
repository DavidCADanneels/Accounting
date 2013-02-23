package be.dafke.Accounting.Objects.Coda;

import be.dafke.Accounting.GUI.CodaManagement.SearchOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Movements implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Movement> movements = new ArrayList<Movement>();

	public void add(Movement value) {
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

	public Movement getMovement(int nr) {
		return movements.get(nr);
	}

	public ArrayList<Movement> getAllMovements() {
		return movements;
	}

	public ArrayList<Movement> getMovements(SearchOptions searchOptions) {
		ArrayList<Movement> result = new ArrayList<Movement>();
        CounterParty counterParty = searchOptions.getCounterParty();
        String transactionCode = searchOptions.getTransactionCode();
        String communication = searchOptions.getCommunication();
        boolean searchOnCounterParty = searchOptions.isSearchOnCounterParty();
        boolean searchOnTransactionCode = searchOptions.isSearchOnTransactionCode();
        boolean searchOnCommunication = searchOptions.isSearchOnCommunication();
		for(Movement movement : movements) {
			if ((!searchOnTransactionCode || transactionCode.equals(movement.getTransactionCode()))  &&
                    (!searchOnCommunication || communication.equals(movement.getCommunication())) &&
                    (!searchOnCounterParty || counterParty == movement.getCounterParty())) {
				result.add(movement);
			}
		}
		return result;
	}

	public int getSize() {
		return movements.size();
	}
}
