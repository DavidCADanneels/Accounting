package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.DeliverooMeals;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class DeliverooOrderCreateGUI extends JFrame {
	private static HashMap<Accounting, DeliverooOrderCreateGUI> deliverooOrderCreateGuis = new HashMap<>();
	private final DeliverooOrderCreatePanel deliverooOrderCreatePanel;

	private DeliverooOrderCreateGUI(Accounting accounting) {
		super(getBundle("Accounting").getString("DELIVEROO_ORDER_INPUT"));
		deliverooOrderCreatePanel = new DeliverooOrderCreatePanel(accounting);
		setContentPane(deliverooOrderCreatePanel);
		pack();
	}

	public static DeliverooOrderCreateGUI getInstance(Accounting accounting) {
		DeliverooOrderCreateGUI gui = deliverooOrderCreateGuis.get(accounting);
		if(gui == null){
			gui = new DeliverooOrderCreateGUI(accounting);
			deliverooOrderCreateGuis.put(accounting, gui);
			Main.addFrame(gui);
		}
		return gui;
	}

	public static void calculateTotalsForAll(Accounting accounting){
		DeliverooOrderCreateGUI gui = deliverooOrderCreateGuis.get(accounting);
		if(gui!=null){
			gui.calculateTotals();
		}
	}

	public void calculateTotals(){
		deliverooOrderCreatePanel.calculateTotals();
	}

}