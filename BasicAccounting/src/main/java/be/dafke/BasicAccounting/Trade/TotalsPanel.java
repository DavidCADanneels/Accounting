package be.dafke.BasicAccounting.Trade;

import javax.swing.*;
import java.awt.*;

public class TotalsPanel extends JPanel {
    protected JLabel net0pct, net6pct, net21pct;
    protected JLabel vat0pct, vat6pct, vat21pct;
    protected JLabel total0pct, total6pct, total21pct;
    protected JLabel totalNet, totalVatRest, totalVat, total;

    public TotalsPanel() {
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

    public void reset(){
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
    }
}

