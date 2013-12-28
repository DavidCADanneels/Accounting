package be.dafke.BasicAccounting.GUI;

import be.dafke.BasicAccounting.Objects.Accounting;

import javax.swing.*;

/**
 * User: david
 * Date: 28-12-13
 * Time: 12:02
 */
public abstract class AccountingPanel extends JPanel {
    public abstract void setAccounting(Accounting accounting);
    public abstract void refresh();
}
