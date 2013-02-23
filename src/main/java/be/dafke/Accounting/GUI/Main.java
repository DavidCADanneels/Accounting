package be.dafke.Accounting.GUI;

import be.dafke.Accounting.Dao.XML.AccountingSAXParser;
import be.dafke.Accounting.GUI.MainWindow.AccountingGUIFrame;
import be.dafke.Accounting.Objects.Accounting.Accountings;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        Accountings accountings = AccountingSAXParser.fromXML();
        AccountingGUIFrame frame = new AccountingGUIFrame(
				java.util.ResourceBundle.getBundle("Accounting").getString("BOEKHOUDING"), accountings);
        frame.setVisible(true);
	}
}
