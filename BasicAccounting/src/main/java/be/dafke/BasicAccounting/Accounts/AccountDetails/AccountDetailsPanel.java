package be.dafke.BasicAccounting.Accounts.AccountDetails;

import be.dafke.BasicAccounting.Journals.JournalEditPanel;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class AccountDetailsPanel extends JPanel {
    private final AccountDetailsPopupMenu popup;
    private SelectableTable<Booking> table;
    private AccountDetailsDataModel accountDetailsDataModel;

    public AccountDetailsPanel(Account account, Journals journals) {

        setLayout(new BorderLayout());
        accountDetailsDataModel = new AccountDetailsDataModel(account);

        table = new SelectableTable<>(accountDetailsDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        AccountColorRenderer renderer = new AccountColorRenderer();
        table.setDefaultRenderer(String.class, renderer);
        table.setDefaultRenderer(Account.class, renderer);
        table.setDefaultRenderer(BigDecimal.class, renderer);

        popup = new AccountDetailsPopupMenu(journals, table);
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }


    public void selectObject(Booking object){
        int row = accountDetailsDataModel.getRow(object);
        if(table !=null){
            table.setRowSelectionInterval(row, row);
            Rectangle cellRect = table.getCellRect(row, 0, false);
            table.scrollRectToVisible(cellRect);
        }
    }

    public void closePopups(){
        popup.setVisible(false);
    }

    public void fireAccountDataChanged() {
        accountDetailsDataModel.fireTableDataChanged();
    }
}
