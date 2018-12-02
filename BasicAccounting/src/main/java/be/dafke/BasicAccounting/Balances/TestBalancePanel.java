package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Journals.JournalEditPanel;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class TestBalancePanel extends JPanel{

    private final JPopupMenu popup;
    private SelectableTable<Account> tabel;
    private TestBalanceDataModel testBalanceDataModel;

    public TestBalancePanel(Accounting accounting, Accounts accounts) {
        setLayout(new BorderLayout());

        testBalanceDataModel = new TestBalanceDataModel(accounts);

        tabel = new SelectableTable<>(testBalanceDataModel);
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
//        tabel.setRowSorter(null);
        JScrollPane scrollPane = new JScrollPane(tabel);

        popup = new BalancePopupMenu(accounting, tabel);
        tabel.addMouseListener(PopupForTableActivator.getInstance(popup,tabel));

        add(scrollPane, BorderLayout.CENTER);
    }

    public void fireAccountDataChanged() {
        testBalanceDataModel.fireTableDataChanged();
    }
}
