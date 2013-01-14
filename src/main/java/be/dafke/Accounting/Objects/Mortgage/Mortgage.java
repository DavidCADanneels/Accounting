package be.dafke.Accounting.Objects.Mortgage;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

public class Mortgage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Vector<BigDecimal>> table;
	private int alreadyPayed = 0;
	private final String name;
	private Account capital, intrest;
	private final BigDecimal startCapital;

	public Mortgage(String name, BigDecimal startCapital) {
		this.name = name;
		this.startCapital = startCapital;
	}

	@Override
	public String toString() {
		return name;
	}

	public BigDecimal getStartCapital() {
		return startCapital;
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

	public void setTable(ArrayList<Vector<BigDecimal>> table) {
		this.table = table;
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
