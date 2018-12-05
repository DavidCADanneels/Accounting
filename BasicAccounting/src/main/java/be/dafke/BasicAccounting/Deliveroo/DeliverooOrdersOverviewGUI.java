package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class DeliverooOrdersOverviewGUI extends JFrame {
	private static DeliverooOrdersOverviewGUI deliverooOrderCreateGUI = null;
	private final DeliverooOrdersOverviewPanel deliverooOrdersOverviewPanel;

	private DeliverooOrdersOverviewGUI(Accounting accounting) {
		super(getBundle("Accounting").getString("DELIVEROO_ORDER_OVERVIEW"));
		deliverooOrdersOverviewPanel = new DeliverooOrdersOverviewPanel(accounting);
		setContentPane(deliverooOrdersOverviewPanel);
		pack();
	}

	public static DeliverooOrdersOverviewGUI getInstance(Accounting accounting) {
		if(deliverooOrderCreateGUI == null){
			deliverooOrderCreateGUI = new DeliverooOrdersOverviewGUI(accounting);
			Main.addFrame(deliverooOrderCreateGUI);
		}
		return deliverooOrderCreateGUI;
	}

	public static void fireOrderAddedForAll() {
		if(deliverooOrderCreateGUI != null)
			deliverooOrderCreateGUI.fireOrderAdded();
	}

	private void fireOrderAdded() {
		deliverooOrdersOverviewPanel.fireOrderAdded();
	}
}