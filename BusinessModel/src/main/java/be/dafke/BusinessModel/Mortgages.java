package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.ChildrenNeedSeparateFile;
import be.dafke.ObjectModel.MustBeRead;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 11:06
 */
public class Mortgages extends BusinessCollection<Mortgage> implements ChildrenNeedSeparateFile, MustBeRead {
    private Accounts accounts;

    public static final String MORTGAGES = "Mortgages";
    public static final String MORTGAGE = "Mortgage";

    public final static String TOTAL = "total";
    public final static String NRPAYED = "nrPayed";
    public final static String CAPITAL_ACCOUNT = "CapitalAccount";
    public final static String INTREST_ACCOUNT = "IntrestAccount";

    public Mortgages(Accounts accounts){
        setName(MORTGAGES);
        this.accounts = accounts;
    }

    @Override
        public String getChildType(){
            return MORTGAGE;
    }

    @Override
    public Set<String> getInitKeySet() {
        Set<String> keySet = new TreeSet<>();
        keySet.add(NAME);
        keySet.add(TOTAL);
        keySet.add(NRPAYED);
        keySet.add(CAPITAL_ACCOUNT);
        keySet.add(INTREST_ACCOUNT);
        return keySet;
    }
    @Override
    public Mortgage createNewChild(TreeMap<String, String> properties) {
        Mortgage mortgage = new Mortgage();
        mortgage.setName(properties.get(NAME));
        String startCapitalString = properties.get(Mortgages.TOTAL);
        String nrPayedString = properties.get(Mortgages.NRPAYED);
        if(startCapitalString!=null){
            mortgage.setStartCapital(new BigDecimal(startCapitalString));
        }
        if(nrPayedString!=null){
            mortgage.setAlreadyPayed(Utils.parseInt(nrPayedString));
        }
        String capitalAccount = properties.get(Mortgages.CAPITAL_ACCOUNT);
        if(capitalAccount!=null){
            mortgage.setCapitalAccount(accounts.getBusinessObject(capitalAccount));
        }
        String intrestAccount = properties.get(Mortgages.INTREST_ACCOUNT);
        if(intrestAccount!=null){
            mortgage.setIntrestAccount(accounts.getBusinessObject(intrestAccount));
        }
        return mortgage;
    }
}