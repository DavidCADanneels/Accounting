package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.Accounts;
import be.dafke.Accounting.BusinessModel.Mortgage;
import be.dafke.Accounting.BusinessModel.Mortgages;

import javax.swing.*;
import java.util.HashMap;

public class MortgageGUI extends JFrame  {
	private static final HashMap<Mortgages, MortgageGUI> mortgageGuis = new HashMap<>();
	private final MortgagePanel mortgagePanel;

	private MortgageGUI(Mortgages mortgages, Accounts accounts) {
		super("Mortgages");
        mortgagePanel = new MortgagePanel(mortgages, accounts);
		setContentPane(mortgagePanel);
		pack();
        refresh();
	}

	public static MortgageGUI showMortgages(Mortgages mortgages, Accounts accounts) {
		MortgageGUI gui = mortgageGuis.get(mortgages);
		if(gui == null){
			gui = new MortgageGUI(mortgages, accounts);
			mortgageGuis.put(mortgages,gui);
			Main.addFrame(gui);
		}
		return gui;
	}

	public static void refreshAllFrames(){
		for (MortgageGUI mortgageGUI:mortgageGuis.values()) {
			mortgageGUI.refresh();
		}
	}

	public static void selectMortgage(Mortgage mortgage){
		for (MortgageGUI mortgageGUI:mortgageGuis.values()) {
			mortgageGUI.reselect(mortgage);
		}
	}

	public void refresh() {
		mortgagePanel.refresh();
	}

	private void reselect(Mortgage mortgage) {
		mortgagePanel.reselect(mortgage);
	}
}