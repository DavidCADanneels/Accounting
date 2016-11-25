package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Mortgage;
import be.dafke.BusinessModel.Mortgages;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;
import be.dafke.Utils.Utils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MortgageGUI extends RefreshableFrame implements ActionListener, ListSelectionListener{//, AccountingListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JList<BusinessObject> mortgagesList;
	private final JButton create;
	private final JTextField nrPayed;
    private Mortgages mortgages;
	private Accounts accounts;
	private boolean init = true;
	private final JComboBox<Account> comboIntrest, comboCapital;
	private Mortgage selectedMortgage = null;
	private final MortgageDataModel model;
	private DefaultListModel<BusinessObject> listModel;
	private DefaultComboBoxModel<Account> intrestModel, capitalModel;

	private final RefreshableTable<Mortgage> table;
	private final JButton save, delete;
	private static final HashMap<Mortgages, MortgageGUI> mortgageGuis = new HashMap<>();

	private MortgageGUI(Mortgages mortgages, Accounts accounts) {
		super("Mortgages");
        this.mortgages = mortgages;
		this.accounts = accounts;
		mortgagesList = new JList<>();
		mortgagesList.setModel(new DefaultListModel<>());
		mortgagesList.addListSelectionListener(this);
		create = new JButton("Create new Mortgage table");
		create.addActionListener(e -> MortgageCalculatorGUI.showCalculator(mortgages).setVisible(true));

		JPanel left = new JPanel(new BorderLayout());
		left.add(mortgagesList, BorderLayout.CENTER);
		left.add(create, BorderLayout.SOUTH);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(left, BorderLayout.WEST);

		model = new MortgageDataModel(selectedMortgage);
		table = new RefreshableTable<>(model);
		table.setPreferredScrollableViewportSize(new Dimension(600, 200));
		JScrollPane scroll = new JScrollPane(table);

		comboIntrest = new JComboBox<>();// comboModel);
		comboIntrest.addActionListener(this);
		comboCapital = new JComboBox<>();// comboModel);
		comboCapital.addActionListener(this);
		nrPayed = new JTextField(4);
		nrPayed.addActionListener(this);
		save = new JButton("Save");
		save.addActionListener(this);
		delete = new JButton("Delete");
		delete.addActionListener(this);

		activateButtons(false);
		save.setEnabled(false);

        JPanel accountsPanel = new JPanel();
        accountsPanel.setLayout(new GridLayout(2,0));
        accountsPanel.add(new JLabel("Intrest Account:"));
        accountsPanel.add(comboIntrest);
        accountsPanel.add(new JLabel("Capital Account:"));
        accountsPanel.add(comboCapital);
		//
		JPanel block3a = new JPanel();
		block3a.add(new JLabel("Already payed:"));
		block3a.add(nrPayed);
		//
		JPanel block3b = new JPanel();
		block3b.add(save);
        block3b.add(delete);
        //
        JPanel block3 = new JPanel();
        block3.setLayout(new BoxLayout(block3, BoxLayout.Y_AXIS));
        block3.add(block3a);
        block3.add(block3b);
        //
        JPanel north = new JPanel();
		north.add(accountsPanel);
		north.add(block3);

		JPanel right = new JPanel(new BorderLayout());
		right.add(scroll, BorderLayout.CENTER);
		right.add(north, BorderLayout.NORTH);

		panel.add(right, BorderLayout.CENTER);
		setContentPane(panel);
		pack();

        refresh();
	}

	public static MortgageGUI showMortgages(Mortgages mortgages, Accounts accounts) {
		MortgageGUI gui = mortgageGuis.get(mortgages);
		if(gui == null){
			gui = new MortgageGUI(mortgages, accounts);
			mortgageGuis.put(mortgages,gui);
			Main.addJFrame(gui);
		}
		return gui;
	}

	private void activateButtons(boolean active) {
		comboCapital.setEnabled(active);
		comboIntrest.setEnabled(active);
		nrPayed.setEnabled(active);
		delete.setEnabled(active);
		if (active) {
			save.setText("Save");
		} else {
			save.setText("Edit");
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		init = true;
		if (!e.getValueIsAdjusting() && mortgagesList.getSelectedIndex() != -1) {
			selectedMortgage = (Mortgage)mortgagesList.getSelectedValue();
			comboIntrest.setSelectedItem(selectedMortgage.getIntrestAccount());
			comboCapital.setSelectedItem(selectedMortgage.getCapitalAccount());
			nrPayed.setText(selectedMortgage.getNrPayed() + "");
			save.setEnabled(true);
		} else {
			selectedMortgage = null;
			comboIntrest.setSelectedIndex(-1);
			comboCapital.setSelectedIndex(-1);
			nrPayed.setText("");
			activateButtons(false);
			save.setEnabled(false);
		}
		model.revalidate(selectedMortgage);
		table.revalidate();
		init = false;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == save) {
			if (save.getText().equals("Edit")) {
				activateButtons(true);
			} else {
				if (selectedMortgage != null) {
                    int nr = Utils.parseInt(nrPayed.getText());
                    selectedMortgage.setPayed(nr);
                }
				activateButtons(false);
			}
		} else if (e.getSource() == delete) {
			if (selectedMortgage != null) {
                try {
                    mortgages.removeBusinessObject(selectedMortgage);
                } catch (NotEmptyException e1) {
                    System.err.println("This mortgage is in use !");
                    e1.printStackTrace();
                }
            }
		} else if (!init) {
			if (e.getSource() == comboIntrest) {
				Account intrestAccount = (Account) comboIntrest.getSelectedItem();
				if (selectedMortgage != null && intrestAccount != null) {
					selectedMortgage.setIntrestAccount(intrestAccount);
				}
			} else if (e.getSource() == comboCapital) {
				Account capitalAccount = (Account) comboCapital.getSelectedItem();
				if (selectedMortgage != null && capitalAccount != null) {
					selectedMortgage.setCapitalAccount(capitalAccount);
				}
			} else if (e.getSource() == nrPayed) {
				if (selectedMortgage != null) {
                    int nr = Utils.parseInt(nrPayed.getText());
                    selectedMortgage.setPayed(nr);
				}
			}
		}
    }

	public void refresh() {
        listModel = new DefaultListModel<>();
        for(BusinessObject mortgage :mortgages.getBusinessObjects()) {
            if (!listModel.contains(mortgage)) {
                listModel.addElement(mortgage);
            }
        }
        mortgagesList.setModel(listModel);
        mortgagesList.revalidate();

        Account[] allAccounts = new Account[accounts.getBusinessObjects().size()];
        for(int i = 0; i < accounts.getBusinessObjects().size(); i++) {
            allAccounts[i] = accounts.getBusinessObjects().get(i);
        }
        intrestModel = new DefaultComboBoxModel<>(allAccounts);
        capitalModel = new DefaultComboBoxModel<>(allAccounts);
        comboCapital.setModel(capitalModel);
        comboIntrest.setModel(intrestModel);
        comboCapital.revalidate();
        comboIntrest.revalidate();
	}

//	@Override
//	public void setAccounting(Accounting accounting) {
//		accounts = accounting==null?null:accounting.getAccounts();
//		mortgages = accounting==null?null:accounting.getMortgages();
//		delete.setEnabled(mortgages!=null);
//	}
}