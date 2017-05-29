package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.JournalType;
import be.dafke.BusinessModel.JournalTypes;
import be.dafke.BusinessModel.VATTransaction;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.util.ResourceBundle.getBundle;

public class JournalTypeManagementGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<JournalType> combo;
	private JournalTypes journalTypes;
	private static final HashMap<JournalTypes, JournalTypeManagementGUI> journalTypeManagementGuis = new HashMap<>();
	private JournalType journalType;
	private Accounts accounts;
	private AccountTypes accountTypes;
	private JComboBox<VATTransaction.VATType> taxType;

	private JournalTypeManagementGUI(Accounts accounts, JournalTypes journalTypes, AccountTypes accountTypes) {
		super(journalTypes.getAccounting().getName() + " / " + getBundle("Accounting").getString("JOURNAL_TYPE_MANAGEMENT_TITLE"));
		this.accounts = accounts;
		this.accountTypes = accountTypes;
		this.journalTypes = journalTypes;
		setContentPane(createContentPanel());
		setJournalTypes(journalTypes);
		pack();
	}

	public static void showJournalTypeManager(Accounts accounts, JournalTypes journalTypes, AccountTypes accountTypes) {
		JournalTypeManagementGUI gui = journalTypeManagementGuis.get(journalTypes);
		if(gui == null){
			gui = new JournalTypeManagementGUI(accounts, journalTypes, accountTypes);
			journalTypeManagementGuis.put(journalTypes,gui);
			Main.addFrame(gui);
		}
		gui.setVisible(true);
	}

	public void setJournalTypes(JournalTypes journalTypes){
		this.journalTypes = journalTypes;
		combo.removeAllItems();
		for(JournalType type : journalTypes.getBusinessObjects()){
			((DefaultComboBoxModel<JournalType>) combo.getModel()).addElement(type);
		}
	}

	public JPanel createContentPanel(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createCenterPanel(), CENTER);
		panel.add(createSavePanel(), NORTH);
		return panel;
	}

	private JPanel createSavePanel() {
		JPanel panel = new JPanel();
		JButton newType = new JButton(getBundle("Accounting").getString("NEW_JOURNAL_TYPE"));
		newType.addActionListener(e -> createNewJournalType());
		combo = new JComboBox<>();
		combo.addActionListener(e -> comboAction());
		taxType = new JComboBox<>();
		taxType.addItem(null);
		taxType.addItem(VATTransaction.VATType.PURCHASE);
		taxType.addItem(VATTransaction.VATType.SALE);
		taxType.setSelectedItem(null);
		taxType.addActionListener(e -> {
			journalType.setVatType((VATTransaction.VATType) taxType.getSelectedItem());
		});
		panel.add(taxType);
		panel.add(combo);
		panel.add(newType);
		return panel;
	}

	private void comboAction() {
		journalType = (JournalType) combo.getSelectedItem();
		taxType.setSelectedItem(journalType.getVatType());
	}

	public void createNewJournalType(){
		String name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
		while (name != null && name.equals(""))
			name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
		if (name != null) {
			JournalType journalType = new JournalType(name, accountTypes);
			try {
				journalTypes.addBusinessObject(journalType);
			} catch (EmptyNameException e) {
				e.printStackTrace();
			} catch (DuplicateNameException e) {
				e.printStackTrace();
			}
			((DefaultComboBoxModel<JournalType>) combo.getModel()).addElement(journalType);
			(combo.getModel()).setSelectedItem(journalType);
		}
	}

	public JPanel createCenterPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));

//		panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("ACCOUNTTYPES")));;
		panel.add(new AccountsListConfigPanel(accounts, accountTypes, getBundle("BusinessModel").getString("LEFT")));
		panel.add(new AccountsListConfigPanel(accounts, accountTypes, getBundle("BusinessModel").getString("RIGHT")));


		return panel;
	}


}