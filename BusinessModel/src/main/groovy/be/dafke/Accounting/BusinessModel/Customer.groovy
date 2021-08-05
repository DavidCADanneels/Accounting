package be.dafke.Accounting.BusinessModel

class Customer {
    BigDecimal turnOver = BigDecimal.ZERO
    BigDecimal VATTotal = BigDecimal.ZERO
    Account customerAccount = null

    void increaseTurnOver(BigDecimal amount){
        turnOver = turnOver.add(amount)
        turnOver = turnOver.setScale(2)
    }

    void increaseVATTotal(BigDecimal amount){
        VATTotal = VATTotal.add(amount)
        VATTotal = VATTotal.setScale(2)
    }

    void decreaseTurnOver(BigDecimal amount){
        turnOver = turnOver.subtract(amount)
        turnOver = turnOver.setScale(2)
    }

    void decreaseVATTotal(BigDecimal amount){
        VATTotal = VATTotal.subtract(amount)
        VATTotal = VATTotal.setScale(2)
    }

    void setTurnOver(BigDecimal turnOver) {
        this.turnOver = turnOver
    }

    void setVATTotal(BigDecimal VATTotal) {
        this.VATTotal = VATTotal
    }

    void setCustomerAccount(Account customerAccount) {
        this.customerAccount = customerAccount
    }
}
