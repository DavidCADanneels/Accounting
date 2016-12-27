package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.MustBeRead;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.*;

import static be.dafke.BusinessModel.MortgageTransaction.NR;

public class Mortgage extends BusinessCollection<MortgageTransaction> implements MustBeRead {
    public static final String MORTGAGE_TRANSACTION = "MortgageTransaction";
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

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = super.getInitKeySet();
        keySet.add("nr");
        keySet.add("mensuality");
        keySet.add("intrest");
        keySet.add("capital");
        keySet.add("restCapital");
        return keySet;
    }

    @Override
    public String getChildType() {
        return MORTGAGE_TRANSACTION;
    }

    @Override
    public String toString(){
        return getName();
    }

    @Override
    public MortgageTransaction createNewChild(TreeMap<String, String> properties) {
        MortgageTransaction mortgageTransaction = new MortgageTransaction();
        mortgageTransaction.setMortgage(this);
        mortgageTransaction.setNr(Utils.parseInt(properties.get(NR)));
        mortgageTransaction.setMensuality(Utils.parseBigDecimal(properties.get(MortgageTransaction.MENSUALITY)));
        mortgageTransaction.setCapital(Utils.parseBigDecimal(properties.get(MortgageTransaction.CAPITAL)));
        mortgageTransaction.setIntrest(Utils.parseBigDecimal(properties.get(MortgageTransaction.INTREST)));
        mortgageTransaction.setRestCapital(Utils.parseBigDecimal(properties.get(MortgageTransaction.RESTCAPITAL)));
        return mortgageTransaction;
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

    @Override
    public Properties getOutputProperties() {
        Properties properties = new Properties();
        properties.put(NAME,getName());
        if(startCapital!=null){
            properties.put(Mortgages.TOTAL, startCapital.toString());
        }
        properties.put(Mortgages.NRPAYED, Integer.toString(alreadyPayed));
        if(capital!=null){
            properties.put(Mortgages.CAPITAL_ACCOUNT,capital.getName());
        }
        if(intrest!=null){
            properties.put(Mortgages.INTREST_ACCOUNT,intrest.getName());
        }
        return properties;
    }

    public void setAlreadyPayed(int alreadyPayed) {
        this.alreadyPayed = alreadyPayed;
    }
}
