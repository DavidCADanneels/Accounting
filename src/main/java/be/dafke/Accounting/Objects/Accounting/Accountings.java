package be.dafke.Accounting.Objects.Accounting;

import java.util.Collection;
import java.util.HashMap;

public class Accountings {
	private final HashMap<String, Accounting> accountings = new HashMap<String, Accounting>();
	private Accounting currentAccounting = null;

	public Accounting getCurrentAccounting() {
		return currentAccounting;
	}

	public void addAccounting(Accounting accounting) {
		accountings.put(accounting.toString(), accounting);
	}

	public boolean contains(String name) {
		return accountings.containsKey(name);
	}

	public Collection<Accounting> getAccountings() {
		return accountings.values();
	}

//	public void setCurrentAccounting(Accounting accounting) {
//		currentAccounting = accounting;
//	}

	public boolean isActive() {
		return currentAccounting != null;
	}

	public void setCurrentAccounting(String name) {
//		if (currentAccounting != null) {
//			currentAccounting.close();
//		}
		currentAccounting = accountings.get(name);
	}

//	public void openAccounting() {
//		if (currentAccounting != null) {
//			currentAccounting.close();
//		}
//		Object[] set = accountings.keySet().toArray();
//		Object obj = JOptionPane.showInputDialog(null, "Chooser", "Select an accounting",
//				JOptionPane.INFORMATION_MESSAGE, null, set, set[0]);
//		String s = (String) obj;
//		currentAccounting = accountings.get(s);
//	}

	public void addAccounting(String name) {
		currentAccounting = new Accounting(name);
		addAccounting(currentAccounting);
	}

//	public Accounting getAccounting(String name) {
//		return accountings.get(name);
//	}
}
