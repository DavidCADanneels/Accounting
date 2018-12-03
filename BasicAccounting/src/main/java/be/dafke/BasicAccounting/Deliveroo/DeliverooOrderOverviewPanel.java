package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Journals.DateAndDescriptionPanel;
import be.dafke.BasicAccounting.Journals.JournalSelectorDialog;
import be.dafke.BusinessModel.*;
import be.dafke.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

public class DeliverooOrderOverviewPanel extends JPanel {
    private Accounting accounting;

    private JTable table;
    private DeliverooOrdersOverviewDataTableModel tableModel;
    private DeliveryTotalsPanel totalsPanel;

    public DeliverooOrderOverviewPanel(Accounting accounting) {
        this.accounting = accounting;

        totalsPanel = new DeliveryTotalsPanel();

        setLayout(new BorderLayout());
        add(totalsPanel, BorderLayout.SOUTH);
        add(createOrderPanel(), BorderLayout.CENTER);
        clear();
    }

    private JScrollPane createOrderPanel() {
        tableModel = new DeliverooOrdersOverviewDataTableModel(accounting.getMealOrders());
        table = new JTable(tableModel);
        return new JScrollPane(table);
    }

    private void clear() {
        totalsPanel.clear();
    }
}
