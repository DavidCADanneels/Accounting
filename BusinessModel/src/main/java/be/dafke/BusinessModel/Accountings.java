package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

public class Accountings extends BusinessCollection<Accounting> {

    private static Accounting activeAccounting = null;

    public static Accounting getActiveAccounting() {
        return activeAccounting;
    }

    public static void setActiveAccounting(Accounting activeAccounting) {
        Accountings.activeAccounting = activeAccounting;
    }
}