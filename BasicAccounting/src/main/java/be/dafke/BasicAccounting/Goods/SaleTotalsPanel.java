package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.SalesOrder;

import javax.swing.*;
import java.awt.*;

public class SaleTotalsPanel extends JPanel{
    private JLabel net0pct, net6pct, net21pct;
    private JLabel vat0pct, vat6pct, vat21pct;
    private JLabel total0pct, total6pct, total21pct;
    private JLabel totalNet, totalVatRest, totalVat, total;

    public SaleTotalsPanel() {
        setLayout(new GridLayout(0,4));

        net0pct = new JLabel("0.00", 10);
        net6pct = new JLabel("0.00",10);
        net21pct = new JLabel("0.00",10);

        vat0pct = new JLabel("0.00",10);
        vat6pct = new JLabel("0.00",10);
        vat21pct = new JLabel("0.00",10);

        total0pct = new JLabel("0.00",10);
        total6pct = new JLabel("0.00",10);
        total21pct = new JLabel("0.00",10);

        totalNet = new JLabel("0.00",10);
        totalVat = new JLabel("0.00",10);
        total = new JLabel("0.00",10);

        totalVatRest = new JLabel("(0.00)", 10);

//        net0pct.setEditable(false);
//        net6pct.setEditable(false);
//        net21pct.setEditable(false);
//
//        vat0pct.setEditable(false);
//        vat6pct.setEditable(false);
//        vat21pct.setEditable(false);
//        
//        total0pct.setEditable(false);
//        total6pct.setEditable(false);
//        total21pct.setEditable(false);
//
//        totalNet.setEditable(false);
//        totalVat.setEditable(false);
//        total.setEditable(false);

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
        add(totalVatRest);
        add(totalVat);
        add(total);
    }

    public void fireOrderContentChanged(SalesOrder order){
        if(order==null){
            net0pct.setText("0.00");
            net6pct.setText("0.00");
            net21pct.setText("0.00");

            vat6pct.setText("0.00");
            vat21pct.setText("0.00");

            total0pct.setText("0.00");
            total6pct.setText("0.00");
            total21pct.setText("0.00");

            totalNet.setText("0.00");
            totalVatRest.setText("(0.00)");
            totalVat.setText("0.00");
            total.setText("0.00");
        } else {
            net0pct.setText(order.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(0)).toString());
            net6pct.setText(order.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(6)).toString());
            net21pct.setText(order.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(21)).toString());

            vat6pct.setText(order.getTotalSalesVat(OrderItem.withSalesVatRate(6)).toString());
            vat21pct.setText(order.getTotalSalesVat(OrderItem.withSalesVatRate(21)).toString());

            total0pct.setText(order.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(0)).toString());
            total6pct.setText(order.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(6)).toString());
            total21pct.setText(order.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(21)).toString());

            totalNet.setText(order.getTotalSalesPriceExclVat().toString());
            totalVatRest.setText("("+order.calculateTotalSalesVat().toString()+")");
            totalVat.setText(order.getTotalSalesVat().toString());
            total.setText(order.getTotalSalesPriceInclVat().toString());
        }
    }
}
