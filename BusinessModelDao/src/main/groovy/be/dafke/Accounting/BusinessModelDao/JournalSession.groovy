package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.AccountType

class JournalSession {

    boolean showNumbersLeft
    boolean showNumbersRight
    HashMap<AccountType, Boolean> checkedAccountTypesLeft = new HashMap<>()
    HashMap<AccountType, Boolean> checkedAccountTypesRight = new HashMap<>()

    boolean getShowNumbersLeft() {
        return showNumbersLeft
    }

    void setShowNumbersLeft(boolean showNumbersLeft) {
        this.showNumbersLeft = showNumbersLeft
    }

    boolean getShowNumbersRight() {
        return showNumbersRight
    }

    void setShowNumbersRight(boolean showNumbersRight) {
        this.showNumbersRight = showNumbersRight
    }

    void setTypeCheckedLeft(AccountType accountType, boolean available){
        checkedAccountTypesLeft.put(accountType, available)
    }

    void setTypeCheckedRight(AccountType accountType, boolean available){
        checkedAccountTypesRight.put(accountType, available)
    }

    ArrayList<AccountType> getCheckedTypesLeft() {
        ArrayList<AccountType> accountTypes = new ArrayList<>()
        for(Map.Entry<AccountType, Boolean> entry : checkedAccountTypesLeft.entrySet()){
            if(entry.getValue()){
                accountTypes.add(entry.getKey())
            }
        }
        accountTypes
    }

    ArrayList<AccountType> getCheckedTypesRight() {
        ArrayList<AccountType> accountTypes = new ArrayList<>()
        for(Map.Entry<AccountType, Boolean> entry : checkedAccountTypesRight.entrySet()){
            if(entry.getValue()){
                accountTypes.add(entry.getKey())
            }
        }
        accountTypes
    }
}
