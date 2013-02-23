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
    private final JCheckBox searchOnTransactionCode, searchOnCommunication;
    private final JTextField transactionCode, communication;
	private final ButtonGroup singleMultiple;
	private final Accountings accountings;

	public CounterPartySelector(JFrame parent, Movement movement, Accountings accountings) {
		super(parent, "Select Counterparty", true);
		this.movement = movement;
		this.accountings = accountings;
		counterParty = null;
		// counterParty = (CounterParty) counterparties[0];

        // COMPONENTS
		combo = new JComboBox(accountings.getCurrentAccounting().getCounterParties().getCounterParties().toArray());
		combo.addItem(null);
		combo.setSelectedItem(null);
		// combo.setSelectedItem(counterparties[0]);
		combo.addActionListener(this);
		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(this);
		create = new JButton("Create Counterparty");
		create.addActionListener(this);
        //
        SearchOptions searchOptionsExisting = new SearchOptions();
        searchOptionsExisting.setCounterParty(counterParty);
        searchOptionsExisting.setSearchOnCounterParty(true);
        movementExistingCounterpartyTableModel = new GenericMovementDataModel(searchOptionsExisting, accountings);
        movementExistingCounterpartyTable = new JTable(movementExistingCounterpartyTableModel);
        SearchOptions searchOptionsNo = new SearchOptions();
        searchOptionsNo.setCounterParty(null);
        searchOptionsNo.setSearchOnCounterParty(true);
        searchOptionsNo.setTransactionCode(movement.getTransactionCode());
        searchOptionsNo.setSearchOnTransactionCode(true);
        movementNoCounterpartyTableModel = new GenericMovementDataModel(searchOptionsNo,
                accountings);
        movementNoCounterpartyTableModel.setSingleMovement(movement);
        movementNoCounterpartyTable = new JTable(movementNoCounterpartyTableModel);
        movementNoCounterpartyTable.setDefaultRenderer(CounterParty.class, new ColorRenderer());
        JScrollPane leftScrollPane = new JScrollPane(movementExistingCounterpartyTable);
        JScrollPane rightScrollPane = new JScrollPane(movementNoCounterpartyTable);
        //
        single = new JRadioButton("Single movement");
        multiple = new JRadioButton("Multiple movements");
        searchOnTransactionCode = new JCheckBox("TransactionCode == ");
        searchOnCommunication = new JCheckBox("Communication == ");
        transactionCode = new JTextField(10);
        communication = new JTextField(10);
        singleMultiple = new ButtonGroup();
        singleMultiple.add(single);
        singleMultiple.add(multiple);
        single.setSelected(true);
        single.addActionListener(this);
        multiple.addActionListener(this);
        apply = new JButton("Apply Changes");
        apply.addActionListener(this);

        // Layout
        //
        // North
        JPanel leftNorth = new JPanel(new GridLayout(0,1));
        leftNorth.add(create);
        leftNorth.add(combo);
        leftNorth.add(apply);
        //
        JPanel rightNorthNorth = new JPanel();
        rightNorthNorth.add(single);
        rightNorthNorth.add(multiple);
        //
        JPanel searchOptionsPanel = new JPanel(new GridLayout(2,0));
        searchOptionsPanel.add(searchOnTransactionCode);
        searchOptionsPanel.add(transactionCode);
        searchOptionsPanel.add(searchOnCommunication);
        searchOptionsPanel.add(communication);
        //
        JPanel rightNorth = new JPanel(new BorderLayout());
        rightNorth.add(rightNorthNorth,BorderLayout.NORTH);
        rightNorth.add(searchOptionsPanel);
        //
        JPanel north = new JPanel();
        north.add(leftNorth);
        north.add(rightNorth);
        //
        // Center
        JPanel center = new JPanel();
        center.add(leftScrollPane);
        center.add(rightScrollPane);
        //
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(north, BorderLayout.NORTH);
		contentPanel.add(center, BorderLayout.CENTER);
		contentPanel.add(ok, BorderLayout.SOUTH);
        setContentPane(contentPanel);
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
				CounterParties counterParties = accountings.getCurrentAccounting().getCounterParties();
				counterParty = counterParties.addCounterParty(s, null);
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
