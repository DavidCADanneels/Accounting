package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class MortgageTransaction extends BusinessObject{
    static final String NR = "nr"
    static final String MENSUALITY = "mensuality"
    static final String CAPITAL = "capital"
    static final String INTREST = "intrest"
    static final String RESTCAPITAL = "restCapital"

    Mortgage mortgage
    int nr=0
    BigDecimal mensuality
    BigDecimal capital
    BigDecimal intrest
    BigDecimal restCapital

    Mortgage getMortgage() {
        mortgage
    }

    int getNr() {
        nr
    }

    BigDecimal getMensuality() {
        mensuality
    }

    BigDecimal getCapital() {
        capital
    }

    BigDecimal getIntrest() {
        intrest
    }

    BigDecimal getRestCapital() {
        restCapital
    }

    void setMortgage(Mortgage mortgage) {
        this.mortgage = mortgage
    }

    void setNr(int nr) {
        this.nr = nr
//        setName(nr+"")
    }

    void setMensuality(BigDecimal mensuality) {
        this.mensuality = mensuality
    }

    void setCapital(BigDecimal capital) {
        this.capital = capital
    }

    void setIntrest(BigDecimal intrest) {
        this.intrest = intrest
    }

    void setCapital(BigDecimal capital, boolean update) {
        BigDecimal diff = capital.subtract(this.capital)
        // if the new capital is higher than before, the diff must be subtracted from restCapital
        this.capital = capital
        if (update){
            intrest = mensuality.subtract(capital)
            restCapital = restCapital.subtract(diff)
        }
    }

    void setIntrest(BigDecimal intrest, boolean update) {
        BigDecimal diff = intrest.subtract(this.intrest)
        // if the new intrest is higher than before, the capital will be lower, hence the diff must be added from restCapital
        this.intrest = intrest
        if (update){
            capital = mensuality.subtract(intrest)
            restCapital = restCapital.add(diff)
        }
    }

    void setRestCapital(BigDecimal restCapital) {
        this.restCapital = restCapital
    }

    TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = new TreeMap()
        properties.put(NR,Integer.toString(nr))
        properties
    }
}
