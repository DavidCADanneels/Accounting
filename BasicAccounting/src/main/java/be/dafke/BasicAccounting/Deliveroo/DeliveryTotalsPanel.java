package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Journals.JournalSelectorDialog;
import be.dafke.BusinessModel.*;
import be.dafke.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import static be.dafke.BasicAccounting.Deliveroo.DeliverooOrderCreatePanel.DELIVERY_PROFIT_PERCENTAGE;
import static be.dafke.BasicAccounting.Deliveroo.DeliverooOrderCreatePanel.DELIVERY_SERVICE_PERCENTAGE;
import static be.dafke.BasicAccounting.Deliveroo.DeliverooOrderCreatePanel.FOOD_SALES_PERCENTAGE;

public class DeliveryTotalsPanel extends JPanel{
    private JTextField receivedInclVat, receivedVat, receivedExclVat;
    private JTextField serviceInclVat, serviceVat, serviceExclVat;

    private BigDecimal salesAmountInclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);
    private BigDecimal salesAmountExclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);
    private BigDecimal salesAmountVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);

    private BigDecimal serviceAmountExclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);
    private BigDecimal serviceAmountVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);
    private BigDecimal serviceAmountInclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);

    public DeliveryTotalsPanel() {
        receivedExclVat = new JTextField(10);
        receivedInclVat = new JTextField(10);
        receivedVat = new JTextField(10);
        serviceExclVat = new JTextField(10);
        serviceVat = new JTextField(10);
        serviceInclVat = new JTextField(10);

        receivedExclVat.setEnabled(false);
        receivedInclVat.setEnabled(false);
        receivedVat.setEnabled(false);
        serviceExclVat.setEnabled(false);
        serviceVat.setEnabled(false);
        serviceInclVat.setEnabled(false);

        JPanel leftPanel = new JPanel(new GridLayout(0,2));
        JPanel rightPanel = new JPanel(new GridLayout(0,2));

        leftPanel.add(new JLabel("Ontvangsten"));
        leftPanel.add(receivedInclVat);

        leftPanel.add(new JLabel("Excl BTW"));
        leftPanel.add(receivedExclVat);

        leftPanel.add(new JLabel("BTW"));
        leftPanel.add(receivedVat);

        rightPanel.add(new JLabel("Service"));
        rightPanel.add(serviceExclVat);
        rightPanel.add(new JLabel("BTW"));
        rightPanel.add(serviceVat);
        rightPanel.add(new JLabel("Te betalen"));
        rightPanel.add(serviceInclVat);

        add(leftPanel);
        add(rightPanel);
    }

    public void clear() {
        salesAmountInclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        salesAmountExclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        salesAmountVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);

        serviceAmountExclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        serviceAmountVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        serviceAmountInclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);

        receivedExclVat.setText("");
        receivedInclVat.setText("");
        receivedVat.setText("");
        serviceExclVat.setText("");
        serviceVat.setText("");
        serviceInclVat.setText("");
    }

    public void calculateTotals(){
        salesAmountInclVat = salesAmountInclVat.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        salesAmountExclVat = salesAmountInclVat.divide(Utils.getFactor(FOOD_SALES_PERCENTAGE),BigDecimal.ROUND_HALF_DOWN).setScale(2,BigDecimal.ROUND_HALF_DOWN);
        salesAmountVat = salesAmountExclVat.multiply(Utils.getPercentage(FOOD_SALES_PERCENTAGE)).setScale(2,BigDecimal.ROUND_HALF_DOWN);

        serviceAmountExclVat = salesAmountInclVat.multiply(Utils.getPercentage(DELIVERY_PROFIT_PERCENTAGE)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
        serviceAmountVat = serviceAmountExclVat.multiply(Utils.getPercentage(DELIVERY_SERVICE_PERCENTAGE)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
        serviceAmountInclVat = serviceAmountExclVat.add(serviceAmountVat).setScale(2,BigDecimal.ROUND_HALF_DOWN);

        receivedInclVat.setText(salesAmountInclVat.toString());
        receivedExclVat.setText(salesAmountExclVat.toString());
        receivedVat.setText(salesAmountVat.toString());

        serviceInclVat.setText(serviceAmountInclVat.toString());
        serviceExclVat.setText(serviceAmountExclVat.toString());
        serviceVat.setText(serviceAmountVat.toString());
    }

    public void setSalesAmountInclVat(BigDecimal salesAmountInclVat) {
        this.salesAmountInclVat = salesAmountInclVat;
    }

    public BigDecimal getSalesAmountInclVat() {
        return salesAmountInclVat;
    }

    public BigDecimal getSalesAmountExclVat() {
        return salesAmountExclVat;
    }

    public BigDecimal getSalesAmountVat() {
        return salesAmountVat;
    }

    public BigDecimal getServiceAmountExclVat() {
        return serviceAmountExclVat;
    }

    public BigDecimal getServiceAmountVat() {
        return serviceAmountVat;
    }

    public BigDecimal getServiceAmountInclVat() {
        return serviceAmountInclVat;
    }
}
