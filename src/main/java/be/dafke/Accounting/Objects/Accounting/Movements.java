package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.GUI.CodaManagement.SearchOptions;

import java.util.ArrayList;

public class Movements extends BusinessCollection<Movement>{

    private ArrayList<Movement> movements = new ArrayList<Movement>();
//    private HashMap<String,Movement> movements = new HashMap<String, Movement>();

    public Movements(){
        super("Movements");
    }

    public void add(Movement value) {
		movements.add(value);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Movements:");
		for (Movement movement : movements){
			builder.append("\r\n").append(movement.toString());
		}
		return builder.toString();
	}

	public Movement getMovement(int nr) {
		return movements.get(nr);
	}

    @Override
    public Movement getBusinessObject(String name) {
        return null;  // Not used yet
    }

    @Override
	public ArrayList<Movement> getBusinessObjects() {
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
