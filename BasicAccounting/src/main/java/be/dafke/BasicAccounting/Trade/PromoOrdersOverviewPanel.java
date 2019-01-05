package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.PromoOrder;
import be.dafke.BusinessModel.SalesOrder;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class PromoOrdersOverviewPanel extends JPanel {
    private final SelectableTable<PromoOrder> overviewTable;
    private final SelectableTable<OrderItem> detailsTable;
    private final PromoOrdersOverviewDataTableModel overviewTableModel;

    private final PromoOrderDetailsDataTableModel detailsTableModel;
    private final TotalsPanel totalsPanel;

    private final PromoOrderDetailPanel salesOrderDetailPanel;
    private final SalesOrderDetailsPopupMenu popup;

    public PromoOrdersOverviewPanel() {
        overviewTableModel = new PromoOrdersOverviewDataTableModel();
        overviewTable = new SelectableTable<>(overviewTableModel);
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        detailsTableModel = new PromoOrderDetailsDataTableModel();
        detailsTable = new SelectableTable<>(detailsTableModel);
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200));
        //
        popup = new SalesOrderDetailsPopupMenu(detailsTable);
        detailsTable.addMouseListener(PopupForTableActivator.getInstance(popup,detailsTable));

        totalsPanel = new TotalsPanel();
        salesOrderDetailPanel = new PromoOrderDetailPanel();

        firePromoOrderAddedOrRemoved();

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                PromoOrder promoOrder = overviewTable.getSelectedObject();
                detailsTableModel.setOrder(promoOrder);
                totalsPanel.fireOrderContentChanged(promoOrder);
                salesOrderDetailPanel.setOrder(promoOrder);
            }
        });
        overviewTable.setSelectionModel(selection);

        JScrollPane overviewScroll = new JScrollPane(overviewTable);
        JScrollPane detailScroll = new JScrollPane(detailsTable);
        JSplitPane splitPane = Main.createSplitPane(overviewScroll, detailScroll, JSplitPane.VERTICAL_SPLIT);

        JPanel center = new JPanel(new BorderLayout());
        center.add(splitPane, BorderLayout.CENTER);
        center.add(totalsPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(center, BorderLayout.CENTER);
        add(salesOrderDetailPanel, BorderLayout.EAST);
    }

    public void firePromoOrderAddedOrRemoved() {
        overviewTableModel.fireTableDataChanged();
    }

    public void setAccounting(Accounting accounting) {
        overviewTableModel.setAccounting(accounting);
        popup.setAccounting(accounting);
        salesOrderDetailPanel.setAccounting(accounting);
    }
}