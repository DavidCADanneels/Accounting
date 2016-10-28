package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.MustBeRead;
import be.dafke.Utils.MultiValueMap;

import java.math.BigDecimal;
import java.util.*;

public class Mortgage extends BusinessCollection<MortgageTransaction> implements MustBeRead {
    public static final String MORTGAGE_TRANSACTION = "MortgageTransaction";
    private ArrayList<Vector<BigDecimal>> table;
	private int alreadyPayed = 0;
	private Account capital, intrest;
	private BigDecimal startCapital;
    private Mortgages mortgages;
    private Accounts accounts;
    private final MultiValueMap<Calendar,MortgageTransaction> bookedtransactions;

    @Override
    public String getChildType() {
        return MORTGAGE_TRANSACTION;
    }

    public Mortgage(Mortgages mortgages, Accounts accounts){
        this.mortgages = mortgages;
        this.accounts = accounts;
        bookedtransactions = new MultiValueMap<>();
    }

    @Override
    public String toString(){
        return getName();
    }

    @Override
    public MortgageTransaction createNewChild(TreeMap<String, String> properties) {
        MortgageTransaction mortgageTransaction = new MortgageTransaction(accounts);
        mortgageTransaction.setMortgage(this);
        return mortgageTransaction;
    }


    @Override
    public TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = new TreeMap<>();
        properties.put(NAME,getName());
        return properties;
    }

    public boolean isBookable(){
        return (capital!=null && intrest!=null && !isPayedOff());
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

    public BigDecimal getMensuality(){
        return table.get(alreadyPayed).get(0);
    }

	public boolean isPayedOff() {
		return alreadyPayed == table.size();
	}

    public BigDecimal getNextIntrestAmount(){
        return table.get(alreadyPayed).get(1);
    }

    public BigDecimal getNextCapitalAmount(){
        return table.get(alreadyPayed).get(2);
    }

    @Override
    public MortgageTransaction addBusinessObject(MortgageTransaction mortgageTransaction) {
        Calendar date = mortgageTransaction.getDate();
        alreadyPayed++;
        return bookedtransactions.addValue(date, mortgageTransaction);
    }

    @Override
    public void removeBusinessObject(MortgageTransaction mortgageTransaction){
        Calendar date = mortgageTransaction.getDate();
        bookedtransactions.removeValue(date, mortgageTransaction);
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
