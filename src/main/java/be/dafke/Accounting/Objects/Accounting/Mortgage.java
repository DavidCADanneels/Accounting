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
    private final MultiValueMap<Calendar,Movement[]> movements;
//    private boolean compressed = true;

    public Mortgage(){
        movements = new MultiValueMap<Calendar, Movement[]>();
    }

    public boolean isBookable(){
        return (capital!=null && intrest!=null);
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

    public ArrayList<Booking> expandBooking(Calendar date, Movement movement){
        System.out.println("Mortgage.book()");

        // Define new Amounts
        BigDecimal newIntrestAmount = table.get(alreadyPayed).get(1);
        BigDecimal newCapitalAmount = table.get(alreadyPayed).get(2);

        // Check for more recent Movements
        ArrayList<Movement[]> moreRecentMovements = movements.tailList(date, false);
        if(!moreRecentMovements.isEmpty())  {
            // TODO: insert
            System.err.println("Insert needed");

            // Check if the amount is equal to the amount of the most recent Movement
            ArrayList<Movement[]> allMovements = movements.values();
            Movement[] lastMovements = allMovements.get(allMovements.size()-1);
            Movement lastMovement = lastMovements[0];

            if(lastMovement.getAmount().compareTo(movement.getAmount()) == 0){
                // Amounts are equal
                for(int i=moreRecentMovements.size()-1;i>=0;i--){
                    Movement[] couple = moreRecentMovements.get(i);
                    Movement oldIntrestMovement = couple[1];
                    Movement oldCapitalMovement = couple[2];

                    BigDecimal oldIntrestAmount = oldIntrestMovement.getAmount();
                    BigDecimal oldCapitalAmount = oldCapitalMovement.getAmount();

                    oldIntrestMovement.setAmount(newIntrestAmount);
                    oldCapitalMovement.setAmount(newCapitalAmount);

                    newIntrestAmount = oldIntrestAmount;
                    newCapitalAmount = oldCapitalAmount;
                }
            } else {
                // Amounts are not equal
                System.err.println("Degressive");
            }
        }

        // Define new Movements
        Movement newIntrestMovement = new Movement(newIntrestAmount, true);
        Movement newCapitalMovement = new Movement(newCapitalAmount, true);

        // Copy booking reference to new Movements
        newIntrestMovement.setBooking(movement.getBooking());
        newCapitalMovement.setBooking(movement.getBooking());

        alreadyPayed++;

        Movement[] couple = new Movement[3];
        couple[0] = movement;
        couple[1] = newIntrestMovement;
        couple[2] = newCapitalMovement;

        movements.addValue(date, couple);

        Booking intrestBooking = new Booking(intrest);
        intrestBooking.setMovement(newIntrestMovement);
        Booking capitalBooking = new Booking(capital);
        capitalBooking.setMovement(newCapitalMovement);

        ArrayList<Booking> result = new ArrayList<Booking>();
        result.add(intrestBooking);
        result.add(capitalBooking);
        return result;
    }

    // TODO: activate this function
//    @Override
//    protected void book(Calendar date, Movement movement){
//        ArrayList<Booking> bookings = expandBooking(date, movement);
//        Booking intrestBooking = bookings.get(0);
//        Booking captitalBooking = bookings.get(1);
//        Movement newIntrestMovement = intrestBooking.getMovement();
//        Movement newCapitalMovement = captitalBooking.getMovement();
//        intrest.book(date, newIntrestMovement);
//        capital.book(date, newCapitalMovement);
//        if(compressed)
//        super.book(date, movement);
//    }

    // TODO: check what happens if changing the date of an old (not last) Mortgage: --> unbook() + book(): amounts not correct !!!

    // TODO: fix unbook()
    @Override
    protected Movement unbook(Calendar date, Movement movement){
        System.out.println("Mortgage.unbook()");

        // Check for more recent Movements
        ArrayList<Movement[]> moreRecentMovements = movements.tailList(date,false);
        if(moreRecentMovements.size()==1){
            // only 1
            Movement[] lastCouple = moreRecentMovements.get(0);

            Movement lastMovement = lastCouple[0];
            // TODO: check amounts: if not equal --> degressive mortgage --> extra actions needed

            Movement intrestMovement = lastCouple[1];
            Movement capitalMovement = lastCouple[2];

            intrest.unbook(date, intrestMovement);
            capital.unbook(date,capitalMovement);
            super.unbook(date,movement);
            alreadyPayed--;
        } else {
            // TODO: move
            System.err.println("Moving needed");
        }

        return movement;
    }

    public BigDecimal getMensuality(){
        return table.get(alreadyPayed).get(0);
    }

	public boolean isPayedOff() {
		return alreadyPayed == table.size();
	}
}
