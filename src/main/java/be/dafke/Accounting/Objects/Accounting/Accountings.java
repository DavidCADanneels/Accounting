package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;

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

	public void setCurrentAccounting(String name) {
		currentAccounting = accountings.get(name);
	}

	public Accounting addAccounting(String name) throws EmptyNameException, DuplicateNameException {
        if(name==null || "".equals(name.trim())){
            throw new EmptyNameException();
        }
        if(accountings.containsKey(name.trim())){
            throw new DuplicateNameException();
        }
		Accounting accounting = new Accounting(name);
		addAccounting(accounting);
        return accounting;
	}
}
