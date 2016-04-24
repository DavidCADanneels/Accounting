package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.MustBeRead;
import be.dafke.Utils.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class Mortgage extends BusinessCollection<MortgageTransaction> implements MustBeRead {
    private final static String TOTAL = "total";
    private final static String NRPAYED = "nrPayed";
    private final static String CAPITAL_ACCOUNT = "CapitalAccount";
    private final static String INTREST_ACCOUNT = "IntrestAccount";
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
        bookedtransactions = new MultiValueMap<Calendar,MortgageTransaction>();
    }

    @Override
    public MortgageTransaction createNewChild() {
        MortgageTransaction mortgageTransaction = new MortgageTransaction(accounts);
        mortgageTransaction.setMortgage(this);
        return mortgageTransaction;
    }


    @Override
    public TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = new TreeMap<String, String>();
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
    public Set<String> getInitKeySet() {
        Set<String> keySet = super.getInitKeySet();
        keySet.add(TOTAL);
        keySet.add(NRPAYED);
        keySet.add(CAPITAL_ACCOUNT);
        keySet.add(INTREST_ACCOUNT);
        return keySet;
    }

    @Override
    public Properties getInitProperties() {
        Properties properties = new Properties();
        properties.put(NAME,getName());
        if(startCapital!=null){
            properties.put(TOTAL, startCapital.toString());
        }
        properties.put(NRPAYED, Integer.toString(alreadyPayed));
        if(capital!=null){
            properties.put(CAPITAL_ACCOUNT,capital.getName());
        }
        if(intrest!=null){
            properties.put(INTREST_ACCOUNT,intrest.getName());
        }
        return properties;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
        String startCapitalString = properties.get(TOTAL);
        String nrPayedString = properties.get(NRPAYED);
        if(startCapitalString!=null){
            startCapital = new BigDecimal(startCapitalString);
        }
        if(nrPayedString!=null){
            alreadyPayed = Integer.parseInt(nrPayedString);
        }
        String capitalAccount = properties.get(CAPITAL_ACCOUNT);
        if(capitalAccount!=null){
            capital = mortgages.getAccounts().getBusinessObject(capitalAccount);
        }
        String intrestAccount = properties.get(INTREST_ACCOUNT);
        if(intrestAccount!=null){
            intrest = mortgages.getAccounts().getBusinessObject(intrestAccount);
        }
    }
}
