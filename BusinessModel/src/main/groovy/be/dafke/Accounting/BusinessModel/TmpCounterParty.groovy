package be.dafke.Accounting.BusinessModel

class TmpCounterParty extends CounterParty {
    /**
     *
     */
    CounterParty counterParty

    void setCounterParty(CounterParty counterParty) {
        this.counterParty = counterParty
    }

    CounterParty getCounterParty() {
        counterParty
    }
}
