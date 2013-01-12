package be.dafke.Mortgage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Transaction;

public class Mortgage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ArrayList<Vector<BigDecimal>> table;
	private int alreadyPayed = 0;
	private final String name;
	private Account capital, intrest;

	public Mortgage(String name, ArrayList<Vector<BigDecimal>> table) {
		this.table = table;
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public void setCapitalAccount(Account capital) {
		this.capital = capital;
	}

	public Account getCapitalAccount() {
		return capital;
	}

	public void setIntrestAccount(Account intrest) {
		this.intrest = intrest;
	}

	public Account getIntrestAccount() {
		return intrest;
	}

	public ArrayList<Vector<BigDecimal>> getTable() {
		return table;

	}

	public void setPayed(int nr) {
		alreadyPayed = nr;
	}

	public int getNrPayed() {
		return alreadyPayed;
	}

	public void increasePayed() {
		alreadyPayed++;
	}

	public void pay(BigDecimal amount, Transaction transaction) throws Exception {
		if (alreadyPayed == table.size()) {
			throw new Exception("Everything is already payed");
		}
		BigDecimal mensualiteit = table.get(alreadyPayed).get(0);
		if (amount.compareTo(mensualiteit) != 0) {
			throw new Exception("Amount must be equal to: " + mensualiteit);
		}
		BigDecimal intrestAmount = table.get(alreadyPayed).get(1);
		BigDecimal kapitalAmount = table.get(alreadyPayed).get(2);
		transaction.debiteer(intrest, intrestAmount);
		transaction.debiteer(capital, kapitalAmount);
		System.out.println("Restkapitaal: " + table.get(alreadyPayed).get(3));
	}

	public void pay(Transaction transaction) {
		BigDecimal intrestAmount = table.get(alreadyPayed).get(1);
		BigDecimal kapitalAmount = table.get(alreadyPayed).get(2);
		transaction.debiteer(intrest, intrestAmount);
		transaction.debiteer(capital, kapitalAmount);
		transaction.addMortgage(this);
		System.out.println("Restkapitaal: " + table.get(alreadyPayed).get(3));
	}

	public boolean isPayedOff() {
		return alreadyPayed == table.size();
	}
}
