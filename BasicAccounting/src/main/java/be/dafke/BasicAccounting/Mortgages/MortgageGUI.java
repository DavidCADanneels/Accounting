package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Mortgage;
import be.dafke.BusinessModel.Mortgages;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;
import be.dafke.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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