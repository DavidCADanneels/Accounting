package be.dafke.BasicAccounting.Objects;

import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 16:01
 */
public interface AccountingExtension {
    public void extendConstructor(Accounting accounting);

    public void extendReadCollection(Accountings accountings, File xmlFolder);

    public void extendAccountingComponentMap(Accountings accountings);
}
