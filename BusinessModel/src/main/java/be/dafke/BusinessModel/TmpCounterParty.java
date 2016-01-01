package be.dafke.BusinessModel;

public class TmpCounterParty extends CounterParty {
	/**
	 * 
	 */
    CounterParty counterParty;

    public void setCounterParty(CounterParty counterParty) {
        this.counterParty = counterParty;
    }

    public CounterParty getCounterParty() {
		return counterParty;
	}
}
