package be.dafke.Coda.Objects;

import be.dafke.ObjectModel.BusinessObject;

public class TmpCounterParty extends CounterParty {
	/**
	 * 
	 */
    BusinessObject counterParty;

    public void setCounterParty(BusinessObject counterParty) {
        this.counterParty = counterParty;
    }

    public BusinessObject getCounterParty() {
		return counterParty;
	}
}
