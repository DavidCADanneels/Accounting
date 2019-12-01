package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Accountings

class Session {
    private static Accountings accountings
    private static HashMap<Accounting, AccountingSession> accountingSessions = new HashMap<>()
    private static Accounting activeAccounting

    static void setAccountings(Accountings accountings) {
        Session.accountings = accountings
    }

    static Accountings getAccountings() {
        accountings
    }

    static Accounting getActiveAccounting() {
        activeAccounting
    }

    static void setActiveAccounting(Accounting accounting) {
        activeAccounting = accounting
    }

    static void addAccountingSession(Accounting accounting, AccountingSession accountingSession){
        accountingSessions.put(accounting, accountingSession)
    }

    static AccountingSession getAccountingSession(Accounting accounting){
        accountingSessions.get(accounting)
    }
}
