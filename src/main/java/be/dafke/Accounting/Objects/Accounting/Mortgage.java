package be.dafke.Accounting.Objects.Accounting;

import be.dafke.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class Mortgage extends Account {
	private ArrayList<Vector<BigDecimal>> table;
	private int alreadyPayed = 0;
	private Account capital, intrest;
	private BigDecimal startCapital;
    private final MultiValueMap<Movement,Movement> movements;

    public Mortgage(){
        movements = new MultiValueMap<Movement, Movement>();
    }

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

    @Override
    protected void book(Calendar date, Movement movement){
        System.out.println("Mortgage.book()");

        BigDecimal intrestAmount = table.get(alreadyPayed).get(1);
        BigDecimal capitalAmount = table.get(alreadyPayed).get(2);
        Movement movement1 = new Movement(intrestAmount, true);
        Movement movement2 = new Movement(capitalAmount, true);

        movement1.setBooking(movement.getBooking());
        movement2.setBooking(movement.getBooking());

        movements.addValue(movement, movement1);
        movements.addValue(movement, movement2);

        intrest.book(date, movement1);
        capital.book(date,movement2);
        super.book(date, movement);

        alreadyPayed++;
    }

    // TODO: check what happens if changing the date of an old (not last) Mortgage: --> unbook() + book(): amounts not correct !!!

    @Override
    protected void unbook(Calendar date, Movement movement){
        System.out.println("Mortgage.unbook()");

        ArrayList<Movement> subMovements = movements.get(movement);

        // check if last movement or not
        ArrayList<Movement> allMovements = movements.values();
        Movement lastMovement = allMovements.get(allMovements.size()-1);
        if(movement!=lastMovement){
            System.err.println("Mortgage.unbook(): WARNING: not last movement");
        }

        Movement movement1 = subMovements.get(0);
        Movement movement2 = subMovements.get(1);

        intrest.unbook(date, movement1);
        capital.unbook(date,movement2);
        super.unbook(date,movement);
        alreadyPayed--;
    }

    public BigDecimal getMensuality(){
        return table.get(alreadyPayed).get(0);
    }

	public boolean isPayedOff() {
		return alreadyPayed == table.size();
	}
}
