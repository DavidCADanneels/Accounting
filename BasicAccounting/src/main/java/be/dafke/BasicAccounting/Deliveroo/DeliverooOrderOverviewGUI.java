package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class DeliverooOrderOverviewGUI extends JFrame {
	private static DeliverooOrderOverviewGUI deliverooOrderCreateGUI = null;

	private DeliverooOrderOverviewGUI(Accounting accounting) {
		super(getBundle("Accounting").getString("DELIVEROO_ORDER"));
		setContentPane(new DeliverooOrderOverviewPanel(accounting));
		pack();
	}

	public static DeliverooOrderOverviewGUI getInstance(Accounting accounting) {
		if(deliverooOrderCreateGUI == null){
			deliverooOrderCreateGUI = new DeliverooOrderOverviewGUI(accounting);
			Main.addFrame(deliverooOrderCreateGUI);
		}
		return deliverooOrderCreateGUI;
	}
}