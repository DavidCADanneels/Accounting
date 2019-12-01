package be.dafke.Accounting.BasicAccounting.Mortgages

import be.dafke.Accounting.BusinessModel.Calculate
import be.dafke.Accounting.BusinessModel.Mortgage
import be.dafke.Accounting.BusinessModel.Mortgages
import be.dafke.Utils.Utils

import javax.swing.BoxLayout
import javax.swing.ButtonGroup
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JTextField
import java.awt.GridLayout
import java.awt.event.FocusEvent
import java.awt.event.FocusListener

class MortgageCalculatorPanel extends JPanel implements FocusListener {
    private final JTextField amountField, months, yearPercent, monthPercent, mensField, totalIntrestFixed,
                             totalToPayFixed, totalIntrestDegres, totalToPayDegres, totalIntrestDifference, totalToPayDifference
    private final JButton converter, create
    private final JRadioButton fix, degres
    private final Mortgages mortgages

    BigDecimal jaarPercentage = null
    BigDecimal maandPercentage = null
    BigDecimal startKapitaal = null
    BigDecimal mensualiteit = null
    int aantalMaanden = 0

    Mortgage fixedTable, degressiveTable

    private static int counter = 1
    private final int nr

    MortgageCalculatorPanel(Mortgages mortgages) {
        nr = counter++
        this.mortgages = mortgages

        amountField = new JTextField(10)
        months = new JTextField(10)
        yearPercent = new JTextField(10)
        monthPercent = new JTextField(10)
        mensField = new JTextField(10)
        amountField.addFocusListener(this)
        months.addFocusListener(this)
        yearPercent.addFocusListener(this)
        monthPercent.addFocusListener(this)
        mensField.addFocusListener(this)

        converter = new JButton("Bereken mensualiteit")
        converter.addActionListener({ e ->
            berekenMensualiteit()
            activateButtons()
        })
        create = new JButton("Create Table")
        create.addActionListener({ e ->
            createTable()
            activateButtons()
        })

        converter.setEnabled(false)
        create.setEnabled(false)

        fix = new JRadioButton("fixed")
        degres = new JRadioButton("degressive")
        ButtonGroup group = new ButtonGroup()
        group.add(fix)
        group.add(degres)
        fix.setSelected(true)

        totalIntrestDegres = new JTextField(10)
        totalToPayDegres = new JTextField(10)
        totalIntrestFixed = new JTextField(10)
        totalToPayFixed = new JTextField(10)
        totalIntrestDifference = new JTextField(10)
        totalToPayDifference = new JTextField(10)
        totalIntrestDegres.setEditable(false)
        totalToPayDegres.setEditable(false)
        totalIntrestFixed.setEditable(false)
        totalToPayFixed.setEditable(false)
        totalIntrestDifference.setEditable(false)
        totalToPayDifference.setEditable(false)

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
        JPanel line1 = new JPanel()
        line1.add(new JLabel("Amount"))
        line1.add(amountField)
        line1.add(new JLabel("Number of months"))
        line1.add(months)
        JPanel line2 = new JPanel()
        line2.add(new JLabel("Year %"))
        line2.add(yearPercent)
        line2.add(new JLabel("Month %"))
        line2.add(monthPercent)
        JPanel line3 = new JPanel()
        line3.add(converter)
        line3.add(new JLabel("Mensualiteit"))
        line3.add(mensField)
        JPanel line4 = new JPanel()
        line4.add(fix)
        line4.add(degres)
        line4.add(create)
        JPanel overview = new JPanel(new GridLayout(0, 4))
        overview.add(new JLabel("Totals"))
        overview.add(new JLabel("Fixed"))
        overview.add(new JLabel("Degressive"))
        overview.add(new JLabel("Difference"))
        overview.add(new JLabel("Total Intrest:"))
        overview.add(totalIntrestFixed)
        overview.add(totalIntrestDegres)
        overview.add(totalIntrestDifference)
        overview.add(new JLabel("Total to pay:"))
        overview.add(totalToPayFixed)
        overview.add(totalToPayDegres)
        overview.add(totalToPayDifference)

        add(line1)
        add(line2)
        add(line3)
        add(overview)
        add(line4)
    }

