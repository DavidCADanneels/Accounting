package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.math.BigDecimal;
import java.util.*;

public class Mortgage extends BusinessCollection<MortgageTransaction> {
	private int alreadyPayed = 0;
	private Account capital, intrest;
	private BigDecimal startCapital;
    private ArrayList<MortgageTransaction> mortgageTransactions;

    public Mortgage() {
//        super(NR);
        alreadyPayed = 0;
        capital = null;
        intrest = null;
        startCapital = null;
        mortgageTransactions = new ArrayList<>();
//        addSearchKey(NR);
    }

    public BigDecimal getTotalIntrest() {
        BigDecimal result = BigDecimal.ZERO;
        for(MortgageTransaction vector : getBusinessObjects()) {
            result = result.add(vector.getIntrest());
        }
        return result;
    }

    public BigDecimal getTotalToPay() {
        BigDecimal result = BigDecimal.ZERO;
        for(MortgageTransaction vector : getBusinessObjects()) {
            result = result.add(vector.getMensuality());
        }
        return result;
    }

    @Override
    public MortgageTransaction addBusinessObject(MortgageTransaction value) {
        mortgageTransactions.add(value);
        return value;
    }

    @Override
    public ArrayList<MortgageTransaction> getBusinessObjects(){
        return mortgageTransactions;
    }

    public void recalculateTable(int row){
        BigDecimal vorigRestCapital;
        if (row == 0) {
            vorigRestCapital = getStartCapital();
        } else {
            vorigRestCapital = getBusinessObjects().get(row - 1).getRestCapital();
        }
        for(int i=row; i<getBusinessObjects().size();i++) {
            MortgageTransaction line = getBusinessObjects().get(i);
            BigDecimal capital = line.getCapital();
            BigDecimal restCapital = vorigRestCapital.subtract(capital);
            line.setRestCapital(restCapital);
            vorigRestCapital = restCapital;
        }
    }

    public boolean isBookable(){
        return (capital!=null && intrest!=null && !isPayedOff());
    }
    @Override
    public boolean isDeletable(){
        return alreadyPayed > 0 && alreadyPayed < getBusinessObjects().size();
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

	public void setPayed(int nr) {
		alreadyPayed = nr;
	}

	public int getNrPayed() {
		return alreadyPayed;
	}

	public boolean isPayedOff() {
		return alreadyPayed == getBusinessObjects().size();
	}

    public BigDecimal getNextIntrestAmount(){
        return getBusinessObjects().get(alreadyPayed).getIntrest();
    }

    public BigDecimal getNextCapitalAmount(){
        return getBusinessObjects().get(alreadyPayed).getCapital();
    }

    public void raiseNrPayed() {
        alreadyPayed++;
    }

    public void decreaseNrPayed(){
        alreadyPayed--;
    }

    public void setAlreadyPayed(int alreadyPayed) {
        this.alreadyPayed = alreadyPayed;
    }
}
