package be.dafke.BusinessModelDao;

import be.dafke.Accounting.BusinessModel.AccountType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JournalSession {

    private HashMap<AccountType, Boolean> checkedAccountTypesLeft = new HashMap<>();
    private HashMap<AccountType, Boolean> checkedAccountTypesRight = new HashMap<>();

    public JournalSession() {
    }

    public void setTypeCheckedLeft(AccountType accountType, boolean available){
        checkedAccountTypesLeft.put(accountType, available);
    }

    public void setTypeCheckedRight(AccountType accountType, boolean available){
        checkedAccountTypesRight.put(accountType, available);
    }

    public ArrayList<AccountType> getCheckedTypesLeft() {
        ArrayList<AccountType> accountTypes = new ArrayList<>();
        for(Map.Entry<AccountType, Boolean> entry : checkedAccountTypesLeft.entrySet()){
            if(entry.getValue()){
                accountTypes.add(entry.getKey());
            }
        }
        return accountTypes;
    }

    public ArrayList<AccountType> getCheckedTypesRight() {
        ArrayList<AccountType> accountTypes = new ArrayList<>();
        for(Map.Entry<AccountType, Boolean> entry : checkedAccountTypesRight.entrySet()){
            if(entry.getValue()){
                accountTypes.add(entry.getKey());
            }
        }
        return accountTypes;
    }
}
