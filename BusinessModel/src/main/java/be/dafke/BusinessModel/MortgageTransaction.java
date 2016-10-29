package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by ddanneels on 26/12/2015.
 */
public class MortgageTransaction extends BusinessObject{
    public static final String NR = "nr";
    public static final String MENSUALITY = "mensuality";
    public static final String CAPITAL = "capital";
    public static final String INTREST = "intrest";
    public static final String RESTCAPITAL = "restCapital";

    private Mortgage mortgage;
    private int nr=0;
    private BigDecimal mensuality;
    private BigDecimal capital;
    private BigDecimal intrest;
    private BigDecimal restCapital;

    public Mortgage getMortgage() {
        return mortgage;
    }

    public int getNr() {
        return nr;
    }

    public BigDecimal getMensuality() {
        return mensuality;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public BigDecimal getIntrest() {
        return intrest;
    }

    public BigDecimal getRestCapital() {
        return restCapital;
    }

    public void setMortgage(Mortgage mortgage) {
        this.mortgage = mortgage;
    }

    public void setNr(int nr) {
        this.nr = nr;
//        setName(nr+"");
    }

    public void setMensuality(BigDecimal mensuality) {
        this.mensuality = mensuality;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public void setIntrest(BigDecimal intrest) {
        this.intrest = intrest;
    }

    public void setCapital(BigDecimal capital, boolean update) {
        this.capital = capital;
        if (update){
            intrest = mensuality.subtract(capital);
        }
    }

    public void setIntrest(BigDecimal intrest, boolean update) {
        this.intrest = intrest;
        if (update){
            capital = mensuality.subtract(intrest);
        }
    }

    public void setRestCapital(BigDecimal restCapital) {
        this.restCapital = restCapital;
    }

    public TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = new TreeMap<>();
        properties.put(NR,Integer.toString(nr));
        return properties;
    }

    public static Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(NR);
        keySet.add(MENSUALITY);
        keySet.add(CAPITAL);
        keySet.add(INTREST);
        keySet.add(RESTCAPITAL);
        return keySet;
    }

    @Override
    public Properties getOutputProperties() {
        Properties properties = new Properties();
        properties.put(NR, nr);
        properties.put(MENSUALITY, mensuality);
        properties.put(CAPITAL, capital);
        properties.put(INTREST, intrest);
        properties.put(RESTCAPITAL, restCapital);
        return properties;
    }
}
