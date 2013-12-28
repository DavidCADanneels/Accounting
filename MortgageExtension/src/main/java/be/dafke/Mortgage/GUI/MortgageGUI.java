package be.dafke.Mortgage.GUI;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.Mortgage.Action.MortgageActionListener;
import be.dafke.Mortgage.Objects.Mortgage;
import be.dafke.ObjectModel.BusinessCollection;
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
	private final JList<Mortgage> mortgagesList;
	private final JButton create;
	private final JTextField nrPayed;
	private boolean init = true;
	private final JComboBox<Account> comboIntrest, comboCapital;
	private Mortgage selectedMortgage = null;
	private final MortgageDataModel model;
	private Account[] accounts;
	private DefaultListModel<Mortgage> listModel;
	private DefaultComboBoxModel<Account> intrestModel, capitalModel;

	private final JTable table;
	private final JButton save, delete;
	private final Accounting accounting;

	public MortgageGUI(Accounting accounting, ActionListener actionListener) {
		super("Mortgages (" + accounting.toString() + ")");
		this.accounting = accounting;
		mortgagesList = new JList<Mortgage>();
		mortgagesList.setModel(new DefaultListModel<Mortgage>());
		mortgagesList.addListSelectionListener(this);
		create = new JButton("Create new Mortgage table");
        create.setActionCommand(MortgageActionListener.MORTGAGE_CALCULATOR);
		create.addActionListener(actionListener);

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

		JPanel block1 = new JPanel();
		block1.add(new JLabel("Intrest Account:"));
		block1.add(comboIntrest);
		JPanel block2 = new JPanel();
		block2.add(new JLabel("Capital Account:"));
		block2.add(comboCapital);
		//
		JPanel block3 = new JPanel();
		block3.add(new JLabel("Already payed:"));
		block3.add(nrPayed);
		//
		JPanel block4 = new JPanel();
		block4.add(save);
		block4.add(delete);
		//
		JPanel north = new JPanel();
		north.setLayout(new GridLayout(2, 0));
		north.add(block1);
		north.add(block3);
		north.add(block2);
		north.add(block4);

		JPanel right = new JPanel(new BorderLayout());
		right.add(scroll, BorderLayout.CENTER);
		right.add(north, BorderLayout.NORTH);

		panel.add(right, BorderLayout.CENTER);
		setContentPane(panel);
		pack();
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
			selectedMortgage = mortgagesList.getSelectedValue();
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
                    accounting.getBusinessObject("Mortgages").removeBusinessObject(selectedMortgage);
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
        listModel = new DefaultListModel<Mortgage>();
        BusinessCollection<BusinessObject> mortgages = accounting.getBusinessObject("Mortgages");
        for(BusinessObject businessObject :mortgages.getBusinessObjects()) {
            Mortgage mortgage =(Mortgage) businessObject;
            if (!listModel.contains(mortgage)) {
                listModel.addElement(mortgage);
            }
        }
        mortgagesList.setModel(listModel);
        mortgagesList.revalidate();

        accounts = new Account[accounting.getAccounts().getBusinessObjects().size()];
        for(int i = 0; i < accounting.getAccounts().getBusinessObjects().size(); i++) {
            accounts[i] = accounting.getAccounts().getBusinessObjects().get(i);
        }
        intrestModel = new DefaultComboBoxModel<Account>(accounts);
        capitalModel = new DefaultComboBoxModel<Account>(accounts);
        comboCapital.setModel(capitalModel);
        comboIntrest.setModel(intrestModel);
        comboCapital.revalidate();
        comboIntrest.revalidate();
	}
}