    private void activateButtons() {
        if (maandPercentage != null && startKapitaal != null && aantalMaanden != 0) {
            converter.setEnabled(true)
            if (mensualiteit != null) {
                create.setEnabled(true)
            }
        }
    }

    private void berekenMensualiteit() {
        if (maandPercentage != null && startKapitaal != null && aantalMaanden != 0) {
            mensualiteit = Calculate.berekenMensualiteit(startKapitaal, maandPercentage, aantalMaanden)
            mensField.setText(mensualiteit.toString())
        }
        calculateTablesAndTotals()
    }

    private void createTable() {
        if (maandPercentage != null && mensualiteit != null && startKapitaal != null && aantalMaanden != 0) {
            Mortgage newMortgage
            if (fix.isSelected()) {
                newMortgage = Calculate.createFixedAmountTable(startKapitaal, aantalMaanden, mensualiteit, maandPercentage)
            } else {
                newMortgage = Calculate.createDegressiveAmountTable(startKapitaal, aantalMaanden,
                        maandPercentage)
            }
            newMortgage.setName("new Mortgage Table")
            newMortgage.setStartCapital(startKapitaal)
            MortgageTable gui = new MortgageTable(newMortgage, startKapitaal, mortgages)
            Main.addFrame(gui)
            gui.setLocation(getLocationOnScreen())
            gui.setVisible(true)
        }
    }

    private void calculateTablesAndTotals() {
        fixedTable = Calculate.createFixedAmountTable(startKapitaal, aantalMaanden, mensualiteit, maandPercentage)
        degressiveTable = Calculate.createDegressiveAmountTable(startKapitaal, aantalMaanden,
                maandPercentage)
        BigDecimal totalIntrestFixedNr = fixedTable.getTotalIntrest()
        BigDecimal totalIntrestDegresNr = degressiveTable.getTotalIntrest()
        BigDecimal totalToPayFixedNr = fixedTable.getTotalToPay()
        BigDecimal totalToPayDegresNr = degressiveTable.getTotalToPay()
        totalIntrestFixed.setText(totalIntrestFixedNr.toString())
        totalIntrestDegres.setText(totalIntrestDegresNr.toString())
        totalToPayFixed.setText(totalToPayFixedNr.toString())
        totalToPayDegres.setText(totalToPayDegresNr.toString())
        totalIntrestDifference.setText(totalIntrestFixedNr.subtract(totalIntrestDegresNr).toString())
        totalToPayDifference.setText(totalToPayFixedNr.subtract(totalToPayDegresNr).toString())
    }

    void focusGained(FocusEvent arg0) {
    }

    void focusLost(FocusEvent e) {
        if (e.getSource() == amountField) {
            String s = amountField.getText()
            startKapitaal = Utils.parseBigDecimal(s)
            if (startKapitaal == null) {
                amountField.setText("")
            }
        } else if (e.getSource() == months) {
            aantalMaanden = Utils.parseInt(months.getText())
            if (aantalMaanden == 0) {
                months.setText("")
            }
        } else if (e.getSource() == yearPercent) {
            jaarPercentage = Utils.parseBigDecimal(yearPercent.getText())
            if (jaarPercentage == null) {
                yearPercent.setText("")
            } else {
                maandPercentage = Calculate.berekenMaandPercentage(jaarPercentage, 12)
                monthPercent.setText(maandPercentage.toString())
            }
        } else if (e.getSource() == monthPercent) {
            maandPercentage = Utils.parseBigDecimal(monthPercent.getText())
            if (maandPercentage == null) {
                monthPercent.setText("")
            } else {
                jaarPercentage = Calculate.berekenJaarPercentage(maandPercentage, 12)
                yearPercent.setText(jaarPercentage.toString())
            }
        } else if (e.getSource() == mensField) {
            mensualiteit = Utils.parseBigDecimal(mensField.getText())
            if (mensualiteit == null) {
                mensField.setText("")
            }
        }
        activateButtons()
    }

    int getNr() {
        nr
    }
}
