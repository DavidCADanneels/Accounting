package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Accountings

class Session {
    public static Accountings accountings
    static HashMap<Accounting, AccountingSession> accountingSessions = new HashMap<>()
    static Accounting activeAccounting


    static void setActiveAccounting(Accounting accounting) {
        activeAccounting = accounting
    }

    static void addAccountingSession(Accounting accounting, AccountingSession accountingSession){
        accountingSessions.put(accounting, accountingSession)
    }

    static AccountingSession getAccountingSession(Accounting accounting){
        accounting?accountingSessions.get(accounting):null
    }
}
