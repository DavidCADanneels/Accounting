package be.dafke.Accounting.Objects.Accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

public class Mortgage extends BusinessObject {
	private ArrayList<Vector<BigDecimal>> table;
	private int alreadyPayed = 0;
	private Account capital, intrest;
	private BigDecimal startCapital;

    @Override
    public boolean isDeletable(){
        return alreadyPayed > 0 && alreadyPayed < table.size();
    }

	public BigDecimal getStartCapital() {
		return startCapital;
	}

    public void setStartCapital(BigDecimal startCapital) {
        this.startCapital = startCapital;
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

	public void pay(Transaction transaction) {
        // TODO: check if not everything is payed yet getBusinessObject(alreadyPayed) --> ArrayOutOfBoundsException
		BigDecimal intrestAmount = table.get(alreadyPayed).get(1);
		BigDecimal kapitalAmount = table.get(alreadyPayed).get(2);
        transaction.addBooking(intrest,intrestAmount,true,false);
        transaction.addBooking(capital, kapitalAmount,true,false);
		transaction.addMortgage(this);
		System.out.println("Restkapitaal: " + table.get(alreadyPayed).get(3));
	}

	public boolean isPayedOff() {
		return alreadyPayed == table.size();
	}
}
