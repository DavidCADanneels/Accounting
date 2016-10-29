package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Calculate;
import be.dafke.BusinessModel.Mortgage;
import be.dafke.BusinessModel.Mortgages;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;

public class MortgageCalculatorGUI extends RefreshableFrame implements ActionListener, FocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String MORTGAGE_TABLE = "MortgageTable";
	private final JTextField amountField, months, yearPercent, monthPercent, mensField, totalIntrestFixed,
			totalToPayFixed, totalIntrestDegres, totalToPayDegres, totalIntrestDifference, totalToPayDifference;
	private final JButton converter, create;
	private final JRadioButton fix, degres;
    private final Mortgages mortgages;

    BigDecimal jaarPercentage = null;
	BigDecimal maandPercentage = null;
	BigDecimal startKapitaal = null;
	BigDecimal mensualiteit = null;
	int aantalMaanden = 0;

	Mortgage fixedTable, degressiveTable;

	private final Accounting accounting;

    private static int counter = 1;
    private final int nr;

	public MortgageCalculatorGUI(Accounting accounting, Mortgages mortgages) {
		super("Mortgage Calculator");
        nr = counter++;
		this.accounting = accounting;
        this.mortgages = mortgages;

		amountField = new JTextField(10);
		months = new JTextField(10);
		yearPercent = new JTextField(10);
		monthPercent = new JTextField(10);
		mensField = new JTextField(10);
		amountField.addFocusListener(this);
		months.addFocusListener(this);
		yearPercent.addFocusListener(this);
		monthPercent.addFocusListener(this);
		mensField.addFocusListener(this);

		converter = new JButton("Bereken mensualiteit");
		converter.addActionListener(this);
		create = new JButton("Create Table");
		create.addActionListener(this);

		converter.setEnabled(false);
		create.setEnabled(false);

		fix = new JRadioButton("fixed");
		degres = new JRadioButton("degressive");
		ButtonGroup group = new ButtonGroup();
		group.add(fix);
		group.add(degres);
		fix.setSelected(true);

		totalIntrestDegres = new JTextField(10);
		totalToPayDegres = new JTextField(10);
		totalIntrestFixed = new JTextField(10);
		totalToPayFixed = new JTextField(10);
		totalIntrestDifference = new JTextField(10);
		totalToPayDifference = new JTextField(10);
		totalIntrestDegres.setEditable(false);
		totalToPayDegres.setEditable(false);
		totalIntrestFixed.setEditable(false);
		totalToPayFixed.setEditable(false);
		totalIntrestDifference.setEditable(false);
		totalToPayDifference.setEditable(false);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JPanel line1 = new JPanel();
		line1.add(new JLabel("Amount"));
		line1.add(amountField);
		line1.add(new JLabel("Number of months"));
		line1.add(months);
		JPanel line2 = new JPanel();
		line2.add(new JLabel("Year %"));
		line2.add(yearPercent);
		line2.add(new JLabel("Month %"));
		line2.add(monthPercent);
		JPanel line3 = new JPanel();
		line3.add(converter);
		line3.add(new JLabel("Mensualiteit"));
		line3.add(mensField);
		JPanel line4 = new JPanel();
		line4.add(fix);
		line4.add(degres);
		line4.add(create);
		JPanel overview = new JPanel(new GridLayout(0, 4));
		overview.add(new JLabel("Totals"));
		overview.add(new JLabel("Fixed"));
		overview.add(new JLabel("Degressive"));
		overview.add(new JLabel("Difference"));
		overview.add(new JLabel("Total Intrest:"));
		overview.add(totalIntrestFixed);
		overview.add(totalIntrestDegres);
		overview.add(totalIntrestDifference);
		overview.add(new JLabel("Total to pay:"));
		overview.add(totalToPayFixed);
		overview.add(totalToPayDegres);
		overview.add(totalToPayDifference);

		panel.add(line1);
		panel.add(line2);
		panel.add(line3);
		panel.add(overview);
		panel.add(line4);

		setContentPane(panel);
		pack();
	}

	public void refresh() {
        // nothing to do
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == converter) {
			berekenMensualiteit();
		} else if (e.getSource() == create) {
			createTable();
		}
		activateButtons();
	}

	private void activateButtons() {
		if (maandPercentage != null && startKapitaal != null && aantalMaanden != 0) {
			converter.setEnabled(true);
			if (mensualiteit != null) {
				create.setEnabled(true);
			}
		}
	}

	private void berekenMensualiteit() {
		if (maandPercentage != null && startKapitaal != null && aantalMaanden != 0) {
			mensualiteit = Calculate.berekenMensualiteit(startKapitaal, maandPercentage, aantalMaanden);
			mensField.setText(mensualiteit.toString());
		}
		calculateTablesAndTotals();
	}

	private void createTable() {
		if (maandPercentage != null && mensualiteit != null && startKapitaal != null && aantalMaanden != 0) {
			Mortgage newMortgage;
			if (fix.isSelected()) {
				newMortgage = Calculate.createFixedAmountTable(startKapitaal, aantalMaanden, mensualiteit, maandPercentage);
			} else {
				newMortgage = Calculate.createDegressiveAmountTable(startKapitaal, aantalMaanden,
						maandPercentage);
			}
            newMortgage.setName("new Mortgage Table");
            newMortgage.setStartCapital(startKapitaal);
			MortgageTable gui = new MortgageTable(newMortgage, startKapitaal, accounting, mortgages);
            ComponentMap.addDisposableComponent(MORTGAGE_TABLE + gui.nr, gui);
			gui.setVisible(true);
		}
	}

	private void calculateTablesAndTotals() {
		fixedTable = Calculate.createFixedAmountTable(startKapitaal, aantalMaanden, mensualiteit, maandPercentage);
		degressiveTable = Calculate.createDegressiveAmountTable(startKapitaal, aantalMaanden,
				maandPercentage);
		BigDecimal totalIntrestFixedNr = fixedTable.getTotalIntrest();
		BigDecimal totalIntrestDegresNr = degressiveTable.getTotalIntrest();
		BigDecimal totalToPayFixedNr = fixedTable.getTotalToPay();
		BigDecimal totalToPayDegresNr = degressiveTable.getTotalToPay();
		totalIntrestFixed.setText(totalIntrestFixedNr.toString());
		totalIntrestDegres.setText(totalIntrestDegresNr.toString());
		totalToPayFixed.setText(totalToPayFixedNr.toString());
		totalToPayDegres.setText(totalToPayDegresNr.toString());
		totalIntrestDifference.setText(totalIntrestFixedNr.subtract(totalIntrestDegresNr).toString());
		totalToPayDifference.setText(totalToPayFixedNr.subtract(totalToPayDegresNr).toString());
	}

	public void focusGained(FocusEvent arg0) {
	}

	public void focusLost(FocusEvent e) {
		if (e.getSource() == amountField) {
			String s = amountField.getText();
			startKapitaal = Utils.parseBigDecimal(s);
			if (startKapitaal == null) {
				amountField.setText("");
			}
		} else if (e.getSource() == months) {
			aantalMaanden = Utils.parseInt(months.getText());
			if (aantalMaanden == 0) {
				months.setText("");
			}
		} else if (e.getSource() == yearPercent) {
			jaarPercentage = Utils.parseBigDecimal(yearPercent.getText());
			if (jaarPercentage == null) {
				yearPercent.setText("");
			} else {
				maandPercentage = Calculate.berekenMaandPercentage(jaarPercentage, 12);
				monthPercent.setText(maandPercentage.toString());
			}
		} else if (e.getSource() == monthPercent) {
			maandPercentage = Utils.parseBigDecimal(monthPercent.getText());
			if (maandPercentage == null) {
				monthPercent.setText("");
			} else {
				jaarPercentage = Calculate.berekenJaarPercentage(maandPercentage, 12);
				yearPercent.setText(jaarPercentage.toString());
			}
		} else if (e.getSource() == mensField) {
			mensualiteit = Utils.parseBigDecimal(mensField.getText());
			if (mensualiteit == null) {
				mensField.setText("");
			}
		}
		activateButtons();
	}

    public int getNr() {
        return nr;
    }
}
