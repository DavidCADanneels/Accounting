package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class BalanceLine extends BusinessObject {
    private Account leftAccount, rightAccount

    BalanceLine(Account leftAccount, Account rightAccount){
        this.leftAccount = leftAccount
        this.rightAccount = rightAccount
    }

    @Override
    TreeMap<String, String> getUniqueProperties(){
        new TreeMap()
    }

    Account getLeftAccount() {
        leftAccount
    }

    Account getRightAccount() {
        rightAccount
    }
}
