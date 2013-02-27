package be.dafke.Accounting.GUI;

import be.dafke.Accounting.Dao.XML.AccountingSAXParser;
import be.dafke.Accounting.GUI.MainWindow.AccountingGUIFrame;
import be.dafke.Accounting.Objects.Accounting.Accountings;

public class Main {

	public static void main(String[] args) {
        Accountings accountings = AccountingSAXParser.readAccountings();
        AccountingGUIFrame frame = new AccountingGUIFrame(accountings);
        frame.setVisible(true);
	}
}
