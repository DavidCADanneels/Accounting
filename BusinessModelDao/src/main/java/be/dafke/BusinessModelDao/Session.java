package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;

import java.util.HashMap;

public class Session {
    private static Accountings accountings;
    private static HashMap<Accounting, AccountingSession> accountingSessions = new HashMap<>();
    private static Accounting activeAccounting;

    public static void setAccountings(Accountings accountings) {
        Session.accountings = accountings;
    }

    public static Accountings getAccountings() {
        return accountings;
    }

    public static Accounting getActiveAccounting() {
        return activeAccounting;
    }

    public static void setActiveAccounting(Accounting accounting) {
        activeAccounting = accounting;
    }

    public static void addAccountingSession(Accounting accounting, AccountingSession accountingSession){
        accountingSessions.put(accounting, accountingSession);
    }

    public static AccountingSession getAccountingSession(Accounting accounting){
        return accountingSessions.get(accounting);
    }
}
