package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.MealOrder;
import be.dafke.BusinessModel.MealOrders;

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

	public static void fireOrderAddedForAll(MealOrders mealOrders, MealOrder mealOrder) {
		if(deliverooOrderCreateGUI != null)
			deliverooOrderCreateGUI.fireOrderAdded(mealOrders, mealOrder);
	}

	private void fireOrderAdded(MealOrders mealOrders, MealOrder mealOrder) {
		deliverooOrdersOverviewPanel.fireOrderAdded(mealOrders, mealOrder);
	}
}