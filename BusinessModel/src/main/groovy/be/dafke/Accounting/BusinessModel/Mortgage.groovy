package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection

class Mortgage extends BusinessCollection<MortgageTransaction> {
    int alreadyPayed = 0
    Account capital, intrest
    BigDecimal startCapital
    ArrayList<MortgageTransaction> mortgageTransactions

    Mortgage() {
//        super(NR)
        alreadyPayed = 0
        capital = null
        intrest = null
        startCapital = null
        mortgageTransactions = new ArrayList()
//        addSearchKey(NR)
    }

    BigDecimal getTotalIntrest() {
        BigDecimal result = BigDecimal.ZERO
        for(MortgageTransaction vector : getBusinessObjects()) {
            result = result.add(vector.getIntrest())
        }
        result
    }

    BigDecimal getTotalToPay() {
        BigDecimal result = BigDecimal.ZERO
        for(MortgageTransaction vector : getBusinessObjects()) {
            result = result.add(vector.getMensuality())
        }
        result
    }

    @Override
    MortgageTransaction addBusinessObject(MortgageTransaction value) {
        mortgageTransactions.add(value)
        value
    }

    @Override
    ArrayList<MortgageTransaction> getBusinessObjects(){
        mortgageTransactions
    }

    void recalculateTable(int row){
        BigDecimal vorigRestCapital
        if (row == 0) {
            vorigRestCapital = getStartCapital()
        } else {
            vorigRestCapital = getBusinessObjects().get(row - 1).getRestCapital()
        }
        for(int i=row ;i<getBusinessObjects().size();i++) {
            MortgageTransaction line = getBusinessObjects().get(i)
            BigDecimal capital = line.getCapital()
            BigDecimal restCapital = vorigRestCapital.subtract(capital)
            line.setRestCapital(restCapital)
            vorigRestCapital = restCapital
        }
    }

    boolean isBookable(){
        (capital && intrest && !isPayedOff())
    }
    @Override
    boolean isDeletable(){
        alreadyPayed == 0 || alreadyPayed == getBusinessObjects().size()
    }

    BigDecimal getStartCapital() {
        startCapital
    }

    void setStartCapital(BigDecimal startCapital) {
        this.startCapital = startCapital
    }

    void setCapitalAccount(Account capital) {
        this.capital = capital
    }

    Account getCapitalAccount() {
        capital
    }

    void setIntrestAccount(Account intrest) {
        this.intrest = intrest
    }

    Account getIntrestAccount() {
        intrest
    }

    void setPayed(int nr) {
        alreadyPayed = nr
    }

    int getNrPayed() {
        alreadyPayed
    }

    boolean isPayedOff() {
        alreadyPayed == getBusinessObjects().size()
    }

    BigDecimal getNextIntrestAmount(){
        getBusinessObjects().get(alreadyPayed).getIntrest()
    }

    BigDecimal getNextCapitalAmount(){
        getBusinessObjects().get(alreadyPayed).getCapital()
    }

    void raiseNrPayed() {
        alreadyPayed++
    }

    void decreaseNrPayed(){
        alreadyPayed--
    }

    void setAlreadyPayed(int alreadyPayed) {
        this.alreadyPayed = alreadyPayed
    }
}
