package be.dafke.Coda;

public class TmpCounterParty extends CounterParty {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CounterParty counterParty;

	public TmpCounterParty(String name, CounterParty counterParty) {
		super(name);
		this.counterParty = counterParty;
	}

	public CounterParty getCounterParty() {
		return counterParty;
	}
}
