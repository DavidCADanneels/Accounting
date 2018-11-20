package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.Journals.DateAndDescriptionPanel;
import be.dafke.BusinessModel.Transaction;
import be.dafke.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.Calendar;

public class DeliverooPanel extends JPanel {
    public static final int FOOD_SALES_PERCENTAGE = 6;
    public static final int DELIVERY_PROFIT_PERCENTAGE = 27;
    public static final int DELIVERY_SERVICE_PERCENTAGE = 21;
    private JTextField price;
    private JTextField receivedInclVat, receivedVat, receivedExclVat;
    private JTextField serviceInclVat, serviceVat, serviceExclVat;
    private JButton book;
    private DateAndDescriptionPanel dateAndDescriptionPanel;
    private Transaction transaction;

    public DeliverooPanel() {
        transaction = new Transaction(Calendar.getInstance(),"");
        dateAndDescriptionPanel = new DateAndDescriptionPanel();
        dateAndDescriptionPanel.setTransaction(transaction);
        dateAndDescriptionPanel.fireTransactionDataChanged();

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

        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH);
        add(createOverviewPanel(), BorderLayout.CENTER);
    }

    private JPanel createTopPanel(){
        book = new JButton("Book");
        book.addActionListener(e -> book());
        price = new JTextField(10);
        price.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                calculateTotals();
            }
        });
        JPanel panel = new JPanel();
        panel.add(dateAndDescriptionPanel);
        JPanel right = new JPanel(new GridLayout(0,2));
        right.add(new JLabel("Sales Price:"));
        right.add(price);
        right.add(new JLabel(""));
        right.add(book);
        panel.add(right);
        return panel;
    }

    private JPanel createOverviewPanel(){
        JPanel panel = new JPanel();
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

        panel.add(leftPanel);
        panel.add(rightPanel);
        return panel;
    }

    private void book() {

    }
    private void calculateTotals() {
        String text = price.getText();
        BigDecimal salesAmountInclVat = Utils.parseBigDecimal(text);
        Calendar date = dateAndDescriptionPanel.getDate();
        if(salesAmountInclVat!=null && date!=null){
            salesAmountInclVat = salesAmountInclVat.setScale(2,BigDecimal.ROUND_HALF_DOWN);
            price.setText(salesAmountInclVat.toString());

            BigDecimal salesAmountExclVat = salesAmountInclVat.divide(Utils.getFactor(FOOD_SALES_PERCENTAGE),BigDecimal.ROUND_HALF_DOWN).setScale(2,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal salesAmountVat = salesAmountExclVat.multiply(Utils.getPercentage(FOOD_SALES_PERCENTAGE)).setScale(2,BigDecimal.ROUND_HALF_DOWN);

            BigDecimal serviceAmountExclVat = salesAmountInclVat.multiply(Utils.getPercentage(DELIVERY_PROFIT_PERCENTAGE)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal serviceAmountVat = serviceAmountExclVat.multiply(Utils.getPercentage(DELIVERY_SERVICE_PERCENTAGE)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal serviceAmountInclVat = serviceAmountExclVat.add(serviceAmountVat).setScale(2,BigDecimal.ROUND_HALF_DOWN);

            receivedInclVat.setText(salesAmountInclVat.toString());
            receivedExclVat.setText(salesAmountExclVat.toString());
            receivedVat.setText(salesAmountVat.toString());
            serviceInclVat.setText(serviceAmountInclVat.toString());
            serviceExclVat.setText(serviceAmountExclVat.toString());
            serviceVat.setText(serviceAmountVat.toString());
        }
    }
}
