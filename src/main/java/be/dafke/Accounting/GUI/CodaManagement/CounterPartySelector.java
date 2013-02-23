package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Coda.CounterParties;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.Accounting.Objects.Coda.Movement;
import be.dafke.Accounting.Objects.Coda.TmpCounterParty;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CounterPartySelector extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton ok, create, apply;
	private final JComboBox combo;
	private CounterParty counterParty;
    private final JTable movementTable;
	private final GenericMovementDataModel movementDataModel;
	private final Movement movement;
	private final JRadioButton single, multiple;
    private final JCheckBox searchOnTransactionCode, searchOnCommunication, searchOnCounterParty;
    private final JTextField transactionCode, communication;
	private final ButtonGroup singleMultiple;
	private final Accountings accountings;
    private final SearchOptions searchOptions;

    public CounterPartySelector(JFrame parent, Movement movement, Accountings accountings) {
		super(parent, "Select Counterparty", true);
		this.movement = movement;
		this.accountings = accountings;
		counterParty = null;

        // COMPONENTS
		combo = new JComboBox(accountings.getCurrentAccounting().getCounterParties().getCounterParties().toArray());
		combo.addItem(null);
		combo.setSelectedItem(null);
		combo.addActionListener(this);
		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(this);
		create = new JButton("Create Counterparty");
		create.addActionListener(this);
        //
        searchOptions = new SearchOptions();
        searchOptions.searchForCounterParty(null);
        searchOptions.searchForTransactionCode(movement.getTransactionCode());
        movementDataModel = new GenericMovementDataModel(searchOptions,
                accountings);
        movementDataModel.setSingleMovement(movement);
        movementTable = new JTable(movementDataModel);
        movementTable.setDefaultRenderer(CounterParty.class, new ColorRenderer());
        JScrollPane scrollPane = new JScrollPane(movementTable);
        //
        single = new JRadioButton("Single movement");
        multiple = new JRadioButton("Multiple movements");
        searchOnTransactionCode = new JCheckBox("TransactionCode == ");
        searchOnTransactionCode.setEnabled(false);
        searchOnTransactionCode.setSelected(true);
        searchOnTransactionCode.addActionListener(this);
        searchOnCommunication = new JCheckBox("Communication == ");
        searchOnCommunication.setEnabled(false);
        searchOnCommunication.addActionListener(this);
        transactionCode = new JTextField(10);
        transactionCode.setEnabled(false);
        transactionCode.setText(movement.getTransactionCode());
        transactionCode.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                setTransactionCode();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                setTransactionCode();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setTransactionCode();
            }
        });
        communication = new JTextField(10);
        communication.setEnabled(false);
        communication.setText(movement.getCommunication());
        communication.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                setCommunication();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                setCommunication();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setCommunication();
            }
        });
        searchOnCounterParty = new JCheckBox("Counterparty == ");
        searchOnCounterParty.setEnabled(false);
        searchOnCounterParty.setSelected(true);
        searchOnCounterParty.addActionListener(this);
        singleMultiple = new ButtonGroup();
        singleMultiple.add(single);
        singleMultiple.add(multiple);
        single.setSelected(true);
        single.addActionListener(this);
        multiple.addActionListener(this);
        apply = new JButton("Apply Changes");
        apply.addActionListener(this);
        apply.setEnabled(false);

        // Layout
        JPanel searchOptionsPanel = new JPanel(new GridLayout(0,2));
        searchOptionsPanel.add(single);
        searchOptionsPanel.add(multiple);
        searchOptionsPanel.add(searchOnTransactionCode);
        searchOptionsPanel.add(transactionCode);
        searchOptionsPanel.add(searchOnCommunication);
        searchOptionsPanel.add(communication);
        searchOptionsPanel.add(searchOnCounterParty);
        searchOptionsPanel.add(combo);
        searchOptionsPanel.add(apply);
        searchOptionsPanel.add(create);
        //
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(searchOptionsPanel, BorderLayout.NORTH);
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		contentPanel.add(ok, BorderLayout.SOUTH);
        setContentPane(contentPanel);
		pack();
	}

    private void setCommunication() {
        searchOptions.setCommunication(communication.getText());
        fillInCounterParty();
    }

    private void setTransactionCode(){
        searchOptions.setTransactionCode(transactionCode.getText());
        fillInCounterParty();
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
            apply.setEnabled(counterParty!=null);
			fillInCounterParty();
		} else if (e.getSource() == single) {
			movementDataModel.setSingleMovement(movement);
            enableSearchOptions(false);
			fillInCounterParty();
		} else if (e.getSource() == multiple) {
			movementDataModel.setSingleMovement(null);
            enableSearchOptions(true);
			fillInCounterParty();
		} else if (e.getSource() == apply) {
			setCounterParty();
		} else if (e.getSource() == searchOnCommunication){
            if(searchOnCommunication.isSelected()){
                searchOptions.searchForCommunication(communication.getText());
            } else {
                searchOptions.setSearchOnCommunication(false);
            }
            fillInCounterParty();
        } else if (e.getSource() == searchOnTransactionCode){
            if(searchOnTransactionCode.isSelected()){
                searchOptions.searchForTransactionCode(transactionCode.getText());
            } else {
                searchOptions.setSearchOnTransactionCode(false);
            }
            fillInCounterParty();
        } else if (e.getSource() == searchOnCounterParty){
            if(searchOnCounterParty.isSelected()){
                searchOptions.searchForCounterParty(counterParty);
            } else {
                searchOptions.setSearchOnCounterParty(false);
            }
            fillInCounterParty();
        }
	}

    private void enableSearchOptions(boolean enabled){
        searchOnTransactionCode.setEnabled(enabled);
        searchOnCommunication.setEnabled(enabled);
        searchOnCounterParty.setEnabled(enabled);
        transactionCode.setEnabled(enabled);
        communication.setEnabled(enabled);
    }

	private void setCounterParty() {
		for(Movement m : movementDataModel.getAllMovements()) {
			TmpCounterParty tmpCounterParty = m.getTmpCounterParty();
			m.setCounterParty(tmpCounterParty.getCounterParty());
		}
		combo.setEnabled(false);
		single.setEnabled(false);
		multiple.setEnabled(false);
		create.setEnabled(false);
		apply.setEnabled(false);
        enableSearchOptions(false);
        movementDataModel.fireTableDataChanged();
	}

	private void fillInCounterParty() {
		for(Movement m : movementDataModel.getAllMovements()) {
			String name;
			if (counterParty == null) {
				name = null;
			} else {
				name = counterParty.getName();
			}
			m.setTmpCounterParty(new TmpCounterParty(name, counterParty));
		}
		movementDataModel.fireTableDataChanged();
	}
}
