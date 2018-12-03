package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.MealOrder;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class DeliverooOrderCreateGUI extends JFrame {
	private static DeliverooOrderCreateGUI deliverooOrderCreateGUI = null;
	private final DeliverooOrderCreatePanel deliverooOrderCreatePanel;

	private DeliverooOrderCreateGUI(Accounting accounting) {
		super(getBundle("Accounting").getString("DELIVEROO_ORDER"));
		deliverooOrderCreatePanel = new DeliverooOrderCreatePanel(accounting);
		setContentPane(deliverooOrderCreatePanel);
		pack();
	}

	public static DeliverooOrderCreateGUI getInstance(Accounting accounting) {
		if(deliverooOrderCreateGUI == null){
			deliverooOrderCreateGUI = new DeliverooOrderCreateGUI(accounting);
			Main.addFrame(deliverooOrderCreateGUI);
		}
		return deliverooOrderCreateGUI;
	}

	public static void calculateTotalsForAll(){
		if(deliverooOrderCreateGUI != null){
			deliverooOrderCreateGUI.calculateTotals();
		}
	}

	public void calculateTotals(){
		deliverooOrderCreatePanel.calculateTotals();
	}

}