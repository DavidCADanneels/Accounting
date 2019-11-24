package be.dafke.BasicAccounting.Meals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.Accounting;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class MealOrderCreateGUI extends JFrame {
	private static HashMap<Accounting, MealOrderCreateGUI> guis = new HashMap<>();
	private final MealOrderCreatePanel mealOrderCreatePanel;

	private MealOrderCreateGUI(Accounting accounting) {
		super(getBundle("Accounting").getString("MEAL_ORDER_INPUT"));
		mealOrderCreatePanel = new MealOrderCreatePanel(accounting);
		setContentPane(mealOrderCreatePanel);
		pack();
	}

	public static MealOrderCreateGUI getInstance(Accounting accounting) {
		MealOrderCreateGUI gui = guis.get(accounting);
		if(gui == null){
			gui = new MealOrderCreateGUI(accounting);
			guis.put(accounting, gui);
			Main.addFrame(gui);
		}
		return gui;
	}

	public static void calculateTotalsForAll(Accounting accounting){
		MealOrderCreateGUI gui = guis.get(accounting);
		if(gui!=null){
			gui.calculateTotals();
		}
	}

	public void calculateTotals(){
		mealOrderCreatePanel.calculateTotals();
	}

}