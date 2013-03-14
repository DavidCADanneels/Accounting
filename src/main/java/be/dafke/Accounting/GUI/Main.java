package be.dafke.Accounting.GUI;

import be.dafke.Accounting.GUI.MainWindow.AccountingGUIFrame;
import be.dafke.Accounting.Objects.Accounting.Accountings;

import javax.swing.*;
import java.io.File;

public class Main {

    private enum Mode{ PROD, TEST}

	public static void main(String[] args) {
        Mode mode = Mode.TEST;

        File userHome = new File(System.getProperty("user.home"));
        File xmlFolder;
        if(mode == Mode.TEST){
            int nr = JOptionPane.showOptionDialog(null,"TEST or PROD", "Which environment?",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,null, Mode.values(),Mode.TEST);
            if(nr == 0){
                mode = Mode.PROD;
            } else {
                mode = Mode.TEST;
            }
        }

        if(mode == Mode.TEST) {
            xmlFolder = new File(userHome, "workspace/trunk/Accounting/src/main/resources/xml");
        } else {// if (mode == Mode.PROD) {
            xmlFolder = new File(userHome, "Accounting");
        }
        System.out.println(mode.toString());
        System.out.println(xmlFolder);

        File xslFolder = new File(userHome, "workspace/trunk/Accounting/src/main/resources/xsl");
        File dtdFolder = new File(userHome, "workspace/trunk/Accounting/src/main/resources/dtd");
        System.setProperty("Accountings_xml", xmlFolder.getPath());
        System.setProperty("Accountings_xsl", xslFolder.getPath());
        System.setProperty("Accountings_dtd", dtdFolder.getPath());

        Accountings accountings = new Accountings();
//        accountings.readCollection();

        AccountingGUIFrame frame = new AccountingGUIFrame(accountings);
        frame.setVisible(true);
	}
}
