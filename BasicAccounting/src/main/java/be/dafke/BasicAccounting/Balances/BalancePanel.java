package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Journals.JournalEditPanel;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Balance;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class BalancePanel extends JPanel {

    private final JPopupMenu popup;
    private SelectableTable<Account> tabel;
    private BalanceDataModel balanceDataModel;

    public BalancePanel(Journals journals, Balance balance, JournalEditPanel journalEditPanel) {
        setLayout(new BorderLayout());

        balanceDataModel = new BalanceDataModel(balance);

        tabel = new SelectableTable<>(balanceDataModel);
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
        //tabel.setAutoCreateRowSorter(true);
        tabel.setRowSorter(null);
        JScrollPane scrollPane = new JScrollPane(tabel);

        popup = new BalancePopupMenu(journals, tabel, journalEditPanel);
        tabel.addMouseListener(PopupForTableActivator.getInstance(popup,tabel));
        tabel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void fireAccountDataChanged() {
        balanceDataModel.fireTableDataChanged();
    }
}
