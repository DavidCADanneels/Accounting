package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.MealOrder;
import be.dafke.BusinessModel.MealOrders;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class DeliverooOrdersOverviewGUI extends JFrame {
	private static HashMap<Accounting, DeliverooOrdersOverviewGUI> deliverooOrderCreateGuis = new HashMap<>();
	private final DeliverooOrdersOverviewPanel deliverooOrdersOverviewPanel;

	private DeliverooOrdersOverviewGUI(Accounting accounting) {
		super(getBundle("Accounting").getString("DELIVEROO_ORDER_OVERVIEW"));
		deliverooOrdersOverviewPanel = new DeliverooOrdersOverviewPanel(accounting);
		setContentPane(deliverooOrdersOverviewPanel);
		pack();
	}

	public static DeliverooOrdersOverviewGUI getInstance(Accounting accounting) {
		DeliverooOrdersOverviewGUI gui = deliverooOrderCreateGuis.get(accounting);
		if(gui == null){
			gui = new DeliverooOrdersOverviewGUI(accounting);
			deliverooOrderCreateGuis.put(accounting, gui);
			Main.addFrame(gui);
		}
		return gui;
	}

	public static void fireOrderAddedForAll(Accounting accounting, MealOrder mealOrder) {
		DeliverooOrdersOverviewGUI gui = deliverooOrderCreateGuis.get(accounting);
		if(gui != null) {
			gui.fireOrderAdded(accounting, mealOrder);
		}
	}

	private void fireOrderAdded(Accounting accounting, MealOrder mealOrder) {
		deliverooOrdersOverviewPanel.fireOrderAdded(accounting, mealOrder);
	}
}