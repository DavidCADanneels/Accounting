package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Mortgages;

import javax.swing.*;
import java.util.HashMap;

public class MortgageCalculatorGUI extends JFrame {

	private static final HashMap<Mortgages, MortgageCalculatorGUI> mortgageCalculatorGuis = new HashMap<>();
	private MortgageCalculatorPanel mortgageCalculatorPanel;

	private MortgageCalculatorGUI(Mortgages mortgages) {
		super("Mortgage Calculator");
		mortgageCalculatorPanel = new MortgageCalculatorPanel(mortgages);
		setContentPane(mortgageCalculatorPanel);
		pack();
	}

	public static MortgageCalculatorGUI showCalculator(Mortgages mortgages) {
		MortgageCalculatorGUI gui = mortgageCalculatorGuis.get(mortgages);
		if (gui == null) {
			gui = new MortgageCalculatorGUI(mortgages);
			mortgageCalculatorGuis.put(mortgages, gui);
			Main.addFrame(gui);
		}
		return gui;
	}
}
