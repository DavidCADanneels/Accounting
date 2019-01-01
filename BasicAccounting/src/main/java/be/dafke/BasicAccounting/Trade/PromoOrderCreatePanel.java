package be.dafke.BasicAccounting.Trade;


import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;

class PromoOrderCreatePanel extends JPanel {
    private PromoOrder promoOrder;
    private Articles articles;
    private final SalesOrderCreateDataTableModel salesOrderCreateDataTableModel;

    PromoOrderCreatePanel(Accounting accounting) {
        this.articles = accounting.getArticles();
        promoOrder = new PromoOrder();
        promoOrder.setArticles(articles);

        TotalsPanel totalsPanel = new TotalsPanel();
        salesOrderCreateDataTableModel = new SalesOrderCreateDataTableModel(articles, promoOrder, totalsPanel);
        SelectableTable<OrderItem> table = new SelectableTable<>(salesOrderCreateDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));


        JPanel north = new JPanel();

        JButton orderButton = new JButton("Add Promo Order");
        orderButton.addActionListener(e -> {
            PromoOrders promoOrders = accounting.getPromoOrders();

            try {
                promoOrder.removeEmptyOrderItems();

                promoOrders.addBusinessObject(promoOrder);
                promoOrder = new PromoOrder();
                promoOrder.setArticles(articles);
                salesOrderCreateDataTableModel.setOrder(promoOrder);
                totalsPanel.fireOrderContentChanged(promoOrder);
                PromoOrdersOverviewGUI.firePromoOrderAddedOrRemovedForAll();
            } catch (EmptyNameException e1) {
                e1.printStackTrace();
            } catch (DuplicateNameException e1) {
                e1.printStackTrace();
            }
        });
        JPanel south = new JPanel(new BorderLayout());
        south.add(orderButton, BorderLayout.SOUTH);
        south.add(totalsPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());

        add(scrollPane, BorderLayout.CENTER);
        add(north, BorderLayout.NORTH);
        add(south, BorderLayout.SOUTH);
    }
}