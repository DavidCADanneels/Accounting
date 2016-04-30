package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;

/**
 * User: david
 * Date: 28-12-13
 * Time: 12:02
 */
public interface AccountingPanelInterface {
    void setAccounting(Accounting accounting);
    void refresh();
}
