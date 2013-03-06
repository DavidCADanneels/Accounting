package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.GUI.CodaManagement.SearchOptions;

import java.util.ArrayList;

public class Movements extends BusinessCollection<Movement>{

	public ArrayList<Movement> getMovements(SearchOptions searchOptions) {
		ArrayList<Movement> result = new ArrayList<Movement>();
        CounterParty counterParty = searchOptions.getCounterParty();
        String transactionCode = searchOptions.getTransactionCode();
        String communication = searchOptions.getCommunication();
        boolean searchOnCounterParty = searchOptions.isSearchOnCounterParty();
        boolean searchOnTransactionCode = searchOptions.isSearchOnTransactionCode();
        boolean searchOnCommunication = searchOptions.isSearchOnCommunication();
		for(Movement movement : getBusinessObjects()) {
			if ((!searchOnTransactionCode || transactionCode.equals(movement.getTransactionCode()))  &&
                    (!searchOnCommunication || communication.equals(movement.getCommunication())) &&
                    (!searchOnCounterParty || counterParty == movement.getCounterParty())) {
				result.add(movement);
			}
		}
		return result;
	}
}
