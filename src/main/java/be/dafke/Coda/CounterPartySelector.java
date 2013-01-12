package be.dafke.Coda;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import be.dafke.Coda.Objects.Movement;

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

	public CounterPartySelector(JFrame parent, Movement movement) {
		super(parent, "Select Counterparty", true);
		this.movement = movement;
		Object[] counterparties = CounterParties.getCounterParties().toArray();
		if (counterparties.length == 0) {
			dispose();
		}
		counterParty = null;
		// counterParty = (CounterParty) counterparties[0];
		combo = new JComboBox(counterparties);
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
		movementExistingCounterpartyTableModel = new GenericMovementDataModel(counterParty, null, true);
		movementExistingCounterpartyTable = new JTable(movementExistingCounterpartyTableModel);
		movementNoCounterpartyTableModel = new GenericMovementDataModel(null, movement.getTransactionCode(), true);
		movementNoCounterpartyTableModel.setSingleMovement(movement);
		movementNoCounterpartyTable = new JTable(movementNoCounterpartyTableModel);
		movementNoCounterpartyTable.setDefaultRenderer(CounterParty.class, new ColorRenderer());
		JScrollPane scroll1 = new JScrollPane(movementExistingCounterpartyTable);
		JScrollPane scroll2 = new JScrollPane(movementNoCounterpartyTable);
		JPanel center = new JPanel();
		single = new JRadioButton("Apply only for the selected movement");
		multiple = new JRadioButton("Apply only for all similar movements");
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
				CounterParties.getInstance().put(s, counterParty);
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
