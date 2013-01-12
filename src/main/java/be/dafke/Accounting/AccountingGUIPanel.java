package be.dafke.Accounting;

//import be.belgium.eid.*;

import javax.swing.*;
import java.awt.*;

/**
 * @author David Danneels
 */

public class AccountingGUIPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private static final ResourceBundle bundle = ResourceBundle.getBundle("Accounting");
	private final JournalGUI dagboekGUI;
	private final AccountsGUI accountsGUI;
	private final JournalsGUI journalsGUI;
	private final MortgagesGUI mortgagesGUI;

	public AccountingGUIPanel(AccountingGUIFrame parent) {
		// if(!checkID())
		// System.exit(0);
		dagboekGUI = new JournalGUI(parent);
		accountsGUI = new AccountsGUI(dagboekGUI, parent);
		journalsGUI = new JournalsGUI(dagboekGUI, parent);
		mortgagesGUI = new MortgagesGUI(dagboekGUI, parent);

		JPanel linksBoven = new JPanel(new BorderLayout());
		linksBoven.add(accountsGUI, BorderLayout.CENTER);
		linksBoven.add(mortgagesGUI, BorderLayout.SOUTH);
		//
		JPanel links = new JPanel(new BorderLayout());
		links.add(linksBoven, BorderLayout.CENTER);
		links.add(journalsGUI, BorderLayout.SOUTH);

		setLayout(new BorderLayout());
		add(dagboekGUI, BorderLayout.CENTER);

		add(links, BorderLayout.WEST);
		// dagboekGUI.init();
	}

	public void activateButtons(/*boolean active*/) {
		dagboekGUI.init();
		accountsGUI.activateButtons(/*active*/);
		journalsGUI.activateButtons(/*active*/);
		mortgagesGUI.refresh();
	}

	/*
	 * private boolean checkID(){ try{ System.loadLibrary("beidlibjni"); BEID_ID_Data IDData = new BEID_ID_Data();
	 * BEID_Certif_Check CertCheck = new BEID_Certif_Check(); BEID_Long CardHandle = new BEID_Long(); BEID_Status
	 * oStatus = eidlib.BEID_Init(null, 0, 0, CardHandle); if(oStatus.getGeneral() != 0){
	 * JOptionPane.showMessageDialog(this, bundle.getString("GEEN_KAART")); return false; } oStatus =
	 * eidlib.BEID_GetID(IDData, CertCheck); // if(!IDData.getChipNumber().equals("534C494E33660013930D29BFB30C323C")){
	 * if(!IDData.getNationalNumber().equals("80092225552")){ JOptionPane.showMessageDialog(this,
	 * bundle.getString("FOUTE_KAART")); return false; } return true; }catch (UnsatisfiedLinkError e){
	 * System.out.println(e.getStackTrace()); JOptionPane.showMessageDialog(this, bundle.getString("ANDERE_FOUT"));
	 * return false; } }
	 */
}