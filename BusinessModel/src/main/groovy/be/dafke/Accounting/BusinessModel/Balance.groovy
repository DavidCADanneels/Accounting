package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection

class Balance extends BusinessCollection<BalanceLine> {
    String leftName=""
    String rightName=""
    String leftTotalName=""
    String rightTotalName=""
    String leftResultName=""
    String rightResultName=""
    ArrayList<AccountType> leftTypes = new ArrayList()
    ArrayList<AccountType> rightTypes = new ArrayList()
    Accounts accounts


    Balance(String name, Accounts accounts) {
        setName(name)
        this.accounts = accounts
    }
    @Override
    boolean isDeletable(){
        !(getName().equals(Balances.YEAR_BALANCE) || getName().equals(Balances.RESULT_BALANCE) || getName().equals(Balances.RELATIONS_BALANCE))
    }

    String getLeftName() {
        leftName
    }

    String getRightName() {
        rightName
    }

    ArrayList<AccountType> getLeftTypes() {
        leftTypes
    }

    ArrayList<AccountType> getRightTypes() {
        rightTypes
    }

    ArrayList<Account> getLeftAccounts(boolean includeEmpty) {
        if(includeEmpty) getAccountsByType(leftTypes)
        else getAccountsNotEmpty(leftTypes)
    }
    ArrayList<Account> getLeftAccounts() {
        getAccountsNotEmpty(leftTypes)
    }

    ArrayList<Account> getRightAccounts(boolean includeEmpty) {
        if(includeEmpty) getAccountsByType(rightTypes)
        else getAccountsNotEmpty(rightTypes)
    }
    ArrayList<Account> getRightAccounts() {
        getAccountsNotEmpty(rightTypes)
    }

    ArrayList<Account> getAccountsNotEmpty(ArrayList<AccountType> types) {
        ArrayList<Account> col = new ArrayList()
        for(AccountType type : types) {
            col.addAll(getAccountsNotEmpty(type))
        }
        col
    }

    BigDecimal getTotalLeft(){
        ArrayList<Account> accounts = getAccountsByType(leftTypes)
        BigDecimal total = BigDecimal.ZERO
        for (Account account:accounts) {
            total = total.add(account.saldo).setScale(2)
        }
        total
    }

    BigDecimal getTotalRight(){
        ArrayList<Account> accounts = getAccountsByType(rightTypes)
        BigDecimal total = BigDecimal.ZERO
        for (Account account:accounts) {
            total = total.add(account.saldo).setScale(2)
        }
        total.negate()

    }

    ArrayList<Account> getAccountsByType(ArrayList<AccountType> types) {
        ArrayList<Account> col = new ArrayList()
        for(AccountType type : types) {
            col.addAll(getAccountsByType(type))
        }
        col
    }

    ArrayList<Account> getAccountsByType(AccountType type) {
        accounts.getAccountsByType(type)
    }

    ArrayList<Account> getAccountsNotEmpty(AccountType type) {
        ArrayList<Account> col = new ArrayList()
        for(Account account : accounts.businessObjects) {
            if (account.type == type && account.saldo.compareTo(BigDecimal.ZERO) != 0) col.add(account)
        }
        col
    }

    @Override
    ArrayList<BalanceLine> getBusinessObjects(){
        ArrayList<Account> leftAccounts = getLeftAccounts()
        ArrayList<Account> rightAccounts = getRightAccounts()

        int nrLeft = leftAccounts.size()
        int nrRight = rightAccounts.size()
        int min,max
        if (nrLeft > nrRight) {
            max = nrLeft
            min = nrRight
        } else {
            max = nrRight
            min = nrLeft
        }
        ArrayList<BalanceLine> balanceLines = new ArrayList()
        for(int i = 0; i < min; i++) {
            Account leftAccount = leftAccounts.get(i)
            Account rightAccount = rightAccounts.get(i)
            balanceLines.add(new BalanceLine(leftAccount,rightAccount))
        }
        for(int i = min; i < max; i++) {
            if(nrLeft > nrRight) {
                Account leftAccount = leftAccounts.get(i)
                balanceLines.add(new BalanceLine(leftAccount,null))
            } else {
                Account rightAccount = rightAccounts.get(i)
                balanceLines.add(new BalanceLine(null,rightAccount))
            }
        }
        balanceLines
    }

    String getLeftTotalName() {
        leftTotalName
    }

    String getRightTotalName() {
        rightTotalName
    }

    String getLeftResultName() {
        leftResultName
    }

    String getRightResultName() {
        rightResultName
    }

    void setLeftName(String leftName) {
        this.leftName = leftName
    }

    void setRightName(String rightName) {
        this.rightName = rightName
    }

    void setLeftTotalName(String leftTotalName) {
        this.leftTotalName = leftTotalName
    }

    void setRightTotalName(String rightTotalName) {
        this.rightTotalName = rightTotalName
    }

    void setLeftResultName(String leftResultName) {
        this.leftResultName = leftResultName
    }

    void setRightResultName(String rightResultName) {
        this.rightResultName = rightResultName
    }

    void setLeftTypes(ArrayList<AccountType> leftTypes) {
        this.leftTypes = leftTypes
    }

    void setRightTypes(ArrayList<AccountType> rightTypes) {
        this.rightTypes = rightTypes
    }

    void addLeftType(AccountType type) {
        leftTypes.add(type)
    }

    void addRightType(AccountType type) {
        rightTypes.add(type)
    }

    void removeLeftType(AccountType type) {
        leftTypes.remove(type)
    }

    void removeRightType(AccountType type) {
        rightTypes.remove(type)
    }
}
