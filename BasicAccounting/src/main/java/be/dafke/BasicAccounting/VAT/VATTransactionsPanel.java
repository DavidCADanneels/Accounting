package be.dafke.BasicAccounting.VAT;

import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.VATBooking;
import be.dafke.BusinessModel.VATField;
import be.dafke.BusinessModel.VATTransactions;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class VATTransactionsPanel extends JPanel {
    private final SelectableTable<VATBooking> tabel;
    private final VATTransactionsDataModel vatTransactionsDataModel;
    private final VATTransactionsPopupMenu popup;

//    private VATTransactions vatTransactions;

    public VATTransactionsPanel(VATTransactions vatTransactions) {
        //        this.vatTransactions = vatTransactions;
        vatTransactionsDataModel = new VATTransactionsDataModel(vatTransactions);

        tabel = new SelectableTable<>(vatTransactionsDataModel);
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
        VATColorRenderer renderer = new VATColorRenderer();
        tabel.setDefaultRenderer(VATField.class, renderer);
        tabel.setDefaultRenderer(BigDecimal.class, renderer);
        tabel.setDefaultRenderer(String.class, renderer);
        //tabel.setAutoCreateRowSorter(true);
        tabel.setRowSorter(null);
        popup = new VATTransactionsPopupMenu(tabel, vatTransactions.getAccounting());
        tabel.addMouseListener(PopupForTableActivator.getInstance(popup,tabel));
        JScrollPane scrollPane = new JScrollPane(tabel);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateVATTransactions(){
        vatTransactionsDataModel.fireTableDataChanged();
    }

}
