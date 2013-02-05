package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Coda.CounterParties;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.Accounting.Objects.Coda.Movement;
import be.dafke.Accounting.Objects.Coda.TmpCounterParty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CounterPartySelector extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton ok, create, apply;
	// private final JList list;
	private final JComboBox combo;
	private CounterParty counterParty;
	private final JTable movementExistingCounterpartyTable, movementNoCounterpartyTable;
	private final GenericMovementDataModel movementExistingCounterpartyTableModel;
	private final GenericMovementDataModel movementNoCounterpartyTableModel;
	private final Movement movement;
	private final JRadioButton single, multiple;
	private final ButtonGroup singleMultiple;
	private final Accountings accountings;

	public CounterPartySelector(JFrame parent, Movement movement, Accountings accountings) {
		super(parent, "Select Counterparty", true);
		this.movement = movement;
		this.accountings = accountings;
		counterParty = null;
		// counterParty = (CounterParty) counterparties[0];
		combo = new JComboBox(accountings.getCurrentAccounting().getCounterParties().getCounterParties().toArray());
		combo.addItem(null);
		combo.setSelectedItem(null);
		// combo.setSelectedItem(counterparties[0]);
		combo.addActionListener(this);
		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(this);
		create = new JButton("Create Counterparty");
		create.addActionListener(this);
		Container panel = getContentPane();
		JPanel innerPanel = new JPanel(new BorderLayout());
		JPanel north = new JPanel();
		north.add(combo);
		north.add(create);
		movementExistingCounterpartyTableModel = new GenericMovementDataModel(counterParty, null, true, accountings);
		movementExistingCounterpartyTable = new JTable(movementExistingCounterpartyTableModel);
		movementNoCounterpartyTableModel = new GenericMovementDataModel(null, movement.getTransactionCode(), true,
				accountings);
		movementNoCounterpartyTableModel.setSingleMovement(movement);
		movementNoCounterpartyTable = new JTable(movementNoCounterpartyTableModel);
		movementNoCounterpartyTable.setDefaultRenderer(CounterParty.class, new ColorRenderer());
		JScrollPane scroll1 = new JScrollPane(movementExistingCounterpartyTable);
		JScrollPane scroll2 = new JScrollPane(movementNoCounterpartyTable);
		JPanel center = new JPanel();
		single = new JRadioButton("Apply only for the selected movement");
		multiple = new JRadioButton("Apply for all similar movements");
		singleMultiple = new ButtonGroup();
		singleMultiple.add(single);
		singleMultiple.add(multiple);
		single.setSelected(true);
		single.addActionListener(this);
		multiple.addActionListener(this);
		apply = new JButton("Apply Changes");
		apply.addActionListener(this);
		north.add(apply);
		north.add(single);
		north.add(multiple);
		center.add(scroll1);
		center.add(scroll2);
		innerPanel.add(north, BorderLayout.NORTH);
		innerPanel.add(center, BorderLayout.CENTER);
		innerPanel.add(ok, BorderLayout.SOUTH);
		panel.add(innerPanel);
		pack();
	}

	public CounterParty getSelection() {
		return counterParty;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			dispose();
		} else if (e.getSource() == create) {
			String s = JOptionPane.showInputDialog(this, "Enter a name for the new counterparty");
			if (s != null && !s.equals("")) {
				counterParty = new CounterParty(s);
				CounterParties counterParties = accountings.getCurrentAccounting().getCounterParties();
				counterParties.put(s, counterParty);
				combo.addItem(counterParty);
				combo.setSelectedItem(counterParty);
			}
		} else if (e.getSource() == combo) {
			counterParty = (CounterParty) combo.getSelectedItem();
			movementExistingCounterpartyTableModel.switchCounterParty(counterParty);
			fillInCounterParty();
		} else if (e.getSource() == single) {
			movementNoCounterpartyTableModel.setSingleMovement(movement);
			fillInCounterParty();
		} else if (e.getSource() == multiple) {
			movementNoCounterpartyTableModel.setSingleMovement(null);
			fillInCounterParty();
		} else if (e.getSource() == apply) {
			setCounterParty();
		}
	}

	private void setCounterParty() {
		for(Movement m : movementNoCounterpartyTableModel.getAllMovements()) {
			TmpCounterParty tmpCounterParty = m.getTmpCounterParty();
			m.setCounterParty(tmpCounterParty.getCounterParty());
		}
		movementNoCounterpartyTableModel.switchCounterParty(null);
		movementNoCounterpartyTableModel.fireTableDataChanged();
		movementExistingCounterpartyTableModel.fireTableDataChanged();
		combo.setEnabled(false);
		single.setEnabled(false);
		multiple.setEnabled(false);
		create.setEnabled(false);
		apply.setEnabled(false);
	}

	private void fillInCounterParty() {
		for(Movement m : movementNoCounterpartyTableModel.getAllMovements()) {
			String name;
			if (counterParty == null) {
				name = null;
			} else {
				name = counterParty.getName();
			}
			m.setTmpCounterParty(new TmpCounterParty(name, counterParty));
		}
		movementNoCounterpartyTableModel.fireTableDataChanged();
	}
}
