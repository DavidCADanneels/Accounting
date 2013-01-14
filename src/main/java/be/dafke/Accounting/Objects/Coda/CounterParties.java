package be.dafke.Accounting.Objects.Coda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CounterParties extends HashMap<String, CounterParty> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<CounterParty> getCounterParties() {
		ArrayList<CounterParty> result = new ArrayList<CounterParty>();
		Iterator<CounterParty> it = values().iterator();
		while (it.hasNext()) {
			result.add(it.next());
		}
		return result;

	}

	@Override
	public CounterParty put(String key, CounterParty value) {
		if (key == null) return null;
		if (!containsKey(key)) {
			super.put(key, value);
			return value;
		}
		CounterParty result = super.get(key);
		HashMap<String, BankAccount> oldAccounts = result.getBankAccounts();
		for(BankAccount newAccount : value.getBankAccounts().values()) {
			String accountKey = newAccount.getAccountNumber();
			if (!oldAccounts.containsKey(accountKey)) {
				result.addAccount(newAccount);
				super.put(key, result);
				System.err.println("account " + accountKey + " added for " + key);
			} else {
				BankAccount oldAccount = oldAccounts.get(accountKey);
				String oldBic = oldAccount.getBic();
				String newBic = newAccount.getBic();
				String oldCurrency = oldAccount.getCurrency();
				String newCurrency = newAccount.getCurrency();
				if (oldBic == null) {
					oldAccount.setBic(newBic);
				} else if (newBic != null) {
					if (!oldBic.equals(newBic)) {
						System.err.println(oldBic + "!=" + newBic);
					}
				}
				if (oldCurrency == null) {
					oldAccount.setCurrency(newCurrency);
				} else if (newCurrency != null) {
					if (!oldCurrency.equals(newCurrency)) {
						System.err.println(oldCurrency + "!=" + newCurrency);
					}
				}
			}
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("CounterParties:\r\n");
		Iterator<CounterParty> it = values().iterator();
		while (it.hasNext()) {
			builder.append(it.next());
		}
		return builder.toString();
	}
}
