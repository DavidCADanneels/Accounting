package be.dafke.BasicAccounting.Meals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.MealOrder;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class MealOrdersOverviewGUI extends JFrame {
	private static HashMap<Accounting, MealOrdersOverviewGUI> guis = new HashMap<>();
	private final MealOrdersOverviewPanel mealOrdersOverviewPanel;

	private MealOrdersOverviewGUI(Accounting accounting) {
		super(getBundle("Accounting").getString("MEAL_ORDER_OVERVIEW"));
		mealOrdersOverviewPanel = new MealOrdersOverviewPanel(accounting);
		setContentPane(mealOrdersOverviewPanel);
		pack();
	}

	public static MealOrdersOverviewGUI getInstance(Accounting accounting) {
		MealOrdersOverviewGUI gui = guis.get(accounting);
		if(gui == null){
			gui = new MealOrdersOverviewGUI(accounting);
			guis.put(accounting, gui);
			Main.addFrame(gui);
		}
		return gui;
	}

	public static void fireOrderAddedForAll(Accounting accounting, MealOrder mealOrder) {
		MealOrdersOverviewGUI gui = guis.get(accounting);
		if(gui != null) {
			gui.fireOrderAdded(accounting, mealOrder);
		}
	}

	private void fireOrderAdded(Accounting accounting, MealOrder mealOrder) {
		mealOrdersOverviewPanel.fireOrderAdded(accounting, mealOrder);
	}
}