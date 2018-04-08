package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.SalesOrder;

import javax.swing.*;
import java.awt.*;

public class SaleTotalsPanel extends JPanel{
    private JTextField net0pct, net6pct, net21pct;
    private JTextField vat0pct, vat6pct, vat21pct;
    private JTextField total0pct, total6pct, total21pct;
    private JTextField totalNet, totalVat, total;

    public SaleTotalsPanel() {
        setLayout(new GridLayout(0,4));

        net0pct = new JTextField("0.00",10);
        net6pct = new JTextField("0.00",10);
        net21pct = new JTextField("0.00",10);

        vat0pct = new JTextField("0.00",10);
        vat6pct = new JTextField("0.00",10);
        vat21pct = new JTextField("0.00",10);

        total0pct = new JTextField("0.00",10);
        total6pct = new JTextField("0.00",10);
        total21pct = new JTextField("0.00",10);

        totalNet = new JTextField("0.00",10);
        totalVat = new JTextField("0.00",10);
        total = new JTextField("0.00",10);

        net0pct.setEditable(false);
        net6pct.setEditable(false);
        net21pct.setEditable(false);

        vat0pct.setEditable(false);
        vat6pct.setEditable(false);
        vat21pct.setEditable(false);

        add(new JLabel("Totals (excl. VAT)"));
        add(new JLabel("VAT Rate"));
        add(new JLabel("VAT Amount"));
        add(new JLabel("Totals (incl. VAT)"));

        add(net0pct);
        add(new JLabel("0 %"));
        add(vat0pct);
        add(total0pct);

        add(net6pct);
        add(new JLabel("6 %"));
        add(vat6pct);
        add(total6pct);

        add(net21pct);
        add(new JLabel("21 %"));
        add(vat21pct);
        add(total21pct);

        add(totalNet);
        add(new JLabel(""));
        add(totalVat);
        add(total);
    }

    public void fireOrderContentChanged(SalesOrder order){
        net0pct.setText(order==null?"":order.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(0)).toString());
        net6pct.setText(order==null?"":order.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(6)).toString());
        net21pct.setText(order==null?"":order.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(21)).toString());

        vat6pct.setText(order==null?"":order.getTotalSalesVat(OrderItem.withSalesVatRate(6)).toString());
        vat21pct.setText(order==null?"":order.getTotalSalesVat(OrderItem.withSalesVatRate(21)).toString());

        total0pct.setText(order==null?"":order.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(0)).toString());
        total6pct.setText(order==null?"":order.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(6)).toString());
        total21pct.setText(order==null?"":order.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(21)).toString());

        totalNet.setText(order==null?"":order.getTotalSalesPriceExclVat().toString());
        totalVat.setText(order==null?"":order.getTotalSalesVat().toString());
        total.setText(order==null?"":order.getTotalSalesPriceInclVat().toString());
    }
}
