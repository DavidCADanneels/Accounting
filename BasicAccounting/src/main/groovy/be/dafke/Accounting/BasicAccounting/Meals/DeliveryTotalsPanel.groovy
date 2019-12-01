package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Utils.Utils

import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import java.awt.GridLayout

import static be.dafke.Accounting.BasicAccounting.Meals.MealOrderCreatePanel.DELIVERY_PROFIT_PERCENTAGE;
import static be.dafke.Accounting.BasicAccounting.Meals.MealOrderCreatePanel.DELIVERY_SERVICE_PERCENTAGE;
import static be.dafke.Accounting.BasicAccounting.Meals.MealOrderCreatePanel.FOOD_SALES_PERCENTAGE;

class DeliveryTotalsPanel extends JPanel{
    JTextField receivedInclVat, receivedVat, receivedExclVat
    JTextField serviceInclVat, serviceVat, serviceExclVat

    BigDecimal salesAmountInclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN)
    BigDecimal salesAmountExclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN)
    BigDecimal salesAmountVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN)

    BigDecimal serviceAmountExclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN)
    BigDecimal serviceAmountVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN)
    BigDecimal serviceAmountInclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN)

    DeliveryTotalsPanel() {
        receivedExclVat = new JTextField(10)
        receivedInclVat = new JTextField(10)
        receivedVat = new JTextField(10)
        serviceExclVat = new JTextField(10)
        serviceVat = new JTextField(10)
        serviceInclVat = new JTextField(10)

        receivedExclVat.enabled = false
        receivedInclVat.enabled = false
        receivedVat.enabled = false
        serviceExclVat.enabled = false
        serviceVat.enabled = false
        serviceInclVat.enabled = false

        JPanel leftPanel = new JPanel(new GridLayout(0,2))
        JPanel rightPanel = new JPanel(new GridLayout(0,2))

        leftPanel.add(new JLabel("Ontvangsten"))
        leftPanel.add(receivedInclVat)

        leftPanel.add(new JLabel("Excl BTW"))
        leftPanel.add(receivedExclVat)

        leftPanel.add(new JLabel("BTW"))
        leftPanel.add(receivedVat)

        rightPanel.add(new JLabel("Service"))
        rightPanel.add(serviceExclVat)
        rightPanel.add(new JLabel("BTW"))
        rightPanel.add(serviceVat)
        rightPanel.add(new JLabel("Te betalen"))
        rightPanel.add(serviceInclVat)

        add(leftPanel)
        add(rightPanel)
    }

    void clear() {
        salesAmountInclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN)
        salesAmountExclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN)
        salesAmountVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN)

        serviceAmountExclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN)
        serviceAmountVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN)
        serviceAmountInclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN)

        receivedExclVat.setText("")
        receivedInclVat.setText("")
        receivedVat.setText("")
        serviceExclVat.setText("")
        serviceVat.setText("")
        serviceInclVat.setText("")
    }

    void calculateTotals(){
        salesAmountInclVat = salesAmountInclVat.setScale(2,BigDecimal.ROUND_HALF_DOWN)
        salesAmountExclVat = salesAmountInclVat.divide(Utils.getFactor(FOOD_SALES_PERCENTAGE),BigDecimal.ROUND_HALF_DOWN).setScale(2,BigDecimal.ROUND_HALF_DOWN)
        salesAmountVat = salesAmountExclVat.multiply(Utils.getPercentage(FOOD_SALES_PERCENTAGE)).setScale(2,BigDecimal.ROUND_HALF_DOWN)

        serviceAmountExclVat = salesAmountInclVat.multiply(Utils.getPercentage(DELIVERY_PROFIT_PERCENTAGE)).setScale(2,BigDecimal.ROUND_HALF_DOWN)
        serviceAmountVat = serviceAmountExclVat.multiply(Utils.getPercentage(DELIVERY_SERVICE_PERCENTAGE)).setScale(2,BigDecimal.ROUND_HALF_DOWN)
        serviceAmountInclVat = serviceAmountExclVat.add(serviceAmountVat).setScale(2,BigDecimal.ROUND_HALF_DOWN)

        receivedInclVat.setText(salesAmountInclVat.toString())
        receivedExclVat.setText(salesAmountExclVat.toString())
        receivedVat.setText(salesAmountVat.toString())

        serviceInclVat.setText(serviceAmountInclVat.toString())
        serviceExclVat.setText(serviceAmountExclVat.toString())
        serviceVat.setText(serviceAmountVat.toString())
    }

    void setSalesAmountInclVat(BigDecimal salesAmountInclVat) {
        this.salesAmountInclVat = salesAmountInclVat
    }

    BigDecimal getSalesAmountInclVat() {
        salesAmountInclVat
    }

    BigDecimal getSalesAmountExclVat() {
        salesAmountExclVat
    }

    BigDecimal getSalesAmountVat() {
        salesAmountVat
    }

    BigDecimal getServiceAmountExclVat() {
        serviceAmountExclVat
    }

    BigDecimal getServiceAmountVat() {
        serviceAmountVat
    }

    BigDecimal getServiceAmountInclVat() {
        serviceAmountInclVat
    }
}
