package be.dafke.Accounting.GUI.MainWindow;

//import be.belgium.eid.*;

import be.dafke.Accounting.Objects.Accounting.Accounting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author David Danneels
 */

public class AccountingGUIPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private static final ResourceBundle bundle = ResourceBundle.getBundle("Accounting");
	private final JournalGUI journalGUI;
	private final AccountsGUI accountsGUI;
	private final JournalsGUI journalsGUI;
	private final MortgagesGUI mortgagesGUI;

	public AccountingGUIPanel(ActionListener actionListener) {
//    public AccountingGUIPanel(ActionListener actionListener) {
		// if(!checkID())
		// System.exit(0);
		journalGUI = new JournalGUI();
		accountsGUI = new AccountsGUI(actionListener);
		journalsGUI = new JournalsGUI(actionListener);
		mortgagesGUI = new MortgagesGUI();

		JPanel linksBoven = new JPanel(new BorderLayout());
		linksBoven.add(accountsGUI, BorderLayout.CENTER);
		linksBoven.add(mortgagesGUI, BorderLayout.SOUTH);
		//
		JPanel links = new JPanel(new BorderLayout());
		links.add(linksBoven, BorderLayout.CENTER);
		links.add(journalsGUI, BorderLayout.SOUTH);

		setLayout(new BorderLayout());
		add(journalGUI, BorderLayout.CENTER);

		add(links, BorderLayout.WEST);
	}

	protected void setAccounting(Accounting accounting) {
        journalGUI.setAccounting(accounting);
        accountsGUI.setAccounting(accounting);
        journalsGUI.setAccounting(accounting);
        mortgagesGUI.setAccounting(accounting);
        refresh();
    }

//    private void setAccounting2(Accounting accounting){
//        if(accounting==null){
//            accountsGUI.setAccounts(null);
//            mortgagesGUI.setMortgages(null);
//            journalsGUI.setJournals(null);
//            accountsGUI.setJournal(null);
//            mortgagesGUI.setJournal(null);
//            journalGUI.setJournal(null);
//            journalGUI.setTransaction(null);
//        } else{
//            accountsGUI.setAccounts(accounting.getAccounts());
//            mortgagesGUI.setMortgages(accounting.getMortgages());
//            journalsGUI.setJournals(accounting.getJournals());
//            if(accounting.getJournals()!=null){
//                accountsGUI.setJournal(accounting.getJournals().getCurrentJournal());
//                mortgagesGUI.setJournal(accounting.getJournals().getCurrentJournal());
//                journalGUI.setJournal(accounting.getJournals().getCurrentJournal());
//                if(accounting.getJournals().getCurrentJournal()!=null)
//                journalGUI.setTransaction(accounting.getJournals().getCurrentJournal().getCurrentTransaction());
//            }
//        }
//	}
//
    public void refresh(){
        journalGUI.refresh();
        accountsGUI.refresh();
        journalsGUI.refresh();
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