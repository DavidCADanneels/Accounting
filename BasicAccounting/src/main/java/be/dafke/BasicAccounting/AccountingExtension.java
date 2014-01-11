package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Objects.Accounting;

import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 16:01
 */
public interface AccountingExtension {
    public void extendConstructor(Accounting accounting);

    public void extendReadCollection(Accounting accounting, File xmlFolder);

    public void extendAccountingComponentMap(Accounting accounting);

    public void extendWriteCollection(Accounting accounting, File xmlFolder);
}
