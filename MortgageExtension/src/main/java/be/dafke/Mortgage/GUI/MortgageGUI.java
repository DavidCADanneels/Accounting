package be.dafke.Mortgage.GUI;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.Mortgage.Actions.ShowMortgageCalculatorActionListener;
import be.dafke.Mortgage.MortgageExtension;
import be.dafke.Mortgage.Objects.Mortgage;
import be.dafke.Mortgage.Objects.Mortgages;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;
import be.dafke.Utils.Utils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MortgageGUI extends RefreshableFrame implements ActionListener, ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JList<BusinessObject> mortgagesList;
	private final JButton create;
	private final JTextField nrPayed;
    private final Mortgages mortgages;
    private boolean init = true;
	private final JComboBox<Account> comboIntrest, comboCapital;
	private Mortgage selectedMortgage = null;
	private final MortgageDataModel model;
//	private Account[] accounts;
	private DefaultListModel<BusinessObject> listModel;
	private DefaultComboBoxModel<Account> intrestModel, capitalModel;

	private final JTable table;
	private final JButton save, delete;
	private final Accounts accounts;

	public MortgageGUI(Accountings accountings, Accounting accounting, Mortgages mortgages) {
		super("Mortgages (" + accounting.toString() + ")");
		this.accounts = accounting.getAccounts();
        this.mortgages = mortgages;
		mortgagesList = new JList<BusinessObject>();
		mortgagesList.setModel(new DefaultListModel<BusinessObject>());
		mortgagesList.addListSelectionListener(this);
		create = new JButton("Create new Mortgage table");
        create.setActionCommand(MortgageExtension.MORTGAGE_CALCULATOR);
		create.addActionListener(new ShowMortgageCalculatorActionListener(accountings));

		JPanel left = new JPanel(new BorderLayout());
		left.add(mortgagesList, BorderLayout.CENTER);
		left.add(create, BorderLayout.SOUTH);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(left, BorderLayout.WEST);

		model = new MortgageDataModel(selectedMortgage);
		table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(600, 200));
		JScrollPane scroll = new JScrollPane(table);

		comboIntrest = new JComboBox<Account>();// comboModel);
		comboIntrest.addActionListener(this);
		comboCapital = new JComboBox<Account>();// comboModel);
		comboCapital.addActionListener(this);
		nrPayed = new JTextField(4);
		nrPayed.addActionListener(this);
		save = new JButton("Save");
		save.addActionListener(this);
		delete = new JButton("Delete");
		delete.addActionListener(this);

		activateButtons(false);
		save.setEnabled(false);

        JPanel accounts = new JPanel();
        accounts.setLayout(new GridLayout(2,0));
        accounts.add(new JLabel("Intrest Account:"));
        accounts.add(comboIntrest);
        accounts.add(new JLabel("Capital Account:"));
        accounts.add(comboCapital);
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
		north.add(accounts);
		north.add(block3);

		JPanel right = new JPanel(new BorderLayout());
		right.add(scroll, BorderLayout.CENTER);
		right.add(north, BorderLayout.NORTH);

		panel.add(right, BorderLayout.CENTER);
		setContentPane(panel);
		pack();

        refresh();
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

	@Override
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

	@Override
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

	@Override
	public void refresh() {
        listModel = new DefaultListModel<BusinessObject>();
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
        intrestModel = new DefaultComboBoxModel<Account>(allAccounts);
        capitalModel = new DefaultComboBoxModel<Account>(allAccounts);
        comboCapital.setModel(capitalModel);
        comboIntrest.setModel(intrestModel);
        comboCapital.revalidate();
        comboIntrest.revalidate();
	}
}